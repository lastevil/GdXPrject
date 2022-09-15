package com.mygdx.game.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAtlasAnimation {
    private TextureAtlas atlas;
    private Animation<TextureAtlas.AtlasRegion> animation;
    private float time;
    private float fps;

    public MyAtlasAnimation(String atlasPath, String regionName, float fps, Animation.PlayMode playMode) {
        time = 0;
        this.fps =fps;
        this.atlas = new TextureAtlas(atlasPath);
        animation = new Animation<>(1 / fps, this.atlas.findRegions(regionName));
        animation.setPlayMode(playMode);
    }

    public TextureRegion draw() {
        return animation.getKeyFrame(time);
    }

    public int getAnimationFramesCount(){
        return animation.getKeyFrameIndex(time);
    }

    public void setRegionName(String regionName, Animation.PlayMode playMode){
        animation = null;
        animation = new Animation<>(1/fps, this.atlas.findRegion(regionName));
        animation.setPlayMode(playMode);
    }


    public void setTime(float timeSet) {
        this.time += timeSet;
    }

    public void dispose() {
        this.atlas.dispose();
    }
}
