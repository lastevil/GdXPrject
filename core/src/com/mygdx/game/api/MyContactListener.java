package com.mygdx.game.api;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.screens.GameLelOne;
import com.mygdx.game.screens.GameLelTwo;

public class MyContactListener implements ContactListener {
    public static int cnt=0;
    public static boolean isDamage=false;
    public static boolean onLand=true;
    public static boolean gameOver=false;
    public static boolean finishLvl = false;
    public static boolean isShocked = false;
    public static boolean isShoot = false;
    public static Vector2 contactDmgVector = new Vector2(0,0);
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData().equals("Hero") && b.getUserData().equals("fireball")) {
            if (GameLelOne.bodyToDelete!=null){
            GameLelOne.bodyToDelete.add(b.getBody());
            }
            if (GameLelTwo.bodyToDelete!=null){
                GameLelTwo.bodyToDelete.add(b.getBody());
            }
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("fireball")) {
            if (GameLelOne.bodyToDelete!=null){
                GameLelOne.bodyToDelete.add(a.getBody());
            }
            if (GameLelTwo.bodyToDelete!=null){
                GameLelTwo.bodyToDelete.add(a.getBody());
            }
        }

        if (a.getUserData().equals("legs") && b.getUserData().equals("fireball")) {
            if (GameLelOne.bodyToDelete!=null){
                GameLelOne.bodyToDelete.add(b.getBody());
            }
            if (GameLelTwo.bodyToDelete!=null){
                GameLelTwo.bodyToDelete.add(b.getBody());
            }
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("fireball")) {
            if (GameLelOne.bodyToDelete!=null){
                GameLelOne.bodyToDelete.add(a.getBody());
            }
            if (GameLelTwo.bodyToDelete!=null){
                GameLelTwo.bodyToDelete.add(a.getBody());
            }
        }

        if (a.getUserData().equals("legs") && b.getUserData().equals("Land")) {
            cnt++;
            onLand = true;
            isShocked = false;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("Land")) {
            cnt++;
            onLand = true;
            isShocked = false;
        }

        if (a.getUserData().equals("legs") && b.getUserData().equals("WATER")) {
            gameOver = true;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("WATER")) {
            gameOver = true;
        }

        if (a.getUserData().equals("Hero") && b.getUserData().equals("finish")){
            finishLvl = true;
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("finish")){
            finishLvl = true;
        }

        if (a.getUserData().equals("Hero") && b.getUserData().equals("Damage")) {
            isDamage = true;
            isShocked = true;
            contactDmgVector = a.getBody().getLinearVelocity();
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("Damage")) {
            isDamage = true;
            isShocked = true;
            contactDmgVector = a.getBody().getLinearVelocity();
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData().equals("legs") && b.getUserData().equals("Land")) {
            cnt--;
            onLand = false;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("Land")) {
            cnt--;
            onLand = false;
        }
        if (a.getUserData().equals("Hero") && b.getUserData().equals("Damage")) {
            isDamage = false;
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("Damage")) {
            isDamage = false;
        }
        if (a.getUserData().equals("Hero") && b.getUserData().equals("finish")){
            finishLvl = false;
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("finish")){
            finishLvl = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
