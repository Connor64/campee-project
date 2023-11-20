package com.campee.starship;

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

import java.io.IOException;
import java.util.ArrayList;

public class PauseScreen {
    private final Game game;
    private Stage stage;
    public boolean visible;
    private ShapeRenderer shapeRenderer;
    private Label messageLabel;
    private Label musicLabel;
    private Label soundEffectsLabel;
    private BitmapFont font;
    private BitmapFont buttonFont;
    private boolean resumeClicked;
    private boolean exitClicked;
    private boolean isResumeButtonHovered = false;
    private boolean isExitButtonHovered = false;


    public PauseScreen(final GameplayScreen screen, final String notificationMessage, final Game game) {
        this.game = game;
        stage = new Stage();
        shapeRenderer = new ShapeRenderer();
        visible = false;

        font = new BitmapFont(Gdx.files.internal("moonships_font.fnt"), Gdx.files.internal("moonships_font.png"), false);

        buttonFont = new BitmapFont();

        // Set font color and scale
        buttonFont.setColor(1, 1, 0, 1);
        buttonFont.getData().setScale(1.25f);

        messageLabel = new Label(notificationMessage, new Label.LabelStyle(font, Color.WHITE));
        messageLabel.setFontScale(1f);
        messageLabel.setPosition(200, 450);

        musicLabel = new Label("Music volume:", new Label.LabelStyle(font, Color.WHITE));
        musicLabel.setFontScale(0.75f);
        musicLabel.setPosition(200, 350);

        soundEffectsLabel = new Label("Sound Effects volume:", new Label.LabelStyle(font, Color.WHITE));
        soundEffectsLabel.setFontScale(0.75f);
        soundEffectsLabel.setPosition(200, 250);

        Pixmap resumeBackgroundPixmap = createRoundedRectanglePixmap(1000, 200, 10, Color.GREEN); // Adjust size and color
        TextButton.TextButtonStyle resumeButtonStyle = new TextButton.TextButtonStyle();
        resumeButtonStyle.font = buttonFont;
        resumeButtonStyle.fontColor = Color.BLACK;
        resumeButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(resumeBackgroundPixmap)));


        Pixmap exitBackgroundPixmap = createRoundedRectanglePixmap(1000, 200, 10, Color.RED); // Adjust size and color
        TextButton.TextButtonStyle exitButtonStyle = new TextButton.TextButtonStyle();
        exitButtonStyle.font =  buttonFont;
        exitButtonStyle.fontColor = Color.BLACK;
        exitButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(exitBackgroundPixmap)));

        // Create buttons
        final TextButton resumeButton = new TextButton("Resume Game",  resumeButtonStyle);
        final TextButton exitButton = new TextButton("Exit Game",  exitButtonStyle);

        resumeButton.setWidth(130);
        resumeButton.setHeight(75);
        exitButton.setWidth(130);
        exitButton.setHeight(75);

        // Set button positions
        resumeButton.setPosition(100, 75);
        exitButton.setPosition(500,75);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resumeClicked = true;
                //screen.resume = false;
                visible = false;
                screen.gamePaused = false;
                //add logic
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                isResumeButtonHovered = true;
                resumeButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isResumeButtonHovered = false;
                resumeButton.setColor(Color.WHITE);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitClicked = true;
                //screen.win = true;
                //screen.resume = false;
                screen.countdownMinutes = 0;
                screen.countdownSeconds = 0;
                screen.gamePaused = false;

                game.setScreen(new LevelScreen(game));

                //add logic

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

        stage.addActor(messageLabel);
        stage.addActor(musicLabel);
        stage.addActor(soundEffectsLabel);
        stage.addActor(resumeButton);
        stage.addActor(exitButton);
    }


    public void setMessageLabel(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }

//    public void setOptionLabel(String option) {
//        optionLabel.setText(option);
//        optionLabel.setVisible(true);
//    }

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

    public boolean resumeClicked() {
        return resumeClicked;
    }
    public boolean exitClicked() {
        return exitClicked;
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
