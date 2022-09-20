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

import java.util.HashMap;

public class MyGdxGame extends ApplicationAdapter {
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

    @Override
    public void create() {
        init();
        setAnimationsTextures();
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
        addPhysicObj(def, fixDef, shape, rect);

        fixDef.restitution = 0.4f;
        textureObj = map.getLayers().get("water");
        rect = textureObj.getObjects().getByType(RectangleMapObject.class);
        addPhysicObj(def, fixDef, shape, rect);


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
        fixDef.density = 1 / 1000;
        fixDef.friction = 1;
        fixDef.restitution = 0;
        body = physic.getWorld().createBody(def);
        body.createFixture(fixDef).setUserData("Hero");
        shape.dispose();
    }

    private void addPhysicObj(BodyDef def, FixtureDef fixDef, PolygonShape shape, Array<RectangleMapObject> rect) {
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

        mapRenderer.setView(camera);
        mapRenderer.render();
        body.setActive(true);
        HashMap<String, Body> myAction = coordinatesActions.doAction(coordinate, body);
        for (HashMap.Entry<String, Body> entry : myAction.entrySet()) {
            currentRegion = entry.getKey();
            body = entry.getValue();
        }

        if (myInputProcessor.getOutString().contains("A")) {
            coordinate[2] = -1;

        } if (myInputProcessor.getOutString().contains("D")){
            coordinate[2] = 1;
        }
        coordinate[0] = body.getPosition().x - 10;
        coordinate[1] = body.getPosition().y - 10;
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
    public void dispose() {
        batch.dispose();
        backgroundMusic.dispose();
        animations.dispose();
        physic.dispose();
        map.dispose();
        mapRenderer.dispose();
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
}
