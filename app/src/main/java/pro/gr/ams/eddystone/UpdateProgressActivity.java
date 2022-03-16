package pro.gr.ams.eddystone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class UpdateProgressActivity extends AppCompatActivity {

    private String activeEmail;
    private String hashKey;
    private String phpServer;
    private EditText projectDTTXT;
    private EditText progressDTTXT;
    private Button updateBTTN;
    private String projectName;
    private String clientEmail;
    private String checkCreateProjectResponse;
    private String projectProgress;
    private String userLevel;
    private String progress;
    private String client_email;
    private String project;
    private String project_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_progress);

        getUserData();
        getView();
        getServer();
        getHashKey();
    }

    private void getView() {
        projectDTTXT = (EditText)findViewById(R.id.projectDTTXT);
        progressDTTXT = (EditText)findViewById(R.id.progressDTTXT);
        updateBTTN = (Button)findViewById(R.id.updateBTTN);

        projectDTTXT.setText(project);
        projectDTTXT.setFocusable(false);

        updateBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectName = projectDTTXT.getText().toString();
                projectProgress = progressDTTXT.getText().toString();

                if (projectName.length() == 0 || projectProgress.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Complete required fields!", Toast.LENGTH_SHORT).show();
                }
                else if (Integer.parseInt(projectProgress) > 100) {
                    Toast.makeText(getApplicationContext(), "100 is the maximum value!", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateProject();
                }
            }
        });

    }



    private void updateProject() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("engineer_email", activeEmail);
        phpData.put("project_id", project_id);
        phpData.put("progress", projectProgress);


        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                checkCreateProjectResponse = s;

                if (checkCreateProjectResponse.equals("Please provide worker's rate first.")) {
                    Toast.makeText(UpdateProgressActivity.this, "Please provide worker's rate first.", Toast.LENGTH_SHORT).show();
                }
                else if (checkCreateProjectResponse.equals("An error occured")) {
                    Toast.makeText(UpdateProgressActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                }
                else if (checkCreateProjectResponse.equals("Project updated")) {
                    Toast.makeText(UpdateProgressActivity.this, "Refresh the activity to retrieve changes.", Toast.LENGTH_LONG).show();
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
        phpTask.execute(phpServer + "android_update_project.php");

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
            client_email = extras.getString("client_email");
        }
        else {
            finish();
        }
    }
}