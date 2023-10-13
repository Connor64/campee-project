package com.campee.starship;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Screen;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameplayScreen extends ApplicationAdapter implements Screen {

    private TextButton backButton;
    private TextButton nextOrderButton;

    private World world;

    private Player player;
    public PlayerAttributes playerAttributes;
    public Order currentOrder;
    private final Popup popup;
    private Coin coin;
    public int coinCounter;
    public Label label;

    private InputMultiplexer multiplexer;

    private final MoonshipGame GAME;
    private SpriteBatch batch;
    private Stage stage;
    private KeyProcessor keyProcessor;

    private PlayerCamera camera;

    ArrayList<String> arrays;
    String s;
    int count;
    Order order;
    String[] orderA;
    GameplayScreen g;

    public GameplayScreen(final MoonshipGame game) throws FileNotFoundException {
        g = this;
        this.GAME = game;
        batch = game.batch;

        stage = new Stage();
        keyProcessor = new KeyProcessor();
        world = new World(new Vector2(0, 0), true);
        multiplexer = new InputMultiplexer();

        player = new Player(world, 150, 200);
        camera = new PlayerCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        coin = new Coin(world, 0, 0);
        coinCounter = 0;

        // For testing
        //currentOrder = new Order(stage, game, 01, "Cosi", "walc", 7, new ArrayList<String>());
        //popup = new Popup(this, currentOrder.toString());
        playerAttributes = new PlayerAttributes();


        order = new Order();
        arrays = new ArrayList<>();
        order.setArray(arrays);
        System.out.println(arrays);
        orderA = order.arrayToArray();
        order.seti(order.i++);
        int time = Integer.parseInt(orderA[3]);
        int id = Integer.parseInt(orderA[0]);
        order = new Order(stage, game, id, orderA[1], orderA[2], time, arrays);
        popup = new Popup(this, order.arrayToString());

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
                try {
                    order.setArray(arrays);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println(order.array);
                order.seti(count);
                //count++;
                //orderA = order.arrayToArray();
                System.out.println(Arrays.toString(orderA));
                int time = Integer.parseInt(orderA[3]);
                int id = Integer.parseInt(orderA[0]);

                order = new Order(stage, game, id, orderA[1], orderA[2], time, arrays);
                order.seti(count);
                count++;
                //popup = new Popup(, order.arrayToString());
                popup.setMessage(order.arrayToString());

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

        // visual indicator boundary
        BitmapFont font = new BitmapFont(Gdx.files.internal("moonships_font.fnt"), Gdx.files.internal("moonships_font.png"), false);
        font.setColor(0, 0, 0, 1);
        font.getData().setScale(0.5f, 0.5f);
        Label.LabelStyle indicatorStyle = new Label.LabelStyle(font, Color.BLACK);
        label = new Label("Careful!", indicatorStyle);
        label.setVisible(false);
        stage.addActor(label);
        label.setSize(font.getScaleX() * 100, font.getScaleY() * 100);
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

        // screen boundary collisions
        float newX = player.sprite.getX();
        float newY = player.sprite.getY();
        Rectangle playerBounds = player.sprite.getBoundingRectangle();
        Rectangle screenBounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        float playerLeft = playerBounds.getX();
        float playerBottom = playerBounds.getY();
        float playerTop = playerBottom + playerBounds.getHeight();
        float playerRight = playerLeft + playerBounds.getWidth();

        final float halfWidth = playerBounds.getWidth() * .5f;
        final float halfHeight = playerBounds.getHeight() * .5f;

        float screenLeft = screenBounds.getX();
        float screenBottom = screenBounds.getY();
        float screenTop = screenBottom + screenBounds.getHeight();
        float screenRight = screenLeft + screenBounds.getWidth();

        if (playerLeft < screenLeft) {
            // clamp to left
            newX = screenLeft + halfWidth;
            player.body.setLinearVelocity(newX, player.body.getLinearVelocity().y);
        } else if (playerRight > screenRight) {
            // clamp to right
            newX = screenRight - halfWidth;
            player.body.setLinearVelocity(-newX, player.body.getLinearVelocity().y);
        }
        // vertical axis
        if (playerBottom < screenBottom) {
            // clamp to bottom
            newY = screenBottom + halfHeight;
            player.body.setLinearVelocity(player.body.getLinearVelocity().x, newY);
        } else if (playerTop > screenTop) {
            // clamp to top
            newY = screenTop - halfHeight;
            player.body.setLinearVelocity(player.body.getLinearVelocity().x, -newY);
        }

        // visual indicator that the player is almost off the screen
        if (!popup.isVisible()) {
            // warning only visible when popup is not
            if (playerLeft <= screenLeft + (2 * halfWidth)) {
                label.setPosition(screenBounds.getWidth() - (screenBounds.getWidth() - label.getWidth()), screenBounds.getHeight() / 2);
                label.setVisible(true);
            } else if (playerRight >= screenRight - (2 * halfWidth)) {
                label.setPosition((screenBounds.getWidth() - (3 * label.getWidth())), screenBounds.getHeight() / 2);
                label.setVisible(true);
            } else if (playerBottom <= screenBottom + (2 * halfHeight)) {
                label.setPosition(screenBounds.getWidth() / 2, screenBounds.getHeight() - (screenBounds.getHeight() - label.getHeight()));
                label.setVisible(true);
            } else if (playerTop >= screenTop - (2 * halfHeight)) {
                label.setPosition(screenBounds.getWidth() / 2, screenBounds.getHeight() - label.getHeight());
                label.setVisible(true);
            } else {
                // remove the label
                label.setVisible(false);
            }
        }

        /* ========================== DRAW ============================ */

        ScreenUtils.clear(Color.PINK);

        // Draw game world stuff
        batch.begin();
        player.render(batch);

        if (!coin.collected) {
            coin.render(batch, 150, 150);
            if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), coin.getSprite().getBoundingRectangle())) {
                coin.setCollected(true);
                coinCounter++;
            }
        }

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
