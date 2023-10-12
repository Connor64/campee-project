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
    float screenWidth;
    float screenHeight;
    int SPEED = 150;
    int move = 0;

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

        //implement KeyProcessor
        if (keyProcessor.upPressed) {
            y += SPEED * deltaTime;
            move = 0;
        } else if (keyProcessor.downPressed) {
            y -= SPEED * deltaTime;
            move = 1;
        } else if (keyProcessor.leftPressed) {
            x -= SPEED * deltaTime;
            move = 2;
        } else if (keyProcessor.rightPressed) {
            x += SPEED * deltaTime;
            move = 3;
        }

        batch.begin();
        player.render(batch, x, y, move);

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
        System.out.println("player: \n" + player.getBounds());
        System.out.println("coin: \n" + coin.getBounds());
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