package com.calvinnordstrom.audioboard.audio;

class ActiveSound {
    final SoundAsset soundAsset;
    final float volume;
    int position;
    volatile boolean stopped;

    ActiveSound(SoundAsset soundAsset, float volume) {
        this.soundAsset = soundAsset;
        this.volume = volume;
    }

    boolean finished() {
        return stopped || position >= soundAsset.getPcm().length;
    }

    void stop() {
        stopped = true;
    }
}
