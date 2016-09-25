package com.pherux.skyquest.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pherux.skyquest.activities.CameraActivity;

/**
 * Created by Fernando Valdez on 8/18/15
 */
public class AlarmCameraReceiver extends BroadcastReceiver {

    public static final String ALARM_ACTION_NAME = "com.pherux.skyquest.broadcast.ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Handle the alarm broadcast
        if (ALARM_ACTION_NAME.equals(intent.getAction())) {
            // Launch the alarm popup dialog
            Intent alarmIntent = new Intent("android.intent.action.MAIN");

            alarmIntent.setClass(context, CameraActivity.class);
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Pass on the alarm ID as extra data
            alarmIntent.putExtra("AlarmID", intent.getIntExtra("AlarmID", -1));

            // Start the popup activity
            context.startActivity(alarmIntent);
        }
    }

}