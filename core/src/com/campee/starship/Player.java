package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends GameObject {
    private Body body;
    private final TextureRegion[] directionalSprites;
    private final float MOVEMENT_SPEED = 10;

    public Player(World world, float x, float y) {
        super(x, y);

        // Set up directional sprites
        Texture upTexture = new Texture(Gdx.files.internal("moonship_up.PNG"));
        upTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        Texture downTexture = new Texture(Gdx.files.internal("moonship_down.PNG"));
        downTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        Texture leftTexture = new Texture(Gdx.files.internal("moonship_left.PNG"));
        leftTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        Texture rightTexture = new Texture(Gdx.files.internal("moonship_right.PNG"));
        rightTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Get regions and put them into the array
        directionalSprites = new TextureRegion[]{
                new TextureRegion(upTexture, 0, 0, 32, 32),
                new TextureRegion(downTexture, 0, 0, 32, 32),
                new TextureRegion(leftTexture, 0, 0, 32, 32),
                new TextureRegion(rightTexture, 0, 0, 32, 32),
        };

        sprite = new Sprite(directionalSprites[0]); // Set default sprite

        bounds = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        // Create fixture from shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth(), sprite.getHeight());
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();

        // Set body attributes
        body.setUserData(this);
        body.setGravityScale(0);
        body.setLinearDamping(5);

        setPosition(x, y);
    }

    public void update(float delta, KeyProcessor keyProcessor) {
        super.update(delta);

        // Move player
        Vector2 direction = new Vector2(0, 0);
        if (keyProcessor.upPressed) {
            direction.y = 1;
            setSprite(0);
        } else if (keyProcessor.downPressed) {
            direction.y = -1;
            setSprite(1);
        } else if (keyProcessor.leftPressed) {
            direction.x = -1;
            setSprite(2);
        } else if (keyProcessor.rightPressed) {
            direction.x = 1;
            setSprite(3);
        }

        body.applyLinearImpulse(direction.scl(MOVEMENT_SPEED), body.getPosition(), true);
        super.setPosition(body.getPosition());
    }

    public void checkBounds(int width, int height) {
        if ((body.getPosition().x + sprite.getWidth() > width) || (body.getPosition().x < 0)) {
            body.setLinearVelocity(body.getLinearVelocity().x * -1, body.getLinearVelocity().y);
        }

        if ((body.getPosition().y + sprite.getHeight() > height) || (body.getPosition().y < 0)) {
            body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y * -1);
        }
    }

    public boolean checkCollision(GameObject object, boolean rigid) {
        if (object instanceof BuildingObject) {
            BuildingObject building = ((BuildingObject) object);
            building.setTranslucent(Intersector.overlaps(getBounds(), building.getTransparencyBounds()));
        }

        // Check if within the collision bounds
        if (Intersector.overlaps(getBounds(), object.getBounds())) {
            System.out.println(object.getBounds());

            if (rigid) {
                if (Math.abs(body.getLinearVelocity().x) > 0) {
                    body.setLinearVelocity(body.getLinearVelocity().x * -1, body.getLinearVelocity().y);
                }
                if (Math.abs(body.getLinearVelocity().y) > 0) {
                    body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y * -1);
                }
            }

            return true;
        }

        return false;
    }

    private void setSprite(int spriteNum) {
        sprite.setRegion(directionalSprites[spriteNum]);
    }

    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);
        body.setTransform(position, 0);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        body.setTransform(x, y, 0);
    }

    public Sprite getSprite() {
        return this.sprite;
    }
}