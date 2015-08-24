package com.pherux.skyquest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.pherux.skyquest.utils.Utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Fernando Valdez on 8/18/15
 */
public class SkyQuestService extends Service {
    private static final String TAG = SkyQuestService.class.getName();
    PowerManager.WakeLock wakeLock = null;
    LocationManager locationManager = null;
    Timer smsTimer = null;
    Timer trackerTimer = null;
    NMEALogListener nmeaListener = null;
    NoopLocationListener noopListener = null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        noopListener = new NoopLocationListener();
        nmeaListener = new NMEALogListener();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting Service");

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SkyQuestTrackerServiceLock");
        wakeLock.acquire();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                150, 0, noopListener);
        locationManager.addNmeaListener(nmeaListener);
        smsTimer = new Timer();
        smsTimer.schedule(new SMSTimerTask(), 5000, 60 * 1000);
        trackerTimer = new Timer();
        trackerTimer.schedule(new TrackerTimerTask(), 8000, 20 * 1000);
        return 0;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Stopping Service");
        smsTimer.cancel();
        smsTimer.purge();
        trackerTimer.cancel();
        trackerTimer.purge();
        locationManager.removeUpdates(noopListener);
        locationManager.removeNmeaListener(nmeaListener);
        wakeLock.release();
        super.onDestroy();
    }

    public class SMSTimerTask extends TimerTask {

        @Override
        public void run() {
            Log.d(TAG, "SMSTimerTask begin run");
            Utils.sendLocationSMS();
        }
    }

    public class TrackerTimerTask extends TimerTask {

        @Override
        public void run() {
            Log.d(TAG, "TrackerTimerTask begin run");
            Utils.sendTrackerPing();
        }

    }

    public class NMEALogListener implements GpsStatus.NmeaListener {
        @Override
        public void onNmeaReceived(long timestamp, String nmea) {
            Utils.appendToFile(Utils.nmeaLogFile, nmea);
        }
    }

    public class NoopLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "NoopLocationListener onLocationChanged");

            DecimalFormat df = new DecimalFormat("#.########");
            String latitude = df.format(location.getLatitude());
            String longitude = df.format(location.getLongitude());
            df = new DecimalFormat("#.#");
            String altitude = df.format(location.getAltitude());

            String gpsStatus = "GPS " + new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date(location.getTime())) + " Pos: " + latitude + " / " + longitude + " / " + altitude;
            Utils.putStringVal(Utils.gpsStatusKey, gpsStatus);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }
}
