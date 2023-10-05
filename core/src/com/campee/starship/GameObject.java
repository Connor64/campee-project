package com.campee.starship;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

public class GameObject {
    public Vector2 position;
    public Vector2 dimension;
    public Vector2 scale;
    public Vector2 velocity;
    public Vector2 friction;
    public Vector2 acceleration;
    public Rectangle bounds;
    public boolean visible;
    public CustomSprite sprite;
    public GameObject() {
        // construct a new game object
        position = new Vector2();
        dimension = new Vector2(1, 1);
        scale = new Vector2(1, 1);
        velocity = new Vector2();
        friction = new Vector2();
        acceleration = new Vector2();
        bounds = new Rectangle();
        visible = true;
    }
}