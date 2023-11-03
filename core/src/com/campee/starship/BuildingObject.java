package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BuildingObject extends GameObject {

    private boolean pickupLocation;
    private boolean dropoffLocation;
    private boolean translucent;
    private Sprite pickSprite;
    private Sprite dropSprite;
    private String name;
    private Rectangle transparencyBounds;
    private float collisionDepth;
    private float transparencyDepth;

    public BuildingObject(String spritePath, float x, float y, float collisionDepth, float transparencyDepth) {
        super(spritePath, x, y);
        pickupLocation = false;
        dropoffLocation = false;
        translucent = false;

        this.collisionDepth = collisionDepth;
        this.transparencyDepth = transparencyDepth;

        // Set transparency and collision bounds
        transparencyBounds = new Rectangle(getBounds());
        transparencyBounds.height -= transparencyDepth;
        bounds.height -= collisionDepth;

        Texture pickupTexture = new Texture(Gdx.files.internal("pickup flag.PNG"));
        pickupTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion pickUpRegion = new TextureRegion(pickupTexture, 0, 0, pickupTexture.getWidth(), pickupTexture.getHeight());
        pickSprite = new Sprite(pickUpRegion);
        pickSprite.setPosition(getBounds().getX() + (getBounds().getWidth() / 2), getBounds().getY() + ((getBounds().getHeight() * 2)));

        Texture dropoffTexture = new Texture(Gdx.files.internal("dropoff flag.PNG"));
        dropoffTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion dropOffRegion = new TextureRegion(dropoffTexture, 0, 0, dropoffTexture.getWidth(), dropoffTexture.getHeight());
        dropSprite = new Sprite(dropOffRegion);
        dropSprite.setPosition(getBounds().getX() + (getBounds().getWidth() / 2), getBounds().getY()  + ((getBounds().getHeight() * 2)));
    }

    public BuildingObject(BuildingObject buildingObject) {
        super(buildingObject);

        pickupLocation = false;
        dropoffLocation = false;
        translucent = false;
        pickSprite = new Sprite(buildingObject.pickSprite);
        dropSprite = new Sprite(buildingObject.dropSprite);
        name = buildingObject.name;
        transparencyBounds = new Rectangle(buildingObject.transparencyBounds);
    }

    @Override
    public void setSprite (String spritePath) {
        super.setSprite(spritePath);
    }

    public void setPickupLocation (boolean status) {
        pickupLocation = status;
    }

    public void setDropoffLocation (boolean status) {
        dropoffLocation = status;
    }

    public void setTranslucent(boolean status) {
        translucent = status;
        float opacity = translucent ? 0.2f : 1f;

        setOpacity(opacity);
        pickSprite.setAlpha(opacity);
        dropSprite.setAlpha(opacity);
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public Rectangle getTransparencyBounds() {
        return transparencyBounds;
    }

    @Override
    public void setPosition(Vector2 position) {
        this.setPosition(position.x, position.y);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        transparencyBounds.setPosition(x, y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);

        if (pickupLocation) {
            pickSprite.draw(batch);
        } else if (dropoffLocation) {
            pickSprite.draw(batch);
        }
    }
}
