package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

//import javax.xml.soap.Text;
import java.security.Key;

public class Player extends GameObject {
    public Body body;
    private TextureRegion region;
    private Sprite sprite;
    private Texture texture;
    private TextureRegion[] textureRegions;

    String spritePath = "";

    private static final float ACCELERATION = 1000f;

    public Player(World world, float x, float y) {
        super(world, x, y);

        // Set bounds for collision detection
        dimension.set(0.5f, 0.5f);
        setBounds(0, 0, dimension.x, dimension.y);

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
        textureRegions = new TextureRegion[]{
                new TextureRegion(upTexture, 0, 0, 32, 32),
                new TextureRegion(downTexture, 0, 0, 32, 32),
                new TextureRegion(leftTexture, 0, 0, 32, 32),
                new TextureRegion(rightTexture, 0, 0, 32, 32),
        };

        sprite = new Sprite(textureRegions[0]); // Set default sprite
        sprite.setSize(125, 125);
        sprite.setOrigin(sprite.getX() / 2, sprite.getY() / 2);
        sprite.setPosition(0, 0);

        setLinearDamping(5);
    }

    public void setLinearDamping(float damping) {
        body.setLinearDamping(damping);
    }

    public void setSprite(int spriteNum) {
        sprite.setRegion(textureRegions[spriteNum]);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        sprite.setBounds(body.getPosition().x, body.getPosition().y, sprite.getWidth(), sprite.getHeight());
    }

    public void update(float delta, KeyProcessor keyProcessor) {

        // Move player
        Vector2 force = new Vector2(0, 0);
        if (keyProcessor.upPressed) {
//            force.y = 4f;
            force.y = ACCELERATION;
            setSprite(0);
        } else if (keyProcessor.downPressed) {
//            force.y = -4f;
            force.y = -ACCELERATION;
            setSprite(1);
        } else if (keyProcessor.leftPressed) {
//            force.x = -4f;
            force.x = -ACCELERATION;
            setSprite(2);
        } else if (keyProcessor.rightPressed) {
//            force.x = 4f;
            force.x = ACCELERATION;
            setSprite(3);
        }

//        body.applyLinearImpulse(force.x, force.y, position.x, position.y, true);
        body.applyForceToCenter(force, true);
        setPosition(body.getPosition());
    }

    private void setPosition(Vector2 position) {
        this.position = position;
        sprite.setPosition(position.x, position.y);
    }

    public Sprite getSprite() {
        return this.sprite;
    }
}