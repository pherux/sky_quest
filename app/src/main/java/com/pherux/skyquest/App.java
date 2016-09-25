package com.pherux.skyquest;

import android.app.Application;
import android.content.Context;

import com.pherux.skyquest.activities.SettingsActivity;
import com.pherux.skyquest.managers.NetworkManager;
import com.pherux.skyquest.managers.Persistence;

/**
 * Created by Fernando Valdez on 8/18/15
 */
public class App extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static NetworkManager getNetwork() {
        return NetworkManager.getInstance();
    }

    public static boolean useWebTracker() {
        return Persistence.getBooleanVal(SettingsActivity.ARG_USE_WEB_TRACKER, Constants.USE_FEATURE_TRACKER);
    }

    public static boolean useSMSTracker() {
        return Persistence.getBooleanVal(SettingsActivity.ARG_USE_SMS, Constants.USE_FEATURE_SMS);
    }

    public static boolean useCameraTracking() {
        return Persistence.getBooleanVal(SettingsActivity.ARG_USE_CAMERA, Constants.USE_FEATURE_PHOTOS);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
