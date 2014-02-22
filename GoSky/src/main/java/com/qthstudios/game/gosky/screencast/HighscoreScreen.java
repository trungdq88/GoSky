package com.qthstudios.game.gosky.screencast;

import com.qthstudios.game.gosky.config.Assets;
import com.qthstudios.game.gosky.config.Settings;
import com.qthstudios.game.gosky.framework.Game;
import com.qthstudios.game.gosky.framework.gl.Camera2D;
import com.qthstudios.game.gosky.framework.gl.SpriteBatcher;
import com.qthstudios.game.gosky.framework.impl.GLScreen;
import com.qthstudios.game.gosky.framework.math.OverlapTester;
import com.qthstudios.game.gosky.framework.math.Rectangle;
import com.qthstudios.game.gosky.framework.math.Vector2;

import javax.microedition.khronos.opengles.GL10;
import java.util.List;

import static com.qthstudios.game.gosky.framework.Input.TouchEvent;

public class HighscoreScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle backBounds;
    Vector2 touchPoint;
    String[] highScores;  
    float xOffset = 0;
    
    public HighscoreScreen(Game game) {
        super(game);
        
        guiCam = new Camera2D(glGraphics, 320, 480);
        backBounds = new Rectangle(0, 0, 64, 64);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 100);
        highScores = new String[5];        
        for(int i = 0; i < 5; i++) {
            highScores[i] = (i + 1) + ". " + Settings.highscores[i];
            xOffset = Math.max(highScores[i].length() * Assets.font.glyphWidth, xOffset);
        }        
        xOffset = 160 - xOffset / 2;
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
        
        batcher.beginBatch(Assets.backgroundRegions.get(0).texture);
        batcher.drawSprite(160, 240, 320, 480, Assets.backgroundRegions.get(0));
        batcher.endBatch();
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);
        
        float y = 240;
        for(int i = 4; i >= 0; i--) {
            Assets.font.drawText(batcher, highScores[i], xOffset, y);
            y += Assets.font.glyphHeight;
        }

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
