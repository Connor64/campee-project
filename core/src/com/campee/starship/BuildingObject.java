package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

public class BuildingObject extends GameObject {

    private boolean pickupLocation;
    private boolean dropoffLocation;
    private boolean transparent;
    private Sprite pickSprite;
    private Sprite dropSprite;
    private Sprite pickSpriteYellow;
    private String name;

    public BuildingObject(World world, float x, float y) {
        super(world, x, y);
        pickupLocation = false;
        dropoffLocation = false;
        transparent = false;

        Texture pickupTexture = new Texture(Gdx.files.internal("pickup flag.PNG"));
        pickupTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion pickUpRegion = new TextureRegion(pickupTexture, 0, 0, pickupTexture.getWidth(), pickupTexture.getHeight());
        pickSprite = new Sprite(pickUpRegion);
        pickSprite.setOrigin(pickSprite.getX() / 2,pickSprite.getY() / 2);
        pickSprite.setPosition(0, 0);

        Texture pickupTextureYellow = new Texture(Gdx.files.internal("pickup flag yellow.PNG"));
        pickupTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion pickUpRegionYellow = new TextureRegion(pickupTextureYellow, 0, 0, pickupTextureYellow.getWidth(), pickupTextureYellow.getHeight());
        pickSpriteYellow = new Sprite(pickUpRegionYellow);
        pickSpriteYellow.setOrigin(pickSpriteYellow.getX() / 2,pickSpriteYellow.getY() / 2);
        pickSpriteYellow.setPosition(0, 0);

        Texture dropoffTexture = new Texture(Gdx.files.internal("dropoff flag.PNG"));
        dropoffTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion dropOffRegion = new TextureRegion(dropoffTexture, 0, 0, dropoffTexture.getWidth(), dropoffTexture.getHeight());
        dropSprite = new Sprite(dropOffRegion);
        dropSprite.setOrigin(dropSprite.getX() / 2,dropSprite.getY() / 2);
        dropSprite.setPosition(0, 0);

//        // Set bounds for collision detection
//        dimension.set(0.5f, 0.5f);
//        setBounds(0, 0, dimension.x, dimension.y);
//
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(x, y);
//        body = world.createBody(bodyDef);
//
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(dimension.x / 2, dimension.y / 2);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef);
//
//        body.setUserData(this);
//        body.setGravityScale(0);
//
//        shape.dispose();
    }

    @Override
    public void setSprite (String spritePath) {
        super.setSprite(spritePath);
        setHeight(sprite.getHeight() / 2);
        setBounds(body.getPosition().x, body.getPosition().y, sprite.getWidth(), sprite.getHeight());
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

    public String getName() {
        return this.name;
    }

    public void setName(String n) {
        name = n;
    }

    @Override
    public void render(SpriteBatch batch, int x, int y) {
        if (transparent) {
            sprite.setAlpha(0.2f);
        } else {
            sprite.setAlpha(1f);
        }
        //  TODO: stupit stuff for flag ac change later
        if (pickupLocation && !name.equals("HAAS")) {
            pickSprite.setPosition(getBounds().getX() + (getBounds().getWidth() / 2), getBounds().getY() + ((getBounds().getHeight() * 2)));
            pickSprite.draw(batch);
        } else if (pickupLocation && name.equals("HAAS")) {
            pickSpriteYellow.setPosition(getBounds().getX() + (getBounds().getWidth() / 2), getBounds().getY() + ((getBounds().getHeight() * 2)));
            pickSpriteYellow.draw(batch);
        }
        if (dropoffLocation) {
            dropSprite.setPosition(getBounds().getX() + (getBounds().getWidth() / 2), getBounds().getY()  + ((getBounds().getHeight() * 2)));
            dropSprite.draw(batch);
        }
        sprite.setBounds(body.getPosition().x, body.getPosition().y, sprite.getWidth(), sprite.getHeight());
        super.render(batch, x, y);
    }
}
