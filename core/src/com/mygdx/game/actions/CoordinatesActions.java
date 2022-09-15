package com.mygdx.game.actions;

import com.mygdx.game.api.MyInputProcessor;
import com.mygdx.game.audio.GameSounds;

public class CoordinatesActions {
    MyInputProcessor inputProcessor;

    public CoordinatesActions(MyInputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }
    //не доогадаля как возвращать текущую
    public float[] doAction(float[] coordinate, GameSounds sounds) {
        if (inputProcessor.getOutString().isEmpty()){
        }
        if (inputProcessor.getOutString().contains("Space")) {
            stopSounds(sounds);
            sounds.getSound("jump").play();
        } else {

            if (inputProcessor.getOutString().contains("A")) {
                coordinate[0]--;
                sounds.getSound("run").play();
            }
            if (inputProcessor.getOutString().contains("S")) {
                coordinate[1]--;
                sounds.getSound("run").play();
            }
            if (inputProcessor.getOutString().contains("D")) {
                coordinate[0]++;
                sounds.getSound("run").play();
            }
            if (inputProcessor.getOutString().contains("W")) {
                sounds.getSound("run").play();
                coordinate[1]++;
            }
        }
        return coordinate;
    }

    private void stopSounds(GameSounds sounds){
        sounds.stopSounds("jump");
        sounds.stopSounds("run");
    }
}
