package com.mygdx.game.api;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.screens.GameLelOne;

public class MyContactListener implements ContactListener {
    public static int cnt=0;
    public static boolean isDamage=false;
    public static boolean onLand=true;

    public static boolean gameOver=false;
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData().equals("Hero") && b.getUserData().equals("fireball")) {
            GameLelOne.bodyToDelete.add(b.getBody());
        }
        if (b.getUserData().equals("Hero") && a.getUserData().equals("fireball")) {
            GameLelOne.bodyToDelete.add(a.getBody());
        }

        if (a.getUserData().equals("legs") && b.getUserData().equals("Land")) {
            cnt++;
            onLand = true;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("Land")) {
            cnt++;
            onLand = true;
        }

        if (a.getUserData().equals("legs") && b.getUserData().equals("WATER")) {
            gameOver = true;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("WATER")) {
            gameOver = true;
        }

        if (a.getUserData().equals("legs") && b.getUserData().equals("damage")) {
            isDamage = true;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("damage")) {
            isDamage = true;
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
        if (a.getUserData().equals("legs") && b.getUserData().equals("damage")) {
            isDamage = false;
        }
        if (b.getUserData().equals("legs") && a.getUserData().equals("damage")) {
            isDamage = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
