package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends GameObject {
    private TextureRegion region;
    private Sprite sprite;
    private Texture texture;
    private final Texture upTexture;
    private final Texture downTexture;
    private final Texture leftTexture;
    private final Texture rightTexture;
    private Texture[] textures;
    private Vector2 velocity = new Vector2();
    private float accelerationSpeed = 20.0f;
    private float decelerationSpeed = 10.0f;
    private float frictionCoefficient = 5.0f;

    String spritePath = "";

    public Player () {
        // constructor for new player object
//        dimension.set(0.5f, 0.5f);

        // set bounds for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);

        upTexture = new Texture(Gdx.files.internal("moonship_up.PNG"));
        downTexture = new Texture(Gdx.files.internal("moonship_down.PNG"));
        leftTexture = new Texture(Gdx.files.internal("moonship_left.PNG"));
        rightTexture = new Texture(Gdx.files.internal("moonship_right.PNG"));

        textures = new Texture[4];
        textures[0] = upTexture;
        textures[1] = downTexture;
        textures[2] = leftTexture;
        textures[3] = rightTexture;
    }

    public void setSprite(int spriteNum) {
        // set the correct directional sprite
        texture = textures[spriteNum];
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion region = new TextureRegion(texture, 0, 0, 32, 32);
        sprite = new Sprite(region);
//        sprite.setOrigin(16,16);
        sprite.setSize(125, 125);
        sprite.setOrigin(sprite.getX() / 2,sprite.getY() / 2);
        sprite.setPosition(0, 0);
    }

    public void render(SpriteBatch batch, float x, float y, int spriteNum, boolean accelerating) {
        // re-render sprite using movement indicator (spriteNum)
        setSprite(spriteNum);

        if (accelerating) {
            // accelerate the player
            velocity.add(accelerationSpeed * Gdx.graphics.getDeltaTime(), 0);
        } else {
            float frictionForce = frictionCoefficient * velocity.x;
            if (Math.abs(velocity.x) > 0.1f) {
                velocity.x -= frictionForce * Gdx.graphics.getDeltaTime();
            } else {
                velocity.x = 0;  // prevent very slow drift
            }
        }


        x += velocity.x * Gdx.graphics.getDeltaTime();


        // render position of player
        sprite.setPosition(x, y);
        bounds.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        sprite.draw(batch);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getX() {
        float x = sprite.getX();
        return x;
    }

    public float getY() {
        float y = sprite.getY();
        return y;
    }

    public float getWidth() {
        float w = sprite.getWidth();
        return w;
    }

    public float getHeight() {
        float h = sprite.getHeight();
        return h;
    }

}