package com.mygdx.game.actions;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class ProjectPhysic {
    private final World world;
    private final Box2DDebugRenderer renderer;

    public World getWorld() {
        return world;
    }

    public ProjectPhysic() {
        world = new World(new Vector2(0,-9.81f),true);
        renderer = new Box2DDebugRenderer();
    }

    public void debugDraw(OrthographicCamera camera){
        renderer.render(this.world,camera.combined);
    }
    public void step(){
        this.world.step(1/60f,3,3);
    }
    public void dispose(){
        this.world.dispose();
        this.renderer.dispose();
    }
}
