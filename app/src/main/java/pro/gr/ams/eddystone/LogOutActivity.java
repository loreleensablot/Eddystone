package pro.gr.ams.eddystone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class LogOutActivity extends AppCompatActivity {

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        spinner = (ProgressBar)findViewById(R.id.logOutPRGRSSBR);

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

    }
}