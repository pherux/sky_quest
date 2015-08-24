package com.pherux.skyquest;

import android.app.Application;
import android.content.Context;

import com.pherux.skyquest.managers.NetworkManager;

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
