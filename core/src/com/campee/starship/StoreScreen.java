package com.campee.starship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class StoreScreen implements Screen {
    private final Game game;
    private Stage stage;
    private BitmapFont font;
    private TextButton backButton;
    private ExtendViewport viewport;

    public StoreScreen (final Game game) {
        this.game = game;
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


        BitmapFont buttonFont = new BitmapFont();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        buttonFont.getData().setScale(1.5f);
        textButtonStyle.font = buttonFont;
        textButtonStyle.fontColor = Color.BLACK;
        Pixmap backgroundPixmap = createRoundedRectanglePixmap(150, 60, 15, Color.valueOf("98FF98"));
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(backgroundPixmap)));
        backButton = new TextButton("BACK", textButtonStyle);
        backButton.setPosition(30, Gdx.graphics.getHeight() - 80);
        backButton.setSize(150, 60);
        backButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new LevelScreen(game));
                return true;
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                backButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                backButton.setColor(Color.WHITE);
            }
        });

        stage.addActor(backButton);
    }

    @Override
    public void show() {
        // This method is called when the screen becomes the active screen.
        // You can use it to set up any initial state or resources.
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a background color
        Gdx.gl.glClearColor(0.7f, 0.9f, 1f, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 80f));
        // Update and render game elements
        stage.act(delta);
        stage.draw();
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

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        backButton.setPosition(30, viewport.getWorldHeight() - 80); // Update button position on resize
        stage.getViewport().update(width, height, true);
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
