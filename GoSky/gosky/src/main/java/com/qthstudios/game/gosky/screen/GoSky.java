package com.qthstudios.game.gosky.screen;

import com.qthstudios.game.gosky.config.Assets;
import com.qthstudios.game.gosky.config.Settings;
import com.qthstudios.game.gosky.framework.Screen;
import com.qthstudios.game.gosky.framework.impl.GLGame;
import com.qthstudios.game.gosky.screencast.MainScreen;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GoSky extends GLGame {

    boolean firstTimeCreate = true;

    @Override
    public Screen getStartScreen() {
        return new MainScreen(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Settings.load(getFileIO());
            Assets.load(this);
            firstTimeCreate = false;
        } else {
            Assets.reload();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Settings.soundEnabled)
            Assets.music.pause();
    }


}
