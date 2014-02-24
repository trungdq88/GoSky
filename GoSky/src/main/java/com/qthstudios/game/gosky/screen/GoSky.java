package com.qthstudios.game.gosky.screen;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdView;
import com.qthstudios.game.gosky.R;
import com.qthstudios.game.gosky.config.Assets;
import com.qthstudios.game.gosky.config.Settings;
import com.qthstudios.game.gosky.framework.Screen;
import com.qthstudios.game.gosky.framework.impl.GLGame;
import com.qthstudios.game.gosky.screencast.MainScreen;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.*;

public class GoSky extends GLGame {
    private static final String MY_AD_UNIT_ID = "a1530b14f884dbc";
    private AdView adView;
    public boolean firstTimeCreate = true;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TRUNGDQ", "Resume game");
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
        // Create the adView.
        adView = new AdView(this);
        adView.setAdUnitId(MY_AD_UNIT_ID);
        adView.setAdSize(AdSize.BANNER);

        // Lookup your LinearLayout assuming it's been given
        // the attribute android:id="@+id/mainLayout".
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.mainLayout);

        // Add the adView to it.
        layout.addView(adView);

        // Initiate a generic request.
        AdRequest adRequest = new AdRequest.Builder().build();

        // Load the adView with the ad request.
        adView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        // No exit app by this way
        // super.onBackPressed();
    }
}
