package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class BuildingObject extends GameObject {

    private boolean pickupLocation;
    private boolean dropoffLocation;
    private boolean transparent;

    public BuildingObject(World world, float x, float y) {
        super(world, x, y);
        pickupLocation = false;
        dropoffLocation = false;
        transparent = false;
    }

    public void setPickupLocation (boolean status) {
        this.pickupLocation = status;
    }

    public void setDropoffLocation (boolean status) {
        this.dropoffLocation = status;
    }

    public void setTransparent (boolean status) {
        this.transparent = status;
    }

    @Override
    public void setSprite(String spritePath) {
        Texture texture = new Texture(Gdx.files.internal(spritePath));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion region = new TextureRegion(texture, 0, 0, 128,128);
        sprite = new Sprite(region);
        sprite.setOrigin(sprite.getX() / 2,sprite.getY() / 2);
        sprite.setPosition(0, 0);
        this.setHeight(sprite.getHeight());
        this.setWidth(sprite.getWidth());
    }

    @Override
    public void render(SpriteBatch batch, int x, int y) {
        if (transparent) {
            sprite.setAlpha(0.2f);
        } else {
            sprite.setAlpha(1f);
        }
        super.render(batch, x, y);
    }
}
