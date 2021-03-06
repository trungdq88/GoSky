package com.dqt.game.gosky.framework.impl;


import com.dqt.game.gosky.framework.Game;
import com.dqt.game.gosky.framework.Screen;

public abstract class GLScreen extends Screen {
    protected final GLGraphics glGraphics;
    protected final GLGame glGame;
    
    public GLScreen(Game game) {
        super(game);
        glGame = (GLGame)game;
        glGraphics = ((GLGame)game).getGLGraphics();
    }

}
