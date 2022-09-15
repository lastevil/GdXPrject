package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.actions.CoordinatesActions;
import com.mygdx.game.animation.MyAnimations;
import com.mygdx.game.animation.MyAtlasAnimation;
import com.mygdx.game.api.MyInputProcessor;
import com.mygdx.game.audio.BackgroundMusic;
import com.mygdx.game.audio.GameSounds;

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private BackgroundMusic backgroundMusic;
    private GameSounds sounds;
    private MyInputProcessor myInputProcessor;

    private CoordinatesActions coordinatesActions;
    private MyAnimations animations;
    private float[] coordinate;

    private String currentRegion;
    private int dir;

    @Override
    public void create() {
        coordinate = new float[2];
        animations = new MyAnimations();
        dir = 1;
        currentRegion = "";
        animations.addAnimation("stay", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_stay", 6, Animation.PlayMode.LOOP));
        animations.addAnimation("smash", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_smash", 6, Animation.PlayMode.LOOP));
        animations.addAnimation("jump", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_jump", 5, Animation.PlayMode.LOOP));
        animations.addAnimation("tired", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_tired", 6, Animation.PlayMode.LOOP));
        animations.addAnimation("run", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_run", 6, Animation.PlayMode.LOOP));
        animations.addAnimation("ball", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_ball", 6, Animation.PlayMode.LOOP));
        myInputProcessor = new MyInputProcessor();
        coordinatesActions = new CoordinatesActions(myInputProcessor);
        Gdx.input.setInputProcessor(myInputProcessor);
        batch = new SpriteBatch();
        backgroundMusic = new BackgroundMusic("sounds/background.wav", 0.123f);
        backgroundMusic.play();
        sounds = new GameSounds();
        sounds.addSound("run", "sounds/footstep.wav");
        sounds.addSound("jump", "sounds/game-sfx-jump.wav");
        sounds.addSound("headPain", "sounds/head_pain.mp3");
        img = new Texture("images/beckground.jpg");
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);
        batch.begin();
        batch.draw(img, 0, 0);
        if (currentRegion.equals("jump")) {
            if(animations.getAnimation(currentRegion).getAnimationFramesCount()!=3){
                currentRegion="jump";
                if(animations.getAnimation(currentRegion).getAnimationFramesCount()==0){
                        coordinate[0] = coordinate[0]+2*dir;
                        coordinate[1] = coordinate[1]+3;
                    }
                if(animations.getAnimation(currentRegion).getAnimationFramesCount()==1){
                    coordinate[0] = coordinate[0]+2*dir;
                    coordinate[1] = coordinate[1]+3;
                }
                if(animations.getAnimation(currentRegion).getAnimationFramesCount()==2){
                    coordinate[0] = coordinate[0]+2*dir;
                    coordinate[1] = coordinate[1]-3;
                }

            }
            else {coordinate[0] = coordinate[0]+2*dir;
                coordinate[1] = coordinate[1]-3;
                currentRegion="stay";}
        } else if (myInputProcessor.getOutString().isEmpty()) {
            currentRegion = "stay";
        }
        if (myInputProcessor.getOutString().contains("Space")) {
            stopSounds(sounds);
            currentRegion = "jump";
            sounds.getSound("jump").play();
        } else {

            if (myInputProcessor.getOutString().contains("A")) {
                currentRegion = "run";
                coordinate[0]--;
                dir = -1;
                sounds.getSound("run").play();
            }
            if (myInputProcessor.getOutString().contains("S")) {
                currentRegion = "run";
                coordinate[1]--;
                sounds.getSound("run").play();
            }
            if (myInputProcessor.getOutString().contains("D")) {
                currentRegion = "run";
                coordinate[0]++;
                dir = 1;
                sounds.getSound("run").play();
            }
            if (myInputProcessor.getOutString().contains("W")) {
                currentRegion = "run";
                sounds.getSound("run").play();
                coordinate[1]++;
            }
        }
        System.out.println(animations.getAnimation(currentRegion).getAnimationFramesCount());
        animations.getAnimation(currentRegion).setTime(Gdx.graphics.getDeltaTime());
        TextureRegion currentAnim = animations.getAnimation(currentRegion).draw();
        if (!currentAnim.isFlipX() & dir == -1) currentAnim.flip(true, false);
        if (currentAnim.isFlipX() & dir == 1) currentAnim.flip(true, false);
        batch.draw(currentAnim, coordinate[0], coordinate[1]);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        backgroundMusic.dispose();
        sounds.dispose();
        animations.dispose();
    }

    private void stopSounds(GameSounds sounds) {
        sounds.stopSounds("jump");
        sounds.stopSounds("run");
    }
}
