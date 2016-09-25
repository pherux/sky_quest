package com.pherux.skyquest.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.pherux.skyquest.R;

public class MainActivity extends AppCompatActivity {

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
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
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


    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissions granted! Enjoy the launching.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Application will not work until ALL permissions are granted", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void onStartClicked() {
        if (checkPermissions()) {
            isStarted = true;
            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        boolean hasMissingPermissions = false;
        if (this.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            hasMissingPermissions = true;
        }
        if (this.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            hasMissingPermissions = true;
        }
        if (this.checkCallingOrSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            hasMissingPermissions = true;
        }
        if (this.checkCallingOrSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            hasMissingPermissions = true;
        }
        if (this.checkCallingOrSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            hasMissingPermissions = true;
        }
        if (this.checkCallingOrSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED) {
            hasMissingPermissions = true;
        }

        return !hasMissingPermissions;
    }

    private void onStopClicked() {
        isStarted = false;
        startButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.GONE);
        Intent name = new Intent(MainActivity.this, StopActivity.class);
        startActivity(name);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.SEND_SMS,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.RECEIVE_BOOT_COMPLETED}, 1);
    }

    private void onTestingFeaturesClicked() {
        if (checkPermissions()) {
            Intent intent = new Intent(this, TestingActivity.class);
            startActivity(intent);
            this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            requestPermissions();
        }
    }

    @Override
    protected void onResume() {
        refreshHeartbeat();
        super.onResume();
    }
}
