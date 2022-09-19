package com.mygdx.game.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.api.MyInputProcessor;
import com.mygdx.game.audio.GameSounds;

import java.util.HashMap;

public class CoordinatesActions {
    MyInputProcessor inputProcessor;

    public CoordinatesActions(MyInputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    //не доогадаля как возвращать текущую
    public HashMap<String, Body> doAction(float[] coordinate, GameSounds sounds, Body body) {
        HashMap<String, Body> map = new HashMap();
        String action = "stay";
        if (inputProcessor.getOutString().isEmpty()) {
            action = "stay";
        }
        if (inputProcessor.getOutString().contains("Space")) {
            stopSounds(sounds);
            body.applyForceToCenter(new Vector2(0f, 200f), true);
            action = "jump";
            sounds.getSound("jump").play();
        } else {

            if (inputProcessor.getOutString().contains("A")) {
                body.applyForceToCenter(new Vector2(-50, 0f), true);

                action = "run";
                sounds.getSound("run").play();
            }
            if (inputProcessor.getOutString().contains("S")) {

                action = "run";
                sounds.getSound("run").play();
            }
            if (inputProcessor.getOutString().contains("D")) {
                body.applyForceToCenter(new Vector2(50, 0f), true);
                action = "run";
                sounds.getSound("run").play();
            }
            if (inputProcessor.getOutString().contains("W")) {
                sounds.getSound("run").play();
                action = "run";
                coordinate[1]++;
            }
        }
        map.put(action, body);
        return map;
    }

    private void stopSounds(GameSounds sounds) {
        sounds.stopSounds("jump");
        sounds.stopSounds("run");
    }
}
