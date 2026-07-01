package com.calvinnordstrom.audioboard.audio;

public class PlayingSound {
    private final ActiveSound activeSound;

    public PlayingSound(ActiveSound activeSound) {
        this.activeSound = activeSound;
    }

    public void stop() {
        activeSound.stop();
    }

    public boolean isFinished() {
        return activeSound.isFinished();
    }
}
