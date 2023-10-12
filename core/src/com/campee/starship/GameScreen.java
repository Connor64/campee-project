package com.campee.starship;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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
    public int coinCounter = 0;

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
        world.step(1 / 60f, 6, 2);

        float xMove = 0;
        float yMove = 0;

        // implement KeyProcessor
        if (keyProcessor.upPressed) {
            yMove = 1;
            move = 0;
        } else if (keyProcessor.downPressed) {
            yMove = -1;
            move = 1;
        } else if (keyProcessor.leftPressed) {
            xMove = -1;
            move = 2;
        } else if (keyProcessor.rightPressed) {
            xMove = 1;
            move = 3;
        } else {
            float linearDamping = 1;
            player.body.setLinearDamping(linearDamping);
        }

        // player still goes out of bounds, this  code no work now :(
        //ensure sprite stays within screen bounds
//        if (x < 0) {
//            x = 0;
//        } else if (x > screenWidth - player.getWidth()) {
//            x = screenWidth - player.getWidth();
//        }
//
//        if (y < 0) {
//            y = 0;
//        } else if (y > screenHeight - player.getHeight()) {
//            y = screenHeight - player.getHeight();
//        }


        // boundaries for player and screen
        Rectangle playerBounds = player.getBounds();
        Rectangle screenBounds = new Rectangle(0, 0, screenWidth, screenHeight);
        float playerLeft = playerBounds.getX();
        float playerBottom = playerBounds.getY();
        float playerTop = playerBottom + playerBounds.getHeight();
        float playerRight = playerLeft + playerBounds.getWidth();

        final float halfWidth = playerBounds.getWidth() * .5f;
        final float halfHeight = playerBounds.getHeight() * .5f;

        float screenLeft = screenBounds.getX();
        float screenBottom = screenBounds.getY();
        float screenTop = screenBottom + screenBounds.getHeight();
        float screenRight = screenLeft + screenBounds.getWidth();

        // float for new position (for screen collisions)
        float newX = player.sprite.getX();
        float newY = player.sprite.getY();



        // testing to make sure the player can't leave bounds
        // TODO: fix bouncing :/
        // horizontal axis
        if (playerLeft < screenLeft) {
            // clamp to left
            newX = screenLeft + halfWidth;
            player.body.setLinearVelocity(newX, player.body.getLinearVelocity().y);
            xMove = 1;
        } else if (playerRight > screenRight) {
            // clamp to right
            newX = screenRight - halfWidth;
            player.body.setLinearVelocity(-newX, player.body.getLinearVelocity().y);
            xMove = 1;
        }
        // vertical axis
        if (playerBottom < screenBottom) {
            // clamp to bottom
            newY = screenBottom + halfHeight;
            player.body.setLinearVelocity(player.body.getLinearVelocity().x, newY);
            yMove = 1;
        } else if(playerTop > screenTop) {
            // clamp to top
            newY = screenTop - halfHeight;
            player.body.setLinearVelocity(player.body.getLinearVelocity().x, -newY);
            yMove = 1;
        }

        batch.begin();
        player.render(batch, xMove, yMove, move);

        if (!coin.collected) {
            coin.render(batch, 50, 25);
            if (Intersector.overlaps(player.getBounds(), coin.getBounds())) {
                coin.setCollected(true);
                coinCounter++;
            }
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