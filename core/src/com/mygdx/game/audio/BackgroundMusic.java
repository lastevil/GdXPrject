package com.mygdx.game.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class BackgroundMusic {
    private Music music;
    public float volume;

    public BackgroundMusic(String path, float volume) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal(path));
        this.volume = volume;
        this.music.setVolume(volume);
        this.music.setLooping(true);
    }

    public Music getMusic() {
        return music;
    }

    public void setNewMusic(String path){
        this.music = Gdx.audio.newMusic(Gdx.files.internal(path));
        this.music.setVolume(volume);
        this.music.setLooping(true);
    }

    public void setVolume(float volume){
        this.volume=volume;
        this.music.setVolume(volume);
    }

    public void play() {
        this.music.play();
    }

    public void stop() {
        this.music.stop();
    }

    public void dispose() {
        this.music.dispose();
    }

}
