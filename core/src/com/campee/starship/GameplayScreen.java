package com.campee.starship;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Screen;

public class GameplayScreen extends ApplicationAdapter implements Screen {

    private TextButton backButton;
    private TextButton nextOrderButton;

    private World world;

    private Player player;
    public PlayerAttributes playerAttributes;
    public Order currentOrder;
    private final Popup popup;

    private InputMultiplexer multiplexer;

    private final MoonshipGame GAME;
    private SpriteBatch batch;
    private Stage stage;
    private KeyProcessor keyProcessor;

    private PlayerCamera camera;

    public GameplayScreen(final MoonshipGame game) {
        GAME = game;
        batch = GAME.batch;

        stage = new Stage();
        keyProcessor = new KeyProcessor();
        world = new World(new Vector2(0, 0), true);
        multiplexer = new InputMultiplexer();

        player = new Player(world, 0, 0);
        camera = new PlayerCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        // For testing
        currentOrder = new Order(stage, game, 01, "Cosi", "walc", 7.00);
        popup = new Popup(this, currentOrder.toString());
        playerAttributes = new PlayerAttributes();

        // Make button style
        Pixmap backgroundPixmap = createRoundedRectanglePixmap(200, 50, 10, Color.PURPLE); // Adjust size and color
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(backgroundPixmap)));
        BitmapFont buttonFont = new BitmapFont();
        buttonFont.getData().setScale(1.5f);
        buttonStyle.font = buttonFont;
        buttonStyle.fontColor = Color.BLACK;

        // Make back button
        backButton = new TextButton("Back", buttonStyle);
        backButton.setPosition(10, Gdx.graphics.getHeight() - 60); // Adjust the position as necessary
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked back");
                game.setScreen(new LevelScreen(game));
            }
        });

        // Make next order button
        nextOrderButton = new TextButton("Next Order", buttonStyle);
        nextOrderButton.setPosition(Gdx.graphics.getWidth() - 220, Gdx.graphics.getHeight() - 60); // Adjust the position as necessary
        nextOrderButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popup.show();
                popup.render();
                multiplexer.addProcessor(popup.getStage());
                System.out.println("Order Menu");
            }
        });

        stage.addActor(backButton);
        stage.addActor(nextOrderButton);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(keyProcessor);

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void create() {
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        /* ========================== UPDATE ============================ */

        // If the popup is not visible, update the player and world
        if (!popup.isVisible()) {
            player.update(delta, keyProcessor);
            world.step(1/60f, 6, 2); // Physics calculations
            camera.follow(player.position, delta);
        }

        batch.setProjectionMatrix(camera.combined);

        stage.act(delta);

        /* ========================== DRAW ============================ */

        ScreenUtils.clear(Color.PINK);

        // Draw game world stuff
        batch.begin();
        player.render(batch);
        batch.end();

        // Draw UI stuff
        batch.begin();
        stage.draw();
        popup.render();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        if (camera != null) camera.setToOrtho(false, width, height);
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
        multiplexer.removeProcessor(stage);
    }

    /**
     * Creates a rectangular panel with rounded corners.
     *
     * @param width Width of the panel.
     * @param height Height of the panel.
     * @param cornerRadius The radius of the corners.
     * @param color The background color of the panel.
     * @return A pixmap object of the panel.
     */
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

    public Stage getStage() {
        return stage;
    }
}
