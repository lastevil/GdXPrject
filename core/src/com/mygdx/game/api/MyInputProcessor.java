package com.mygdx.game.api;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class MyInputProcessor implements InputProcessor {
    private ArrayList<String> outString;
    private Vector2 outForce;

    public MyInputProcessor() {
        outString = new ArrayList<>();
        outForce = new Vector2();
    }

    public String getOutString() {
        return outString.toString();
    }

    public Vector2 getVector() {
        return outForce;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!outString.contains(Input.Keys.toString(keycode))) {
            if (Input.Keys.toString(keycode).equals("Left")) {
                outString.add("A");
            } else if (Input.Keys.toString(keycode).equals("Right")) {
                outString.add("D");
            } else if (Input.Keys.toString(keycode).equals("Up")) {
                outString.add("W");
            } else if (Input.Keys.toString(keycode).equals("Down")) {
                outString.add("S");
            } else {
                outString.add(Input.Keys.toString(keycode));
            }
        }
        if (outString.contains("Space")) {
            outForce.add(0, 3.5f);
        }
        if (outString.contains("A") && !outString.contains("Shift")) {
            outForce.add(-0.6f, 0);
        }
/*        if (outString.contains("S")) {
        }*/
        if ((outString.contains("D")) && !outString.contains("Shift")) {
            outForce.add(0.6f, 0);
        }
/*        if (outString.contains("W")) {
        }*/
        if (outString.contains("L-Shift") && (outString.contains("A") || outString.contains("Left"))) {
            outForce.add(-0.05f, 0);
        }
        if (outString.contains("L_Shift") && outString.contains("D") || outString.contains("Right")) {
            outForce.add(0.05f, 0);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        String s = Input.Keys.toString(keycode);
        if (s.equals("Right")){
            s = "D";
        }
        if (s.equals("Left")){
            s = "A";
        }
        if (s.equals("Up")){
            s = "W";
        }
        if (s.equals("Down")){
            s = "S";
        }

        if (outString.contains(s)) {
            if (s.equals("Space")) {
                outForce.set(outForce.x, 0);
            }
            if (s.equals("A") ) {
                outForce.set(0, outForce.y);
            }
            if (s.equals("D") ) {
                outForce.set(0, outForce.y);
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
}
