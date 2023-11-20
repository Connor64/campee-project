package com.campee.starship.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

public class BasicScreen implements Screen {
    private Stage stage;

    public BasicScreen() {
        stage = new Stage();
        // Initialize and add actors (UI elements) to the stage
    }

    @Override
    public void show() {
        // This method is called when the screen becomes the active screen.
        // You can use it to set up any initial state or resources.
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a background color
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen to black (RGBA values)

        // Update and render game elements
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Handle screen resizing (if necessary)
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Handle pausing the screen (if necessary)
    }

    @Override
    public void resume() {
        // Handle resuming the screen (if necessary)
    }

    @Override
    public void hide() {
        // This method is called when the screen is no longer the active screen.
        // You can use it to dispose of resources or clean up.
    }

    @Override
    public void dispose() {
        // Dispose of any resources (textures, sounds, stages, etc.) used by the screen
        stage.dispose();
    }
}
