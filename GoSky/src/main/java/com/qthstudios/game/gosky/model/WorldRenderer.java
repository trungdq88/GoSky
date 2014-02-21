package com.qthstudios.game.gosky.model;

import android.util.Log;

import com.qthstudios.game.gosky.config.Assets;
import com.qthstudios.game.gosky.framework.gl.Animation;
import com.qthstudios.game.gosky.framework.gl.Camera2D;
import com.qthstudios.game.gosky.framework.gl.LazyTextureRegion;
import com.qthstudios.game.gosky.framework.gl.SpriteBatcher;
import com.qthstudios.game.gosky.framework.gl.TextureRegion;
import com.qthstudios.game.gosky.framework.impl.GLGraphics;

import javax.microedition.khronos.opengles.GL10;

public class WorldRenderer {
    static final float FRUSTUM_WIDTH = 10;
    static final float FRUSTUM_HEIGHT = 15;
    GLGraphics glGraphics;
    World world;
    public Camera2D cam;
    SpriteBatcher batcher;

    public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world) {
        this.glGraphics = glGraphics;
        this.world = world;
        this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.batcher = batcher;
    }

    public void render() {
        if(world.bob.position.y > cam.position.y)
            cam.position.y = world.bob.position.y;
        if (world.bob.position.y < cam.position.y - 6) {
            cam.position.y = world.bob.position.y + 6;
        }
        cam.setViewportAndMatrices();
        renderBackground();
        renderObjects();
    }

    public void renderBackground() {

        for (int i = (int) ((cam.position.y * 32 - 480) / 480);
                i < (int) ((cam.position.y * 32 + 2 * 480) / 480)
                && i < Assets.backgroundRegions.size(); ++i) {
            if (i < 0) continue;
            LazyTextureRegion backgroundRegion = Assets.backgroundRegions.get(i);
            batcher.beginBatch(backgroundRegion.texture);
            batcher.drawSprite(FRUSTUM_WIDTH / 2,
                    (backgroundRegion.texture.contextTopOffset / 32 + FRUSTUM_HEIGHT / 2 - 1),
                    FRUSTUM_WIDTH, FRUSTUM_HEIGHT,
                    backgroundRegion);
            batcher.endBatch();
        }
    }

    public void renderObjects() {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);
        renderBob();
        renderPlatforms();
        renderItems();
//        renderSquirrels();
        renderCastle();
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void renderBob() {
        TextureRegion keyFrame;
        switch(world.bob.state) {
            case Bob.BOB_STATE_FALL:
                keyFrame = Assets.bobFall.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
                break;
            case Bob.BOB_STATE_JUMP:
                keyFrame = Assets.bobJump.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
                break;
            case Bob.BOB_STATE_HIT:
            default:
                keyFrame = Assets.bobHit;
        }

        float side = world.bob.velocity.x < 0? -1: 1;
//        float angle = world.bob.velocity.y * 8; // 45 = 0, 90 = 45

        double r = Math.abs(world.bob.velocity.y / world.bob.velocity.x);

        float angle = 0;
        if (world.bob.velocity.y > 0 && world.bob.velocity.x > 0) {
            angle = (float) Math.toDegrees(Math.atan(r)) ;
        }

        if (world.bob.velocity.y > 0 && world.bob.velocity.x < 0) {
            angle = (float) (90 +  Math.toDegrees(Math.atan(1 / r)));
        }

        if (world.bob.velocity.y < 0 && world.bob.velocity.x > 0) {
            angle = - (float) Math.toDegrees(Math.atan(r));
        }

        if (world.bob.velocity.y < 0 && world.bob.velocity.x < 0) {
            angle = (float) (- 90 - Math.toDegrees(Math.atan(1 / r)));
        }

        angle += 45* side;
        batcher.drawSprite(world.bob.position.x, world.bob.position.y, 1 , side, angle, keyFrame);
    }

    private void renderPlatforms() {
        int len = world.platforms.size();
        for(int i = 0; i < len; i++) {
            Platform platform = world.platforms.get(i);
            TextureRegion keyFrame = Assets.platform;
            if(platform.state == Platform.PLATFORM_STATE_PULVERIZING) {
                keyFrame = Assets.brakingPlatform.getKeyFrame(platform.stateTime, Animation.ANIMATION_NONLOOPING);
            }

            batcher.drawSprite(platform.position.x, platform.position.y,
                    platform.bounds.width, 0.5f, keyFrame);
        }
    }

    private void renderItems() {
        int len = world.springs.size();
        for(int i = 0; i < len; i++) {
            Spring spring = world.springs.get(i);
            batcher.drawSprite(spring.position.x, spring.position.y, 1, 1, Assets.spring);
        }

//        len = world.coins.size();
//        for(int i = 0; i < len; i++) {
//            Coin coin = world.coins.get(i);
//            TextureRegion keyFrame = Assets.coinAnim.getKeyFrame(coin.stateTime, Animation.ANIMATION_LOOPING);
//            batcher.drawSprite(coin.position.x, coin.position.y, 1, 1, keyFrame);
//        }
    }

//    private void renderSquirrels() {
//        int len = world.squirrels.size();
//        for(int i = 0; i < len; i++) {
//            Squirrel squirrel = world.squirrels.get(i);
//            TextureRegion keyFrame = Assets.squirrelFly.getKeyFrame(squirrel.stateTime, Animation.ANIMATION_LOOPING);
//            float side = squirrel.velocity.x < 0?-1:1;
//            batcher.drawSprite(squirrel.position.x, squirrel.position.y, side * 1, 1, keyFrame);
//        }
//    }

    private void renderCastle() {
        Castle castle = world.castle;
        batcher.drawSprite(castle.position.x, castle.position.y, 2, 2, Assets.castle);
    }
}

