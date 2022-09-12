package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    AnimationOne anim;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("beckground.jpg");
        anim = new AnimationOne("pngwing.png", 3, 4, 24f, Animation.PlayMode.LOOP);
    }

    @Override
    public void render() {
        anim.setTime(Gdx.graphics.getDeltaTime());
        float x = Gdx.input.getX() - anim.draw().getRegionHeight() / 2;
        float y = Gdx.graphics.getWidth() - Gdx.input.getY() - anim.draw().getRegionWidth() + 10;
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.draw(anim.draw(), x, y);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        anim.dispose();
        img.dispose();
    }
}
