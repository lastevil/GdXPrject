package com.mygdx.game.persons;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.actions.Actions;
import com.mygdx.game.animation.MyAtlasAnimation;
import com.mygdx.game.api.MyContactListener;
import com.mygdx.game.api.ProjectPhysic;

import java.util.HashMap;
import java.util.Map;

public class MainHero {
    HashMap<Actions, MyAtlasAnimation> manAssetss;
    private final float FPS = 1 / 4f;
    private float time;
    public static boolean canJump;
    private Animation<TextureAtlas.AtlasRegion> baseAnm;
    private boolean loop;
    private Body body;
    private Dir dir;

    private Actions currentAction;
    private static float dScale = 2.8f;
    private float hitPoints, live;

    public enum Dir {LEFT, RIGHT}

    public MainHero(Body body) {
        hitPoints = live = 100;
        this.body = body;
        manAssetss = new HashMap<>();
        manAssetss.put(Actions.JUMP, new MyAtlasAnimation("atlases/hero.atlas", "jump", FPS, "music/game-sfx-jump.wav", true));
        manAssetss.put(Actions.RUN, new MyAtlasAnimation("atlases/hero.atlas", "run", FPS, "music/footstep.wav", true));
        manAssetss.put(Actions.SPRINT, new MyAtlasAnimation("atlases/hero.atlas", "sprint", FPS, "music/footstep.wav", true));
        manAssetss.put(Actions.STAY, new MyAtlasAnimation("atlases/hero.atlas", "stay", FPS, null, true));
        manAssetss.put(Actions.HIT, new MyAtlasAnimation("atlases/hero.atlas", "hit", FPS, null, true));
        manAssetss.put(Actions.SHOT, new MyAtlasAnimation("atlases/hero.atlas", "shot", FPS, "music/sprint.wav", true));
        manAssetss.put(Actions.JUMP_SHOT, new MyAtlasAnimation("atlases/hero.atlas", "jump_shot", FPS, "music/sprint.wav", true));
        manAssetss.put(Actions.WIN, new MyAtlasAnimation("atlases/hero.atlas", "win", FPS, "music/win.wav", true));
        baseAnm = manAssetss.get(Actions.STAY).getAnimationRegion();
        loop = true;
        dir = Dir.LEFT;
        currentAction = Actions.STAY;
    }

    public float getHit(float damage) {
        hitPoints -= damage;
        return hitPoints;
    }

    public void setHitPoint(float oldHit) {
        hitPoints = hitPoints - (live - oldHit);
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

    public int getDir() {
        return (dir == Dir.LEFT) ? -1 : 1;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public TextureRegion getActionFrame(Actions actions) {
        return manAssetss.get(actions).draw();
    }

    public Body setFPS(Vector2 vector, boolean onGround) {
        if (vector.x > 0.1f) setDir(Dir.RIGHT);
        if (vector.x < -0.1f) setDir(Dir.LEFT);
        float tmp = (float) (Math.sqrt(vector.x * vector.x + vector.y * vector.y)) * 4;
        if (MyContactListener.isShocked) {
            setState(Actions.HIT);
            manAssetss.get(Actions.HIT).setTime(FPS);
            if (MyContactListener.isDamage) {
                body.setLinearVelocity(0,0);
                body.applyForceToCenter(-0.7f * MyContactListener.contactDmgVector.x, -1*MyContactListener.contactDmgVector.y, true);
            }
        } else {
            if (Math.abs(vector.x) > 0.2f && Math.abs(vector.y) < 10 && onGround && canJump) {
                setState(Actions.RUN);
                manAssetss.get(Actions.RUN).setTime(4 / tmp);
            }
            if (Math.abs(vector.x) > 3f && Math.abs(vector.y) < 10 && onGround && canJump) {
                setState(Actions.SPRINT);
                manAssetss.get(Actions.SPRINT).setTime(1 / tmp);
            }
            if (Math.abs(vector.y) > 1 && !canJump) {
                setState(Actions.JUMP);
                manAssetss.get(Actions.JUMP).setTime(1.3f / tmp);
            }
            if (MyContactListener.isShoot && onGround) {
                setState(Actions.SHOT);
                manAssetss.get(Actions.SHOT).setTime(FPS);
                return body;
            }
            if (MyContactListener.isShoot && !onGround) {
                setState(Actions.JUMP_SHOT);
                manAssetss.get(Actions.JUMP_SHOT).setTime(FPS);
                return body;
            }

            if (Math.abs(vector.x) <= 0.02f && onGround && !MyContactListener.isShoot) {
                setState(Actions.STAY);
            }
        }
        return null;
    }

    public float setTime(float deltaTime) {
        time += deltaTime;
        return time;
    }

    public void setState(Actions state) {
        baseAnm = manAssetss.get(state).getAnimationRegion();
        currentAction = state;
        switch (state) {
            case STAY:
            case SPRINT:
            case JUMP_SHOT:
            case SHOT:
                manAssetss.get(state).setTime(FPS);
                manAssetss.get(state).setPlayMode(true);
                loop = true;
                break;
            case JUMP:
            case HIT:
                manAssetss.get(state).setPlayMode(true);
                loop = false;
                break;
            default:
                loop = true;
        }
    }

    public TextureRegion getFrame() {
        if (time > baseAnm.getAnimationDuration() && loop) time = 0;
        if (time > baseAnm.getAnimationDuration()) time = 0;
        TextureRegion tr = manAssetss.get(currentAction).draw();
        if (!tr.isFlipX() && dir == Dir.RIGHT) tr.flip(true, false);
        if (tr.isFlipX() && dir == Dir.LEFT) tr.flip(true, false);
        return tr;
    }

    public Rectangle getRect() {
        TextureRegion tr = manAssetss.get(currentAction).draw();
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
