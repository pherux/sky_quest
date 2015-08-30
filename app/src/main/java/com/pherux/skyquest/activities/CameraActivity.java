package com.pherux.skyquest.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.gc.materialdesign.views.ButtonRectangle;
import com.pherux.skyquest.R;
import com.pherux.skyquest.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fernando Valdez on 8/18/15
 */
@SuppressWarnings("deprecation")
public class CameraActivity extends Activity implements SurfaceHolder.Callback, Camera.PictureCallback {

    private static final String TAG = CameraActivity.class.getName();
    final Activity me = this;
    Camera cam = null;
    SurfaceView surface = null;
    SurfaceHolder holder = null;
    PowerManager.WakeLock wakeLock = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "CameraActivity onCreate");

        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.d(TAG, "CameraActivity onUncaughtException");
                Utils.logException(ex);
                Utils.pingError();
                me.finish();
            }
        });

        setContentView(R.layout.camera_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ButtonRectangle cancel = (ButtonRectangle) findViewById(R.id.photo_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.finish();
            }
        });

        surface = (SurfaceView) findViewById(R.id.photo_surface);
        holder = surface.getHolder();
        holder.addCallback(this);
        cam = Camera.open();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.FULL_WAKE_LOCK, "SkyQuestTrackerCameraActivityLock");
        wakeLock.acquire();
    }

    @Override
    public void onPause() {
        super.onPause();
        cam.stopPreview();
    }

    @Override
    protected void onStop() {
        Log.d("SkyQuest", "CameraActivity onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("SkyQuest", "CameraActivity onDestroy");
        wakeLock.release();
        if (cam != null) {
            cam.release();
        }
        super.onDestroy();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.d("SkyQuest", "PhotoActivity onPictureTaken");
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("SkyQuest", "Error creating media file, check storage permissions: ");
            return;
        }

        try {
            FileOutputStream out = new FileOutputStream(pictureFile);
            out.write(data);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            Log.d("SkyQuest", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("SkyQuest", "Error accessing file: " + e.getMessage());
        }
        //cam.stopPreview();
        //cam.release();

        // Let the android gallery know that it can show this file.
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(pictureFile)));

        Utils.pingSuccess();

        Integer iteration = Utils.getIntVal(Utils.photoCountKey, 0);
        String photoStatus = "Photo " + new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date()) + " Number: " + iteration.toString();
        Utils.putStringVal(Utils.photoStatusKey, photoStatus);

        pleaseWait(5);
        me.finish();
    }


    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("SkyQuest", "PhotoActivity surfaceCreated");
        try {
            setUpCameraParameter();

            cam.setPreviewDisplay(holder);
            cam.startPreview();
            takePicture();
        } catch (IOException e) {
            Log.d("SkyQuest", "Error setting camera preview: " + e.getMessage());
            me.finish();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void setUpCameraParameter() {

        try {
            Camera.Parameters params = cam.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
            List<String> sceneModes = params.getSupportedSceneModes();
            if (sceneModes != null) {
                for (String sceneMode : sceneModes) {
                    if (sceneMode.equals(Camera.Parameters.SCENE_MODE_ACTION)) {
                        params.setSceneMode(Camera.Parameters.SCENE_MODE_ACTION);
                    } else if (sceneMode.equals(Camera.Parameters.SCENE_MODE_SPORTS)) {
                        params.setSceneMode(Camera.Parameters.SCENE_MODE_SPORTS);
                    }
                }
            }
            params.setJpegQuality(100);
            params.setPictureFormat(ImageFormat.JPEG);
            params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            params.setColorEffect(Camera.Parameters.EFFECT_NONE);
            params.setAutoExposureLock(false);
            params.setAutoWhiteBalanceLock(false);

            Camera.Size maxSize = params.getPictureSize();
            for (Camera.Size size : params.getSupportedPictureSizes()) {
                if ((size.width * size.height) > (maxSize.width * maxSize.height)) {
                    maxSize = size;
                }
            }
            params.setPictureSize(maxSize.width, maxSize.height);

            try {
                params = cam.getParameters();
                Location location = Utils.getLocation();
                Log.d("SkyQuest", "EXIF GPS Altitude = " + Double.toString(location.getAltitude()));
                Log.d("SkyQuest", "EXIF GPS Latitude = " + Double.toString(location.getLatitude()));
                Log.d("SkyQuest", "EXIF GPS Longitude = " + Double.toString(location.getLongitude()));
                Log.d("SkyQuest", "EXIF GPS Time = " + Double.toString(location.getTime()));
                params.setGpsAltitude(location.getAltitude());
                params.setGpsLatitude(location.getLatitude());
                params.setGpsLongitude(location.getLongitude());
                params.setGpsTimestamp(location.getTime());
                cam.setParameters(params);
                Log.d("SkyQuest", "EXIF location saved");
            } catch (Throwable ex) {
                Log.d("SkyQuest", "Error saving EXIF location");
            }

        } catch (Throwable ex) {
            Log.d("SkyQuest", "Error setting camera parameters");
        }
    }

    private void takePicture() {
        pleaseWait(2);
        cam.takePicture(null, null, this);
    }

    private void pleaseWait(int seconds) {
        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Utils.getStorageRoot(), "Photos");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("SkyQuest", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        Integer iteration = Utils.incrementIntVal(Utils.photoCountKey);
        String fileName = Utils.getStringVal(Utils.photoPrefixKey, "SkyQuest_") + iteration.toString() + ".jpg";

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + fileName);
        Log.d("SkyQuest", "Saved file to " + fileName);

        return mediaFile;
    }

}
