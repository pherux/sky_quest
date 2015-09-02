package com.pherux.skyquest.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pherux.skyquest.activities.StartActivity;
import com.pherux.skyquest.utils.Tracker;

/**
 * Created by Fernando Valdez on 8/18/15
 */
public class StartupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Tracker.getBooleanVal(Tracker.photoRunningKey, false)) {
            Intent App = new Intent(context, StartActivity.class);
            App.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(App);
        }
    }

}
