package pro.gr.ams.eddystone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class MainClientActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String mypreference = "EddystonePreference";
    private String activeEmail;
    private String hashKey;
    private String phpServer;
    private ProgressBar overviewPRGRSSBR;
    private TextView nameTXTVW;
    private TextView projectNameTXTVW;
    private TextView projectCountNewTXTVW;
    private LinearLayout homeNRLYT;
    private LinearLayout addLNRLYT;
    private LinearLayout tasksNRLYT;
    private LinearLayout projectsLNRLYT;
    private ImageView logOutMGVW;
    private LinearLayout accountLNRLYT;
    private TextView progressIntTXTVW;
    private LinearLayout progressLNRLYT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client);

        getUserData();
        getView();
        getServer();
        getHashKey();
        getName();
        getLatestProject();
        getLatestProjectProgress();
        getCountProjectNew();

    }


    private void getCountProjectNew() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", activeEmail);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                //Log.d(TAG, s);
                if (s != null) {
                    if (s.length() > 0) {
                        projectCountNewTXTVW.setText(s+" new");
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
        phpTask.execute(phpServer + "android_count_project_new.php");
    }

    private void getLatestProject() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", activeEmail);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                //Log.d(TAG, s);
                if (s != null) {
                    if (s.length() > 0) {
                        projectNameTXTVW.setText(s);
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
        phpTask.execute(phpServer + "android_retrieve_latest_project.php");
    }

    private void getLatestProjectProgress() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", activeEmail);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                //Log.d(TAG, s);
                if (s != null) {
                    if (s.length() > 0) {
                        progressIntTXTVW.setText(s+"%");
                        overviewPRGRSSBR.setMax(100);
                        overviewPRGRSSBR.setProgress(Integer.parseInt(s));
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
        phpTask.execute(phpServer + "android_retrieve_latest_project_progress.php");
    }

    private void getName() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", activeEmail);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                //Log.d(TAG, s);
                if (s != null) {
                    if (s.length() > 0) {
                        String lastWord = s.substring(s.lastIndexOf(" ")+1);
                        nameTXTVW.setText("Hello Client\n" + lastWord);
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

    private void getView() {
        overviewPRGRSSBR = (ProgressBar) findViewById(R.id.overviewPRGRSSBR);
        nameTXTVW = (TextView)findViewById(R.id.nameTXTVW);
        projectNameTXTVW = (TextView)findViewById(R.id.projectNameTXTVW);
        projectCountNewTXTVW = (TextView)findViewById(R.id.projectCountNewTXTVW);
        homeNRLYT = (LinearLayout)findViewById(R.id.homeNRLYT);
        tasksNRLYT = (LinearLayout)findViewById(R.id.tasksNRLYT);
        projectsLNRLYT = (LinearLayout)findViewById(R.id.projectsLNRLYT);
        logOutMGVW = (ImageView)findViewById(R.id.logOutMGVW);
        accountLNRLYT = (LinearLayout)findViewById(R.id.accountLNRLYT);
        progressIntTXTVW = (TextView)findViewById(R.id.progressIntTXTVW);
        progressLNRLYT = (LinearLayout)findViewById(R.id.progressLNRLYT);

        homeNRLYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You're already at home activity.", Toast.LENGTH_SHORT).show();
            }
        });

        projectsLNRLYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListOfProjectsActivity.class);
                i.putExtra("email", activeEmail);
                i.putExtra("user_level", "client");
                i.putExtra("intent_activity", "ProjectDetailsActivity");
                startActivity(i);
            }
        });

        progressLNRLYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListOfProjectsActivity.class);
                i.putExtra("email", activeEmail);
                i.putExtra("user_level", "client");
                i.putExtra("intent_activity", "ProjectDetailsActivity");
                startActivity(i);
            }
        });

        tasksNRLYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListOfProjectsActivity.class);
                i.putExtra("email", activeEmail);
                i.putExtra("user_level", "client");
                i.putExtra("intent_activity", "ProjectDetailsActivity");
                startActivity(i);
            }
        });

        logOutMGVW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainClientActivity.this);

                //builder.setTitle("Log Out.");

                builder.setMessage("Log Out");

                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeSession();

                        Thread thread=new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(3000);

                                    Intent i = new Intent(getApplicationContext(), LogOutActivity.class);
                                    startActivity(i);
                                    finish();

                                }
                                catch (Exception ex)
                                {
                                }
                            }
                        };
                        thread.start();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        accountLNRLYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                i.putExtra("email", activeEmail);
                startActivity(i);
            }
        });
    }

    private void removeSession() {
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Logged", "false");
        editor.commit();
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
        }
        else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        getLatestProject();
        getLatestProjectProgress();
        getCountProjectNew();
        super.onResume();
    }
}