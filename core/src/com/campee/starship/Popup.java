package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

public class Popup {
    private Stage stage;
    private boolean visible;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private GlyphLayout glyphLayout;

    public Popup() {
        stage = new Stage();
        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();
        // Set font color and scale
        font.setColor(1, 1, 0, 1);
        font.getData().setScale(3);
        glyphLayout = new GlyphLayout();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        font.getData().setScale(1.5f);
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.BLACK;

        // Create buttons
        TextButton acceptButton = new TextButton("Accept", textButtonStyle);
        TextButton declineButton = new TextButton("Decline", textButtonStyle);

        acceptButton.setWidth(100);
        acceptButton.setHeight(50);
        declineButton.setWidth(100);
        declineButton.setHeight(50);

        // Set button positions
        acceptButton.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 25);
        declineButton.setPosition(Gdx.graphics.getWidth() / 2 + 20, Gdx.graphics.getHeight() / 2 - 25);

        // Add click listeners to buttons
        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                visible = false;
                // Handle accept button click
                //hide();
            }
        });

        declineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("something happened");
                visible = false;
                // Handle decline button click
                //hide();
            }
        });

        // Add buttons to the stage
        stage.addActor(acceptButton);
        stage.addActor(declineButton);
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