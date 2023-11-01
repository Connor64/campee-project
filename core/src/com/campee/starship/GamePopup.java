package com.campee.starship;

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

import java.util.ArrayList;

public class GamePopup {
    private Stage stage;
    public boolean visible;
    private ShapeRenderer shapeRenderer;
    private BitmapFont gameStatsfont;
    private Label gameStatsMessage;
    private Label ordersCompletedLabel;

    public GamePopup(final GameplayScreen screen, final String notificationMessage) {
        stage = new Stage();
        shapeRenderer = new ShapeRenderer();
        visible = false;

        gameStatsfont = new BitmapFont(Gdx.files.internal("moonships_font.fnt"), Gdx.files.internal("moonships_font.png"), false);

        gameStatsfont.setColor(1, 1, 0, 1);
        gameStatsfont.getData().setScale(1f);
        gameStatsMessage = new Label(notificationMessage, new Label.LabelStyle(gameStatsfont, Color.YELLOW));
        gameStatsMessage.setFontScale(0.5f);
        gameStatsMessage.setPosition(250, 500);

        ordersCompletedLabel = new Label("", new Label.LabelStyle(gameStatsfont, Color.YELLOW));
        ordersCompletedLabel.setFontScale(0.5f);
        ordersCompletedLabel.setPosition(50, 400);
        stage.addActor(ordersCompletedLabel);

        stage.addActor(gameStatsMessage);
    }


    public void showGameStatsMessage(String message) {
        gameStatsMessage.setText(message);
        gameStatsMessage.setVisible(true);
        ordersCompletedLabel.setVisible(true);
    }

    public void showOrderCompletedList(String message) {
        ordersCompletedLabel.setText("");
        gameStatsMessage.setVisible(true);
        ordersCompletedLabel.setText(message);
        ordersCompletedLabel.setVisible(true);
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
}