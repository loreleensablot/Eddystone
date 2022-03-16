package pro.gr.ams.eddystone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {


    private String phpServer;
    private String hashKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);

        /*
        Thread thread=new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);

                    Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                    startActivity(i);
                    finish();

                }
                catch (Exception ex)
                {
                }
            }
        };
        thread.start();
         */

        try {
            getHashKey();
            getServer();
            getSetUp();
        }
        catch (Exception ex) {
            //Toast.makeText(this, ""+ex, Toast.LENGTH_SHORT).show();
        }

    }

    private void getSetUp() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                //Log.d(TAG, s);
                if (s.contains("true")) {
                    logInActivity();
                } else {
                    getSetUpMessage();
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
        phpTask.execute(phpServer + "android_setup.php");
    }

    private void getSetUpMessage() {
        HashMap<String, String> phpData = new HashMap<>();
        phpData.put("hashKey", hashKey);

        PostResponseAsyncTask phpTask = new PostResponseAsyncTask(this, phpData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                //Log.d(TAG, s);
                if (s != null) {
                    if (s.length() > 0) {
                        Toast.makeText(getApplicationContext(), "" + s, Toast.LENGTH_LONG).show();
                    }
                    else {
                        if(!(CheckNetwork.isInternetAvailable(getApplicationContext())))
                        {
                            Toast.makeText(getApplicationContext(), "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    finish();
                } else {
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
        phpTask.execute(phpServer + "android_setup_message.php");
    }

    private void logInActivity() {
        setContentView(R.layout.activity_splash);
        Thread thread=new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1600);

                    Intent in = new Intent(getApplicationContext(), LogInActivity.class);
                    startActivity(in);
                    finish();

                }
                catch (Exception ex)
                {
                }
            }
        };
        thread.start();
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
}