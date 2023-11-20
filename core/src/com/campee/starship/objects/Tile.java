package com.campee.starship;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tile {
    private final Sprite sprite;

    public Tile(TextureRegion textureRegion, float xPosition, float yPosition) {
        sprite = new Sprite(textureRegion);

        sprite.setPosition(xPosition, yPosition);
    }

    public void draw(Batch batch) {
        sprite.draw(batch);
    }
}
