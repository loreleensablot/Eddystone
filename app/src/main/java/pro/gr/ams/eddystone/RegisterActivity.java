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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private String user_level;
    private EditText fullnameDTTXT;
    private EditText homeaddressDTTXT;
    private EditText contactnumberDTTXT;
    private EditText emailDTTXT;
    private EditText passwordDTTXT;
    private EditText confirmPasswordDTTXT;
    private Button registerBTTN;
    private Button loginBTTN;
    private String fullname;
    private String homeaddress;
    private String contactnumber;
    private String email;
    private String password;
    private String confirmPassword;
    private String hashKey;
    private String phpServer;
    private String registrationResponse = "";
    SharedPreferences sharedpreferences;
    public static final String mypreference = "EddystonePreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        try {
            getHashKey();
            setSpinner();
            getServer();
            getView();
        }
        catch (Exception ex) {
            Toast.makeText(this, ""+ex, Toast.LENGTH_SHORT).show();
        }
    }

    private void getView() {
        fullnameDTTXT = (EditText)findViewById(R.id.fullnameDTTXT);
        homeaddressDTTXT = (EditText)findViewById(R.id.homeaddressDTTXT);
        contactnumberDTTXT = (EditText)findViewById(R.id.contactnumberDTTXT);
        emailDTTXT = (EditText)findViewById(R.id.emailDTTXT);
        passwordDTTXT = (EditText)findViewById(R.id.passwordDTTXT);
        confirmPasswordDTTXT = (EditText)findViewById(R.id.confirmPasswordDTTXT);
        registerBTTN = (Button)findViewById(R.id.registerBTTN);
        loginBTTN = (Button)findViewById(R.id.loginBTTN);

        loginBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(i);
                finish();
            }
        });

        registerBTTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = fullnameDTTXT.getText().toString();
                homeaddress = homeaddressDTTXT.getText().toString();
                contactnumber = contactnumberDTTXT.getText().toString();
                email = emailDTTXT.getText().toString();
                password = passwordDTTXT.getText().toString();
                confirmPassword = confirmPasswordDTTXT.getText().toString();

                if (fullname.trim().length() == 0 || homeaddress.trim().length() == 0 || contactnumber.trim().length() == 0 || email.trim().length() == 0 || password.trim().length() == 0 || confirmPassword.trim().length() == 0) {
                    Snackbar.make(v, "Complete your information!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if ((contactnumber.contains("+639") == false && contactnumber.length() != 11) || (contactnumber.contains("+639") == true && contactnumber.length() != 13)) {
                    Snackbar.make(v, "Invalid phone number", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if ((email.length() != (email.replaceAll("@","").length() + 1)) || (email.charAt(email.length() - 1)+"").equals("@") == true) {
                    Snackbar.make(v, "Invalid email", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if ((password.length() < 8)) {
                    Snackbar.make(v, "Password must be atleast 8 characters!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if ((password.equals(confirmPassword) == false)) {
                    Snackbar.make(v, "Password didn't match!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    hideKeyboard();
                    registerUser();
                }

            }
        });
    }

    private void registerUser() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);
        phpData.put("email", email);
        phpData.put("password", password);
        phpData.put("fullname", fullname);
        phpData.put("user_level", user_level.toLowerCase());
        phpData.put("password", password);
        phpData.put("contact_number", contactnumber);
        phpData.put("home_address", homeaddress);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                registrationResponse = s;
                if (registrationResponse.equals("submitted")) {
                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    saveSession();
                    Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
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
        phpTask.execute(phpServer + "android_register_account.php");

    }


    private void saveSession() {
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Logged", "true");
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.commit();
    }


    private void setSpinner() {
        spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Engineer");
        categories.add("Client");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        user_level=item;
    }
    public void onNothingSelected(AdapterView<?> arg0) {

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

    private void hideKeyboard() {
        View view = (this).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)(this).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}
