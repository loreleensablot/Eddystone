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

public class ChangePasswordActivity extends AppCompatActivity {

    private String activeEmail;
    private String hashKey;
    private String phpServer;
    private EditText oldPasswordDTTXT;
    private EditText newPasswordDTTXT;
    private Button updateBTTN;
    private String oldPass;
    private String newPass;
    private String checkUpdateResponse;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "EddystonePreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getUserData();
        getView();
        getServer();
        getHashKey();
    }

    private void getView() {
        oldPasswordDTTXT = (EditText)findViewById(R.id.oldPasswordDTTXT);
        newPasswordDTTXT = (EditText)findViewById(R.id.newPasswordDTTXT);
        updateBTTN = (Button)findViewById(R.id.updateBTTN);

        updateBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPass = oldPasswordDTTXT.getText().toString();
                newPass = newPasswordDTTXT.getText().toString();

                if (oldPass.length() == 0 || newPass.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Complete required fields!", Toast.LENGTH_SHORT).show();
                }
                else if (oldPass.equals(newPass)) {
                    Toast.makeText(getApplicationContext(), "Existing password detected!", Toast.LENGTH_SHORT).show();
                }
                else if (newPass.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must be atleast 8 characters.", Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String, String> phpData = new HashMap<>();
                    phpData.put("hashKey", hashKey);
                    phpData.put("email", activeEmail);
                    phpData.put("oldpass", oldPass);
                    phpData.put("newpass", newPass);

                    PostResponseAsyncTask phpTask = new PostResponseAsyncTask(ChangePasswordActivity.this, phpData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            checkUpdateResponse = s;

                            if (checkUpdateResponse.equals("updated")) {
                                Toast.makeText(getApplicationContext(), "Password updated.", Toast.LENGTH_SHORT).show();

                                sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("Password", newPass);
                                editor.commit();

                                oldPasswordDTTXT.setText("");
                                newPasswordDTTXT.setText("");

                                finish();

                            }
                            else if (checkUpdateResponse.equals("Password didn't match")) {
                                Toast.makeText(getApplicationContext(), "Password didn't match.", Toast.LENGTH_SHORT).show();
                            }
                            else if (checkUpdateResponse.equals("An error occurred")) {
                                Toast.makeText(getApplicationContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
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
                    phpTask.execute(phpServer + "android_update_password.php");
                }

            }
        });
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