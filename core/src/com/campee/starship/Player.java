package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends GameObject {
    private TextureRegion region;
    private Sprite sprite;
    private Texture texture;
    private final Texture upTexture;
    private final Texture downTexture;
    private final Texture leftTexture;
    private final Texture rightTexture;
    private Texture[] textures;

    String spritePath = "";

    public Player (World world, float x, float y) {
        // constructor for new player object
        super(world, x, y);
        // set bounds for collision detection
        dimension.set(0.5f, 0.5f);
        setBounds(0, 0, dimension.x, dimension.y);

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
        sprite.setSize(125, 125);
        sprite.setOrigin(sprite.getX() / 2,sprite.getY() / 2);
        sprite.setPosition(0, 0);
        this.setHeight(sprite.getHeight());
        this.setWidth(sprite.getWidth());
    }

    public void render(SpriteBatch batch, float x, float y, int spriteNum) {
        // re-render sprite using movement indicator (spriteNum)
        setSprite(spriteNum);
        // render position of player
        sprite.setPosition(x, y);
        sprite.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        setBounds(x, y, getWidth(), getHeight());
        sprite.draw(batch);
    }
}