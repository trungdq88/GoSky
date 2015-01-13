package com.dqt.game.gosky.framework.impl;

import java.util.List;

import android.view.View.OnTouchListener;
import com.dqt.game.gosky.framework.Input;


public interface TouchHandler extends OnTouchListener {
    public boolean isTouchDown(int pointer);
    
    public int getTouchX(int pointer);
    
    public int getTouchY(int pointer);
    
    public List<Input.TouchEvent> getTouchEvents();
}
