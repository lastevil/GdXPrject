package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

import java.util.Arrays;

public class GameLelOne implements Screen {
    Game game;
    private SpriteBatch batch;
    private Music music;
    private MyInputProcessor myInputProcessor;
    private OrthographicCamera camera;
    private ProjectPhysic projectPhysic;
    private Body body;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private int[] front, tL;
    private final MainHero hero;
    public static Array<Body> bodyToDelete;
    private final Label font;
    private int balls;

    private int winCount;
    private MyAtlasAnimation fireBall;

    public GameLelOne(Game game) {
        balls = 0;
        winCount = 0;
        font = new Label(12);

        bodyToDelete = new Array<>();

        fireBall = new MyAtlasAnimation("atlases/fire.atlas", "fire", 4, null, true);

        this.game = game;

        map = new TmxMapLoader().load("map/map_lvl1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        tL = new int[5];
        front = new int[2];

        front[0] = map.getLayers().getIndex("water_front");
        front[1] = map.getLayers().getIndex("grass");
        tL[0] = map.getLayers().getIndex("sky");
        tL[1] = map.getLayers().getIndex("fon");
        tL[2] = map.getLayers().getIndex("water_back");
        ;
        tL[3] = map.getLayers().getIndex("land");
        tL[4] = map.getLayers().getIndex("cave");

        //  TiledMapTileMapObject mo = (TiledMapTileMapObject) map.getLayers().get("damage").getObjects().get("monster1");

        projectPhysic = new ProjectPhysic();
        Array<RectangleMapObject> objects = new Array<>();
        objects.addAll(map.getLayers().get("lands").getObjects().getByType(RectangleMapObject.class));
        objects.addAll(map.getLayers().get("water").getObjects().getByType(RectangleMapObject.class));
        objects.addAll(map.getLayers().get("fireballs").getObjects().getByType(RectangleMapObject.class));
        objects.addAll(map.getLayers().get("caveIn").getObjects().getByType(RectangleMapObject.class));

        for (int i = 0; i < objects.size; i++) {
            projectPhysic.addObject(objects.get(i));
            if(objects.get(i).getName().equals("fireball")){
                winCount++;
            }
        }

        objects.clear();
        //  objects.addAll(map.getLayers().get("damage").getObjects().getByType(RectangleMapObject.class));
        //  for (int i = 0; i < objects.size; i++) {
        //     projectPhysic.addDmgObject(objects.get(i));
        // }

        body = projectPhysic.addObject((RectangleMapObject) map.getLayers().get("hero").getObjects().get("Hero"));
        body.setFixedRotation(true);
        hero = new MainHero(body);

        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);

        music = Gdx.audio.newMusic(Gdx.files.internal("music/background.wav"));
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
        ScreenUtils.clear(Color.BLACK);

        camera.position.x = body.getPosition().x * projectPhysic.PPM;
        camera.position.y = body.getPosition().y * projectPhysic.PPM;
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
        }
        else {
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
        font.draw(batch, "HP:" + hero.getHit(0) + "\nОсталось: " + (winCount-balls), (int) tmp.x, (int) (tmp.y + tmp.height*2 * ProjectPhysic.PPM));
        batch.draw(hero.getFrame(), tmp.x, tmp.y, tmp.width * ProjectPhysic.PPM, tmp.height * ProjectPhysic.PPM);

        Array<Body> bodys = projectPhysic.getBodys("fireball");
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

        for (Body bd : bodyToDelete) {
            balls++;
            projectPhysic.destroyBody(bd);
        }
        bodyToDelete.clear();

        projectPhysic.step();
        projectPhysic.debugDraw(camera);

        if(MyContactListener.gameOver){
            dispose();
            MyContactListener.gameOver = false;
            game.setScreen(new GameOverScreen(game));
        }

        if((winCount-balls)==0){
            dispose();
            game.setScreen(new VictoryScreen(game));
        }

/*        if (MyContactListener.isDamage) {
            if (hero.getHit(1) < 1) {
                dispose();
                game.setScreen(new GameOverScreen(game));
            }
        }*/
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
        music.dispose();
        map.dispose();
        mapRenderer.dispose();
        this.hero.dispose();
        this.font.dispose();
        this.projectPhysic.dispose();
        this.fireBall.dispose();
    }
}
