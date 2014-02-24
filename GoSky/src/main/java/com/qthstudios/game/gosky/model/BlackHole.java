package com.qthstudios.game.gosky.model;

import com.qthstudios.game.gosky.framework.GameObject;

public class BlackHole extends GameObject {
    public static float CASTLE_WIDTH = 1.7f;
    public static float CASTLE_HEIGHT = 1.7f;

    public BlackHole(float x, float y) {
        super(x, y, CASTLE_WIDTH, CASTLE_HEIGHT);
    }

}

