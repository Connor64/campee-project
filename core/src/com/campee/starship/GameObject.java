package com.campee.starship;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class GameObject {
    public Vector2 position;
    public Vector2 dimension;
    public Vector2 scale;
    public Vector2 velocity;
    public Vector2 friction;
    public Vector2 acceleration;
    public Rectangle bounds;
    public boolean visible;
    public Sprite sprite;
    private World world;
    protected Body body;
    public float height;
    public float width;

    public GameObject(World world, float x, float y) {
        // construct a new game object
        this.world = world;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        position = new Vector2();
        dimension = new Vector2(1, 1);
        scale = new Vector2(1, 1);
        velocity = new Vector2();
        friction = new Vector2();
        acceleration = new Vector2();
        bounds = new Rectangle();
        visible = true;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(float x, float y, float width, float height) {
        bounds.set(x, y, width, height);
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public float setHeight(float h) {
        height = h;
        return height;
    }

    public float setWidth(float w) {
        width = w;
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public void setSprite(String spritePath) {
        Texture texture = new Texture(Gdx.files.internal(spritePath));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
        sprite = new Sprite(region);
        sprite.setOrigin(sprite.getX() / 2,sprite.getY() / 2);
        sprite.setPosition(0, 0);
        this.setHeight(sprite.getHeight());
        this.setWidth(sprite.getWidth());
    }

    public void render(SpriteBatch batch, int x, int y) {
        // render sprite in x, y position
        sprite.setPosition(x, y);
        setBounds(x, y, getWidth(), getHeight());
        sprite.draw(batch);
    }

}
