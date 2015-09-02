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

/**
 * Created by Fernando Valdez on 8/18/15
 */
public class StopActivity extends Activity {


    private final Activity me = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlarmManager m_alarmMgr;

        m_alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(AlarmReceiver.ALARM_ACTION_NAME);
        PendingIntent alarmPI = PendingIntent.getBroadcast(me, Tracker.alarmId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        m_alarmMgr.cancel(alarmPI);

        Intent serviceIntent = new Intent(me, SkyQuestService.class);
        me.stopService(serviceIntent);

        Persistence.putBooleanVal(Tracker.photoRunningKey, false);
        me.finish();
    }

}