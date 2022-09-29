package com.mygdx.game.persons;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.actions.Actions;
import com.mygdx.game.animation.MyAtlasAnimation;
import com.mygdx.game.api.ProjectPhysic;

import java.util.HashMap;
import java.util.Map;

public class MainHero {
    HashMap<Actions, MyAtlasAnimation> manAssetss;
    private final float FPS = 1 / 2f;
    private float time;
    public static boolean canJump;
    private Animation<TextureAtlas.AtlasRegion> baseAnm;
    private boolean loop;
    private Body body;
    private Dir dir;
    private static float dScale = 2.8f;
    private float hitPoints, live;

    public enum Dir {LEFT, RIGHT}

    public MainHero(Body body) {
        hitPoints = live = 100;
        this.body = body;
        manAssetss = new HashMap<>();
        manAssetss.put(Actions.JUMP,new MyAtlasAnimation("atlases/hero.atlas", "jump", FPS, "music/game-sfx-jump.wav", true));
        manAssetss.put(Actions.RUN, new MyAtlasAnimation("atlases/hero.atlas", "run", FPS, "music/footstep.wav", true));
        manAssetss.put(Actions.SPRINT,  new MyAtlasAnimation("atlases/hero.atlas", "sprint", FPS, "music/sprint.wav", true));
        manAssetss.put(Actions.STAY, new MyAtlasAnimation("atlases/hero.atlas", "stay", FPS, null, true));
        manAssetss.put(Actions.SHOOT, new MyAtlasAnimation("atlases/hero.atlas", "shoot", FPS, null, true));
        manAssetss.put(Actions.WIN, new MyAtlasAnimation("atlases/hero.atlas", "win", FPS, "music/win.wav", true));
        baseAnm = manAssetss.get(Actions.STAY).getAnimationRegion();
        loop = true;
        dir = Dir.LEFT;
    }

    public float getHit(float damage) {
        hitPoints -= damage;
        return hitPoints;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public void setCanJump(boolean isJump) {
        canJump = isJump;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setFPS(Vector2 vector, boolean onGround) {
        if (vector.x > 0.1f) setDir(Dir.RIGHT);
        if (vector.x < -0.1f) setDir(Dir.LEFT);
        float tmp = (float) (Math.sqrt(vector.x * vector.x + vector.y * vector.y)) * 10;
        setState(Actions.STAY);
        if (Math.abs(vector.x) > 0.25f && Math.abs(vector.y) < 10 && onGround) {
            setState(Actions.RUN);
            baseAnm.setFrameDuration(1 / tmp);
        }
        if (Math.abs(vector.x) > 2f && Math.abs(vector.y) < 10 && onGround) {
            setState(Actions.SPRINT);
            baseAnm.setFrameDuration(1 / tmp);
        }
        if (Math.abs(vector.y) > 1 && !canJump) {
            setState(Actions.JUMP);
            baseAnm.setFrameDuration(FPS);
        }
    }

    public float setTime(float deltaTime) {
        time += deltaTime;
        return time;
    }

    public void setState(Actions state) {
        baseAnm = manAssetss.get(state).getAnimationRegion();
        switch (state) {
            case STAY:
                loop = true;
                baseAnm.setFrameDuration(FPS);
                break;
            case SPRINT:
                loop = true;
                baseAnm.setFrameDuration(FPS);
                break;
            case JUMP:
                loop = false;
                break;
            default:
                loop = true;
        }
    }
    public TextureRegion getFrame() {
        if (time > baseAnm.getAnimationDuration() && loop) time = 0;
        if (time > baseAnm.getAnimationDuration()) time = 0;
        TextureRegion tr = baseAnm.getKeyFrame(time);
        if (!tr.isFlipX() && dir == Dir.RIGHT) tr.flip(true, false);
        if (tr.isFlipX() && dir == Dir.LEFT) tr.flip(true, false);
        return tr;
    }

    public Rectangle getRect() {
        TextureRegion tr = baseAnm.getKeyFrame(time);
        float cx = body.getPosition().x * ProjectPhysic.PPM - tr.getRegionWidth() / 2 / dScale;
        float cy = body.getPosition().y * ProjectPhysic.PPM - tr.getRegionHeight() / 2 / dScale;
        float cW = tr.getRegionWidth() / ProjectPhysic.PPM / dScale;
        float cH = tr.getRegionHeight() / ProjectPhysic.PPM / dScale;
        return new Rectangle(cx, cy, cW, cH);
    }

    public void dispose() {
        for (Map.Entry<Actions, MyAtlasAnimation> entry : manAssetss.entrySet()) {
            entry.getValue().dispose();
        }
        this.manAssetss.clear();
    }
}
