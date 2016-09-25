package com.pherux.skyquest.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pherux.skyquest.SkyQuestTrackerService;
import com.pherux.skyquest.managers.Persistence;
import com.pherux.skyquest.receivers.AlarmCameraReceiver;
import com.pherux.skyquest.utils.Tracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.pherux.skyquest.Constants.INTERVAL_PHOTO_SECONDS;
import static com.pherux.skyquest.Constants.USE_FEATURE_PHOTOS;

/**
 * Created by Fernando Valdez on 8/18/15
 */
public class StartActivity extends Activity {

    private final Activity me = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (USE_FEATURE_PHOTOS) {
            AlarmManager m_alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(AlarmCameraReceiver.ALARM_ACTION_NAME);
            PendingIntent alarmPI = PendingIntent.getBroadcast(me, Tracker.alarmId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            long intervalMillis = INTERVAL_PHOTO_SECONDS * 1000;
            m_alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, intervalMillis, intervalMillis, alarmPI);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            Persistence.putStringVal(Tracker.photoPrefixKey, "SkyQuest_" + timeStamp + "_");
            Persistence.putBooleanVal(Tracker.photoRunningKey, true);
            Persistence.putIntVal(Tracker.photoCountKey, 0);
        }

        Intent serviceIntent = new Intent(me, SkyQuestTrackerService.class);
        me.startService(serviceIntent);

        me.finish();
    }
}
