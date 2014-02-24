package com.qthstudios.game.gosky.screencast;

import com.qthstudios.game.gosky.config.Assets;
import com.qthstudios.game.gosky.config.Settings;
import com.qthstudios.game.gosky.framework.Game;
import com.qthstudios.game.gosky.framework.gl.Camera2D;
import com.qthstudios.game.gosky.framework.gl.FPSCounter;
import com.qthstudios.game.gosky.framework.gl.SpriteBatcher;
import com.qthstudios.game.gosky.framework.impl.GLScreen;
import com.qthstudios.game.gosky.framework.math.OverlapTester;
import com.qthstudios.game.gosky.framework.math.Rectangle;
import com.qthstudios.game.gosky.framework.math.Vector2;
import com.qthstudios.game.gosky.model.World;
import com.qthstudios.game.gosky.model.WorldRenderer;
import com.qthstudios.game.gosky.screen.BannerController;
import com.qthstudios.game.gosky.screen.GoSky;

import javax.microedition.khronos.opengles.GL10;
import java.util.List;

import static com.qthstudios.game.gosky.framework.Input.TouchEvent;
import static com.qthstudios.game.gosky.model.World.WorldListener;

public class GameScreen extends GLScreen {
    static final int GAME_READY = 0;    
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;
  
    int state;
    Camera2D guiCam;
    Vector2 touchPoint;
    SpriteBatcher batcher;
    World world;
    WorldListener worldListener;
    WorldRenderer renderer;
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    int lastScore;
    String scoreString;    
    FPSCounter fpsCounter;
    Game _game;
    
    public GameScreen(Game game) {
        super(game);
        _game = game;
        state = GAME_READY;
        guiCam = new Camera2D(glGraphics, 320, 480);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 5000); // 5 thousands sprite per batch~
        worldListener = new WorldListener() {
            @Override
            public void jump() {            
                Assets.playSound(Assets.jumpSound);

                // Lazy load background
                Assets.loadBackground((int) (renderer.cam.position.y / 2 * 32 + 2 * 480) / 480);
            }

            @Override
            public void highJump() {
                Assets.playSound(Assets.highJumpSound);

                // Lazy load background
                Assets.loadBackground((int) (renderer.cam.position.y / 2 * 32 + 2 * 480) / 480);
            }

            @Override
            public void hit() {
                Assets.playSound(Assets.hitSound);
            }

            @Override
            public void coin() {
                Assets.playSound(Assets.hitTopSound);
            }

            @Override
            public void hitTop() {
                Assets.playSound(Assets.hitTopSound);
            }
        };
        world = new World(worldListener);
        renderer = new WorldRenderer(glGraphics, batcher, world);
        pauseBounds = new Rectangle(320- 64, 480- 64, 64, 64);
        resumeBounds = new Rectangle(160 - 96, 200, 192, 72);
        lastScore = 0;
        scoreString = "0";
        fpsCounter = new FPSCounter();
    }

	@Override
	public void update(float deltaTime) {
	    if(deltaTime > 0.1f)
	        deltaTime = 0.1f;
	    
	    switch(state) {
	    case GAME_READY:
	        updateReady();
	        break;
	    case GAME_RUNNING:
	        updateRunning(deltaTime);
	        break;
	    case GAME_PAUSED:
	        updatePaused();
	        break;
	    case GAME_LEVEL_END:
	        updateLevelEnd();
	        break;
	    case GAME_OVER:
	        updateGameOver();
	        break;
	    }
	}
	
	private void updateReady() {
	    if(game.getInput().getTouchEvents().size() > 0) {
	        state = GAME_RUNNING;
            ((BannerController)_game).hideBanner();
	    }
	}
	
	private void updateRunning(float deltaTime) {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {
	        TouchEvent event = touchEvents.get(i);
	        if(event.type != TouchEvent.TOUCH_UP)
	            continue;
	        
	        touchPoint.set(event.x, event.y);
	        guiCam.touchToWorld(touchPoint);
	        
	        if(OverlapTester.pointInRectangle(pauseBounds, touchPoint)) {
	            Assets.playSound(Assets.clickSound);
                if (Assets.nyan1.isPlaying()) {
                    Assets.nyan1.pause();
                }
                if (Assets.nyan2.isPlaying()) {
                    Assets.nyan2.pause();
                }
	            state = GAME_PAUSED;
	            return;
	        }            
	    }
	    
	    world.update(deltaTime, game.getInput().getAccelX());
	    if(world.score != lastScore) {
	        lastScore = world.score;
	        scoreString = "" + lastScore;
	    }
	    if(world.state == World.WORLD_STATE_NEXT_LEVEL) {
	        state = GAME_LEVEL_END;        
	    }
	    if(world.state == World.WORLD_STATE_GAME_OVER) {
	        state = GAME_OVER;
	        if(lastScore >= Settings.highscores[4])
	            scoreString = "" + world.maxScore;
	        else
	            scoreString = "" + world.maxScore;
	        Settings.addScore(world.maxScore);
	        Settings.save(game.getFileIO());
            ((BannerController)_game).showBanner();
	    }
	}
	
	private void updatePaused() {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {
	        TouchEvent event = touchEvents.get(i);
	        if(event.type != TouchEvent.TOUCH_UP)
	            continue;
	        
	        touchPoint.set(event.x, event.y);
	        guiCam.touchToWorld(touchPoint);
	        
	        if(OverlapTester.pointInRectangle(resumeBounds, touchPoint)) {
	            Assets.playSound(Assets.clickSound);
	            state = GAME_RUNNING;
                if (Assets.nyan1.getCurrentSeekPosition() > 0) {
                    Assets.nyan1.resume();
                }
                if (Assets.nyan2.getCurrentSeekPosition() > 0) {
                    Assets.nyan2.resume();
                }
	            return;
	        }
	    }
	}
	
	private void updateLevelEnd() {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {                   
	        TouchEvent event = touchEvents.get(i);
	        if(event.type != TouchEvent.TOUCH_UP)
	            continue;
	        world = new World(worldListener);
	        renderer = new WorldRenderer(glGraphics, batcher, world);
	        world.score = lastScore;
	        state = GAME_READY;
	    }
	}
	
	private void updateGameOver() {
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    int len = touchEvents.size();
	    for(int i = 0; i < len; i++) {
            try {
                TouchEvent event = touchEvents.get(i);
                if(event.type != TouchEvent.TOUCH_UP)
                    continue;
                Assets.playSound(Assets.clickSound);
	        game.setScreen(new MainScreen(game));
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                // Weird behavior
            }
	    }
	}

	@Override
	public void present(float deltaTime) {
	    GL10 gl = glGraphics.getGL();
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    gl.glEnable(GL10.GL_TEXTURE_2D);
	    
	    renderer.render();
	    
	    guiCam.setViewportAndMatrices();
	    gl.glEnable(GL10.GL_BLEND);
	    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    batcher.beginBatch(Assets.items);
	    switch(state) {
	    case GAME_READY:
	        presentReady();
	        break;
	    case GAME_RUNNING:
	        presentRunning();
	        break;
	    case GAME_PAUSED:
	        presentPaused();
	        break;
	    case GAME_LEVEL_END:
	        presentLevelEnd();
	        break;
	    case GAME_OVER:
	        presentGameOver();
	        break;
	    }
	    batcher.endBatch();
	    gl.glDisable(GL10.GL_BLEND);
	    fpsCounter.logFrame();
    }
	
	private void presentReady() {
        batcher.drawSprite(160, 240, 153, 102, Assets.howtoplay);
        batcher.drawSprite(160, 240 - 100, 192, 32, Assets.ready);

    }
	
	private void presentRunning() {
	    batcher.drawSprite(320 - 32, 480 - 32, 64, 64, Assets.pause);
        float scoreWidth = Assets.font.glyphWidth * scoreString.length();
	    Assets.font.drawText(batcher, scoreString, 160 - scoreWidth / 2, 480-100, 2);
	}
	
	private void presentPaused() {        
	    batcher.drawSprite(160, 240, 192, 96, Assets.pauseMenu);
        float scoreWidth = Assets.font.glyphWidth * scoreString.length();
	    Assets.font.drawText(batcher, scoreString, 160 - scoreWidth / 2, 480-80, 2);
    }
	
	private void presentLevelEnd() {
	    String topText = "oh no! this is...";
	    String bottomText = "a time machine!";
	    float topWidth = Assets.font.glyphWidth * topText.length();
	    float bottomWidth = Assets.font.glyphWidth * bottomText.length();
	    Assets.font.drawText(batcher, topText, 160 - topWidth / 2, 480 - 40);
	    Assets.font.drawText(batcher, bottomText, 160 - bottomWidth / 2, 40);
	}
	
	private void presentGameOver() {
	    batcher.drawSprite(160, 380, 160, 96, Assets.gameOver);
	    float scoreWidth = Assets.font.glyphWidth * scoreString.length() * scoreString.length();
	    Assets.font.drawText(batcher, scoreString, 160 - scoreWidth / 2, 480-220, 4);

        float hightScoreStringWidth = Assets.font.glyphWidth * "best".length();
        Assets.font.drawText(batcher, "best", 160 - hightScoreStringWidth / 2, 480-300, 1.5f);

        float maxScoreWidth = Assets.font.glyphWidth
                * (Settings.highscores[0] + "").length();
        Assets.font.drawText(batcher, Settings.highscores[0] + "", 160 - maxScoreWidth / 2, 480-350, 2);


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
