package com.pherux.skyquest.activities;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.gc.materialdesign.views.ButtonRectangle;
import com.pherux.skyquest.Constants;
import com.pherux.skyquest.R;
import com.pherux.skyquest.managers.Persistence;
import com.pherux.skyquest.utils.Utils;

public class SettingsActivity extends Activity {

    public static final String ARG_DEVICE_NAME = SettingsActivity.class.getName() + "ARG_DEVICE_NAME";
    public static final String ARG_PHONE_NUMBER = SettingsActivity.class.getName() + "ARG_PHONE_NUMBER";
    public static final String ARG_USE_CAMERA = SettingsActivity.class.getName() + "ARG_USE_CAMERA";
    public static final String ARG_CAMERA_INTERVAL = SettingsActivity.class.getName() + "ARG_CAMERA_INTERVAL";
    public static final String ARG_WEB_TRACKER_INTERVAL = SettingsActivity.class.getName() + "ARG_WEB_TRACKER_INTERVAL";
    public static final String ARG_USE_SMS = SettingsActivity.class.getName() + "ARG_USE_SMS";
    public static final String ARG_USE_WEB_TRACKER = SettingsActivity.class.getName() + "ARG_USE_WEB_TRACKER";
    public static final String ARG_SMS_INTERVAL = SettingsActivity.class.getName() + "ARG_SMS_INTERVAL";

    private EditText deviceNameEditText;
    private EditText phoneNumberEditText;
    private EditText cameraIntervalEditText;
    private EditText smsIntervalEditText;
    private EditText webTrackerIntervalEditText;
    private Switch useCameraSwitch;
    private Switch useSMSSwitch;
    private Switch useWebTrackerSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        deviceNameEditText = (EditText) findViewById(R.id.activity_activity_device_name);
        useCameraSwitch = (Switch) findViewById(R.id.activity_activity_use_camera);
        cameraIntervalEditText = (EditText) findViewById(R.id.activity_activity_camera_interval);

        useWebTrackerSwitch = (Switch) findViewById(R.id.activity_activity_use_web_tracker);
        webTrackerIntervalEditText = (EditText) findViewById(R.id.activity_activity_web_tracker_interval);

        useSMSSwitch = (Switch) findViewById(R.id.activity_activity_use_sms);
        smsIntervalEditText = (EditText) findViewById(R.id.activity_activity_sms_interval);
        phoneNumberEditText = (EditText) findViewById(R.id.activity_activity_phone_number);
        ButtonRectangle saveButton = (ButtonRectangle) findViewById(R.id.activity_activity_save);

        String deviceName = Persistence.getStringVal(ARG_DEVICE_NAME, Utils.getDeviceName());
        deviceNameEditText.setText(deviceName);

        boolean useCamera = Persistence.getBooleanVal(ARG_USE_CAMERA, Constants.USE_FEATURE_PHOTOS);
        int cameraInterval = Persistence.getIntVal(ARG_CAMERA_INTERVAL, Constants.INTERVAL_PHOTO_SECONDS);
        cameraIntervalEditText.setText(String.valueOf(cameraInterval));
        useCameraSwitch.setChecked(useCamera);
        cameraIntervalEditText.setEnabled(useCamera);

        boolean useWebTracker = Persistence.getBooleanVal(ARG_USE_WEB_TRACKER, Constants.USE_FEATURE_TRACKER);
        int webTrackerInterval = Persistence.getIntVal(ARG_WEB_TRACKER_INTERVAL, Constants.INTERVAL_TRACKER_SECONDS);
        useWebTrackerSwitch.setChecked(useWebTracker);
        webTrackerIntervalEditText.setText(String.valueOf(webTrackerInterval));
        webTrackerIntervalEditText.setEnabled(useWebTracker);

        boolean useSMS = Persistence.getBooleanVal(ARG_USE_SMS, Constants.USE_FEATURE_SMS);
        int smsInterval = Persistence.getIntVal(ARG_SMS_INTERVAL, Constants.INTERVAL_SMS_SECONDS);
        String phoneNumber = Persistence.getStringVal(ARG_PHONE_NUMBER, "");
        useSMSSwitch.setChecked(useSMS);
        smsIntervalEditText.setEnabled(useSMS);
        smsIntervalEditText.setText(String.valueOf(smsInterval));
        phoneNumberEditText.setText(phoneNumber);
        phoneNumberEditText.setEnabled(useSMS);


        useSMSSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                smsIntervalEditText.setEnabled(b);
                phoneNumberEditText.setEnabled(b);
            }
        });

        useWebTrackerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                webTrackerIntervalEditText.setEnabled(b);
            }
        });

        useCameraSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cameraIntervalEditText.setEnabled(b);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        saveButton.requestFocus();
    }

    private boolean areFieldsValid() {
        if (useSMSSwitch.isChecked()) {
            if (!isValidMobile(phoneNumberEditText.getText().toString())) {
                phoneNumberEditText.setError("Wrong phone number format");
                phoneNumberEditText.requestFocus();
                return false;
            } else if (!isInteger(smsIntervalEditText.getText().toString())) {
                smsIntervalEditText.setError("SMS interval number is not a valid");
                smsIntervalEditText.requestFocus();
                return false;
            }
        }
        if (useCameraSwitch.isChecked()) {
            if (!isInteger(cameraIntervalEditText.getText().toString())) {
                cameraIntervalEditText.setError("SMS interval number is not a valid");
                cameraIntervalEditText.requestFocus();
                return false;
            }
        }
        if (TextUtils.isEmpty(deviceNameEditText.getText().toString())) {
            deviceNameEditText.setError("Device name invalid");
            deviceNameEditText.requestFocus();
            return false;
        }
        if (useWebTrackerSwitch.isChecked()) {
            if (!isInteger(webTrackerIntervalEditText.getText().toString())) {
                webTrackerIntervalEditText.setError("Value is not valid");
                webTrackerIntervalEditText.requestFocus();
                return false;
            }
        }
        return true;
    }


    private boolean isValidMobile(String phone) {
        return !TextUtils.isEmpty(phone) && phone.length() >= 10 && android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private void save() {
        if (areFieldsValid()) {
            Persistence.putBooleanVal(ARG_USE_SMS, useSMSSwitch.isChecked());
            Persistence.putBooleanVal(ARG_USE_CAMERA, useCameraSwitch.isChecked());
            Persistence.putBooleanVal(ARG_USE_WEB_TRACKER, useWebTrackerSwitch.isChecked());

            Persistence.putIntVal(ARG_CAMERA_INTERVAL, Integer.parseInt(cameraIntervalEditText.getText().toString()));
            Persistence.putIntVal(ARG_SMS_INTERVAL, Integer.parseInt(smsIntervalEditText.getText().toString()));
            Persistence.putIntVal(ARG_WEB_TRACKER_INTERVAL, Integer.parseInt(webTrackerIntervalEditText.getText().toString()));

            Persistence.putStringVal(ARG_PHONE_NUMBER, phoneNumberEditText.getText().toString());
            Persistence.putStringVal(ARG_DEVICE_NAME, deviceNameEditText.getText().toString());

            onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}
