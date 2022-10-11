package com.mygdx.game.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationOne {
    Texture img;
    Animation<TextureRegion> anim;

    private float time;

    AnimationOne(String name, int row, int column, float frame, Animation.PlayMode playMode) {
        time=0;
        img = new Texture(name);
        TextureRegion region = new TextureRegion(img);
        TextureRegion[][] splitRegion = region.split(img.getWidth() / column, img.getHeight() / row);
        TextureRegion[] finalRegion = new TextureRegion[splitRegion.length * splitRegion[0].length];
        int count=0;
        for (int i = 0; i < splitRegion.length; i++) {
            for (int j = 0; j < splitRegion[0].length; j++) {
                finalRegion[count++] = splitRegion[i][j];
            }
        }
        anim = new Animation<TextureRegion>(1 / frame, finalRegion);
        anim.setPlayMode(playMode);
    }

    public TextureRegion draw() {
        return anim.getKeyFrame(time);
    }

    public void setTime(float timeSet){
        this.time += timeSet;
    }
    public void dispose(){
        this.img.dispose();
    }
}
