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

public class KeepPlayingPopup {
    private final Game game;
    private Stage stage;
    public boolean visible;
    private ShapeRenderer shapeRenderer;
    private BitmapFont gameStatsfont;
    private Label messageLabel;
    private Label optionLabel;
    private BitmapFont font;
    private BitmapFont buttonFont;
    private boolean keepPlayingClicked;
    private boolean endGameClicked;
    private boolean isKeepPlayingButtonHovered = false;
    private boolean isendGameButtonHovered = false;

    float popupWidth;
    float popupHeight;
    float popupX;
    float popupY;

    public KeepPlayingPopup(final GameplayScreen screen, final String notificationMessage, final Game game, final String fileName) {
        this.game = game;
        stage = new Stage();
        shapeRenderer = new ShapeRenderer();
        visible = false;

        font = new BitmapFont(Gdx.files.internal("fonts/moonships_font.fnt"), Gdx.files.internal("fonts/moonships_font.png"), false);

        buttonFont = new BitmapFont();

        // Set font color and scale
        buttonFont.setColor(1, 1, 0, 1);
        buttonFont.getData().setScale(1.25f);

        messageLabel = new Label(notificationMessage, new Label.LabelStyle(font, Color.WHITE));
        messageLabel.setFontScale(1f);
        messageLabel.setPosition(200, 350);

        optionLabel = new Label(notificationMessage, new Label.LabelStyle(font, Color.WHITE));
        optionLabel.setFontScale(0.5f);
        optionLabel.setPosition(200, 300);


        Pixmap keepPlayingBackgroundPixmap = createRoundedRectanglePixmap(1000, 200, 10, Color.GREEN); // Adjust size and color
        TextButton.TextButtonStyle keepPlayingButtonStyle = new TextButton.TextButtonStyle();
        keepPlayingButtonStyle.font = buttonFont;
        keepPlayingButtonStyle.fontColor = Color.BLACK;
        keepPlayingButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(keepPlayingBackgroundPixmap)));


        Pixmap endGameBackgroundPixmap = createRoundedRectanglePixmap(1000, 200, 10, Color.RED); // Adjust size and color
        TextButton.TextButtonStyle endGameButtonStyle = new TextButton.TextButtonStyle();
        endGameButtonStyle.font =  buttonFont;
        endGameButtonStyle.fontColor = Color.BLACK;
        endGameButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(endGameBackgroundPixmap)));

        // Create buttons
        final TextButton keepPlayingButton = new TextButton("Keep Playing",  keepPlayingButtonStyle);
        final TextButton endGameButton = new TextButton("End Game",  endGameButtonStyle);

        keepPlayingButton.setWidth(120);
        keepPlayingButton.setHeight(75);
        endGameButton.setWidth(120);
        endGameButton.setHeight(75);

        // Set button positions
        keepPlayingButton.setPosition(100, 200);
        endGameButton.setPosition(500,200);

        keepPlayingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                keepPlayingClicked = true;
                screen.keepPlaying = false;
                visible = false;
                screen.popupInAction = false;
                //add logic
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                isKeepPlayingButtonHovered = true;
                keepPlayingButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isKeepPlayingButtonHovered = false;
                keepPlayingButton.setColor(Color.WHITE);
            }
        });

        endGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                endGameClicked = true;
                screen.win = true;
                screen.keepPlaying = false;
                screen.countdownMinutes = 0;
                screen.countdownSeconds = 0;
                screen.popupInAction = false;
                screen.showGameResult();
                //add logic

            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                isendGameButtonHovered = true;
                endGameButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isendGameButtonHovered = false;
                endGameButton.setColor(Color.WHITE);
            }
        });

        stage.addActor(messageLabel);
        stage.addActor(optionLabel);
        stage.addActor(keepPlayingButton);
        stage.addActor(endGameButton);
    }


    public void setMessageLabel(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }

    public void setOptionLabel(String option) {
        optionLabel.setText(option);
        optionLabel.setVisible(true);
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

    public boolean keepPlayingClicked() {
        return keepPlayingClicked;
    }
    public boolean endGameClicked() {
        return endGameClicked;
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
