package com.qthstudios.game.gosky.screen;

import android.os.Bundle;
import android.util.Log;

import com.google.analytics.tracking.android.EasyTracker;
import com.qthstudios.game.gosky.config.Assets;
import com.qthstudios.game.gosky.config.Settings;
import com.qthstudios.game.gosky.framework.Screen;
import com.qthstudios.game.gosky.framework.impl.GLGame;
import com.qthstudios.game.gosky.screencast.MainScreen;
import com.startapp.android.publish.StartAppAd;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GoSky extends GLGame {
    public final String DEV_ID = "101352520";
    public final String APP_ID = "202676613";
    public boolean firstTimeCreate = true;
    private StartAppAd startAppAd = new StartAppAd(this);



    @Override
    public Screen getStartScreen() {
//        if (firstTimeCreate) {
//            Assets.loadSplashScreen(this);
//            return new SplashScreen(this);
//        } else {
            return new MainScreen(this);
//        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Settings.load(getFileIO());
            Assets.load(GoSky.this);
            firstTimeCreate = false;
        } else {
            Assets.reload();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TRUNGDQ", "Pause game");
        startAppAd.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TRUNGDQ", "Resume game");
        startAppAd.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("TRUNGDQ", "Start game");
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TRUNGDQ", "Stop game");
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppAd.init(this, DEV_ID, APP_ID);
//        StartAppSearch.init(this, DEV_ID, APP_ID);
//        StartAppSearch.showSearchBox(this);
    }

    @Override
    public void onBackPressed() {
        // no accidentally exit app
    }
}
