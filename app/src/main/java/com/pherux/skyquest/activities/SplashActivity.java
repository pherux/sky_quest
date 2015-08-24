package com.pherux.skyquest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pherux.skyquest.R;

/**
 * Created by Fernando Valdez on 8/23/15
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
    }

    @SuppressWarnings("unused")
    public void onTrackerClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressWarnings("unused")
    public void onMonitorClicked(View view) {
        Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show();
    }
}
