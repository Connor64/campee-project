package com.campee.starship.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.io.FileNotFoundException;

public class SettingsScreen implements Screen {
    private final Game game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private TextButton backButton;
    private Stage stage;
    private ExtendViewport viewport;
    private float timer = 10; // Countdown timer in seconds
    public Music music;

    public SettingsScreen(final Game game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600); // Set the camera dimensions as per your requirement
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2.0f); // Set the font scale to make the text larger

        music = Gdx.audio.newMusic(Gdx.files.internal("audio/menu screen sound.mp3"));
        music.setLooping(true);
        music.setVolume(0.35f);

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
                // Switch back to the title screen when the BACK button is clicked
                try {
                    music.pause();
                    game.setScreen(new LevelScreen(game)); // Change to the screen you want
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        stage.addActor(backButton);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1); // Light gray background color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw the text "SETTINGS" at the top of the screen
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, "SETTINGS");
        //add camera stuff so that the extending works
        float textX = (camera.viewportWidth - layout.width) / 2;
        float textY = camera.viewportHeight - 50;
        font.draw(batch, "SETTINGS", textX, textY);

        // Draw countdown timer
        GlyphLayout timerLayout = new GlyphLayout();
        timerLayout.setText(font, "Time Left: " + (int) timer);
        float timerX = (camera.viewportWidth - timerLayout.width) / 2;
        float timerY = textY - 50;
        font.draw(batch, "Time Left: " + (int) timer, timerX, timerY);

        batch.end();

        stage.act(delta);
        music.play();
        stage.draw();

        // Update countdown timer
        timer -= delta;
        if (timer <= 0) {
            // When the timer reaches 0, switch back to the title screen
            try {
                music.pause();
                game.setScreen(new LevelScreen(game)); // Change to the screen you want
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height); // Update the camera when the screen is resized
        viewport.update(width, height, true);
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
        stage.dispose();
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