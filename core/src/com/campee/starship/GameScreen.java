package com.campee.starship;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends ApplicationAdapter implements Screen {
    SpriteBatch batch;
    Texture img;
    private Game game;
    private Stage stage;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    Player player;
    KeyProcessor keyProcessor;
    Coin coin;
    float x;
    float y;
    int move = 0;
    float screenWidth;
    float screenHeight;

    public GameScreen(final Game game) {
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
        x = 100;
        y = 100;

        stage = new Stage();
        this.game = game;
        batch = new SpriteBatch();
        keyProcessor = new KeyProcessor();
        Gdx.input.setInputProcessor(keyProcessor);
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        player = new Player(world, x, y);
        coin = new Coin(world, x, y);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.PINK);
        float deltaTime =  Gdx.graphics.getDeltaTime();
        world.step(1 / 60f, 6, 2);

        int xMove = 0;
        int yMove = 0;

        //implement KeyProcessor
        if (keyProcessor.upPressed) {
//            y += SPEED * deltaTime;
            yMove = 1;
            move = 0;
        } else if (keyProcessor.downPressed) {
//            y -= SPEED * deltaTime;
            yMove = -1;
            move = 1;
        } else if (keyProcessor.leftPressed) {
//            x -= SPEED * deltaTime;
            xMove = -1;
            move = 2;
        } else if (keyProcessor.rightPressed) {
//            x += SPEED * deltaTime;
            xMove = 1;
            move = 3;
        } else {
            float linearDamping = 2;
            player.body.setLinearDamping(linearDamping);
//            player.body.setLinearVelocity(0, 0);
        }

        batch.begin();
        player.render(batch, xMove, yMove, move);

        //ensure sprite stays within screen bounds
        if (x < 0) {
            x = 0;
        } else if (x > screenWidth - player.getWidth()) {
            x = screenWidth - player.getWidth();
        }

        if (y < 0) {
            y = 0;
        } else if (y > screenHeight - player.getHeight()) {
            y = screenHeight - player.getHeight();
        }

        if (!coin.collected) {
            coin.render(batch, 5, 5);
        }

        if (Intersector.overlaps(player.getBounds(), coin.getBounds())) {
            coin.setCollected(true);
        }
        batch.end();
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }
}