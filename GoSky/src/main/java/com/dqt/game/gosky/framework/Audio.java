package com.dqt.game.gosky.framework;

import com.dqt.game.gosky.framework.impl.AndroidSound;

public interface Audio {
    public Music newMusic(String filename);

    public AndroidSound newSound(String filename);
}
