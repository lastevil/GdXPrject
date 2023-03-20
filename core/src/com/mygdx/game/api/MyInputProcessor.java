package com.mygdx.game.api;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class MyInputProcessor implements InputProcessor {
    private ArrayList<String> outString;
    private Vector2 outForce;

    private float cameraY, cameraX;

    public MyInputProcessor() {
        outString = new ArrayList<>();
        outForce = new Vector2();
        cameraY = cameraX= 0;
    }

    public String getOutString() {
        return outString.toString();
    }

    public Vector2 getVector() {
        return outForce;
    }

    public float getCameraY() {
        return cameraY;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!outString.contains(Input.Keys.toString(keycode))) {
            outString.add(Input.Keys.toString(keycode));
            if (outString.contains("Space")) {
                MyContactListener.isShoot = true;
            }
            if (outString.contains("A") && !outString.contains("Shift") && !outString.contains("Space")) {
                outForce.add(-0.4f, 0);
            }
            if (outString.contains("S")) {
                outForce.add(0, -0.5f);
            }
            if ((outString.contains("D")) && !outString.contains("Shift") && !outString.contains("Space")) {
                outForce.add(0.4f, 0);
            }
            if (outString.contains("W")) {
                outForce.add(0, 2f);
            }
            if (outString.contains("L-Shift") && (outString.contains("A"))) {
                outForce.add(-0.05f, 0);
            }
            if (outString.contains("L_Shift") && outString.contains("D")) {
                outForce.add(0.05f, 0);
            }
            if (Input.Keys.toString(keycode).equals("Left")) {
                cameraX = -50;
            }
            if (Input.Keys.toString(keycode).equals("Right")) {
                cameraX = 50;
            }
            if (Input.Keys.toString(keycode).equals("Up")) {
                cameraY = 50;
            }
            if (Input.Keys.toString(keycode).equals("Down")) {
                cameraY = -50;
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        String s = Input.Keys.toString(keycode);
        if (outString.contains(s)) {
            if (s.equals("Space")) {
                MyContactListener.isShoot = false;
            }
            if (s.equals("A")) {
                outForce.set(0, outForce.y);
            }
            if (s.equals("D")) {
                outForce.set(0, outForce.y);
            }
            if (s.equals("UP") || s.equals("Down")) {
                cameraY = 0;
            }
            if (s.equals("Left") || s.equals("Right")){
                cameraX = 0;
            }
            if (s.equals("S") || s.equals("W")) {
                outForce.set(outForce.x, 0);
            }
            outString.remove(s);
        }
        return true;

    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public float getCameraX() {
        return cameraX;
    }
}
