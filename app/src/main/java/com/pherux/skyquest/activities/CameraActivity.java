package com.pherux.skyquest.activities;

import android.app.Activity;
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
import android.widget.Button;

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
public class CameraActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = CameraActivity.class.getName();
    final Activity me = this;
    Camera cam = null;
    SurfaceView surface = null;
    SurfaceHolder holder = null;
    PowerManager.WakeLock wakeLock = null;

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d("CasaDeBalloon", "PhotoActivity onPictureTaken");
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                Log.d("CasaDeBalloon", "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("CasaDeBalloon", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("CasaDeBalloon", "Error accessing file: " + e.getMessage());
            }
            cam.stopPreview();
            cam.release();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(pictureFile)));
            Utils.pingSuccess();

            Integer iteration = Utils.getIntVal(Utils.photoCountKey, 0);
            String photoStatus = "Photo " + new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date()) + " Number: " + iteration.toString();
            Utils.putStringVal(Utils.photoStatusKey, photoStatus);

            me.finish();
        }
    };

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("CasaDeBalloon", "PhotoActivity onStart");

        Button cancel;

        cancel = (Button) findViewById(R.id.photo_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.finish();
            }
        });

        surface = (SurfaceView) findViewById(R.id.photo_surface);
        holder = surface.getHolder();
        holder.addCallback(this);
    }

    @Override
    protected void onStop() {
        Log.d("CasaDeBalloon", "PhotoActivity onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("CasaDeBalloon", "PhotoActivity onDestroy");
        wakeLock.release();
        if (cam != null) {
            cam.release();
        }
        super.onDestroy();
    }

//	private AutoFocusCallback mFocus = new AutoFocusCallback() {
//        @Override
//        public void onAutoFocus(boolean success, Camera camera) {
//    		Log.d("CasaDeBalloon","PhotoActivity onAutoFocus");
//        	takePicture();
//        }
//    };

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("CasaDeBalloon", "PhotoActivity surfaceCreated");
        try {
            cam = Camera.open();

            Camera.Parameters params = cam.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
            List<String> sceneModes = params.getSupportedSceneModes();
            if (sceneModes != null) {
                for (String sceneMode : sceneModes) {
                    if (sceneMode == Camera.Parameters.SCENE_MODE_ACTION) {
                        params.setSceneMode(Camera.Parameters.SCENE_MODE_ACTION);
                    } else if (sceneMode == Camera.Parameters.SCENE_MODE_SPORTS) {
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
                cam.setParameters(params);
            } catch (Throwable ex) {
                Log.d("CasaDeBalloon", "Error setting camera parameters");
            }
            try {
                params = cam.getParameters();
                Location location = Utils.getLocation();
                Log.d("CasaDeBalloon", "EXIF GPS Altitude = " + Double.toString(location.getAltitude()));
                Log.d("CasaDeBalloon", "EXIF GPS Latitude = " + Double.toString(location.getLatitude()));
                Log.d("CasaDeBalloon", "EXIF GPS Longitude = " + Double.toString(location.getLongitude()));
                Log.d("CasaDeBalloon", "EXIF GPS Time = " + Double.toString(location.getTime()));
                params.setGpsAltitude(location.getAltitude());
                params.setGpsLatitude(location.getLatitude());
                params.setGpsLongitude(location.getLongitude());
                params.setGpsTimestamp(location.getTime());
                cam.setParameters(params);
                Log.d("CasaDeBalloon", "EXIF location saved");
            } catch (Throwable ex) {
                Log.d("CasaDeBalloon", "Error saving EXIF location");
            }

            cam.setPreviewDisplay(holder);
            cam.startPreview();
            //cam.autoFocus(mFocus);
            takePicture();
        } catch (IOException e) {
            Log.d("CasaDeBalloon", "Error setting camera preview: " + e.getMessage());
            me.finish();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void takePicture() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //throw new RuntimeException();
        cam.takePicture(null, null, mPicture);
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Utils.getStorageRoot(), "Photos");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CasaDeBalloon", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        Integer iteration = Utils.incrementIntVal(Utils.photoCountKey);
        String fileName = Utils.getStringVal(Utils.photoPrefixKey, "CasaDeBalloon_") + iteration.toString() + ".jpg";

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + fileName);
        Log.d("CasaDeBalloon", "Saved file to " + fileName);

        return mediaFile;
    }

}
