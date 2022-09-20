package com.mygdx.game.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAtlasAnimation {
    private TextureAtlas atlas;
    private Animation<TextureAtlas.AtlasRegion> animation;
    private float time;
    private float fps;
    private Sound sound;
    private boolean loop;
    private float d;
    private String name;

    public MyAtlasAnimation(String atlasPath, String regionName, float fps, String animSound, boolean playMode) {
        if (playMode) loop = true;
        name = regionName;
        if (animSound == null || animSound.isEmpty()) {
            this.sound = null;
        } else {
            this.sound = Gdx.audio.newSound(Gdx.files.internal(animSound));
        }
        time = 0;
        this.fps = fps;
        this.atlas = new TextureAtlas(atlasPath);
        animation = new Animation<>(1 / fps, this.atlas.findRegions(regionName));
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        d = animation.getAnimationDuration() / 2;
    }

    public TextureRegion draw() {
        return animation.getKeyFrame(time);
    }

    public void setTime(float timeSet) {
        time += timeSet;
        if (time > d && time < animation.getAnimationDuration()) {
            if (sound != null) {
                sound.play();
            }
            d *= 2;
        } else if (time >= animation.getAnimationDuration() && loop) {
            time = 0;
            d = animation.getAnimationDuration() / 2;
            if (sound != null) {
                sound.play();
            }

        }
    }

    public void dispose() {
        this.atlas.dispose();
        if (sound != null) {
            this.sound.dispose();
        }

    }
}
