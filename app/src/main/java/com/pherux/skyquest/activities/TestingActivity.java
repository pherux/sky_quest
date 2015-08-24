package com.pherux.skyquest.activities;

import android.app.Activity;
import android.os.Bundle;

import com.pherux.skyquest.R;

/**
 * Created by Fernando Valdez on 8/23/15
 */
public class TestingActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
