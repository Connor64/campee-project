package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Coin extends GameObject {
    public boolean collected;
    private TextureRegion region;
    private Sprite sprite;
    private Texture texture;

    public Coin() {
        // constructor for new coin object
        dimension.set(0.5f, 0.5f);

        // set bounds for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);

        texture = new Texture(Gdx.files.internal("coin.PNG"));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion region = new TextureRegion(texture, 0, 0, 16, 16);
        sprite = new Sprite(region);
        sprite.setSize(50, 50);
        sprite.setOrigin(sprite.getX() / 2,sprite.getY() / 2);
        sprite.setPosition(0, 0);
    }

    public void render(SpriteBatch batch, int x, int y) {
        // render sprite in x,y position
        sprite.setPosition(x, y);
        bounds.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        sprite.draw(batch);
    }

    public void setCollected(boolean collected) {
        if (collected) {
            this.collected = true;
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }
}