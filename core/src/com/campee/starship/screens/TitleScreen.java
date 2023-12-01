package com.campee.starship.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.FileNotFoundException;

public class TitleScreen implements Screen {
    private MoonshipGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont buttonFont;
    private GlyphLayout glyphLayout;
    private Texture img;
    private Stage stage;
    private ExtendViewport viewport;
    private TextButton button;
    private TextButton closeButton;
    private boolean isPlayButtonHovered = false;
    private boolean isCloseButtonHovered = false;
    Music music;

    public TitleScreen(final MoonshipGame game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/moonships_font.fnt"), Gdx.files.internal("fonts/moonships_font.png"), false);
        buttonFont = new BitmapFont(Gdx.files.internal("fonts/moonships_font.fnt"), Gdx.files.internal("fonts/moonships_font.png"), false);

        //font = new BitmapFont();

        music = Gdx.audio.newMusic(Gdx.files.internal("audio/Moon Final.mp3"));
        music.setLooping(true);
        music.setVolume(0.35f);

        // Set font color and scale
        font.setColor(1, 1, 0, 1);
        font.getData().setScale(3);

        img = new Texture(Gdx.files.internal("sprites/title_moonship.png"));

        glyphLayout = new GlyphLayout();
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        ScreenUtils.clear(1, 0.8f, 1, 1);

//        BitmapFont buttonFont = new BitmapFont();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        buttonFont.getData().setScale(3f);
        textButtonStyle.font = buttonFont;
        textButtonStyle.fontColor = Color.BLACK;

        // Create a pixmap for the background (rounded rectangle)
        Pixmap backgroundPixmap = createRoundedRectanglePixmap(300, 120, 15, Color.valueOf("98FF98")); // Light green color

        // Set the background of the button
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(backgroundPixmap)));

        button = new TextButton("PLAY", textButtonStyle);
        button.setColor(Color.WHITE);

        updateButtonPosition();

        //float buttonWidth = 200;
        //float buttonHeight = 80;
        //float buttonX = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        //float buttonY = (Gdx.graphics.getHeight() - buttonHeight + 100) / 2;
        //button.setPosition(buttonX, buttonY);
        //button.setSize(buttonWidth, buttonHeight);
        button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Switch to another screen when the button is clicked
                try {
                    music.pause();
                    game.setScreen(new LevelScreen(game)); // Change to the screen you want
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                isPlayButtonHovered = true;
                button.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isPlayButtonHovered = false;
                button.setColor(Color.WHITE);
            }
        });

        stage.addActor(button);

        BitmapFont closeFont = buttonFont;
        TextButton.TextButtonStyle closeButtonStyle = new TextButton.TextButtonStyle();
        closeFont.getData().setScale(0.55f);
        closeButtonStyle.font = closeFont;
        closeButtonStyle.fontColor = Color.BLACK;
        //Pixmap backgroundPixmap = createRoundedRectanglePixmap(150, 60, 15, Color.valueOf("98FF98"));
        closeButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(backgroundPixmap)));
        closeButton = new TextButton("CLOSE", closeButtonStyle);
        closeButton.setPosition(10, Gdx.graphics.getHeight() - 50);
        closeButton.setSize(100, 30);
        closeButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Switch back to the title screen when the BACK button is clicked
//                game.setScreen(new TitleScreen((MoonshipGame) game)); // Change to the screen you want
//                return true;
                Gdx.app.exit();
                return true;
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                isCloseButtonHovered = true;
                closeButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isCloseButtonHovered = false;
                closeButton.setColor(Color.WHITE);
            }
        });
        stage.addActor(closeButton);
    }

    private void updateButtonPosition() {
        float buttonWidth = 200;
        float buttonHeight = 80;
        float buttonX = (viewport.getWorldWidth() - buttonWidth) / 2;
        float buttonY = (viewport.getWorldHeight() - buttonHeight + 100) / 2;
        button.setPosition(buttonX, buttonY);
        button.setSize(buttonWidth, buttonHeight);
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
        ScreenUtils.clear(Color.PINK);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        glyphLayout.setText(font, "MOONSHIPS");
        float textX = (viewport.getWorldWidth() - glyphLayout.width) / 2;
        float textY = viewport.getWorldHeight() * 3 / 4 + glyphLayout.height;
        font.draw(batch, glyphLayout, textX, textY);

        float scaleFactor = 0.2f;
        float imgWidth = img.getWidth()  * scaleFactor;
        float imgHeight = img.getHeight() * scaleFactor;
        float imgX = (viewport.getWorldWidth() - imgWidth) / 2;
        float imgY = 30; // Adjust this value to move the image up or down
        batch.draw(img, imgX, imgY, imgWidth, imgHeight);
        music.play();

        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        updateButtonPosition();

        float closeButtonX = 10; // X-coordinate of the CLOSE button
        float closeButtonY = viewport.getWorldHeight() - 50; // Y-coordinate of the CLOSE button
        closeButton.setPosition(closeButtonX, closeButtonY);
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