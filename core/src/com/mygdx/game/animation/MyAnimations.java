package com.mygdx.game.animation;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.MyGdxGame;

import java.util.HashMap;
import java.util.Map;

public class MyAnimations {
    HashMap<String,MyAtlasAnimation> map;

    public MyAnimations(){
        map = new HashMap<>();
    }

    public void addAnimation(String name,MyAtlasAnimation animation){
        map.put(name,animation);
    }

    public MyAtlasAnimation getAnimation(String name){
        return map.get(name);
    }

    public void dispose(){
        for (Map.Entry<String, MyAtlasAnimation> entry : map.entrySet()) {
            entry.getValue().dispose();
        }
        map.clear();
    }
}
