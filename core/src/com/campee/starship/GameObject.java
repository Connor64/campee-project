package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {
    private boolean visible;
    protected Sprite sprite;
    private Vector2 position;
    private Rectangle bounds;

    public GameObject(String spritePath, float x, float y) {
        this(x, y);

        setSprite(spritePath);
        sprite.setPosition(x, y);
    }

    public GameObject(float x, float y) {
        position = new Vector2(x, y);
        visible = true;
    }

    public GameObject(GameObject object) {
        visible = object.visible;
        sprite = new Sprite(object.sprite);
        position = new Vector2(object.position);
        bounds = new Rectangle(object.bounds);
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
        position.set(x, y);
    }

    public void setPosition(Vector2 position) {
        sprite.setPosition(position.x, position.y);
        this.position.set(position);
    }

    public void setOpacity(float alpha) {
        sprite.setAlpha(alpha);
    }

    public void setSprite(String spritePath) {
        Texture texture = new Texture(Gdx.files.internal(spritePath));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
        sprite = new Sprite(region);
    }

    public void update(float delta) {}

    public void draw(SpriteBatch batch) {
        if ((sprite == null) || !visible) return;

        sprite.draw(batch);
    }
}
