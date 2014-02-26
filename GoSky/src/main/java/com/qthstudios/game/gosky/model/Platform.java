package com.qthstudios.game.gosky.model;

import com.qthstudios.game.gosky.framework.DynamicGameObject;

public class Platform extends DynamicGameObject {
    public static final float PLATFORM_WIDTH_MIN = 0.3f;
    public static final float PLATFORM_WIDTH_MAX = 1.5f;

    public static final float PLATFORM_HEIGHT = 0.5f;

    public static final int PLATFORM_TYPE_STATIC = 0;
    public static final int PLATFORM_TYPE_MOVING = 1;
    public static final int PLATFORM_STATE_NORMAL = 0;

    public static final int PLATFORM_STATE_PULVERIZING = 1;
    public static final float PLATFORM_PULVERIZE_TIME = 0.2f * 8;
    public static final float PLATFORM_VELOCITY_MIN = 1;
    public static final float PLATFORM_VELOCITY_MAX = 3;
    public static final float PLATFORM_TYPE_SPRING_PERCENT = 0f;
    public static final float PLATFORM_TYPE_MOVING_PERCENT = 0.1f;

    int type;
    int state;
    float stateTime;

    public Platform(int type, float x, float y, float width) {
        super(x, y, width, PLATFORM_HEIGHT);
        this.type = type;
        this.state = PLATFORM_STATE_NORMAL;
        this.stateTime = 0;
        if(type == PLATFORM_TYPE_MOVING) {
            velocity.x = (float) (Math.random() * PLATFORM_VELOCITY_MAX + PLATFORM_VELOCITY_MIN);
        }
    }

    public void update(float deltaTime) {
        if(type == PLATFORM_TYPE_MOVING) {
            position.add(velocity.x * deltaTime, 0);
            bounds.lowerLeft.set(position).sub(bounds.width / 2, PLATFORM_HEIGHT / 2);

            if(position.x < bounds.width / 2) {
                velocity.x = -velocity.x;
                position.x = bounds.width / 2;
            }
            if(position.x > World.WORLD_WIDTH - bounds.width / 2) {
                velocity.x = -velocity.x;
                position.x = World.WORLD_WIDTH - bounds.width / 2;
            }
        }

        stateTime += deltaTime;
    }

    public void pulverize() {
        state = PLATFORM_STATE_PULVERIZING;
        stateTime = 0;
        // velocity.x = 0;
    }
}

