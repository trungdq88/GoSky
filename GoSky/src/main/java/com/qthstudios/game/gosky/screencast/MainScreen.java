package com.qthstudios.game.gosky.screencast;

import com.qthstudios.game.gosky.config.Assets;
import com.qthstudios.game.gosky.config.Settings;
import com.qthstudios.game.gosky.framework.Game;
import com.qthstudios.game.gosky.framework.Input;
import com.qthstudios.game.gosky.framework.gl.Camera2D;
import com.qthstudios.game.gosky.framework.gl.SpriteBatcher;
import com.qthstudios.game.gosky.framework.impl.GLScreen;
import com.qthstudios.game.gosky.framework.math.OverlapTester;
import com.qthstudios.game.gosky.framework.math.Rectangle;
import com.qthstudios.game.gosky.framework.math.Vector2;

import javax.microedition.khronos.opengles.GL10;
import java.util.List;

public class MainScreen extends GLScreen {

    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle playBounds;
    Rectangle highscoresBounds;
    Vector2 touchPoint;

    boolean isPlayButtonPressing = false;
    boolean isScoreButtonPressing = false;

    public MainScreen(Game game) {
        super(game);
        guiCam = new Camera2D(glGraphics, 320, 480);
        batcher = new SpriteBatcher(glGraphics, 100);
        playBounds =            new Rectangle(160 - 150, 150, 300, 100);
        highscoresBounds =      new Rectangle(160 - 150, 0, 300, 50);
        touchPoint = new Vector2();
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if(event.type == Input.TouchEvent.TOUCH_UP) {
                isPlayButtonPressing = false;
                isScoreButtonPressing = false;

                touchPoint.set(event.x, event.y);
                // use design coordinate
                guiCam.touchToWorld(touchPoint);

                if(OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new GameScreen(game));
                    return;
                }
                if(OverlapTester.pointInRectangle(highscoresBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new HighscoreScreen(game));
                    return;
                }
            }

            if(event.type == Input.TouchEvent.TOUCH_DOWN) {
                touchPoint.set(event.x, event.y);
                // use design coordinate
                guiCam.touchToWorld(touchPoint);

                if(OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                    isPlayButtonPressing = true;
                }
                if(OverlapTester.pointInRectangle(highscoresBounds, touchPoint)) {
                    isScoreButtonPressing = true;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();

        gl.glEnable(GL10.GL_TEXTURE_2D);

        if (Assets.backgroundRegions.size() > 0) {
            batcher.beginBatch(Assets.backgroundRegions.get(0).texture);
            batcher.drawSprite(160, 240, 320, 480, Assets.backgroundRegions.get(0));
            batcher.endBatch();
        }

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);

        batcher.drawSprite(160, 480 - 10 - 71, 274, 142, Assets.logo);
        batcher.drawSprite(160, 200 + (isPlayButtonPressing ? -3 : 0), 209, 59, Assets.mainMenu);
        batcher.drawSprite(160, 40 + (isScoreButtonPressing ? -2 : 0), 209, 32, Assets.highScoresRegion);
        // batcher.drawSprite(32, 32, 64, 64, Settings.soundEnabled?Assets.soundOn:Assets.soundOff);

        batcher.endBatch();

        gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void pause() {
        Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
