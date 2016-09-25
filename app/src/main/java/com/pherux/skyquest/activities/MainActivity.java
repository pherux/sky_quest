package com.pherux.skyquest.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.pherux.skyquest.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private boolean isStarted;
    private ButtonRectangle startButton;
    private ButtonRectangle stopButton;

    private void refreshHeartbeat() {
      /*
        TextView nameText = (TextView) findViewById(R.id.main_trackernametext);
        nameText.setText(Tracker.getStringVal(Tracker.trackerNameKey, ""));

        TextView heartbeatText = (TextView) findViewById(R.id.main_heartbeattext);
        String heartbeat = "Phone: " + Tracker.getStringVal(Tracker.pingSMSKey, "") + "\n"
                + "URL: " + Tracker.getStringVal(Tracker.trackerUrlKey, "") + "\n"
                + Tracker.getStringVal(Tracker.gpsStatusKey, "") + "\n"
                + Tracker.getStringVal(Tracker.smsStatusKey, "") + "\n"
                + Tracker.getStringVal(Tracker.trackerStatusKey, "") + "\n"
                + Tracker.getStringVal(Tracker.photoStatusKey, "");

        heartbeatText.setText(heartbeat);
        */
    }

    private Boolean isValidClick() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            openSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void openSettings() {
        Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        ButtonRectangle testFeatures = (ButtonRectangle) findViewById(R.id.activity_main_test_features);
        startButton = (ButtonRectangle) findViewById(R.id.activity_main_start);
        stopButton = (ButtonRectangle) findViewById(R.id.activity_main_stop);

        testFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTestingFeaturesClicked();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartClicked();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStopClicked();
            }
        });


        /*

        Button sendSMS = (Button) findViewById(R.id.main_smsbutton);
        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidClick()) {
                    //Tracker.sendLocationSMS();
                    Tracker.sendTrackerPing();
                }
            }
        });

        Button reboot = (Button) findViewById(R.id.main_rebootbutton);
        reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidClick()) {
                    Tracker.reboot();
                }
            }
        });

        Button quit = (Button) findViewById(R.id.main_quitbutton);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidClick()) {
                    me.finish();
                }
            }
        });


        Button loadConfig = (Button) findViewById(R.id.main_configreload);
        loadConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidClick()) {
                    Tracker.loadConfig();
                    refreshHeartbeat();
                }
            }
        });
        */

    }


    private void onStartClicked() {
        isStarted = true;
        startButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    private void onStopClicked() {
        isStarted = false;
        startButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.GONE);
        Intent name = new Intent(MainActivity.this, StopActivity.class);
        startActivity(name);
    }

    private void onTestingFeaturesClicked() {
        Intent intent = new Intent(this, TestingActivity.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onResume() {
        refreshHeartbeat();
        super.onResume();
    }
}
