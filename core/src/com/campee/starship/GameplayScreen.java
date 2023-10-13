package com.campee.starship;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameplayScreen extends ApplicationAdapter implements Screen {

    private TextButton backButton;
    private TextButton nextOrderButton;

    private World world;
    private int levelWidth;
    private int levelHeight;

    private Player player;
    public PlayerAttributes playerAttributes;
    ArrayList<String> visibleQ;

    public Order currentOrder;
    private final Popup popup;
    private Coin coin;
    public int coinCounter;

    GameObject log;
    GameObject rock;
    float x;
    float y;

    public Label label;
    public Label pickupLabel;
    public Label dropoffLabel;

    public GameObject pickupObject;
    public GameObject dropoffObject;

    private InputMultiplexer multiplexer;

    private final MoonshipGame GAME;
    private SpriteBatch batch;
    private Stage stage;
    private KeyProcessor keyProcessor;

    private PlayerCamera camera;

    private ShapeRenderer shapeRenderer;
    float sidePanelX;
    float sidePanelY;
    float sidePanelWidth;
    float sidePanelHeight;
    Color sidePanelColor;
    BitmapFont font;
    float screenWidth;
    float screenHeight;


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

        levelWidth = 500;
        levelHeight = 500;

        x = screenWidth / 2;
        y = screenHeight / 2;
        log = new GameObject(world, x, y);
        rock = new GameObject(world, 300, 300);
        rock.setSprite("rock.png");
        log.setSprite("log.png");

        player = new Player(world, 150, 200);
        camera = new PlayerCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        // Define side panel properties
        sidePanelWidth = screenWidth / 5; // Width
        sidePanelX = screenWidth - sidePanelWidth-10; // Position the panel on the right side
        sidePanelY = 60; // Y position
        sidePanelHeight = Gdx.graphics.getHeight() - 150; // Height
        sidePanelColor = new Color(0.2f, 0.2f, 0.2f, 0.8f); // Background color (RGBA)


        coin = new Coin(world, 0, 0);
        coinCounter = 0;

        playerAttributes = new PlayerAttributes();

        visibleQ = new ArrayList<>();
        playerAttributes.setArray(visibleQ);
        playerAttributes.orderInProgress = false;

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
        order.setPickupBounds(-levelWidth + 50, -levelHeight + 50, 50, 50);
        order.setDropoffBounds(levelWidth - sidePanelWidth * 2, levelHeight - sidePanelHeight * 2, 50, 50);

        pickupObject = new GameObject(world, order.getPickupBounds().getX(), order.getPickupBounds().getY());
        pickupObject.setSprite("borger.png");
        pickupObject.sprite.setSize(order.getPickupBounds().getWidth(), order.getPickupBounds().getHeight());
        pickupObject.sprite.setPosition(order.getPickupBounds().getX(), order.getPickupBounds().getY());

        dropoffObject = new GameObject(world, order.getDropoffBounds().getX(), order.getDropoffBounds().getY());
        dropoffObject.setSprite("plate.png");
        dropoffObject.sprite.setSize(order.getDropoffBounds().getWidth(), order.getDropoffBounds().getHeight());
        dropoffObject.sprite.setPosition(order.getDropoffBounds().getX(), order.getDropoffBounds().getY());

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
                order.seti(count);
                count++;
                popup.setMessage(order.arrayToString());

                popup.show();
                popup.render();
                multiplexer.addProcessor(popup.getStage());

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

        // pickup label
        pickupLabel = new Label("Press P to pickup!", indicatorStyle);
        pickupLabel.setVisible(false);
        stage.addActor(pickupLabel);
        pickupLabel.setSize(font.getScaleX() * 50, font.getScaleY() * 50);

        // dropoff label
        dropoffLabel = new Label("Press O to dropoff!", indicatorStyle);
        dropoffLabel.setVisible(false);
        stage.addActor(dropoffLabel);
        dropoffLabel.setSize(font.getScaleX() * 50, font.getScaleY() * 50);
    }

    @Override
    public void create() {
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont(Gdx.files.internal("moonships_font.fnt"), Gdx.files.internal("moonships_font.png"), false);; // Define your BitmapFont
        // Set font color and scale
        font.setColor(1, 1, 0, 1);
        font.getData().setScale(0.5f);

    }

    @Override
    public void render(float delta) {
        /* ========================== UPDATE ============================ */



        // If the popup is not visible, update the player and world
        if (!popup.isVisible()) {
            player.update(delta, keyProcessor);
            player.checkBounds(levelWidth, levelHeight, 5000, sidePanelWidth);
            world.step(1/60f, 6, 2); // Physics calculations
//            player.position.y = MathUtils.clamp(player.position.y, -levelHeight, levelHeight);
            camera.follow(player.position, levelWidth, levelHeight);
        }

        batch.setProjectionMatrix(camera.combined);



        stage.act(delta);

        // screen boundary collisions
        Rectangle playerBounds = player.sprite.getBoundingRectangle();
        Rectangle screenBounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        float playerLeft = playerBounds.getX();
        float playerBottom = playerBounds.getY();
        float playerTop = playerBottom + playerBounds.getHeight();
        float playerRight = playerLeft + playerBounds.getWidth();

        float threshold = 50;

        // visual indicator that the player is almost off the screen
        if (!popup.isVisible()) {
            // warning only visible when popup is not
            if (playerLeft <= -(levelWidth - sidePanelWidth) + threshold) {
                label.setPosition(levelWidth- (levelWidth - label.getWidth()), levelHeight / 2);
                label.setVisible(true);
            } else if (playerRight >= (levelWidth - sidePanelWidth) - threshold) {
                label.setPosition((levelWidth - (3 * label.getWidth())), levelHeight / 2);
                label.setVisible(true);
            } else if (playerBottom <= (-levelHeight + backButton.getHeight()) + threshold) {
                label.setPosition(levelWidth / 2, levelHeight - (levelHeight - label.getHeight()));
                label.setVisible(true);
            } else if (playerTop >= (levelHeight - backButton.getHeight()) - threshold) {
                label.setPosition(levelWidth / 2, levelHeight - label.getHeight());
                label.setVisible(true);
            } else {
                // remove the label
                label.setVisible(false);
            }
        }

        final float halfWidth = playerBounds.getWidth() * .5f;
        final float halfHeight = playerBounds.getHeight() * .5f;
        // float for new position (for screen collisions)
        float newX = player.sprite.getX();
        float newY = player.sprite.getY();

        // object collision
        /*rock.sprite.setSize(300, 300);
        rock.sprite.setPosition(300, 300);
        rock.setBounds(rock.sprite.getX(), rock.sprite.getY(), rock.sprite.getWidth(), rock.sprite.getHeight());
//        System.out.println(rock.sprite.getX());
        Rectangle rockBounds = rock.getBounds();
        float rockLeft = rockBounds.getX();
        float rockBottom = screenBounds.getY();
        float rockTop = rockBottom + rockBounds.getHeight();
        float rockRight = rockLeft + rockBounds.getWidth();
        if (playerLeft < rockLeft) {
            // clamp to left
            newX = rockLeft + halfWidth;
            player.body.setLinearVelocity(newX, player.body.getLinearVelocity().y);
            //xMove = 1;
        } else if (playerRight > rockRight) {
            // clamp to right
            newX = rockRight - halfWidth;
            player.body.setLinearVelocity(-newX, player.body.getLinearVelocity().y);
            //xMove = 1;
        }
        // vertical axis
        if (playerBottom < rockBottom) {
            // clamp to bottom
            newY = rockBottom + halfHeight;
            player.body.setLinearVelocity(player.body.getLinearVelocity().x, newY);
            //yMove = 1;
        } else if (playerTop > rockTop) {
            // clamp to top
            newY = rockTop - halfHeight;
            player.body.setLinearVelocity(player.body.getLinearVelocity().x, -newY);
            //yMove = 1;
        }*/


        /* ========================== DRAW ============================ */

        ScreenUtils.clear(Color.PINK);

        // Draw game world stuff
        batch.begin();

        player.render(batch);
        //rock.render(batch, 300, 300);
        log.render(batch, 0, 10);
        log.sprite.setSize(100, 75);

        // coin collision
        if (!coin.collected) {
            coin.render(batch, 100, 100);
            if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), coin.getSprite().getBoundingRectangle())) {
                coin.setCollected(true);
                coinCounter++;
            }
        }

        if (playerAttributes.array.size() > 1 && !order.isPickedUp()) {
            pickupObject.sprite.draw(batch);
            if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), order.getPickupBounds())) {
                pickupLabel.setVisible(true);
                if (keyProcessor.pPressed) {
                    order.setPickedUp(true);
                    order.setDroppedOff(false);
                    pickupLabel.setVisible(false);
                }
            } else {
                pickupLabel.setVisible(false);
            }
        }

        if (playerAttributes.array.size() > 1 && !order.isDroppedOff() && order.isPickedUp()) {
            dropoffObject.sprite.draw(batch);
            if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), order.getDropoffBounds())) {
                dropoffLabel.setVisible(true);
                if (keyProcessor.oPressed) {
                    order.setPickedUp(false);
                    order.setDroppedOff(true);
                    playerAttributes.orderInProgress = false;
                    playerAttributes.array.remove(1);
                    dropoffLabel.setVisible(false);
                }
            } else {
                dropoffLabel.setVisible(false);
            }
        }

        if (!order.isPickedUp() && playerAttributes.orderInProgress) {
            pickupObject.sprite.draw(batch);
            if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), order.getPickupBounds())) {
                pickupLabel.setVisible(true);
                if (keyProcessor.pPressed) {
                    order.setPickedUp(true);
                    order.setDroppedOff(false);
                    pickupLabel.setVisible(false);
                }
            } else {
                pickupLabel.setVisible(false);
            }
        } else if (order.isPickedUp() && !order.isDroppedOff() && playerAttributes.orderInProgress) {
            dropoffObject.sprite.draw(batch);
            if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), order.getDropoffBounds())) {
                dropoffLabel.setVisible(true);
                if (keyProcessor.oPressed) {
                    order.setPickedUp(false);
                    order.setDroppedOff(true);
                    playerAttributes.orderInProgress = false;
                    playerAttributes.array.remove(1);
                    dropoffLabel.setVisible(false);
                }
            } else {
                dropoffLabel.setVisible(false);
            }
        }

        batch.end();

        // Draw UI stuff
        batch.begin();

        // Render the side panel
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(sidePanelColor);
        shapeRenderer.rect(sidePanelX, sidePanelY, sidePanelWidth, sidePanelHeight);
        shapeRenderer.end();

        //shapeRenderer.setProjectionMatrix(camera.combined);


        String[] items = playerAttributes.array.toArray(new String[0]);

        font.draw(batch, "Order List:", sidePanelX + 10, sidePanelY + sidePanelHeight - 10);
        //font.draw(batch, "\n", sidePanelX + 10, sidePanelY + sidePanelHeight - 10);

        for (int i = 1; i < items.length; i++) {
                font.draw(batch, items[i], sidePanelX + 10, sidePanelY + sidePanelHeight - 70 * i);
        }

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
