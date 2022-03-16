package pro.gr.ams.eddystone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListOfProjectsActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String mypreference = "EddystonePreference";
    private String activeEmail;
    private String hashKey;
    private String phpServer;
    private LinearLayout backNRLYT;
    private LinearLayout layout;
    private String HttpURL;
    private String FinalJSonObject;
    HashMap<String,String> ResultHash = new HashMap<>();
    String ParseResult ;
    HttpParse httpParse = new HttpParse();
    private String userLevel;
    private String intentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_projects);

        getUserData();
        getView();
        getServer();
        getHashKey();
        showProjects();
    }

    private void getView() {
        backNRLYT = (LinearLayout)findViewById(R.id.backNRLYT);
        layout = (LinearLayout)findViewById(R.id.layout1);
        backNRLYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void showProjects() {
        try {
            //layout.removeAllViews();
            HttpURL = phpServer + "android_retrieve_projects.php";
            HttpWebCall(activeEmail);
        }
        catch (Exception ex ) {
            Toast.makeText(getApplicationContext(), ""+ex, Toast.LENGTH_SHORT).show();
        }
    }


    public void HttpWebCall(final String PreviousListViewClickedItem){

        class HttpWebCallFunction extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //pDialog = ProgressDialog.show(ChatActivity.this,null,"Loading...",true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                //pDialog.dismiss();

                FinalJSonObject = httpResponseMsg ;

                new GetHttpResponse(getApplicationContext()).execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("email",params[0]);
                ResultHash.put("hashKey",hashKey);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }

    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        ArrayList<String> idArray = new ArrayList<String>();
        ArrayList<String> engineerEmailArray = new ArrayList<String>();
        ArrayList<String> clientArray = new ArrayList<String>();
        ArrayList<String> projectArray = new ArrayList<String>();
        ArrayList<String> progressArray = new ArrayList<String>();
        ArrayList<String> createdArray = new ArrayList<String>();

        public Context context;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            try
            {
                if(FinalJSonObject != null)
                {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(FinalJSonObject);

                        JSONObject jsonObject;

                        jsonObject = jsonArray.getJSONObject(0);

                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);

                            try {

                                idArray.add(jsonObject.getString("id"));
                                engineerEmailArray.add(jsonObject.getString("engineer_email"));
                                clientArray.add(jsonObject.getString("client_email"));
                                projectArray.add(jsonObject.getString("project"));
                                progressArray.add(jsonObject.getString("progress"));
                                createdArray.add(jsonObject.getString("created"));
                            }
                            catch (Exception ex) {
                            }


                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {

            //addViewLNRLYT.removeAllViews();


            if(!(CheckNetwork.isInternetAvailable(ListOfProjectsActivity.this)))
            {
                Toast.makeText(context, "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
            }

            try {

                int arrayPos = 0;
                Iterator<String> foreachSubjects = idArray.iterator();

                while (foreachSubjects.hasNext()) {
                    String id = idArray.get(arrayPos);
                    String engineer_email = engineerEmailArray.get(arrayPos);
                    String client_email = clientArray.get(arrayPos);
                    String project = projectArray.get(arrayPos);
                    String progress = progressArray.get(arrayPos);
                    String time = createdArray.get(arrayPos);

                    //addView(foreachSubjects.next(), startTime + " - " + endTime);

                    addProject(id, project, progress, engineer_email, client_email);


                    arrayPos++;
                }

            }
            catch (Exception ex){
                //Toast.makeText(context, ""+ex, Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void addProject(final String id, final String project, String progress, String engineer_email, String client_email) {

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        //final TextView contentTXTVW = new TextView(this);
        final TextView titleTXTVW = new TextView(this);
        //final TextView timeTXTVW = new TextView(this);
        TextView separatorTXTVW = new TextView(this);
        TextView lineTXTVW = new TextView(this);
        final ImageView profileIMGVW = new ImageView(this);
        final LinearLayout userData = new LinearLayout(this);
        final LinearLayout imageAndUserData = new LinearLayout(this);
        userData.setOrientation(LinearLayout.VERTICAL);
        imageAndUserData.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout messageAndAttachment = new LinearLayout(this);
        messageAndAttachment.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams profileParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        profileParams.weight = 1.0f;

        LinearLayout.LayoutParams dataParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dataParams.weight = 1.0f;

        dataParams.setMargins(0, 10, 0, 10);

        LinearLayout.LayoutParams userParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        userParams.weight = 1.0f;

        profileParams.setMargins(0, 8, 8, 8);
        userData.setLayoutParams(dataParams);
        imageAndUserData.setLayoutParams(dataParams);

        //contentTXTVW.setText(content);

        lp2.gravity = Gravity.LEFT;
        lp2.setMargins(0, 0, 0, 0);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        profileIMGVW.setLayoutParams(layoutParams);

        profileIMGVW.setImageResource(R.drawable.ic_projects);
        profileIMGVW.setPadding(20, 20, 20, 20);
        profileIMGVW.setColorFilter(Color.argb(255, 255, 255, 255));

        profileIMGVW.setBackgroundResource(R.drawable.dynamic_icon_background);

        titleTXTVW.setPadding(0, 10, 0, 10);
        titleTXTVW.setLayoutParams(userParams);
        //titleTXTVW.setTextSize(14);
        //titleTXTVW.setTextSize(18);
        titleTXTVW.setTextSize(16);
        titleTXTVW.setTextColor(Color.BLACK);
        titleTXTVW.setGravity(Gravity.CENTER_VERTICAL);
        titleTXTVW.setTypeface(null, Typeface.BOLD);

        titleTXTVW.setText(project);
        titleTXTVW.setTextColor(Color.WHITE);

        //contentTXTVW.setPadding(0, 0, 0, 0);

        //contentTXTVW.setLayoutParams(lp2);

        //contentTXTVW.setTextSize(12);
        //contentTXTVW.setTextSize(14);

        userData.addView(titleTXTVW);
        //userData.addView(lineTXTVW);
        //userData.addView(contentTXTVW);
        //imageAndUserData.setBackgroundResource(R.drawable.white_background);

        //timeTXTVW.setPadding(0, 32, 0, 0);
        //timeTXTVW.setTextSize(8);
        //timeTXTVW.setLayoutParams(lp2);
        //userData.addView(timeTXTVW);

        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
        //lineTXTVW.setBackgroundColor(Color.parseColor("#000000"));
        LinearLayout.LayoutParams separatorParams = new LinearLayout.LayoutParams(30, 120);

        separatorTXTVW.setLayoutParams(separatorParams);
        //lineTXTVW.setLayoutParams(lineParams);

        imageAndUserData.addView(profileIMGVW);
        imageAndUserData.addView(separatorTXTVW);
        imageAndUserData.addView(userData);
        imageAndUserData.setPadding(22, 0, 22, 0);

        //timeTXTVW.setText(time);

        imageAndUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intentActivity.equals("ProjectDetailsActivity")) {
                    checkRateAvailable(id, project, progress, engineer_email,client_email);
                }
                else if (intentActivity.equals("WorkersRateActivity")) {
                    Intent i = new Intent(getApplicationContext(), WorkersRateActivity.class);
                    i.putExtra("email", activeEmail);
                    i.putExtra("user_level", userLevel);
                    i.putExtra("project_id", id);
                    startActivity(i);
                }
            }
        });

        //layout.addView(imageAndUserData,0);
        layout.addView(imageAndUserData);



    }


    private void checkRateAvailable(String id, String project, String progress, String engineer_email,String client_email) {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", activeEmail);
        phpData.put("project_id", id);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                //Log.d(TAG, s);
                if (s != null) {
                    if (s.length() > 0) {
                        if (s.equals("exists")) {
                            Intent i = new Intent(getApplicationContext(), ProjectDetailsActivity.class);
                            i.putExtra("email", activeEmail);
                            i.putExtra("user_level", userLevel);
                            i.putExtra("project_id", id);
                            i.putExtra("project", project);
                            i.putExtra("progress", progress);
                            i.putExtra("engineer_email", engineer_email);
                            i.putExtra("client_email", client_email);
                            startActivity(i);
                        }
                        else {
                            if (userLevel.equals("engineer")) {
                                Intent i = new Intent(getApplicationContext(), WorkersRateActivity.class);
                                i.putExtra("email", activeEmail);
                                i.putExtra("user_level", userLevel);
                                i.putExtra("project_id", id);
                                startActivity(i);
                            }
                            else if (userLevel.equals("client")) {
                                Toast.makeText(getApplicationContext(), "Engineer must update the rate first.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else {
                        if(!(CheckNetwork.isInternetAvailable(getApplicationContext())))
                        {
                            Toast.makeText(getApplicationContext(), "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //finish();
                } else {
                    //finish();
                }
            }
        });
        phpTask.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (e != null && e.getMessage() != null) {
                    //Log.d(TAG, e.getMessage());
                }
            }
        });
        phpTask.execute(phpServer + "android_check_rate_available.php");
    }

    private void getServer() {
        PhpServer phpsrvr = new PhpServer();
        phpServer = phpsrvr.getServer();
    }

    private void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                /*Log.e("MY_KEY_HASH:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));*/
                hashKey = (Base64.encodeToString(md.digest(), Base64.DEFAULT)).replaceAll("\n","");
            }
        }
        catch (PackageManager.NameNotFoundException e) {
        }
        catch (NoSuchAlgorithmException e) {
        }
    }

    private void getUserData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            activeEmail = extras.getString("email");
            userLevel = extras.getString("user_level");
            intentActivity = extras.getString("intent_activity");
        }
        else {
            finish();
        }
    }
}