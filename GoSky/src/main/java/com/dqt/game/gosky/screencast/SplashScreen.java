package com.dqt.game.gosky.screencast;

import com.dqt.game.gosky.config.Assets;
import com.dqt.game.gosky.framework.gl.Camera2D;
import com.dqt.game.gosky.framework.gl.SpriteBatcher;
import com.dqt.game.gosky.framework.impl.GLGame;
import com.dqt.game.gosky.framework.impl.GLScreen;
import com.dqt.game.gosky.framework.math.Vector2;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import static com.dqt.game.gosky.framework.Input.TouchEvent;

public class SplashScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Vector2 touchPoint;

    GLGame gameLoader;

    public SplashScreen(final GLGame game) {
        super(game);

        guiCam = new Camera2D(glGraphics, 320, 480);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 100);

        gameLoader = game;

    }


    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if(event.type == TouchEvent.TOUCH_UP) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainScreen(game));
                return;
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();        
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();
        
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        batcher.beginBatch(Assets.splashScreenTexture);
        batcher.drawSprite(160, 240, 320, 480, Assets.splashScreen);
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);

    }
    
    @Override
    public void resume() {        
    }
    
    @Override
    public void pause() {        
    }

    @Override
    public void dispose() {
    }
}
