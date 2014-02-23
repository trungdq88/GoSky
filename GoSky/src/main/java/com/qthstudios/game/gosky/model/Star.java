package com.qthstudios.game.gosky.model;

import com.qthstudios.game.gosky.config.Assets;
import com.qthstudios.game.gosky.framework.GameObject;
import com.qthstudios.game.gosky.framework.gl.Animation;
import com.qthstudios.game.gosky.framework.gl.SpriteBatcher;
import com.qthstudios.game.gosky.framework.gl.TextureRegion;

/**
 * Created by Dinh Quang Trung on 2/23/14.
 */
public class Star extends GameObject {
    public static float STAR_WIDTH = 21;
    public static float STAR_HEIGHT = 21;
    public static float STAR_SPEED = 10;
    public static float STAR_MAX_COUNT = 10;

    SpriteBatcher batcher;

    float stateTime;
    private TextureRegion _keyframe;

    public Star(float x, float y, float width, float height) {
        super(x, y, width, height);
        stateTime = (float) (Math.random() * 100);
    }

    public Star(SpriteBatcher batcher, int x, int y, float star_width, float star_height) {
        super(x, y, star_width, star_height);
        stateTime = 0;
        this.batcher = batcher;
    }

    public void render(float deltaTime) {
        if (batcher != null) {
            stateTime += deltaTime;
            _keyframe = Assets.star.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING);
            batcher.drawSprite(position.x, position.y, STAR_WIDTH, STAR_HEIGHT, _keyframe);
        }
    }
}
