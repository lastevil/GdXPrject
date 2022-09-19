package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.actions.CoordinatesActions;
import com.mygdx.game.actions.ProjectPhysic;
import com.mygdx.game.animation.MyAnimations;
import com.mygdx.game.animation.MyAtlasAnimation;
import com.mygdx.game.api.MyInputProcessor;
import com.mygdx.game.audio.BackgroundMusic;
import com.mygdx.game.audio.GameSounds;

import java.util.HashMap;

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private BackgroundMusic backgroundMusic;
    private GameSounds sounds;
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
        animations.addAnimation("stay", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_stay", 10, Animation.PlayMode.LOOP));
        animations.addAnimation("smash", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_smash", 10, Animation.PlayMode.LOOP));
        animations.addAnimation("jump", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_jump", 7, Animation.PlayMode.LOOP));
        animations.addAnimation("tired", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_tired", 10, Animation.PlayMode.LOOP));
        animations.addAnimation("run", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_run", 10, Animation.PlayMode.LOOP));
        animations.addAnimation("ball", new MyAtlasAnimation("images/atlas/pika.atlas", "pika_ball", 10, Animation.PlayMode.LOOP));
    }
    @Override
    public void create() {
        init();
        setAnimationsTextures();
        setSounds();

        backgroundMusic.play();
        BodyDef def = new BodyDef();
        FixtureDef fixDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        def.type = BodyDef.BodyType.StaticBody;
        fixDef.shape = shape;
        fixDef.density = 1;
        fixDef.friction = 1;
        fixDef.restitution = 0;

        MapLayer textureObj = map.getLayers().get("land");
        Array<RectangleMapObject> rect = textureObj.getObjects().getByType(RectangleMapObject.class);
        addPhysicObj(def,fixDef, shape, rect);

        fixDef.restitution = 0.4f;
        textureObj = map.getLayers().get("water");
        rect = textureObj.getObjects().getByType(RectangleMapObject.class);
        addPhysicObj(def, fixDef,shape, rect);


        def.type = BodyDef.BodyType.DynamicBody;
        MapLayer layer = map.getLayers().get("hero");
        RectangleMapObject hero = (RectangleMapObject) layer.getObjects().get("hero");
        float x = hero.getRectangle().x + hero.getRectangle().width / 2;
        float y = hero.getRectangle().y + hero.getRectangle().height / 2;
        float w = hero.getRectangle().width / 2;
        float h = hero.getRectangle().height / 2;
        def.position.set(x, y);
        shape.setAsBox(w, h);
        fixDef.shape = shape;
        fixDef.density = 1/3000;
        fixDef.friction = 3;
        fixDef.restitution = 0;
        body = physic.getWorld().createBody(def);
        body.createFixture(fixDef).setUserData("Hero");
        body.setGravityScale(3);
        shape.dispose();
    }

    private void addPhysicObj(BodyDef def,FixtureDef fixDef, PolygonShape shape, Array<RectangleMapObject> rect) {
        for (int i = 0; i < rect.size; i++) {
            float x = rect.get(i).getRectangle().x + rect.get(i).getRectangle().width / 2;
            float y = rect.get(i).getRectangle().y + rect.get(i).getRectangle().height / 2;
            float w = rect.get(i).getRectangle().width / 2;
            float h = rect.get(i).getRectangle().height / 2;
            def.position.set(x, y);
            shape.setAsBox(w, h);
            physic.getWorld().createBody(def).createFixture(fixDef).setUserData("Land");
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.position.x = body.getPosition().x;
        camera.position.y = body.getPosition().y;
        coordinate[0] = body.getPosition().x;
        coordinate[1] = body.getPosition().y;
        camera.zoom = 0.3f;
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        mapRenderer.setView(camera);
        mapRenderer.render();
        body.setActive(true);
        HashMap<String, Body> myAction = coordinatesActions.doAction(coordinate, sounds, body);
        for (HashMap.Entry<String, Body> entry : myAction.entrySet()) {
            currentRegion = entry.getKey();
            body = entry.getValue();
        }

        if (body.getLinearVelocity().x < 0) {
            coordinate[2] = -1;

        } else {
            coordinate[2] = 1;
        }
        coordinate[0] = body.getPosition().x-10;
        coordinate[1] = body.getPosition().y-10;
        animations.getAnimation(currentRegion).setTime(Gdx.graphics.getDeltaTime());
        TextureRegion currentAnim = animations.getAnimation(currentRegion).draw();
        if (!currentAnim.isFlipX() & coordinate[2] == -1) currentAnim.flip(true, false);
        if (currentAnim.isFlipX() & coordinate[2] == 1) currentAnim.flip(true, false);
        physic.step();
        physic.debugDraw(camera);
        batch.draw(currentAnim, coordinate[0], coordinate[1]);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundMusic.dispose();
        sounds.dispose();
        animations.dispose();
        physic.dispose();
        map.dispose();
        mapRenderer.dispose();
    }

    private void init() {
        coordinate = new float[3];
        animations = new MyAnimations();
        currentRegion = "";
        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);
        coordinatesActions = new CoordinatesActions(myInputProcessor);
        batch = new SpriteBatch();
        backgroundMusic = new BackgroundMusic("sounds/background.wav", 0.123f);
        sounds = new GameSounds();
        camera = new OrthographicCamera();
        physic = new ProjectPhysic();
        map = new TmxMapLoader().load("map/myMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
    }

    private void setSounds() {
        sounds.addSound("run", "sounds/footstep.wav");
        sounds.addSound("jump", "sounds/game-sfx-jump.wav");
        sounds.addSound("headPain", "sounds/head_pain.mp3");
    }

}
