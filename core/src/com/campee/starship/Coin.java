package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Coin extends GameObject {
    public boolean collected;
    private TextureRegion region;
    private Sprite sprite;
    private Texture texture;

    public Coin(World world, float x, float y) {
        super(world, x, y);

        // constructor for new coin object
        dimension.set(0.5f, 0.5f);

        // set bounds for collision detection
        setBounds(0, 0, dimension.x, dimension.y);

        texture = new Texture(Gdx.files.internal("coin.PNG"));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion region = new TextureRegion(texture, 0, 0, 16, 16);
        sprite = new Sprite(region);
//        sprite.setSize(50, 50);
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

    public void setCollected(boolean collected) {
        if (collected) {
            this.collected = true;
        }
    }
    public Sprite getSprite() {
        return this.sprite;
    }
}