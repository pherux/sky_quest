package com.pherux.skyquest.activities;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.pherux.skyquest.R;
import com.pherux.skyquest.utils.Utils;

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
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
