package com.pherux.skyquest.managers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.pherux.skyquest.App;

/**
 * Created by Fernando Valdez on 9/1/15
 */
public class Persistence {

    public static String getStringVal(String key,
                                      String defValue) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        return settings.getString(key, defValue);
    }

    public static void putStringVal(String key, String val) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static Boolean getBooleanVal(String key,
                                        Boolean defValue) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        return settings.getBoolean(key, defValue);
    }

    public static void putBooleanVal(String key, Boolean val) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public static Integer getIntVal(String key,
                                    Integer defValue) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        return settings.getInt(key, defValue);
    }

    public static void putIntVal(String key, Integer val) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, val);
        editor.apply();
    }

}
