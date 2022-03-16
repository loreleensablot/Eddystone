package pro.gr.ams.eddystone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText passwordDTTXT;
    private EditText emailDTTXT;
    private Button loginBTTN;
    private Button registerBTTN;
    private String phpServer;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "EddystonePreference";
    private String logged;
    private String email;
    private String password;
    private LinearLayout logInLNRLYT;
    private String hashKey;
    private String checkAccountValidatedResponse;
    private String checkUserLevelResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        try {
            getHashKey();
            getServer();
            getView();
            checkSession();
        }
        catch (Exception ex) {
            //Toast.makeText(this, ""+ex, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        email = emailDTTXT.getText().toString();
        password =  passwordDTTXT.getText().toString();

        if (email.length() != 0 && password.length() != 0) {

            hideKeyboard();

            HashMap<String, String> phpData = new HashMap<>();
            phpData.put("hashKey", hashKey);
            phpData.put("email", email);
            phpData.put("password", password);

            PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    //Log.d(TAG, s);
                    if (s.contains("Log In Successful!")) {
                        saveSession();
                        checkAccountValidated();
                        //Toast.makeText(getApplicationContext(), "" + s, Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(getApplicationContext(), "" + s, Toast.LENGTH_LONG).show();
                        logInLNRLYT.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Log In Failed!", Toast.LENGTH_LONG).show();
                        removeSession();
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
            phpTask.execute(phpServer + "android_login.php");
        }
        else {
            Toast.makeText(this, "Complete your details!", Toast.LENGTH_LONG).show();
        }
    }


    private void checkAccountValidated() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", email);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                checkAccountValidatedResponse = s;
                if (checkAccountValidatedResponse.equals("validated")) {
                    checkUserLevel();
                }
                else if (checkAccountValidatedResponse.equals("not validated")) {
                    finish();
                    Intent i = new Intent(getApplicationContext(), VerifyAccountActivity.class);
                    i.putExtra("email", email);
                    startActivity(i);
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
        phpTask.execute(phpServer + "android_check_account_validated.php");

    }


    private void checkUserLevel() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", email);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                checkUserLevelResponse = s;

                if (checkUserLevelResponse.equals("engineer")) {
                    Intent i = new Intent(getApplicationContext(), MainEngineerActivity.class);
                    i.putExtra("email", email);
                    startActivity(i);
                    finish();
                }
                else if (checkUserLevelResponse.equals("client")) {
                    Intent i = new Intent(getApplicationContext(), MainClientActivity.class);
                    i.putExtra("email", email);
                    startActivity(i);
                    finish();
                }
                else {
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
        phpTask.execute(phpServer + "android_check_user_level.php");

    }

    private void hideKeyboard() {
        View view = (this).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)(this).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void saveSession() {
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Logged", "true");
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.commit();
    }

    private void checkSession() {
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if (sharedpreferences.contains("Logged")) {
            logged = sharedpreferences.getString("Logged", "");
            if (logged.equals("true")) {
                emailDTTXT.setText(sharedpreferences.getString("Email", ""));
                passwordDTTXT.setText(sharedpreferences.getString("Password", ""));
                loginBTTN.performClick();
                logInLNRLYT.setVisibility(View.INVISIBLE);
            }
        }
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

    private void getView() {
        emailDTTXT = (EditText)findViewById(R.id.emailDTTXT);
        passwordDTTXT = (EditText)findViewById(R.id.passwordDTTXT);
        loginBTTN = (Button)findViewById(R.id.loginBTTN);
        registerBTTN = (Button)findViewById(R.id.registerBTTN);
        logInLNRLYT = (LinearLayout)findViewById(R.id.logInLNRLYT);
        loginBTTN.setOnClickListener(this);
        registerBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
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

}
