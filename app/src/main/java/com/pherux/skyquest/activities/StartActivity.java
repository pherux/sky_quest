package com.pherux.skyquest.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pherux.skyquest.SkyQuestService;
import com.pherux.skyquest.managers.Persistence;
import com.pherux.skyquest.receivers.AlarmReceiver;
import com.pherux.skyquest.utils.Tracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Fernando Valdez on 8/18/15
 */
public class StartActivity extends Activity {
    public static int photoIntervalSeconds = 15;
    private final Activity me = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlarmManager m_alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(AlarmReceiver.ALARM_ACTION_NAME);
        PendingIntent alarmPI = PendingIntent.getBroadcast(me, Tracker.alarmId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long intervalMillis = photoIntervalSeconds * 1000l;
        m_alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, intervalMillis, intervalMillis, alarmPI);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        Persistence.putStringVal(Tracker.photoPrefixKey, "SkyQuest_" + timeStamp + "_");
        Persistence.putBooleanVal(Tracker.photoRunningKey, true);
        Persistence.putIntVal(Tracker.photoCountKey, 0);

        Intent serviceIntent = new Intent(me, SkyQuestService.class);
        me.startService(serviceIntent);

        me.finish();
    }
}
