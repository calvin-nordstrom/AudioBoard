package com.calvinnordstrom.audioboard.audio;

class ActiveSound {
    final Sound sound;
    final float volume;
    int position;
    volatile boolean stopped;

    ActiveSound(Sound sound, float volume) {
        this.sound = sound;
        this.volume = volume;
    }

    boolean finished() {
        return stopped || position >= sound.getPcm().length;
    }

    void stop() {
        stopped = true;
    }
}
