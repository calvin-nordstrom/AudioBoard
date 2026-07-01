package com.calvinnordstrom.audioboard.audio;

public class ActiveSound {
    private final SoundAsset soundAsset;
    private final float volume;
    private int position;
    private volatile boolean stopped;

    public ActiveSound(SoundAsset soundAsset, float volume) {
        this.soundAsset = soundAsset;
        this.volume = volume;
    }

    public boolean isFinished() {
        return stopped || position >= soundAsset.pcm().length;
    }

    public void stop() {
        stopped = true;
    }

    public SoundAsset getSoundAsset() {
        return soundAsset;
    }

    public float getVolume() {
        return volume;
    }

    public int getPosition() {
        return position;
    }

    public int getPositionAndIncrement() {
        return position++;
    }
}
