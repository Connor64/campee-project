package com.campee.starship;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;


public class GameScreen extends ApplicationAdapter implements Screen {
    int test;
    SpriteBatch batch;

    Texture img;
    private Game game;
    private Stage stage;
    private Popup popup;
    TextButton backButton;
    TextButton nextOrderButton;
    Player player;
    PlayerAttributes attributes;
    KeyProcessor keyProcessor;
    OrderScreen orderScreen;
    Coin coin;
    Order order;
    float x;
    float y;
    float screenWidth;
    float screenHeight;
    int SPEED = 150;
    int move = 0;
    ArrayList<String> array;

    public GameScreen(final Game game) {
        test = 0;

        stage = new Stage();
        this.game = game;
        batch = new SpriteBatch();
        keyProcessor = new KeyProcessor();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        x = 100;
        y = 100;
        player = new Player();
        attributes = new PlayerAttributes();

        Pixmap backgroundPixmap = createRoundedRectanglePixmap(200, 50, 10, Color.PURPLE); // Adjust size and color
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(backgroundPixmap)));
        BitmapFont buttonFont = new BitmapFont();
        buttonFont.getData().setScale(1.5f);
        buttonStyle.font = buttonFont;
        buttonStyle.fontColor = Color.BLACK;
        backButton = new TextButton("Back", buttonStyle);
        backButton.setPosition(10, Gdx.graphics.getHeight() - 60); // Adjust the position as necessary
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked back");
                game.setScreen(new TitleScreen(game));
            }
        });

        nextOrderButton = new TextButton("Next Order", buttonStyle);
        nextOrderButton.setPosition(Gdx.graphics.getWidth() - 220, Gdx.graphics.getHeight() - 60); // Adjust the position as necessary
        nextOrderButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                popup.show();
                Gdx.input.setInputProcessor(popup.getStage());
                System.out.println("Order Menu");

            }
        });

        stage.addActor(nextOrderButton);
        stage.addActor(backButton);
        Gdx.input.setInputProcessor(stage);


        array = attributes.array;
        orderScreen = new OrderScreen();
        coin = new Coin();
        orderScreen.visible = false;
        order = new Order(stage, game, 01, "Cosi", "walc", 7.00 );
        popup = new Popup(this, order.toString());
    }


    @Override
    public void show() {

    }

    public Pixmap createRoundedRectanglePixmap(int width, int height, int cornerRadius, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);

        // Draw rounded rectangle
        pixmap.fillRectangle(cornerRadius, 0, width - 2 * cornerRadius, height);
        pixmap.fillRectangle(0, cornerRadius, width, height - 2 * cornerRadius);
        pixmap.fillCircle(cornerRadius, cornerRadius, cornerRadius);
        pixmap.fillCircle(cornerRadius, height - cornerRadius - 1, cornerRadius);
        pixmap.fillCircle(width - cornerRadius - 1, cornerRadius, cornerRadius);
        pixmap.fillCircle(width - cornerRadius - 1, height - cornerRadius - 1, cornerRadius);

        return pixmap;
    }

    @Override
    public void render(float delta) {
        //Gdx.input.setInputProcessor(keyProcessor);
        //Gdx.input.setInputProcessor(popup.getStage());


        Gdx.input.setInputProcessor(stage);


        orderScreen.visible = false;
        ScreenUtils.clear(Color.PINK);
        float deltaTime =  Gdx.graphics.getDeltaTime();

        // Handle player movement


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
            coin.render(batch, 0, 0);
        }

        if (Intersector.overlaps(player.getBounds(), coin.getBounds())) {
            coin.setCollected(true);
        }
        if (popup.isVisible()) {
            popup.render();
        } else {
            Gdx.input.setInputProcessor(stage); // Enable the stage input for the buttons
        }


        batch.end();
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
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