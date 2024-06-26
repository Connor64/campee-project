package com.campee.starship.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SplashScreen implements Screen {
    private Game game;
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout glyphLayout;
    private Texture img;
    private Stage stage;
    private ExtendViewport viewport;
//    private TextButton button;

    public SplashScreen (final Game game) {
        this.game = game;
        batch = new SpriteBatch();
//        font = new BitmapFont(Gdx.files.internal("moonships_font.fnt"), Gdx.files.internal("moonships_font.png"), false);
        //font = new BitmapFont();

        // Set font color and scale
//        font.setColor(1, 1, 0, 1);
//        font.getData().setScale(3);

        img = new Texture(Gdx.files.internal("sprites/campee_logo.png"));

        glyphLayout = new GlyphLayout();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        ScreenUtils.clear(1, 0.8f, 1, 1);



        // Create a pixmap for the background (rounded rectangle)
        Pixmap backgroundPixmap = createRoundedRectanglePixmap(300, 120, 15, Color.valueOf("98FF98")); // Light green color

        // Set the background of the button
//        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(backgroundPixmap)));

//        button = new TextButton("PLAY", textButtonStyle);
//        button.setColor(Color.WHITE);

        updateButtonPosition();

        //float buttonWidth = 200;
        //float buttonHeight = 80;
        //float buttonX = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        //float buttonY = (Gdx.graphics.getHeight() - buttonHeight + 100) / 2;
        //button.setPosition(buttonX, buttonY);
        //button.setSize(buttonWidth, buttonHeight);

//        stage.addActor(button);
    }

    private void updateButtonPosition() {
        float buttonWidth = 200;
        float buttonHeight = 80;
        float buttonX = (viewport.getWorldWidth() - buttonWidth) / 2;
        float buttonY = (viewport.getWorldHeight() - buttonHeight + 100) / 2;
//        button.setPosition(buttonX, buttonY);
//        button.setSize(buttonWidth, buttonHeight);
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
    public void show() {
    }

    @Override
    public void render(float delta) {
        //ScreenUtils.clear(1, 0.8f, 1, 1);
        ScreenUtils.clear(0.25f, 0.25f, 0.25f, 1);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
//        glyphLayout.setText(font, "MOONSHIPS");
//        float textX = (viewport.getWorldWidth() - glyphLayout.width) / 2;
//        float textY = viewport.getWorldHeight() * 3 / 4 + glyphLayout.height;
//        font.draw(batch, glyphLayout, textX, textY);
//
//        float scaleFactor = 0.2f;
//        float imgWidth = img.getWidth()  * scaleFactor;
//        float imgHeight = img.getHeight() * scaleFactor;
//        float imgX = (viewport.getWorldWidth() - imgWidth) / 2;
//        float imgY = 30; // Adjust this value to move the image up or down
        batch.draw(img, 250, 150, 300, 300);


        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        updateButtonPosition();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        img.dispose();
        stage.dispose();
    }
}