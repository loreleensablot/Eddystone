package pro.gr.ams.eddystone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

public class ProjectEstimationActivity extends AppCompatActivity {

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
    private String option_subtitle;
    private TextView costOfLaborTXTVW;
    private LinearLayout volumeLNRLYT;
    private EditText volumeDTTX;
    private LinearLayout preferredTimeLNRLYT;
    private EditText preferredTimeDTTX;
    private LinearLayout numberOfDaysLNRLYT;
    private EditText numberOfDaysDTTX;
    private LinearLayout numberOfWorkersLNRLYT;
    private EditText numberOfWorkersDTTX;
    private LinearLayout saveNRLYT;
    private LinearLayout carpenterLNRLYT;
    private EditText carpenterDTTX;
    private HorizontalScrollView horizontalSCRLLVW;
    private LinearLayout laborerLNRLYT;
    private EditText laborerDTTX;
    private LinearLayout masonLNRLYT;
    private LinearLayout steelManLNRLYT;
    private LinearLayout painterLNRLYT;
    private LinearLayout electricianLNRLYT;
    private LinearLayout plumberLNRLYT;
    private LinearLayout tileManLNRLYT;
    private LinearLayout doorAndWindowInstallerLNRLYT;
    private LinearLayout tinsmithLNRLYT;
    private LinearLayout welderLNRLYT;
    private EditText masonDTTX;
    private EditText steelManDTTX;
    private EditText painterDTTX;
    private EditText electricianDTTX;
    private EditText plumberDTTX;
    private EditText tileManDTTX;
    private EditText doorAndWindowInstallerDTTX;
    private EditText tinsmithDTTX;
    private EditText welderDTTX;
    private String updateEstimateResponse;
    private String HttpURL;
    private String FinalJSonObject;
    HashMap<String,String> ResultHash = new HashMap<>();
    String ParseResult ;
    HttpParse httpParse = new HttpParse();
    private String carpenterRate;
    private String laborerRate;
    private String masonRate;
    private String steelManRate;
    private String painterRate;
    private String electricianRate;
    private String plumberRate;
    private String tileManRate;
    private String doorAndWindowInstallerRate;
    private String tinsmithRate;
    private String welderRate;
    private TextView unitTitleTXTVW;
    private TextView unitTXTVW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_estimation);

        getUserData();
        getView();
        getServer();
        getHashKey();
        showEstimation();

    }

    private void getView() {
        backNRLYT = (LinearLayout)findViewById(R.id.backNRLYT);
        layout = (LinearLayout)findViewById(R.id.layout1);
        projectNameTXTVW = (TextView)findViewById(R.id.projectNameTXTVW);
        subtitleTXTVW = (TextView)findViewById(R.id.subtitleTXTVW);
        costOfLaborTXTVW = (TextView)findViewById(R.id.costOfLaborTXTVW);
        saveNRLYT = (LinearLayout)findViewById(R.id.saveNRLYT);
        volumeLNRLYT = (LinearLayout)findViewById(R.id.volumeLNRLYT);
        preferredTimeLNRLYT = (LinearLayout)findViewById(R.id.preferredTimeLNRLYT);
        numberOfDaysLNRLYT = (LinearLayout)findViewById(R.id.numberOfDaysLNRLYT);
        numberOfWorkersLNRLYT = (LinearLayout)findViewById(R.id.numberOfWorkersLNRLYT);
        carpenterLNRLYT = (LinearLayout)findViewById(R.id.carpenterLNRLYT);
        laborerLNRLYT = (LinearLayout)findViewById(R.id.laborerLNRLYT);
        masonLNRLYT = (LinearLayout)findViewById(R.id.masonLNRLYT);
        steelManLNRLYT = (LinearLayout)findViewById(R.id.steelManLNRLYT);
        painterLNRLYT = (LinearLayout)findViewById(R.id.painterLNRLYT);
        electricianLNRLYT = (LinearLayout)findViewById(R.id.electricianLNRLYT);
        plumberLNRLYT = (LinearLayout)findViewById(R.id.plumberLNRLYT);
        tileManLNRLYT = (LinearLayout)findViewById(R.id.tileManLNRLYT);
        doorAndWindowInstallerLNRLYT = (LinearLayout)findViewById(R.id.doorAndWindowInstallerLNRLYT);
        tinsmithLNRLYT = (LinearLayout)findViewById(R.id.tinsmithLNRLYT);
        welderLNRLYT = (LinearLayout)findViewById(R.id.welderLNRLYT);
        volumeDTTX = (EditText)findViewById(R.id.volumeDTTX);
        preferredTimeDTTX = (EditText)findViewById(R.id.preferredTimeDTTX);
        numberOfDaysDTTX = (EditText)findViewById(R.id.numberOfDaysDTTX);
        numberOfWorkersDTTX = (EditText)findViewById(R.id.numberOfWorkersDTTX);
        carpenterDTTX = (EditText)findViewById(R.id.carpenterDTTX);
        laborerDTTX = (EditText)findViewById(R.id.laborerDTTX);
        masonDTTX = (EditText)findViewById(R.id.masonDTTX);
        steelManDTTX = (EditText)findViewById(R.id.steelManDTTX);
        painterDTTX = (EditText)findViewById(R.id.painterDTTX);
        electricianDTTX = (EditText)findViewById(R.id.electricianDTTX);
        plumberDTTX = (EditText)findViewById(R.id.plumberDTTX);
        tileManDTTX = (EditText)findViewById(R.id.tileManDTTX);
        doorAndWindowInstallerDTTX = (EditText)findViewById(R.id.doorAndWindowInstallerDTTX);
        tinsmithDTTX = (EditText)findViewById(R.id.tinsmithDTTX);
        welderDTTX = (EditText)findViewById(R.id.welderDTTX);
        horizontalSCRLLVW = (HorizontalScrollView)findViewById(R.id.horizontalSCRLLVW);
        laborerDTTX = (EditText)findViewById(R.id.laborerDTTX);
        unitTitleTXTVW = (TextView)findViewById(R.id.unitTitleTXTVW);
        unitTXTVW = (TextView)findViewById(R.id.unitTXTVW);
        

        backNRLYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveNRLYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (volumeDTTX.getText().toString().length() != 0 &&
                        preferredTimeDTTX.getText().toString().length() != 0 &&
                        numberOfDaysDTTX.getText().toString().length() != 0 &&
                        numberOfWorkersDTTX.getText().toString().length() != 0 &&
                        carpenterDTTX.getText().toString().length() != 0 &&
                        laborerDTTX.getText().toString().length() != 0 &&
                        masonDTTX.getText().toString().length() != 0 &&
                        steelManDTTX.getText().toString().length() != 0 &&
                        painterDTTX.getText().toString().length() != 0 &&
                        electricianDTTX.getText().toString().length() != 0 &&
                        plumberDTTX.getText().toString().length() != 0 &&
                        tileManDTTX.getText().toString().length() != 0 &&
                        doorAndWindowInstallerDTTX.getText().toString().length() != 0 &&
                        tinsmithDTTX.getText().toString().length() != 0 &&
                        welderDTTX.getText().toString().length() != 0) {
                    updateEstimate();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please complete the required details!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        projectNameTXTVW.setText(option_subtitle);
        subtitleTXTVW.setVisibility(View.GONE);

        int Measuredwidth = 0;
        int Measuredheight = 0;
        Point size = new Point();
        WindowManager w = getWindowManager();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)    {
            w.getDefaultDisplay().getSize(size);
            Measuredwidth = size.x;
            Measuredheight = size.y;
        }else{
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }

        int widthItemHorizontalScroll = (Measuredwidth/2)-((32*2)+(8*4)+32);

        carpenterLNRLYT.setMinimumWidth(widthItemHorizontalScroll);
        laborerLNRLYT.setMinimumWidth(widthItemHorizontalScroll);
        masonLNRLYT.setMinimumWidth(widthItemHorizontalScroll);
        steelManLNRLYT.setMinimumWidth(widthItemHorizontalScroll);
        painterLNRLYT.setMinimumWidth(widthItemHorizontalScroll);
        electricianLNRLYT.setMinimumWidth(widthItemHorizontalScroll);
        plumberLNRLYT.setMinimumWidth(widthItemHorizontalScroll);
        tileManLNRLYT.setMinimumWidth(widthItemHorizontalScroll);
        doorAndWindowInstallerLNRLYT.setMinimumWidth(widthItemHorizontalScroll);
        tinsmithLNRLYT.setMinimumWidth(widthItemHorizontalScroll);
        welderLNRLYT.setMinimumWidth(widthItemHorizontalScroll);

        if (userLevel.equals("engineer")) {
            saveNRLYT.setVisibility(View.VISIBLE);

            volumeDTTX.setFocusable(true);
            preferredTimeDTTX.setFocusable(true);
            numberOfDaysDTTX.setFocusable(true);
            numberOfWorkersDTTX.setFocusable(true);
            carpenterDTTX.setFocusable(true);
            laborerDTTX.setFocusable(true);
            masonDTTX.setFocusable(true);
            steelManDTTX.setFocusable(true);
            painterDTTX.setFocusable(true);
            electricianDTTX.setFocusable(true);
            plumberDTTX.setFocusable(true);
            tileManDTTX.setFocusable(true);
            doorAndWindowInstallerDTTX.setFocusable(true);
            tinsmithDTTX.setFocusable(true);
            welderDTTX.setFocusable(true);
        }
        else if (userLevel.equals("client")) {
            volumeDTTX.setFocusable(false);
            preferredTimeDTTX.setFocusable(false);
            numberOfDaysDTTX.setFocusable(false);
            numberOfWorkersDTTX.setFocusable(false);
            carpenterDTTX.setFocusable(false);
            laborerDTTX.setFocusable(false);
            masonDTTX.setFocusable(false);
            steelManDTTX.setFocusable(false);
            painterDTTX.setFocusable(false);
            electricianDTTX.setFocusable(false);
            plumberDTTX.setFocusable(false);
            tileManDTTX.setFocusable(false);
            doorAndWindowInstallerDTTX.setFocusable(false);
            tinsmithDTTX.setFocusable(false);
            welderDTTX.setFocusable(false);
        }

        carpenterLNRLYT.setVisibility(View.GONE);
        laborerLNRLYT.setVisibility(View.GONE);
        masonLNRLYT.setVisibility(View.GONE);
        steelManLNRLYT.setVisibility(View.GONE);
        painterLNRLYT.setVisibility(View.GONE);
        electricianLNRLYT.setVisibility(View.GONE);
        plumberLNRLYT.setVisibility(View.GONE);
        tileManLNRLYT.setVisibility(View.GONE);
        doorAndWindowInstallerLNRLYT.setVisibility(View.GONE);
        tinsmithLNRLYT.setVisibility(View.GONE);
        welderLNRLYT.setVisibility(View.GONE);

        if (option_subtitle.equals("Earthworks")) {
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Excavation");
            unitTXTVW.setText("Cu.m");
        }
        else if (option_subtitle.equals("Steel Reinforcement Work")) {
            steelManLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Weight");
            unitTXTVW.setText("kgs.");
        }
        else if (option_subtitle.equals("Reinforced Cement Concrete")) {
            masonLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Volume");
            unitTXTVW.setText("Cu.m");
        }
        else if (option_subtitle.equals("Masonry Walls (Exterior)") || option_subtitle.equals("Masonry Walls (Interior)" )) {
            masonLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Surface Area");
            unitTXTVW.setText("sqm");
        }
        else if (option_subtitle.equals("Plastering works (Exterior)") || option_subtitle.equals("Plastering works (Interior)" )) {
            masonLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Area");
            unitTXTVW.setText("sqm");
        }
        else if (option_subtitle.equals("Formworks")) {
            carpenterLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Area");
            unitTXTVW.setText("sqm");
        }
        else if (option_subtitle.equals("Ceiling Steel Frame") || option_subtitle.equals("Ceiling")) {
            carpenterLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Area");
            unitTXTVW.setText("sqm");
        }
        else if (option_subtitle.equals("Roofing Works (Trusses)")) {
            welderLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("PCS");
            unitTXTVW.setText("pcs");
        }
        else if (option_subtitle.equals("Roofing Works (GI Sheets)")) {
            tinsmithLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Area");
            unitTXTVW.setText("sqm.");

        }
        else if (option_subtitle.equals("Roofing Works (Gutter)")) {
            tinsmithLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Perimeter");
            unitTXTVW.setText("Lm.");
        }
        else if (option_subtitle.equals("Flooring Works (EXC T&B)") || option_subtitle.equals("Flooring Works (T&B)") ) {
            tileManLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Area");
            unitTXTVW.setText("sqm.");
        }
        else if (option_subtitle.equals("Painting Works (INT. SKIM COAT)") || option_subtitle.equals("Painting Works (EXT. SKIM COAT)") || option_subtitle.equals("Painting Works (INTERIOR)") || option_subtitle.equals("Painting Works (EXTERIOR)")) {
            painterLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Area");
            unitTXTVW.setText("sqm.");
        }
        else if (option_subtitle.equals("Door Lockset") || option_subtitle.equals("Door Jamb") ) {
            doorAndWindowInstallerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Sets");
            unitTXTVW.setText("set/s.");
        }
        else if (option_subtitle.equals("Door")) {
            doorAndWindowInstallerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Area");
            unitTXTVW.setText("sqm.");
        }
        else if (option_subtitle.equals("Windows")) {
            doorAndWindowInstallerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Sets");
            unitTXTVW.setText("set/s.");
        }
        else if (option_subtitle.equals("Plumbing Works")) {
            plumberLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Lengths");
            unitTXTVW.setText("Lm.");
        }
        else if (option_subtitle.equals("Plumbing Fixtures")) {
            plumberLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Sets");
            unitTXTVW.setText("set/s.");
        }
        else if (option_subtitle.equals("Electrical Works (Roughing INS)")) {
            electricianLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Lengths");
            unitTXTVW.setText("Lm.");
        }
        else if (option_subtitle.equals("Electrical Works (Cable Pulling)")) {
            electricianLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Linear Meter");
            unitTXTVW.setText("Lm.");
        }
        else if (option_subtitle.equals("Electrical Works (Fixtures)")) {
            electricianLNRLYT.setVisibility(View.VISIBLE);
            laborerLNRLYT.setVisibility(View.VISIBLE);
            unitTitleTXTVW.setText("Sets");
            unitTXTVW.setText("set/s.");
        }
        else {
            Toast.makeText(getApplicationContext(), "No data available!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }



    private void showEstimation() {
        try {
            //layout.removeAllViews();
            HttpURL = phpServer + "android_retrieve_estimation.php";
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
                ResultHash.put("category",option_subtitle);

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
        ArrayList<String> categoryArray = new ArrayList<String>();
        ArrayList<String> projectArray = new ArrayList<String>();
        ArrayList<String> volumeArray = new ArrayList<String>();
        ArrayList<String> preferredTimeArray = new ArrayList<String>();
        ArrayList<String> numberOfDaysArray = new ArrayList<String>();
        ArrayList<String> numberOfWorkersArray = new ArrayList<String>();
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
                                categoryArray.add(jsonObject.getString("category"));
                                projectArray.add(jsonObject.getString("project"));
                                volumeArray.add(jsonObject.getString("volume"));
                                preferredTimeArray.add(jsonObject.getString("preferred_time"));
                                numberOfDaysArray.add(jsonObject.getString("number_of_days"));
                                numberOfWorkersArray.add(jsonObject.getString("number_of_workers"));
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


            if(!(CheckNetwork.isInternetAvailable(ProjectEstimationActivity.this)))
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
                    String category = categoryArray.get(arrayPos);
                    String project = projectArray.get(arrayPos);
                    String volume = volumeArray.get(arrayPos);
                    String preferredTime = preferredTimeArray.get(arrayPos);
                    String numberOfDays = numberOfDaysArray.get(arrayPos);
                    String numberOfWorkers = numberOfWorkersArray.get(arrayPos);
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



                    volumeDTTX.setText(volume);
                    preferredTimeDTTX.setText(preferredTime);
                    numberOfDaysDTTX.setText(numberOfDays);
                    numberOfWorkersDTTX.setText(numberOfWorkers);
                    carpenterDTTX.setText(carpenter);
                    laborerDTTX.setText(laborer);
                    masonDTTX.setText(mason);
                    steelManDTTX.setText(steelMan);
                    painterDTTX.setText(painter);
                    electricianDTTX.setText(electrician);
                    plumberDTTX.setText(plumber);
                    tileManDTTX.setText(tileMan);
                    doorAndWindowInstallerDTTX.setText(doorAndWindowInstaller);
                    tinsmithDTTX.setText(tinsmith);
                    welderDTTX.setText(welder);






                    int costs = 0;
                    int carpenterCost = (Integer.parseInt(numberOfDays)) * (Integer.parseInt(carpenter)) * (Integer.parseInt(carpenterRate));
                    int laborerCost = (Integer.parseInt(numberOfDays)) * (Integer.parseInt(laborer)) * (Integer.parseInt(laborerRate));
                    int masonCost = (Integer.parseInt(numberOfDays)) * (Integer.parseInt(mason)) * (Integer.parseInt(masonRate));
                    int steelManCost = (Integer.parseInt(numberOfDays)) * (Integer.parseInt(steelMan)) * (Integer.parseInt(steelManRate));
                    int painterCost = (Integer.parseInt(numberOfDays)) * (Integer.parseInt(painter)) * (Integer.parseInt(painterRate));
                    int electricianCost = (Integer.parseInt(numberOfDays)) * (Integer.parseInt(electrician)) * (Integer.parseInt(electricianRate));
                    int plumberCost = (Integer.parseInt(numberOfDays)) * (Integer.parseInt(plumber)) * (Integer.parseInt(plumberRate));
                    int tileManCost = (Integer.parseInt(numberOfDays)) * (Integer.parseInt(tileMan)) * (Integer.parseInt(tileManRate));
                    int doorAndWindowInstallerCost = (Integer.parseInt(numberOfDays)) * (Integer.parseInt(doorAndWindowInstaller)) * (Integer.parseInt(doorAndWindowInstallerRate));
                    int tinsmithCost = (Integer.parseInt(numberOfDays)) * (Integer.parseInt(tinsmith)) * (Integer.parseInt(tinsmithRate));
                    int welderCost = (Integer.parseInt(numberOfDays)) * (Integer.parseInt(welder)) * (Integer.parseInt(welderRate));

                    if (option_subtitle.equals("Earthworks")) {
                        costs = laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Steel Reinforcement Work")) {
                        costs = steelManCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Reinforced Cement Concrete")) {
                        costs = masonCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Masonry Walls (Exterior)") || option_subtitle.equals("Masonry Walls (Interior)" )) {
                        costs = masonCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Plastering works (Exterior)") || option_subtitle.equals("Plastering works (Interior)" )) {
                        costs = masonCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Formworks")) {
                        costs = carpenterCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Ceiling Steel Frame") || option_subtitle.equals("Ceiling")) {
                        costs = carpenterCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Roofing Works (Trusses)")) {
                        costs = welderCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Roofing Works (GI Sheets)")) {
                        costs = tinsmithCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Roofing Works (Gutter)")) {
                        costs = tinsmithCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Flooring Works (EXC T&B)") || option_subtitle.equals("Flooring Works (T&B)") ) {
                        costs = tileManCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Painting Works (INT. SKIM COAT)") || option_subtitle.equals("Painting Works (EXT. SKIM COAT)") || option_subtitle.equals("Painting Works (INTERIOR)") || option_subtitle.equals("Painting Works (EXTERIOR)")) {
                        costs = painterCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Door Lockset") || option_subtitle.equals("Door Jamb") ) {
                        costs = doorAndWindowInstallerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Door")) {
                        costs = doorAndWindowInstallerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Windows")) {
                        costs = doorAndWindowInstallerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Plumbing Works")) {
                        costs = plumberCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Plumbing Fixtures")) {
                        costs = plumberCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Electrical Works (Roughing INS)")) {
                        costs = electricianCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Electrical Works (Cable Pulling)")) {
                        costs = electricianCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }
                    else if (option_subtitle.equals("Electrical Works (Fixtures)")) {
                        costs = electricianCost + laborerCost;
                        costOfLaborTXTVW.setText(costs+".00");
                    }





                    arrayPos++;
                }

            }
            catch (Exception ex){
                //Toast.makeText(context, ""+ex, Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void updateEstimate() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", activeEmail);
        phpData.put("project_id", project_id);
        phpData.put("category", option_subtitle);
        phpData.put("project", project);
        phpData.put("volume", volumeDTTX.getText().toString());
        phpData.put("preferred_time", preferredTimeDTTX.getText().toString());
        phpData.put("number_of_days", numberOfDaysDTTX.getText().toString());
        phpData.put("number_of_workers", numberOfWorkersDTTX.getText().toString());
        phpData.put("carpenter", carpenterDTTX.getText().toString());
        phpData.put("laborer", laborerDTTX.getText().toString());
        phpData.put("mason", masonDTTX.getText().toString());
        phpData.put("steel_man", steelManDTTX.getText().toString());
        phpData.put("painter", painterDTTX.getText().toString());
        phpData.put("electrician", electricianDTTX.getText().toString());
        phpData.put("plumber", plumberDTTX.getText().toString());
        phpData.put("tile_man", tileManDTTX.getText().toString());
        phpData.put("door_and_window_installer", doorAndWindowInstallerDTTX.getText().toString());
        phpData.put("tinsmith", tinsmithDTTX.getText().toString());
        phpData.put("welder", welderDTTX.getText().toString());

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                updateEstimateResponse = s;
                if (updateEstimateResponse.equals("submitted")) {
                    Toast.makeText(ProjectEstimationActivity.this, "Estimation was updated!", Toast.LENGTH_SHORT).show();
                    //finish();
                    showEstimation();
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
        phpTask.execute(phpServer + "android_update_estimate.php");

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
            option_subtitle = extras.getString("option_subtitle");
            carpenterRate = extras.getString("carpenter");
            laborerRate = extras.getString("laborer");
            masonRate = extras.getString("mason");
            steelManRate = extras.getString("steel_man");
            painterRate = extras.getString("painter");
            electricianRate = extras.getString("electrician");
            plumberRate = extras.getString("plumber");
            tileManRate = extras.getString("tile_man");
            doorAndWindowInstallerRate = extras.getString("door_and_window_installer");
            tinsmithRate = extras.getString("tinsmith");
            welderRate = extras.getString("welder");
        }
        else {
            finish();
        }
    }

}