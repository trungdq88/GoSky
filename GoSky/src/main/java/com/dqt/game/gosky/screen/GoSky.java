package com.dqt.game.gosky.screen;

import android.os.Bundle;
import android.util.Log;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.dqt.game.gosky.R;
import com.dqt.game.gosky.config.Assets;
import com.dqt.game.gosky.config.Settings;
import com.dqt.game.gosky.framework.Screen;
import com.dqt.game.gosky.framework.impl.GLGame;
import com.dqt.game.gosky.screencast.MainScreen;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GoSky extends GLGame implements BannerController{
    public boolean firstTimeCreate = true;
    private AdView mAdView;
    private InterstitialAd interstitial;

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
            Assets.loadBackground(0);
            Assets.loadBackground(1);
            firstTimeCreate = false;
        } else {
            Assets.reload();
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
        Log.e("TRUNGDQ", "Pause game");

        System.exit(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TRUNGDQ", "Resume game");
        if (mAdView != null) {
            mAdView.resume();
        }
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
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("0F82DF5F69AF00607A52105AD1C04E72")
                .build();
        mAdView.loadAd(adRequest);

        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getString(R.string.int_ad_unit_id));

        // Create ad request.
        AdRequest adRequest2 = new AdRequest.Builder()
                .addTestDevice("0F82DF5F69AF00607A52105AD1C04E72")
                .build();

        // Begin loading your interstitial.
        interstitial.loadAd(adRequest2);
    }

    @Override
    public void showBanner() {
//        if (startAppBanner != null) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    startAppBanner.showBanner();
//                }
//            });
//        }
    }

    @Override
    public void hideBanner() {
//        if (startAppBanner != null) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    startAppBanner.hideBanner();            }
//            });
//        }
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
        System.exit(0);
    }

}
