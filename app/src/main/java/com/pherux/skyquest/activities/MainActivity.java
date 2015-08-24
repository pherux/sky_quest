package com.pherux.skyquest.activities;

import android.app.Activity;
import android.content.Intent;
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

    private final Activity me = this;
    private Toolbar toolbar;

    private void refreshHeartbeat() {
      /*
        TextView nameText = (TextView) findViewById(R.id.main_trackernametext);
        nameText.setText(Utils.getStringVal(Utils.trackerNameKey, ""));

        TextView heartbeatText = (TextView) findViewById(R.id.main_heartbeattext);
        String heartbeat = "Phone: " + Utils.getStringVal(Utils.pingSMSKey, "") + "\n"
                + "URL: " + Utils.getStringVal(Utils.trackerUrlKey, "") + "\n"
                + Utils.getStringVal(Utils.gpsStatusKey, "") + "\n"
                + Utils.getStringVal(Utils.smsStatusKey, "") + "\n"
                + Utils.getStringVal(Utils.trackerStatusKey, "") + "\n"
                + Utils.getStringVal(Utils.photoStatusKey, "");

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
        testFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTestingFeaturesClicked();
            }
        });

        /*
        Button start = (Button) findViewById(R.id.main_startbutton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidClick()) {
                    Intent name = new Intent(MainActivity.this, StartActivity.class);
                    startActivity(name);
                }
            }
        });

        Button stop = (Button) findViewById(R.id.main_stopbutton);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidClick()) {
                    Intent name = new Intent(MainActivity.this, StopActivity.class);
                    startActivity(name);
                }
            }
        });

        Button takePhoto = (Button) findViewById(R.id.main_photobutton);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidClick()) {
                    Intent name = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(name);
                }
            }
        });

        Button sendSMS = (Button) findViewById(R.id.main_smsbutton);
        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidClick()) {
                    //Utils.sendLocationSMS();
                    Utils.sendTrackerPing();
                }
            }
        });

        Button reboot = (Button) findViewById(R.id.main_rebootbutton);
        reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidClick()) {
                    Utils.reboot();
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
                    Utils.loadConfig();
                    refreshHeartbeat();
                }
            }
        });
        */

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
