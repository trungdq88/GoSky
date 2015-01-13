package com.dqt.game.gosky.framework;

public interface Music {
    int getCurrentSeekPosition();

    public void play();

    public void stop();

    public void pause();

    public void resume();

    public void setLooping(boolean looping);

    public void setVolume(float volume);

    public boolean isPlaying();

    public boolean isStopped();

    public boolean isLooping();

    public void dispose();

    public void setMusicEndListener(MusicEndListener musicEndListener);

    public void seekTo(int i);

    public interface MusicEndListener {
        public void onMusicEnd();
    }
}
