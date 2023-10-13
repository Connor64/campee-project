package com.campee.starship;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.io.FileNotFoundException;

public class LevelScreen implements Screen {
    private final MoonshipGame game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;
    private TextButton backButton; // Add a TextButton for the "BACK" button
    private TextButton beginButton;
    private TextButton settingsButton;
    private ExtendViewport viewport;

    public LevelScreen(final Game game) {
        this.game = (MoonshipGame) game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        // Create BACK button similar to PLAY button in TitleScreen
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
                game.setScreen(new TitleScreen((MoonshipGame) game)); // Change to the screen you want
                return true;
            }
        });
        stage.addActor(backButton);
        TextButton.TextButtonStyle beginButtonStyle = new TextButton.TextButtonStyle();
        beginButtonStyle.font = buttonFont;
        beginButtonStyle.fontColor = Color.BLACK;
        Pixmap beginButtonPixmap = createRoundedRectanglePixmap(150, 60, 15, Color.valueOf("98FF98"));
        beginButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(beginButtonPixmap)));


        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float buttonX = 30;
        //float buttonY = (screenHeight - buttonHeight) / 2;
//        float buttonHeight = 60;
//        float buttonWidth = 150;
//        float screenWidth = Gdx.graphics.getWidth();
//        float screenHeight = Gdx.graphics.getHeight();
//        float buttonX = 30;
//        float buttonY = (screenHeight - buttonHeight) / 2;

        beginButton = new TextButton("BEGIN DEMO", beginButtonStyle);
        //beginButton.setPosition(buttonX, buttonY);
        beginButton.setPosition(30, Gdx.graphics.getHeight() - 80);
        //beginButton.setPosition(30, Gdx.graphics.getHeight() - 80);
        //beginButton.setSize(Gdx.graphics.getWidth() - 180, Gdx.graphics.getHeight() - 80);
        beginButton.setSize(150, 60);
        beginButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Switch back to the title screen when the BACK button is clicked
                try {
                    game.setScreen(new GameplayScreen((MoonshipGame) game)); // Change to the screen you want
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        stage.addActor(beginButton);

        TextButton.TextButtonStyle settingsButtonStyle = new TextButton.TextButtonStyle();
        settingsButtonStyle.font = buttonFont;
        settingsButtonStyle.fontColor = Color.BLACK;
        Pixmap settingsButtonPixmap = createRoundedRectanglePixmap(150, 60, 15, Color.valueOf("98FF98"));
        settingsButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(settingsButtonPixmap)));

        settingsButton = new TextButton("SETTINGS", settingsButtonStyle);
        settingsButton.setPosition(30, Gdx.graphics.getHeight() - 150); // Adjust Y-coordinate as needed
        settingsButton.setSize(150, 60);
        settingsButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new SettingsScreen(game));
                return true;
            }
        });

        stage.addActor(settingsButton);
    }

    @Override
    public void show() {
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.7f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Filled);

        float boxWidth = 200;
        //float boxHeight = 150;
        float boxHeight = 200;
        float boxSpacing = 50;
        float startY = (camera.viewportHeight - boxHeight) / 2;

        shapeRenderer.setColor(new Color(0.8f, 0.6f, 1f, 1f));
        shapeRenderer.rect(boxSpacing, startY, boxWidth, boxHeight); // Level 1 box
        shapeRenderer.rect(boxSpacing * 2 + boxWidth, startY, boxWidth, boxHeight); // Level 2 box
        shapeRenderer.rect(boxSpacing * 3 + boxWidth * 2, startY, boxWidth, boxHeight); // Level 3 box
        shapeRenderer.end();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, "LEVEL SELECT SCREEN");
        float textX = (camera.viewportWidth - layout.width) / 2;
        float textY = camera.viewportHeight - 50;
        font.draw(batch, "LEVEL SELECT SCREEN", textX, textY);

        layout.setText(font, "LEVEL 1");
        float level1X = boxSpacing + (boxWidth - layout.width) / 2;
        //float level1Y = startY + boxHeight / 2 + font.getLineHeight() / 2;
        float level1Y = startY + boxHeight / 2 + font.getLineHeight() / 2 + 50;
        font.getData().setScale(2f);
        font.draw(batch, "LEVEL 1", level1X, level1Y);

        layout.setText(font, "LEVEL 2");
        float level2X = boxSpacing * 2 + boxWidth + (boxWidth - layout.width) / 2;
        //float level2Y = startY + boxHeight / 2 + font.getLineHeight() / 2;
        float level2Y = startY + boxHeight / 2 + font.getLineHeight() / 2 + 50;
        font.draw(batch, "LEVEL 2", level2X, level2Y);

        layout.setText(font, "LEVEL 3");
        float level3X = boxSpacing * 3 + boxWidth * 2 + (boxWidth - layout.width) / 2;
        //float level3Y = startY + boxHeight / 2 + font.getLineHeight() / 2;
        float level3Y = startY + boxHeight / 2 + font.getLineHeight() / 2 + 50;
        font.draw(batch, "LEVEL 3", level3X, level3Y);

        batch.end();
        stage.act(delta);
        stage.draw();
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        backButton.setPosition(30, viewport.getWorldHeight() - 80); // Update button position on resize
        //beginButton.setPosition(viewport.getWorldWidth() - 180, viewport.getWorldHeight() - 80);
        float buttonWidth = 150;
        float buttonHeight = 60;
        float buttonXPercentage = 0.19f; // 90% from the left side of the screen
        float buttonYPercentage = 0.4f;
        float buttonX = viewport.getWorldWidth() * buttonXPercentage - buttonWidth / 2;
        float buttonY = viewport.getWorldHeight() * buttonYPercentage - buttonHeight / 2;

        beginButton.setSize(buttonWidth, buttonHeight);
        beginButton.setPosition(buttonX, buttonY);
        //beginButton.setPosition(viewport.getWorldWidth() - 700, viewport.getWorldHeight() - 80);
        settingsButton.setPosition(viewport.getWorldWidth() - 180, viewport.getWorldHeight() - 80);
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
        shapeRenderer.dispose();
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
