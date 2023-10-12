package com.campee.starship;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.concurrent.TimeUnit;


public class GameScreen extends ApplicationAdapter implements Screen {
    int test;
    SpriteBatch batch;
    Texture img;
    private Game game;
    private Stage stage;
    private Skin skin;
    private ShapeRenderer shapeRenderer;
    private Popup popup;
    private boolean screenClicked = false; // Add this variable
    private boolean prevClickState = false; // Add this variable to track the previous click state


    Player player;
    KeyProcessor keyProcessor;
    OrderScreen orderScreen;
    Coin coin;
    float x;
    float y;
    float screenWidth;
    float screenHeight;
    int SPEED = 150;
    int move = 0;

    public GameScreen(final Game game) {
        test = 0;
        popup = new Popup();
        stage = new Stage();
        this.game = game;
        batch = new SpriteBatch();
        keyProcessor = new KeyProcessor();
        Gdx.input.setInputProcessor(keyProcessor);
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        x = 100;
        y = 100;
        player = new Player();
        orderScreen = new OrderScreen();
        coin = new Coin();
        orderScreen.visible = false;
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        orderScreen.visible = false;
        ScreenUtils.clear(Color.PINK);
        float deltaTime =  Gdx.graphics.getDeltaTime();

        // Handle player movement
        if (!popup.isVisible()) {
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
            coin.render(batch, 0, 0);
        }

        if (Intersector.overlaps(player.getBounds(), coin.getBounds())) {
            coin.setCollected(true);
        }
        // Gdx.input.setInputProcessor(keyProcessor);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            test++;
            System.out.println(test);
            if (test == 2) {
                popup.show();
                Gdx.input.setInputProcessor(popup.getStage());
            }
        }

        if (popup.isVisible()) {
            popup.render();
        } else {
            Gdx.input.setInputProcessor(keyProcessor); // Enable arrow key input
        }

        batch.end();

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void hide() {
        orderScreen.setVisible(false);
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }
}