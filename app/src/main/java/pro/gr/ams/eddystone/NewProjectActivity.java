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

public class NewProjectActivity extends AppCompatActivity {

    private String activeEmail;
    private String hashKey;
    private String phpServer;
    private EditText projectDTTXT;
    private EditText clientEddystoneAccountDTTXT;
    private Button addBTTN;
    private String projectName;
    private String clientEmail;
    private String checkCreateProjectResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        getUserData();
        getView();
        getServer();
        getHashKey();
    }

    private void getView() {
        projectDTTXT = (EditText)findViewById(R.id.projectDTTXT);
        clientEddystoneAccountDTTXT = (EditText)findViewById(R.id.clientEddystoneAccountDTTXT);
        addBTTN = (Button)findViewById(R.id.addBTTN);

        addBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectName = projectDTTXT.getText().toString();
                clientEmail = clientEddystoneAccountDTTXT.getText().toString();

                if (projectName.length() == 0 || clientEmail.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Complete required fields!", Toast.LENGTH_SHORT).show();
                }
                else {
                    createProject();
                }
            }
        });
    }



    private void createProject() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("engineer_email", activeEmail);
        phpData.put("client_email", clientEmail);
        phpData.put("project", projectName);


        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                checkCreateProjectResponse = s;

                if (checkCreateProjectResponse.equals("Please provide worker's rate first.")) {
                    Toast.makeText(NewProjectActivity.this, "Please provide worker's rate first.", Toast.LENGTH_SHORT).show();
                }
                else if (checkCreateProjectResponse.equals("An error occured")) {
                    Toast.makeText(NewProjectActivity.this, "An error occured!", Toast.LENGTH_SHORT).show();
                }
                else if (checkCreateProjectResponse.equals("Client email does not exists")) {
                    Toast.makeText(NewProjectActivity.this, "Client email does not exists!", Toast.LENGTH_SHORT).show();
                }
                else if (checkCreateProjectResponse.equals("Project created")) {
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
        phpTask.execute(phpServer + "android_create_project.php");

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
}