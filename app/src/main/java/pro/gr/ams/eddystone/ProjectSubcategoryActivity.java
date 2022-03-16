package pro.gr.ams.eddystone;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.List;

public class ProjectSubcategoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SharedPreferences sharedpreferences;
    public static final String mypreference = "EddystonePreference";
    private String activeEmail;
    private String hashKey;
    private String phpServer;
    private String userLevel;
    private String progress;
    private String client_email;
    private LinearLayout backNRLYT;
    private LinearLayout layout;
    private TextView projectNameTXTVW;
    private String project;
    private TextView subtitleTXTVW;
    private String project_id;
    private Spinner spinner;
    private String option_title;
    private String HttpURL;
    private String FinalJSonObject;
    HashMap<String,String> ResultHash = new HashMap<>();
    String ParseResult ;
    HttpParse httpParse = new HttpParse();
    private String carpenter = "";
    private String laborer = "";
    private String mason = "";
    private String steelMan = "";
    private String painter = "";
    private String electrician = "";
    private String plumber = "";
    private String tileMan = "";
    private String doorAndWindowInstaller = "";
    private String tinsmith = "";
    private String welder = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_subcategory);

        getUserData();
        getView();
        getServer();
        getHashKey();
        setSpinner();
        showRate();

    }

    private void getView() {
        backNRLYT = (LinearLayout)findViewById(R.id.backNRLYT);
        layout = (LinearLayout)findViewById(R.id.layout1);
        projectNameTXTVW = (TextView)findViewById(R.id.projectNameTXTVW);
        subtitleTXTVW = (TextView)findViewById(R.id.subtitleTXTVW);
        backNRLYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        projectNameTXTVW.setText(option_title);
        subtitleTXTVW.setVisibility(View.GONE);

        if (option_title.equals("Excavation and Backfilling")) {
            addOption("Earthworks", "on progress");
        }
        else if (option_title.equals("Steel Works")) {
            addOption("Steel Reinforcement Work", "on progress");
        }
        else if (option_title.equals("Concreting")) {
            addOption("Reinforced Cement Concrete", "on progress");
        }
        else if (option_title.equals("Masonry Works")) {
            addOption("Masonry Walls (Exterior)", "on progress");
            addOption("Masonry Walls (Interior)", "on progress");
            addOption("Plastering works (Exterior)", "on progress");
            addOption("Plastering works (Interior)", "on progress");
        }
        else if (option_title.equals("Carpentry")) {
            addOption("Formworks", "on progress");
            addOption("Ceiling Steel Frame", "on progress");
            addOption("Ceiling", "on progress");
            addOption("Roofing Works (Trusses)", "on progress");
            addOption("Roofing Works (GI Sheets)", "on progress");
            addOption("Roofing Works (Gutter)", "on progress");
        }
        else if (option_title.equals("Finishing")) {
            addOption("Flooring Works (EXC T&B)", "on progress");
            addOption("Flooring Works (T&B)", "on progress");
            addOption("Painting Works (INT. SKIM COAT)", "on progress");
            addOption("Painting Works (EXT. SKIM COAT)", "on progress");
            addOption("Painting Works (INTERIOR)", "on progress");
            addOption("Painting Works (EXTERIOR)", "on progress");
            addOption("Door Lockset", "on progress");
            addOption("Door Jamb", "on progress");
            addOption("Door", "on progress");
            addOption("Windows", "on progress");
        }
        else if (option_title.equals("Plumbing")) {
            addOption("Plumbing Works", "on progress");
            addOption("Plumbing Fixtures", "on progress");
        }
        else if (option_title.equals("Electrical")) {
            addOption("Electrical Works (Roughing INS)", "on progress");
            addOption("Electrical Works (Cable Pulling)", "on progress");
            addOption("Electrical Works (Fixtures)", "on progress");
        }
        else {
            Toast.makeText(getApplicationContext(), "No data available!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setSpinner() {
        /*if (userLevel.equals("engineer")) {
            spinner = findViewById(R.id.spinner);

            spinner.setOnItemSelectedListener(this);

            List<String> categories = new ArrayList<String>();
            categories.add("⋮");
            categories.add("Update Progress");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(dataAdapter);

            spinner.setVisibility(View.VISIBLE);

        }*/
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        //user_level=item;

        if (item.equals("Update Progress")) {
            Intent i = new Intent(getApplicationContext(), UpdateProgressActivity.class);
            i.putExtra("email", activeEmail);
            i.putExtra("project_id", project_id);
            i.putExtra("user_level", "engineer");
            i.putExtra("project", project);
            i.putExtra("progress", progress);
            i.putExtra("client_email", client_email);
            i.putExtra("carpenter", carpenter);
            i.putExtra("laborer", laborer);
            i.putExtra("mason", mason);
            i.putExtra("steel_man", steelMan);
            i.putExtra("painter", painter);
            i.putExtra("electrician", electrician);
            i.putExtra("plumber", plumber);
            i.putExtra("tile_man", tileMan);
            i.putExtra("door_and_window_installer", doorAndWindowInstaller);
            i.putExtra("tinsmith", tinsmith);
            i.putExtra("welder", welder);
            startActivity(i);
        }

        spinner.setSelection(0);
    }
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void addOption(final String title, final String status) {

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


        //profileIMGVW.setBackgroundResource(R.drawable.dynamic_icon_background);

        if (status.equals("on progress")) {
            profileIMGVW.setImageResource(R.drawable.ic_check_list);
            profileIMGVW.setBackgroundResource(R.drawable.on_progress_icon_background);
        }
        else if (status.equals("completed")) {
            profileIMGVW.setBackgroundResource(R.drawable.completed_icon_background);
            profileIMGVW.setImageResource(R.drawable.ic_completed);
        }
        else if (status.equals("delayed")) {
            profileIMGVW.setBackgroundResource(R.drawable.delayed_icon_background);
            profileIMGVW.setImageResource(R.drawable.ic_warning);
        }
        profileIMGVW.setPadding(20, 20, 20, 20);
        profileIMGVW.setColorFilter(Color.argb(255, 255, 255, 255));

        titleTXTVW.setPadding(0, 10, 0, 10);
        titleTXTVW.setLayoutParams(userParams);
        //titleTXTVW.setTextSize(14);
        //titleTXTVW.setTextSize(18);
        titleTXTVW.setTextSize(16);
        titleTXTVW.setTextColor(Color.BLACK);
        titleTXTVW.setGravity(Gravity.CENTER_VERTICAL);
        titleTXTVW.setTypeface(null, Typeface.BOLD);

        titleTXTVW.setText(title);
        titleTXTVW.setTextColor(Color.WHITE);

        //contentTXTVW.setPadding(0, 0, 0, 0);

        //contentTXTVW.setLayoutParams(lp2);

        //contentTXTVW.setTextSize(12);
        //contentTXTVW.setTextSize(14);

        userData.addView(titleTXTVW);
        //userData.addView(lineTXTVW);
        //userData.addView(contentTXTVW);
        imageAndUserData.setBackgroundResource(R.drawable.black_background);

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
        imageAndUserData.setPadding(22, 18, 22, 0);

        //timeTXTVW.setText(time);

        imageAndUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (carpenter.length() != 0) {
                    Intent i = new Intent(getApplicationContext(), ProjectEstimationActivity.class);
                    i.putExtra("email", activeEmail);
                    i.putExtra("user_level", userLevel);
                    i.putExtra("project_id", project_id);
                    i.putExtra("project", project);
                    i.putExtra("progress", progress);
                    i.putExtra("client_email", client_email);
                    i.putExtra("option_title", option_title);
                    i.putExtra("option_subtitle", title);
                    i.putExtra("carpenter", carpenter);
                    i.putExtra("laborer", laborer);
                    i.putExtra("mason", mason);
                    i.putExtra("steel_man", steelMan);
                    i.putExtra("painter", painter);
                    i.putExtra("electrician", electrician);
                    i.putExtra("plumber", plumber);
                    i.putExtra("tile_man", tileMan);
                    i.putExtra("door_and_window_installer", doorAndWindowInstaller);
                    i.putExtra("tinsmith", tinsmith);
                    i.putExtra("welder", welder);
                    startActivity(i);
                }
            }
        });

        //layout.addView(imageAndUserData,0);
        layout.addView(imageAndUserData);



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
                ResultHash.put("project_id",project_id);

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


            if(!(CheckNetwork.isInternetAvailable(ProjectSubcategoryActivity.this)))
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
                    carpenter = carpenterArray.get(arrayPos);
                    laborer = laborerArray.get(arrayPos);
                    mason = masonArray.get(arrayPos);
                    steelMan = steelManArray.get(arrayPos);
                    painter = painterArray.get(arrayPos);
                    electrician = electricianArray.get(arrayPos);
                    plumber = plumberArray.get(arrayPos);
                    tileMan = tileManArray.get(arrayPos);
                    doorAndWindowInstaller = doorAndWindowInstallerArray.get(arrayPos);
                    tinsmith = tinsmithArray.get(arrayPos);
                    welder = welderArray.get(arrayPos);
                    String time = createdArray.get(arrayPos);

                    //addView(foreachSubjects.next(), startTime + " - " + endTime);

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
            project_id = extras.getString("project_id");
            project = extras.getString("project");
            progress = extras.getString("progress");
            client_email = extras.getString("client_email");;
            option_title = extras.getString("option_title");
        }
        else {
            finish();
        }
    }

}