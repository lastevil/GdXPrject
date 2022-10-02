package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MainMenu implements Screen {
    private Game game;
    Texture fon, button;
    SpriteBatch batch;
    int x;
    Rectangle rectangle;
    Music music;
    Sound sound;

    public MainMenu(Game game) {
        this.game = game;
        fon = new Texture("images/menuScreen/FonMenu.png");
        button = new Texture("images/menuScreen/pressstart.png");
        x = Gdx.graphics.getWidth() / 2 - button.getWidth() / 2;
        rectangle = new Rectangle(x, 0, button.getWidth(), button.getHeight());
        batch = new SpriteBatch();
        music = Gdx.audio.newMusic(Gdx.files.internal("music/menuMusic/fon.mp3"));
        music.setVolume(0.123f);
        sound = Gdx.audio.newSound(Gdx.files.internal("music/menuMusic/button.wav"));
        music.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(fon, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(button, x, 0);
        batch.end();
        if (Gdx.input.isTouched()) {
            if (rectangle.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                sound.play();
                dispose();
                game.setScreen(new GameLelOne(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        button.dispose();
        fon.dispose();
        batch.dispose();
        music.dispose();
        sound.dispose();
    }
}
