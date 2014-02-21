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

public class MainMenu extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle soundBounds;
    Rectangle playBounds;
    Rectangle highscoresBounds;
    Rectangle helpBounds;
    Vector2 touchPoint;

    public MainMenu(Game game) {
        super(game);
        guiCam = new Camera2D(glGraphics, 320, 480);
        batcher = new SpriteBatcher(glGraphics, 100);
        soundBounds = new Rectangle(0, 0, 64, 64);
        playBounds = new Rectangle(160 - 150, 200 + 18, 300, 36);
        highscoresBounds = new Rectangle(160 - 150, 200 - 18, 300, 36);
        helpBounds = new Rectangle(160 - 150, 200 - 18 - 36, 300, 36);
        touchPoint = new Vector2();
        
        Settings.load(game.getFileIO());
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
                if(OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                    game.setScreen(new GameScreen(game));
                    return;
                }
                if(OverlapTester.pointInRectangle(highscoresBounds, touchPoint)) {
                    game.setScreen(new HighscoreScreen(game));
                    return;
                }
                if(OverlapTester.pointInRectangle(helpBounds, touchPoint)) {
                    game.setScreen(new HelpScreen(game));
                    return;
                }
                if(OverlapTester.pointInRectangle(soundBounds, touchPoint)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
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


        batcher.beginBatch(Assets.backgroundRegions.get(0).texture);
        batcher.drawSprite(160, 240, 320, 480, Assets.backgroundRegions.get(0));
        batcher.endBatch();
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);               
        
        batcher.beginBatch(Assets.items);                 
        
        batcher.drawSprite(160, 480 - 10 - 71, 274, 142, Assets.logo);
        batcher.drawSprite(160, 200, 300, 110, Assets.mainMenu);
        batcher.drawSprite(32, 32, 64, 64, Settings.soundEnabled?Assets.soundOn:Assets.soundOff);
                
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