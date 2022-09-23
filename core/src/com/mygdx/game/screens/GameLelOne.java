package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.actions.CoordinatesActions;
import com.mygdx.game.animation.MyAnimations;
import com.mygdx.game.animation.MyAtlasAnimation;
import com.mygdx.game.api.MyInputProcessor;
import com.mygdx.game.api.ProjectPhysic;
import com.mygdx.game.audio.BackgroundMusic;

import java.util.HashMap;

public class GameLelOne implements Screen {

    private SpriteBatch batch;
    private BackgroundMusic backgroundMusic;
    private MyInputProcessor myInputProcessor;
    private OrthographicCamera camera;
    private CoordinatesActions coordinatesActions;
    private MyAnimations animations;
    private float[] coordinate;
    private String currentRegion;
    private ProjectPhysic physic;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Body body;

    private void setAnimationsTextures() {
        animations.addAnimation("stay", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_stay", 10, null, true));
        animations.addAnimation("smash", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_smash", 10, null, true));
        animations.addAnimation("jump", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_jump", 7, "music/game-sfx-jump.wav", true));
        animations.addAnimation("tired", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_tired", 10, null, true));
        animations.addAnimation("run", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_run", 10, "music/footstep.wav", true));
        animations.addAnimation("ball", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_ball", 10, null, true));
    }

    private void init() {
        coordinate = new float[3];
        coordinate[2] = 1;
        animations = new MyAnimations();
        currentRegion = "";
        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);
        coordinatesActions = new CoordinatesActions(myInputProcessor);
        batch = new SpriteBatch();
        backgroundMusic = new BackgroundMusic("music/background.wav", 0.123f);
        camera = new OrthographicCamera();
        physic = new ProjectPhysic();
        map = new TmxMapLoader().load("map/myMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
    }

    public GameLelOne(Game game) {
        init();
        setAnimationsTextures();
        backgroundMusic.play();
        Array<RectangleMapObject> objects = map.getLayers().get("land").getObjects().getByType(RectangleMapObject.class);
        objects.addAll(map.getLayers().get("water").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            physic.addObject(objects.get(i));
        }
        body = physic.addObject((RectangleMapObject) map.getLayers().get("hero").getObjects().get("Hero"));
        body.setFixedRotation(true);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.position.x = body.getPosition().x * physic.PPM;
        camera.position.y = body.getPosition().y * physic.PPM;
        coordinate[0] = body.getPosition().x * physic.PPM;
        coordinate[1] = body.getPosition().y * physic.PPM;
        camera.zoom = 0.3f;
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();
        body.setActive(true);
        HashMap<String, Body> myAction = coordinatesActions.doAction(body);
        for (HashMap.Entry<String, Body> entry : myAction.entrySet()) {
            currentRegion = entry.getKey();
            body = entry.getValue();
        }
        if (myInputProcessor.getOutString().contains("A")) {
            coordinate[2] = -1;
        }
        if (myInputProcessor.getOutString().contains("D")) {
            coordinate[2] = 1;
        }
        coordinate[0] = body.getPosition().x * physic.PPM - 10;
        coordinate[1] = body.getPosition().y * physic.PPM - 10;
        animations.getAnimation(currentRegion).setTime(Gdx.graphics.getDeltaTime());
        TextureRegion currentAnim = animations.getAnimation(currentRegion).draw();
        if (!currentAnim.isFlipX() & coordinate[2] == -1) currentAnim.flip(true, false);
        if (currentAnim.isFlipX() & coordinate[2] == 1) currentAnim.flip(true, false);

        physic.step();
        physic.debugDraw(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(currentAnim, coordinate[0], coordinate[1], 20, 20);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
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
        batch.dispose();
        backgroundMusic.dispose();
        animations.dispose();
        physic.dispose();
        map.dispose();
        mapRenderer.dispose();
    }
}
