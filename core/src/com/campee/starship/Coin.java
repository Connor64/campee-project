package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Coin extends GameObject {
    public boolean collected;
    private TextureRegion region;
    private Sprite sprite;
    private Texture texture;

    public Coin () {
        // constructor for new coin object
        dimension.set(0.5f, 0.5f);

        texture = new Texture(Gdx.files.internal("moonship_down.png"));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion region = new TextureRegion(texture, 0, 0, 32, 32);
        sprite = new Sprite(region);
        sprite.setOrigin(16,16);
        sprite.setSize(500, 500);
        sprite.setPosition(0, 0);
        // coin = Assets.instance.coin.coin;
        // set bounds for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
    }

    public void render(SpriteBatch batch) {
        // if it's collected, don't render anymore
        if (collected) {
            return;
        }
        // else, render position of coin
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sprite.draw(batch);
    }
}
