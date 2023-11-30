package com.campee.starship.userinterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.campee.starship.screens.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class GamePopup {
    private final Game game;
    private Stage stage;
    public boolean visible;
    private ShapeRenderer shapeRenderer;
    private BitmapFont gameStatsfont;
    private Label gameStatsMessage;
    private Label ordersCompletedLabel;
    private Label levelResultMessage;
    private Label ordersOutOfTimeLabel;
    private BitmapFont font;
    private boolean isExitButtonHovered = false;
    private boolean isReplayButtonHovered = false;

    float popupWidth;
    float popupHeight;
    float popupX;
    float popupY;

    public GamePopup(final GameplayScreen screen, final String notificationMessage, final Game game, final String fileName) {
        this.game = game;
        stage = new Stage();
        shapeRenderer = new ShapeRenderer();
        visible = false;

        gameStatsfont = new BitmapFont(Gdx.files.internal("fonts/moonships_font.fnt"), Gdx.files.internal("fonts/moonships_font.png"), false);

        gameStatsfont.setColor(1, 1, 0, 1);
        gameStatsfont.getData().setScale(1f);
        gameStatsMessage = new Label(notificationMessage, new Label.LabelStyle(gameStatsfont, Color.YELLOW));
        gameStatsMessage.setFontScale(0.5f);
        gameStatsMessage.setPosition(250, 475);

        ordersCompletedLabel = new Label("", new Label.LabelStyle(gameStatsfont, Color.YELLOW));
        ordersCompletedLabel.setFontScale(0.5f);
        ordersCompletedLabel.setPosition(50, 400);


        levelResultMessage = new Label("", new Label.LabelStyle(gameStatsfont, Color.YELLOW));
        levelResultMessage.setFontScale(1f);
        levelResultMessage.setPosition(Gdx.graphics.getWidth()/2 - levelResultMessage.getWidth() - 300, 550);

        ordersOutOfTimeLabel = new Label("", new Label.LabelStyle(gameStatsfont, Color.YELLOW));
        ordersOutOfTimeLabel.setFontScale(0.5f);
        ordersOutOfTimeLabel.setPosition(500, 400);

        stage.addActor(ordersCompletedLabel);
        stage.addActor(levelResultMessage);
        stage.addActor(ordersOutOfTimeLabel);
        stage.addActor(gameStatsMessage);

        font = new BitmapFont();

        // Set font color and scale
        font.setColor(1, 1, 0, 1);
        font.getData().setScale(1.25f);

        Pixmap exitBackgroundPixmap = createRoundedRectanglePixmap(200, 50, 10, Color.RED); // Adjust size and color
        TextButton.TextButtonStyle exitButtonStyle = new TextButton.TextButtonStyle();
        exitButtonStyle.font = font;
        exitButtonStyle.fontColor = Color.BLACK;
        exitButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(exitBackgroundPixmap)));


        Pixmap replayBackgroundPixmap = createRoundedRectanglePixmap(200, 50, 10, Color.CYAN); // Adjust size and color
        TextButton.TextButtonStyle replayButtonStyle = new TextButton.TextButtonStyle();
        replayButtonStyle.font = font;
        replayButtonStyle.fontColor = Color.BLACK;
        replayButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(replayBackgroundPixmap)));

        // Create buttons
        final TextButton exitButton = new TextButton("Exit", exitButtonStyle);
        final TextButton replayButton = new TextButton("Replay", replayButtonStyle);

        exitButton.setWidth(75);
        exitButton.setHeight(25);
        replayButton.setWidth(75);
        replayButton.setHeight(25);


        // Set button positions
        exitButton.setPosition(Gdx.graphics.getWidth() - 150, 100);
        replayButton.setPosition(150, 100);

        // Add click listeners to buttons
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    game.setScreen(new LevelScreen(game));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                isExitButtonHovered = true;
                exitButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isExitButtonHovered = false;
                exitButton.setColor(Color.WHITE);
            }
        });

        replayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    game.setScreen(new GameplayScreen((MoonshipGame) game, fileName));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                isReplayButtonHovered = true;
                replayButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isReplayButtonHovered = false;
                replayButton.setColor(Color.WHITE);
            }
        });

        // Add buttons to the stage

        stage.addActor(exitButton);
        stage.addActor(replayButton);
    }


    public void showGameStatsMessage(String message) {
        gameStatsMessage.setText(message);
        gameStatsMessage.setVisible(true);
        System.out.println(message);
        ordersCompletedLabel.setVisible(true);
        ordersOutOfTimeLabel.setVisible(true);
    }

    public void showOrderCompletedList(String message) {
        String[] lines = message.split("\n");
        int numberOfLines = lines.length;
        int yPos = numberOfLines * 6;
        ordersCompletedLabel.setText(message);
        ordersCompletedLabel.setPosition(50, 400 - yPos);
        gameStatsMessage.setVisible(true);
        ordersOutOfTimeLabel.setVisible(true);
        ordersCompletedLabel.setVisible(true);
    }

    public void showOutoffTimeList(String message) {
        String[] lines = message.split("\n");
        int numberOfLines = lines.length;
        int yPos = numberOfLines * 6;
        ordersOutOfTimeLabel.setText(message);
        ordersOutOfTimeLabel.setPosition(500, 400 - yPos);
        gameStatsMessage.setVisible(true);
        ordersOutOfTimeLabel.setVisible(true);
        ordersCompletedLabel.setVisible(true);
    }

    public void showLevelResultMessage(String message) {
        levelResultMessage.setText(message);
        levelResultMessage.setVisible(true);
    }

    public void hideOrderCompletedList() {
        ordersCompletedLabel.setVisible(false);
    }

    public void hideGameStatsMessage() {
        // Hide the "Game Stats" message
        gameStatsMessage.setVisible(false);
    }

    public void show() {
        visible = true;
    }

    public void hide() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public Stage getStage() {
        return stage;
    }


    public void render() {
        if (visible) {
            // Clear the background
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.7f));
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();

            stage.act();
            stage.draw();
        }
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
}