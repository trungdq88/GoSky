package com.qthstudios.game.gosky.config;

import android.util.Log;

import com.qthstudios.game.gosky.framework.Music;
import com.qthstudios.game.gosky.framework.Sound;
import com.qthstudios.game.gosky.framework.gl.Animation;
import com.qthstudios.game.gosky.framework.gl.Font;
import com.qthstudios.game.gosky.framework.gl.LazyTexture;
import com.qthstudios.game.gosky.framework.gl.LazyTextureRegion;
import com.qthstudios.game.gosky.framework.gl.Texture;
import com.qthstudios.game.gosky.framework.gl.TextureRegion;
import com.qthstudios.game.gosky.framework.impl.GLGame;

import java.util.ArrayList;
import java.util.List;

public class Assets {
    private static int BACKGROUND_FULL_HEIGHT = 48000;
//    private static int BACKGROUND_FULL_HEIGHT = 1440;

    public static GLGame mGame;
    private static List<LazyTexture> backgrounds = new ArrayList<LazyTexture>();
    public static List<LazyTextureRegion> backgroundRegions = new ArrayList<LazyTextureRegion>();

    public static Texture splashScreenTexture;
    public static TextureRegion splashScreen;

    public static Texture items;
    public static TextureRegion mainMenu;
    public static TextureRegion pauseMenu;
    public static TextureRegion ready;
    public static TextureRegion gameOver;
    public static TextureRegion highScoresRegion;
    public static TextureRegion logo;
    public static TextureRegion soundOn;
    public static TextureRegion soundOff;
    public static TextureRegion arrow;
    public static TextureRegion pause;
    public static TextureRegion spring;
    public static TextureRegion castle;
    public static Animation coinAnim;
    public static Animation bobJump;
    public static Animation bobFall;
    public static TextureRegion bobHit;
    public static Animation squirrelFly;
    public static TextureRegion platform;
    public static Animation brakingPlatform;
    public static Animation star;
    public static Font font;

    public static Sound jumpSound;
    public static Sound highJumpSound;
    public static Sound hitSound;
    public static Sound hitTopSound;
    public static Sound clickSound;
    public static Music nyan2;
    public static Music nyan1;

    public static boolean loadBackground(final int pos) {
        if (pos >= backgroundRegions.size() && (BACKGROUND_FULL_HEIGHT - 480 - pos * 480 >= 0)) {
            final LazyTexture background = new LazyTexture(mGame, "background.jpg");

//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {

                    background.contextTopOffset = pos * 480;
                    background.topOffset = BACKGROUND_FULL_HEIGHT - 480 - pos * 480;
                    background.reload();
                    backgrounds.add(background);
                    backgroundRegions.add(new LazyTextureRegion(background, 0, 0, 320, 480));

                    Log.e("TRUNGDQ", "load pos true: " + pos);
//                }
//            });
//            t.start();
            return true;
        } else {

            Log.e("TRUNGDQ", "load pos false: " + pos);
            return false;
        }

    }

    public static void loadSplashScreen(GLGame game) {
        Log.e("TRUNGDQ", "load splash screen");
        splashScreenTexture = new Texture(game, "background1.jpg");
        splashScreen = new TextureRegion(splashScreenTexture, 0, 0, 320, 480);
    }

    public static void load(GLGame game) {
        mGame = game;


        items = new Texture(game, "items.png");
        mainMenu = new TextureRegion(items, 45, 255, 209, 59);
        pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
        ready = new TextureRegion(items, 320, 224, 192, 32);
        gameOver = new TextureRegion(items, 352, 256, 160, 96);
        highScoresRegion = new TextureRegion(Assets.items, 45, 315, 209, 32);
        logo = new TextureRegion(items, 0, 352, 274, 142);
        soundOff = new TextureRegion(items, 0, 0, 64, 64);
        soundOn = new TextureRegion(items, 64, 0, 64, 64);
        arrow = new TextureRegion(items, 0, 64, 64, 64);
        pause = new TextureRegion(items, 64, 64, 64, 64);

        spring = new TextureRegion(items, 128, 0, 32, 32);
        castle = new TextureRegion(items, 128, 64, 64, 64);
        coinAnim = new Animation(0.2f,
                new TextureRegion(items, 128, 32, 32, 32),
                new TextureRegion(items, 160, 32, 32, 32),
                new TextureRegion(items, 192, 32, 32, 32),
                new TextureRegion(items, 160, 32, 32, 32));
        bobJump = new Animation(0.2f,
                new TextureRegion(items, 0, 128, 32, 32),
                new TextureRegion(items, 32, 128, 32, 32));
        bobFall = new Animation(0.2f,
                new TextureRegion(items, 64, 128, 32, 32),
                new TextureRegion(items, 96, 128, 32, 32));
        bobHit = new TextureRegion(items, 128, 128, 32, 32);
        squirrelFly = new Animation(0.2f,
                new TextureRegion(items, 0, 160, 32, 32),
                new TextureRegion(items, 32, 160, 32, 32));
        platform = new TextureRegion(items, 64, 160, 64, 16);
        brakingPlatform = new Animation(0.2f,
                new TextureRegion(items, 64, 160, 64, 16),
                new TextureRegion(items, 64, 176, 64, 16),
                new TextureRegion(items, 64, 192, 64, 16),
                new TextureRegion(items, 64, 208, 64, 16));
        star = new Animation(0.1f,
                new TextureRegion(items, 0     , 0, 21, 21),
                new TextureRegion(items, 21    , 0, 21, 21),
                new TextureRegion(items, 21 * 2, 0, 21, 21),
                new TextureRegion(items, 21 * 3, 0, 21, 21),
                new TextureRegion(items, 21 * 4, 0, 21, 21),
                new TextureRegion(items, 21 * 5, 0, 21, 21));

        font = new Font(items, 225, 0, 16, 16, 20);

        jumpSound = game.getAudio().newSound("jump.ogg");
        highJumpSound = game.getAudio().newSound("highjump.ogg");
        hitSound = game.getAudio().newSound("hit.ogg");
        hitTopSound = game.getAudio().newSound("hittop.wav");
        clickSound = game.getAudio().newSound("click.wav");
        nyan1 = game.getAudio().newMusic("nyan1.mp3");
        nyan2 = game.getAudio().newMusic("nyan2.mp3");
        nyan1.setVolume(50);
        nyan2.setVolume(50);
        nyan1.setMusicEndListener(new Music.MusicEndListener() {

            @Override
            public void onMusicEnd() {
                nyan2.setLooping(true);
                nyan2.play();
            }
        });

        // Big things come last
        for (int i = 0; i < 2; ++i) {
            loadBackground(i);
        }
    }


    public static void reload() {
        for (LazyTexture background : backgrounds) {
            background.reload();
        }
        items.reload();
    }

    public static void playSound(Sound sound) {
        try {
            if (Settings.soundEnabled)
                sound.play(1);
        } catch (NullPointerException e) {
            // Sound is not loaded yet
        }
    }
}
