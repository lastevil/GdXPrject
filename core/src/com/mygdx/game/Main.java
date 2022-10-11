package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screens.MainMenu;

public class Main extends Game {
    @Override
    public void render() {
        super.render();
    }

    @Override
    public void create() {
        setScreen(new MainMenu(this));
    }
}
