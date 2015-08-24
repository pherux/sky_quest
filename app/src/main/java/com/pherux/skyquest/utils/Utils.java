package com.pherux.skyquest.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;

import com.pherux.skyquest.App;
import com.pherux.skyquest.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Fernando Valdez on 8/18/15
 */
public class Utils {

    private static final String TAG = Utils.class.getName();
    public static int alarmId = 314;
    public static String photoPrefixKey = "PhotoPrefix";
    public static String photoCountKey = "PhotoCount";
    public static String photoRunningKey = "PhotoRunning";
    public static String errorCountKey = "ErrorCount";
    public static String trackerNameKey = "TrackerName";
    public static String pingSMSKey = "PhoneNumber";
    public static String trackerUrlKey = "TrackerUrl";

    public static String smsStatusKey = "SMSStatus";
    public static String gpsStatusKey = "GPSStatus";
    public static String trackerStatusKey = "TrackerStatus";
    public static String photoStatusKey = "PhotoStatus";

    public static String errorLogFile = "skyquest.error.log";
    public static String nmeaLogFile = "skyquest.nmea.log";
    public static String configFile = "skyquest.config";

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

    public static Integer incrementIntVal(String key) {
        Integer val = getIntVal(key, 0);
        val = val + 1;
        putIntVal(key, val);
        return val;
    }

    public static String getStorageRoot() {
        return Environment.getExternalStorageDirectory() + "/skyquest";
    }

    public static void logException(Throwable ex) {
        try {
            PrintWriter pw = new PrintWriter(
                    new FileWriter(Utils.getStorageRoot()
                            + "/" + Utils.errorLogFile, true));
            ex.printStackTrace(pw);
            pw.flush();
            pw.close();
        } catch (Throwable e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static void appendToFile(String filename, String data) {
        try {
            PrintWriter pw = new PrintWriter(
                    new FileWriter(Utils.getStorageRoot()
                            + "/" + filename, true));
            pw.print(data);
            pw.flush();
            pw.close();
        } catch (Throwable e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static void reboot() {
        try {
            Log.d(TAG, "Utils attempting to reboot via su");
            java.lang.Process proc = Runtime.getRuntime().exec(
                    new String[]{"su", "-c", "reboot"});
            proc.waitFor();
        } catch (Throwable ex) {
            Log.d(TAG, "Error attempting to reboot via su");
        }
    }

    public static void pingSuccess() {
        putIntVal(Utils.errorCountKey, 0);
    }

    public static void pingError() {
        Integer errorCount = incrementIntVal(Utils.errorCountKey);
        if (errorCount > 5) {
            putIntVal(Utils.errorCountKey, 0);
            reboot();
        }
    }

    public static String getLocationString(Location location) {

        String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date(location.getTime()));

        DecimalFormat df = new DecimalFormat("#.########");
        String latitude = df.format(location.getLatitude());
        String longitude = df.format(location.getLongitude());

        df = new DecimalFormat("#.#");
        String altitude = df.format(location.getAltitude());

        df = new DecimalFormat("#.#");
        String accuracy = df.format(location.getAccuracy());

        return timestamp + " http://maps.google.com/?q=" + latitude + ","
                + longitude + " Alt:" + altitude + " Acc:" + accuracy;
    }

    public static Location getLocation() {
        LocationManager locationManager = (LocationManager) App.getContext()
                .getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = new Location("reverseGeocoded");
            location.setAltitude(0);
            location.setLatitude(0);
            location.setLongitude(0);
        }
        return location;
    }

    public static void sendLocationSMS() {
        Log.d(TAG, "Utils attempting to send location SMS");
        String phoneNumber = Utils.getStringVal(Utils.pingSMSKey, "");
        String trackerName = Utils.getStringVal(Utils.trackerNameKey, "");
        try {
            Location location = getLocation();
            SmsManager sm = SmsManager.getDefault();
            String locationString = Utils.getLocationString(location);
            String battString = Utils.getBatteryString();
            String smsBody = trackerName + ": " + locationString + " " + battString;
            sm.sendTextMessage(phoneNumber, null, smsBody, null, null);
            Log.d(TAG, "Utils location SMS sent to " + phoneNumber + " : " + smsBody);
            String smsStatus = "SMS " + new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date()) + " sent to " + phoneNumber;
            Utils.putStringVal(Utils.smsStatusKey, smsStatus);
        } catch (Throwable ex) {
            try {
                SmsManager sm = SmsManager.getDefault();
                String locationString = "Unknown location";
                String battString = Utils.getBatteryString();
                String smsBody = trackerName + ": " + locationString + " " + battString;
                sm.sendTextMessage(phoneNumber, null, smsBody, null, null);
            } catch (Throwable ignored) {
            }
            Log.d(TAG, "Utils error sending location SMS");
        }
    }

    public static void sendTrackerPing() {
        Log.d(TAG, "Utils attempting to send tracker ping");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Location location = getLocation();

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            String time = dateFormat.format(Calendar.getInstance().getTime());

            App.getNetwork().getService().log(
                    Constants.MOBILE_SERVICE_PRIVATE_KEY,
                    "battery",
                    time,
                    "" + location.getLatitude(),
                    "" + location.getLongitude(),
                    "" + location.getAltitude(),
                    "some notes",
                    new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            pingSuccess();
                            Log.d(TAG, "Data uploaded!");
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(TAG, "Error sending data to server");
                            pingError();
                        }
                    }
            );

        } catch (Throwable ex) {
            Log.d(TAG, "Utils error sending tracker ping");
        }
    }

    public static String getBatteryString() {
        Intent batteryIntent = App.getContext().registerReceiver(null, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        int level;
        if (batteryIntent != null) {
            level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            double pct = 0.0;
            if (level > 0 && scale > 0) {
                pct = ((double) level / (double) scale) * 100.0d;
            }

            double volts = 0.0;
            int mv = batteryIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            if (mv > 0) {
                volts = (double) mv / 1000.0d;
            }

            DecimalFormat df = new DecimalFormat("##");
            String spct = df.format(pct);

            df = new DecimalFormat("#.##");
            String svolts = df.format(volts);
            return "BVlt: " + svolts + " BPct:" + spct;
        } else {
            return "";
        }
    }

    public static void loadConfig() {
        //// TODO: 8/18/15 create config file
        try {
            String configFileName = Utils.getStorageRoot() + "/" + Utils.configFile;
            BufferedReader reader = new BufferedReader(new FileReader(configFileName));

            String input;
            if ((input = reader.readLine()) != null) {
                Utils.putStringVal(Utils.trackerNameKey, input);
            }
            if ((input = reader.readLine()) != null) {
                Utils.putStringVal(Utils.pingSMSKey, input);
            }
            if ((input = reader.readLine()) != null) {
                Utils.putStringVal(Utils.trackerUrlKey, input);
            }
            reader.close();
        } catch (Throwable ignored) {
        }
    }
}
