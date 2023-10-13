package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends GameObject {
    public Body body;
    private TextureRegion region;
    public Sprite sprite;
    private int xMove;
    private int yMove;
    private int move;
    private Texture texture;
    private final Texture upTexture;
    private final Texture downTexture;
    private final Texture leftTexture;
    private final Texture rightTexture;
    private Texture[] textures;

    String spritePath = "";
    private float speedBoostX;
    private float speedBoostY;

    private static final float ACCELERATION = 1000f;

    public Player (World world, float x, float y) {
        // constructor for new player object
        super(world, x, y);
        // set bounds for collision detection
        dimension.set(0.5f, 0.5f);
        setBounds(0, 0, dimension.x, dimension.y);

        speedBoostX = 0;
        speedBoostY = 0;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(dimension.x / 2, dimension.y / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);

        body.setUserData(this);
        body.setGravityScale(0);

        shape.dispose();

        upTexture = new Texture(Gdx.files.internal("moonship_up.PNG"));
        downTexture = new Texture(Gdx.files.internal("moonship_down.PNG"));
        leftTexture = new Texture(Gdx.files.internal("moonship_left.PNG"));
        rightTexture = new Texture(Gdx.files.internal("moonship_right.PNG"));



        textures = new Texture[4];
        textures[0] = upTexture;
        textures[1] = downTexture;
        textures[2] = leftTexture;
        textures[3] = rightTexture;

        texture = textures[2];
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion region = new TextureRegion(texture, 0, 0, 32, 32);
        sprite = new Sprite(region);
    }

    public void setLinearDamping(float damping) {
        body.setLinearDamping(damping);
    }

    public void setSprite(int spriteNum) {
        // set the correct directional sprite
        texture = textures[spriteNum];
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion region = new TextureRegion(texture, 0, 0, 32, 32);
        sprite = new Sprite(region);
        sprite.setSize(125, 125);
        sprite.setOrigin(sprite.getX() / 2,sprite.getY() / 2);
        sprite.setPosition(0, 0);
        this.setHeight(sprite.getHeight());
        this.setWidth(sprite.getWidth());
    }


    public void render(SpriteBatch batch, float moveX, float moveY, int spriteNum) {
//        if (moveX != 0) {
//            speedBoostX = speedBoostX + 50;
//        } else {
//            speedBoostX = 0;
//        }
//        if (moveY != 0) {
//            speedBoostY = speedBoostY + 50;
//        } else {
//            speedBoostY = 0;
//        }
//        speedBoostX = moveX * (speedBoostX + 50);
//        speedBoostY = moveY * (speedBoostY + 50);

        //       System.out.println("speedX: " + speedBoostX);
//        System.out.println("speedY: " + speedBoostY);
//        body.applyForceToCenter((1000 * moveX) +(50 * speedBoostX), (1000 * moveY) + (50 * speedBoostY),  true);
//        body.setLinearVelocity(speedBoostX, speedBoostY);
//        float maxSpeed = ACCELERATION;
//        float desiredVelocityX = moveX * maxSpeed;
//        float desiredVelocityY = moveY * maxSpeed;
//
//        Vector2 currentVelocity = body.getLinearVelocity();
//        float velocityChangeX = desiredVelocityX - currentVelocity.x;
//        float velocityChangeY = desiredVelocityY - currentVelocity.y;
//
//        float impulseX = body.getMass() * velocityChangeX;
//        float impulseY = body.getMass() * velocityChangeY;
//
//        Vector2 impulse = new Vector2(impulseX, impulseY);
//        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);

        // re-render sprite using movement indicator (spriteNum)
        setSprite(spriteNum);

        // render position of player
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        setBounds(sprite.getX(), sprite.getY(), getWidth(), getHeight());
        sprite.draw(batch);
    }
}