package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.animation.Label;
import com.mygdx.game.animation.MyAtlasAnimation;
import com.mygdx.game.api.MyContactListener;
import com.mygdx.game.api.MyInputProcessor;
import com.mygdx.game.api.ProjectPhysic;
import com.mygdx.game.persons.MainHero;

public class GameLelTwo implements Screen {
    Game game;
    private SpriteBatch batch;
    private Music music;
    private MyInputProcessor myInputProcessor;
    private OrthographicCamera camera;
    private ProjectPhysic projectPhysic;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;
    private MyAtlasAnimation fireBall;
    private int[] front, tL;
    private final Label font;
    private final MainHero hero;
    public static Array<Body> bodyToDelete;
    private float damage;
    private Body body;
    private int winCount;

    private int lives;

    public GameLelTwo(Game game, float hitPoint, int lives) {
        this.lives = lives;
        winCount = 0;
        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);
        fireBall = new MyAtlasAnimation("atlases/fire.atlas", "fire", 4, null, true);
        map = new TmxMapLoader().load("map/map_lvl2.tmx");
        font = new Label(12);
        tL = new int[4];
        front = new int[2];
        front[1] = map.getLayers().getIndex("land");
        front[0] = map.getLayers().getIndex("tree");
        tL[0] = map.getLayers().getIndex("background");
        tL[1] = map.getLayers().getIndex("dmg");
        tL[2] = map.getLayers().getIndex("fon");
        tL[3] = map.getLayers().getIndex("water_fon");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        bodyToDelete = new Array<>();
        this.game = game;
        projectPhysic = new ProjectPhysic();
        Array<RectangleMapObject> objects = new Array<>();
        objects.addAll(map.getLayers().get("water").getObjects().getByType(RectangleMapObject.class));
        objects.addAll();
        objects.addAll(map.getLayers().get("lands").getObjects().getByType(RectangleMapObject.class));
        objects.addAll(map.getLayers().get("Finish").getObjects().getByType(RectangleMapObject.class));
        objects.addAll(map.getLayers().get("fireballs").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            projectPhysic.addObject(objects.get(i));
        }
        objects.clear();
        Array<PolylineMapObject> shape = map.getLayers().get("chain").getObjects().getByType(PolylineMapObject.class);
        for (int i = 0; i < shape.size; i++) {
            projectPhysic.addObject(shape.get(i));
        }

        objects.addAll(map.getLayers().get("damage").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            projectPhysic.addDmgObject(objects.get(i));
        }
        damage = (float) objects.get(0).getProperties().get("damage");
        body = projectPhysic.addObject((RectangleMapObject) map.getLayers().get("hero").getObjects().get("Hero"));
        body.setFixedRotation(true);
        hero = new MainHero(body);
        hero.setHitPoint(hitPoint);

        music = Gdx.audio.newMusic(Gdx.files.internal("music/levelTwo/cave.wav"));
        music.setVolume(0.025f);
        music.setLooping(true);
        music.play();

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.zoom = 0.35f;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.DARK_GRAY);

        camera.position.x = body.getPosition().x * projectPhysic.PPM + myInputProcessor.getCameraX();
        camera.position.y = body.getPosition().y * projectPhysic.PPM + myInputProcessor.getCameraY();
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render(tL);

        hero.setTime(delta);
        Vector2 vector = myInputProcessor.getVector();
        System.out.println(myInputProcessor.getOutString());
        if (MyContactListener.cnt < 1) {
            vector.set(vector.x, 0);
        }
        if (MyContactListener.onLand) {
            hero.setCanJump(true);
        } else {
            hero.setCanJump(false);
        }
        body.applyForceToCenter(vector, true);
        hero.setFPS(body.getLinearVelocity(), true);

        Rectangle tmp = hero.getRect();
        ((PolygonShape) body.getFixtureList().get(0).getShape()).setAsBox(tmp.width / 2, tmp.height / 2);
        ((PolygonShape) body.getFixtureList().get(1).getShape()).setAsBox(
                tmp.width / 3,
                tmp.height / 10,
                new Vector2(0, -tmp.height / 2), 0);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(hero.getFrame(), tmp.x, tmp.y, tmp.width * ProjectPhysic.PPM, tmp.height * ProjectPhysic.PPM);
        Array<Body> bodys = projectPhysic.getBodys("fireball");
        winCount = bodys.size;
        fireBall.setTime(delta);
        TextureRegion tr = fireBall.draw();
        float dScale = 2;
        for (Body bd : bodys) {
            float cx = bd.getPosition().x * ProjectPhysic.PPM - tr.getRegionWidth() / 2 / dScale;
            float cy = bd.getPosition().y * ProjectPhysic.PPM - tr.getRegionHeight() / 2 / dScale;
            float cW = tr.getRegionWidth() / ProjectPhysic.PPM / dScale;
            float cH = tr.getRegionHeight() / ProjectPhysic.PPM / dScale;
            ((PolygonShape) bd.getFixtureList().get(0).getShape()).setAsBox(cW / 2, cH / 2);
            batch.draw(tr, cx, cy, cW * ProjectPhysic.PPM, cH * ProjectPhysic.PPM);
        }
        batch.end();

        mapRenderer.render(front);
        batch.begin();
        font.draw(batch, "HP:" + (int) hero.getHit(0) +" Жизней: " +lives+ "\nОсталось: " + winCount, (int) camera.position.x - Gdx.graphics.getWidth()/6, (int)  (camera.position.y + Gdx.graphics.getHeight()/7));
        batch.end();
        for (Body bd : bodyToDelete) {
            projectPhysic.destroyBody(bd);
        }
        bodyToDelete.clear();

        projectPhysic.step();
        projectPhysic.debugDraw(camera);


        if (MyContactListener.gameOver) {
            if (lives > 1) {
                lives--;
                MyContactListener.gameOver = false;
                dispose();
                game.setScreen(new GameLelTwo(game,100,lives));
            } else {
                game.setScreen(new GameOverScreen(game));
                dispose();
            }
        }
        if (MyContactListener.isDamage) {
            if (hero.getHit(1) < 1) {
                game.setScreen(new GameOverScreen(game));
                dispose();
            }
        }
        if (winCount == 0 && MyContactListener.finishLvl) {
            MyContactListener.finishLvl = false;
            game.setScreen(new VictoryScreen(game));
            dispose();
        }
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
        this.hero.dispose();
        game.dispose();
        fireBall.dispose();
        batch.dispose();
        this.projectPhysic.dispose();
        this.map.dispose();
        mapRenderer.dispose();
        this.font.dispose();
        music.dispose();
        game.dispose();
    }
}
