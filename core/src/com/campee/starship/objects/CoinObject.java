package com.campee.starship;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class CoinObject extends GameObject {
    private boolean collected;

    public CoinObject(float x, float y) {
        super("coin.PNG", x, y);

        collected = false;
    }

    public CoinObject(CoinObject coin) {
        super(coin);

        collected = false;
    }

    public void setCollected(boolean collected) {
        if (collected) {
            this.collected = true;
        }
    }

    public boolean isCollected() {
        return collected;
    }

    public Sprite getSprite() {
        return this.sprite;
    }
}