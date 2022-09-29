package com.mygdx.game.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Label {
    private BitmapFont font;

    public Label(int size){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ofont.ru_Tolkien.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.RED;
        parameter.genMipMaps = true;
        parameter.magFilter = Texture.TextureFilter.MipMap;
        parameter.characters = "-HP:0123456789ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮйцукенгшщзхъфывапролджэячсмитьбю!.,";
        font = generator.generateFont(parameter);
    }

    public void draw(SpriteBatch batch, String text, int x, int y){font.draw(batch, text, x, y+font.getLineHeight());}

    public void dispose(){
        font.dispose();
    }
}
