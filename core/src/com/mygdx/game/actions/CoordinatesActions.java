package com.mygdx.game.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.api.MyInputProcessor;

import java.util.HashMap;

public class CoordinatesActions {
    MyInputProcessor inputProcessor;

    public CoordinatesActions(MyInputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    //не доогадаля как возвращать текущую
    public HashMap<String, Body> doAction(Body body) {
        HashMap<String, Body> map = new HashMap();
        String action = "stay";
        if (inputProcessor.getOutString().isEmpty()) {
            action = "stay";
        }
        if (inputProcessor.getOutString().contains("Space")) {
            body.applyForceToCenter(new Vector2(0f, 0.35f), true);
            action = "jump";
        }
        if (inputProcessor.getOutString().contains("A")) {
            body.applyForceToCenter(new Vector2(-0.27f, 0f), true);
            action = "run";
        }
        if (inputProcessor.getOutString().contains("S")) {
            action = "run";
        }
        if (inputProcessor.getOutString().contains("D")) {
            body.applyForceToCenter(new Vector2(0.27f, 0f), true);
            action = "run";
        }
        if (inputProcessor.getOutString().contains("W")) {
            action = "run";
        }
        map.put(action, body);
        return map;
    }
}
