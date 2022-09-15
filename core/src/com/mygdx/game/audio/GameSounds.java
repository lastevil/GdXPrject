package com.mygdx.game.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class GameSounds {
    private HashMap<String, Sound> sounds;

    public GameSounds() {
        sounds = new HashMap<>();
    }

    public void addSound(String name, String path) {
        sounds.put(name, Gdx.audio.newSound(Gdx.files.internal(path)));
    }

    public void playSound(String name){
        sounds.get(name).play();
    }
    public void stopSounds(String name){
        sounds.get(name).stop();
    }
    public Sound getSound(String name) {
        return sounds.get(name);
    }

    public void replaceSound(String name, String path){
        sounds.get(name).dispose();
        sounds.remove(name);
        addSound(name,path);
    }
    public void dispose() {
        for (Map.Entry<String, Sound> entry : sounds.entrySet()) {
            entry.getValue().dispose();
        }
        sounds.clear();
    }
}

