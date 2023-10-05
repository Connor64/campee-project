package com.campee.starship;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Coin extends GameObject {
    public boolean collected;
    public TextureRegion coin;

    public Coin () {
        // constructor for new coin object
        dimension.set(0.5f, 0.5f);
        // coin = Assets.instance.coin.coin;
        // set bounds for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
    }

    public void render() {
        // if it's collected, don't render anymore
        if (collected) {
            return;
        }
        // else, render position of coin
        // set texture
        // batch.draw
        // set position
    }
}
