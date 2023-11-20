package com.campee.starship.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {
    protected Sprite sprite;
    protected Rectangle bounds;
    private Vector2 position;
    private boolean visible;

    public GameObject(String spritePath, float x, float y) {
        this(x, y);

        setSprite(spritePath);
        sprite.setPosition(x, y);
        bounds = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
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
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
        position.set(x, y);

        if (bounds != null) {
            bounds.setPosition(x, y);
        }
    }

    public void setPosition(Vector2 position) {
        this.setPosition(position.x, position.y);
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
