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
import android.widget.Button;
import android.widget.EditText;
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

public class WorkersRateActivity extends AppCompatActivity {

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
    private EditText carpenterRateDTTXT;
    private EditText laborerRateDTTXT;
    private EditText masonRateDTTXT;
    private EditText steelManRateDTTXT;
    private EditText painterRateDTTXT;
    private EditText electricianRateDTTXT;
    private EditText plumberRateDTTXT;
    private EditText tileManRateDTTXT;
    private EditText doorAndWindowInstallerRateDTTXT;
    private EditText tinsmithRateDTTXT;
    private EditText welderRateDTTXT;
    private Button updateBTTN;
    private String updateRateResponse;
    private String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_rate);

        getUserData();
        getView();
        getServer();
        getHashKey();
        showRate();
    }

    private void getView() {
        backNRLYT = (LinearLayout)findViewById(R.id.backNRLYT);
        layout = (LinearLayout)findViewById(R.id.layout1);
        carpenterRateDTTXT = (EditText)findViewById(R.id.carpenterRateDTTXT);
        laborerRateDTTXT = (EditText)findViewById(R.id.laborerRateDTTXT);
        masonRateDTTXT = (EditText)findViewById(R.id.masonRateDTTXT);
        steelManRateDTTXT = (EditText)findViewById(R.id.steelManRateDTTXT);
        painterRateDTTXT = (EditText)findViewById(R.id.painterRateDTTXT);
        electricianRateDTTXT = (EditText)findViewById(R.id.electricianRateDTTXT);
        plumberRateDTTXT = (EditText)findViewById(R.id.plumberRateDTTXT);
        tileManRateDTTXT = (EditText)findViewById(R.id.tileManRateDTTXT);
        doorAndWindowInstallerRateDTTXT = (EditText)findViewById(R.id.doorAndWindowInstallerRateDTTXT);
        tinsmithRateDTTXT = (EditText)findViewById(R.id.tinsmithRateDTTXT);
        welderRateDTTXT = (EditText)findViewById(R.id.welderRateDTTXT);
        updateBTTN = (Button)findViewById(R.id.updateBTTN);

        updateBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carpenterRateDTTXT.getText().toString().length() == 0 || laborerRateDTTXT.getText().toString().length() == 0 ||
                        masonRateDTTXT.getText().toString().length() == 0 || steelManRateDTTXT.getText().toString().length() == 0 ||
                        painterRateDTTXT.getText().toString().length() == 0 || electricianRateDTTXT.getText().toString().length() == 0 ||
                        plumberRateDTTXT.getText().toString().length() == 0 || tileManRateDTTXT.getText().toString().length() == 0 ||
                        doorAndWindowInstallerRateDTTXT.getText().toString().length() == 0 || tinsmithRateDTTXT.getText().toString().length() == 0 ||
                        welderRateDTTXT.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Incomplete details!", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateRate();
                }
            }
        });

        backNRLYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void updateRate() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", activeEmail);
        phpData.put("project_id", projectId);
        phpData.put("carpenter", carpenterRateDTTXT.getText().toString());
        phpData.put("laborer", laborerRateDTTXT.getText().toString());
        phpData.put("mason", masonRateDTTXT.getText().toString());
        phpData.put("steel_man", steelManRateDTTXT.getText().toString());
        phpData.put("painter", painterRateDTTXT.getText().toString());
        phpData.put("electrician", electricianRateDTTXT.getText().toString());
        phpData.put("plumber", plumberRateDTTXT.getText().toString());
        phpData.put("tile_man", tileManRateDTTXT.getText().toString());
        phpData.put("door_and_window_installer", doorAndWindowInstallerRateDTTXT.getText().toString());
        phpData.put("tinsmith", tinsmithRateDTTXT.getText().toString());
        phpData.put("welder", welderRateDTTXT.getText().toString());

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                updateRateResponse = s;
                if (updateRateResponse.equals("submitted")) {
                    Toast.makeText(WorkersRateActivity.this, "Rate was updated!", Toast.LENGTH_SHORT).show();
                    finish();
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
        phpTask.execute(phpServer + "android_update_rate.php");

    }


    private void showRate() {
        try {
            //layout.removeAllViews();
            HttpURL = phpServer + "android_retrieve_rate.php";
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
                ResultHash.put("project_id",projectId);

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
        ArrayList<String> emailArray = new ArrayList<String>();
        ArrayList<String> projectIdArray = new ArrayList<String>();
        ArrayList<String> carpenterArray = new ArrayList<String>();
        ArrayList<String> laborerArray = new ArrayList<String>();
        ArrayList<String> masonArray = new ArrayList<String>();
        ArrayList<String> steelManArray = new ArrayList<String>();
        ArrayList<String> painterArray = new ArrayList<String>();
        ArrayList<String> electricianArray = new ArrayList<String>();
        ArrayList<String> plumberArray = new ArrayList<String>();
        ArrayList<String> tileManArray = new ArrayList<String>();
        ArrayList<String> doorAndWindowInstallerArray = new ArrayList<String>();
        ArrayList<String> tinsmithArray = new ArrayList<String>();
        ArrayList<String> welderArray = new ArrayList<String>();
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
                                emailArray.add(jsonObject.getString("email"));
                                projectIdArray.add(jsonObject.getString("project_id"));
                                carpenterArray.add(jsonObject.getString("carpenter"));
                                laborerArray.add(jsonObject.getString("laborer"));
                                masonArray.add(jsonObject.getString("mason"));
                                steelManArray.add(jsonObject.getString("steel_man"));
                                painterArray.add(jsonObject.getString("painter"));
                                electricianArray.add(jsonObject.getString("electrician"));
                                plumberArray.add(jsonObject.getString("plumber"));
                                tileManArray.add(jsonObject.getString("tile_man"));
                                doorAndWindowInstallerArray.add(jsonObject.getString("door_and_window_installer"));
                                tinsmithArray.add(jsonObject.getString("tinsmith"));
                                welderArray.add(jsonObject.getString("welder"));
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


            if(!(CheckNetwork.isInternetAvailable(WorkersRateActivity.this)))
            {
                Toast.makeText(context, "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
            }

            try {

                int arrayPos = 0;
                Iterator<String> foreachSubjects = idArray.iterator();

                while (foreachSubjects.hasNext()) {

                    String id = idArray.get(arrayPos);
                    String email = emailArray.get(arrayPos);
                    String projectId = projectIdArray.get(arrayPos);
                    String carpenter = carpenterArray.get(arrayPos);
                    String laborer = laborerArray.get(arrayPos);
                    String mason = masonArray.get(arrayPos);
                    String steelMan = steelManArray.get(arrayPos);
                    String painter = painterArray.get(arrayPos);
                    String electrician = electricianArray.get(arrayPos);
                    String plumber = plumberArray.get(arrayPos);
                    String tileMan = tileManArray.get(arrayPos);
                    String doorAndWindowInstaller = doorAndWindowInstallerArray.get(arrayPos);
                    String tinsmith = tinsmithArray.get(arrayPos);
                    String welder = welderArray.get(arrayPos);
                    String time = createdArray.get(arrayPos);

                    //addView(foreachSubjects.next(), startTime + " - " + endTime);

                    carpenterRateDTTXT.setText(carpenter);
                    laborerRateDTTXT.setText(laborer);
                    masonRateDTTXT.setText(mason);
                    steelManRateDTTXT.setText(steelMan);
                    painterRateDTTXT.setText(painter);
                    electricianRateDTTXT.setText(electrician);
                    plumberRateDTTXT.setText(plumber);
                    tileManRateDTTXT.setText(tileMan);
                    doorAndWindowInstallerRateDTTXT.setText(doorAndWindowInstaller);
                    tinsmithRateDTTXT.setText(tinsmith);
                    welderRateDTTXT.setText(welder);


                    arrayPos++;
                }

            }
            catch (Exception ex){
                //Toast.makeText(context, ""+ex, Toast.LENGTH_SHORT).show();
            }

        }
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
            projectId = extras.getString("project_id");
        }
        else {
            finish();
        }
    }
}