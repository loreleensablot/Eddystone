package pro.gr.ams.eddystone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    private String engineer_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        getUserData();
        getView();
        getServer();
        getHashKey();
        getNameOpposite();
        setSpinner();

    }

    private void getNameOpposite() {
        if (userLevel.equals("engineer")) {
            getClientName();
        }
        else if (userLevel.equals("client")) {
            getEngineerName();
        }
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

        projectNameTXTVW.setText(project);

        addOption("Excavation and Backfilling", "on progress");
        addOption("Steel Works", "on progress");
        addOption("Concreting", "on progress");
        addOption("Masonry Works", "on progress");
        addOption("Carpentry", "on progress");
        addOption("Finishing", "on progress");
        addOption("Plumbing", "on progress");
        addOption("Electrical", "on progress");
    }

    private void setSpinner() {
        if (userLevel.equals("engineer")) {
            spinner = findViewById(R.id.spinner);

            spinner.setOnItemSelectedListener(this);

            List<String> categories = new ArrayList<String>();
            categories.add("⋮");
            categories.add("Update Progress");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(dataAdapter);

            spinner.setVisibility(View.VISIBLE);

        }
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
                Intent i = new Intent(getApplicationContext(), ProjectSubcategoryActivity.class);
                i.putExtra("email", activeEmail);
                i.putExtra("user_level", userLevel);
                i.putExtra("project_id", project_id);
                i.putExtra("project", project);
                i.putExtra("progress", progress);
                i.putExtra("client_email", client_email);
                i.putExtra("option_title", title);
                startActivity(i);
            }
        });

        //layout.addView(imageAndUserData,0);
        layout.addView(imageAndUserData);



    }


    private void getEngineerName() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", engineer_email);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                //Log.d(TAG, s);
                if (s != null) {
                    if (s.length() > 0) {
                        subtitleTXTVW.setText(s+" | "+progress+"% finished");
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
        phpTask.execute(phpServer + "android_retrieve_name.php");
    }


    private void getClientName() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", client_email);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                //Log.d(TAG, s);
                if (s != null) {
                    if (s.length() > 0) {
                        subtitleTXTVW.setText(s+" | "+progress+"% finished");
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
        phpTask.execute(phpServer + "android_retrieve_name.php");
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
            engineer_email = extras.getString("engineer_email");
            client_email = extras.getString("client_email");
        }
        else {
            finish();
        }
    }

}