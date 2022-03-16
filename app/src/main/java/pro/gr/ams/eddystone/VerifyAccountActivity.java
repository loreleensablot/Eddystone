package pro.gr.ams.eddystone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class VerifyAccountActivity extends AppCompatActivity {

    private TextView validationCodeDTTXT;
    private Button submitBTTN;
    private Button resendCodeBTTN;
    private String validationCode;
    private String checkValidationCodeResponse;
    private String activeEmail;
    private String hashKey;
    private String phpServer;
    private String checkResendValidationCodeResponse;
    private TextView switchAccountTXTVW;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "EddystonePreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        getUserData();
        getHashKey();
        getServer();
        getView();
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

    private void getView() {
        validationCodeDTTXT = (TextView)findViewById(R.id.validationCodeDTTXT);
        submitBTTN = (Button)findViewById(R.id.submitBTTN);
        resendCodeBTTN = (Button)findViewById(R.id.resendCodeBTTN);
        switchAccountTXTVW = (TextView)findViewById(R.id.switchAccountTXTVW);

        submitBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationCode = validationCodeDTTXT.getText().toString();
                if (validationCode.length() != 0) {
                    hideKeyboard();
                    checkValidationCode();
                }
                else {
                    Toast.makeText(VerifyAccountActivity.this, "Enter your code!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resendCodeBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendValidationCode();
            }
        });

        switchAccountTXTVW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSession();
                Intent i = new Intent(getApplicationContext(), LogOutActivity.class);
                startActivity(i);
                finish();
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

    private void checkValidationCode() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", activeEmail);
        phpData.put("validation_code", validationCode);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                checkValidationCodeResponse = s;
                if (checkValidationCodeResponse.equals("Incorrect code")) {
                    Toast.makeText(VerifyAccountActivity.this, "Incorrect code!", Toast.LENGTH_SHORT).show();
                }
                else if (checkValidationCodeResponse.equals("An error occured")) {
                    Toast.makeText(VerifyAccountActivity.this, "An error occured!", Toast.LENGTH_SHORT).show();
                }
                else if (checkValidationCodeResponse.equals("Account verified")) {
                    finish();
                    Intent i = new Intent(getApplicationContext(), LogInActivity.class);
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
        phpTask.execute(phpServer + "android_check_validation_code.php");

    }

    private void resendValidationCode() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", activeEmail);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                checkResendValidationCodeResponse = s;
                if (checkResendValidationCodeResponse.equals("Check your email")) {
                    Toast.makeText(VerifyAccountActivity.this, "Check your email.", Toast.LENGTH_SHORT).show();
                    resendCodeBTTN.setClickable(false);
                }
                else {
                    Toast.makeText(VerifyAccountActivity.this, "An error occured!", Toast.LENGTH_SHORT).show();
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
        phpTask.execute(phpServer + "android_resend_code.php");

    }

    private void hideKeyboard() {
        View view = (this).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)(this).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
