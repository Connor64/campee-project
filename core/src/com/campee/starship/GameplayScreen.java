package com.campee.starship;

import Serial.LevelData;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.Collections;


import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameplayScreen extends ApplicationAdapter implements Screen {

    private TextButton backButton;
    private TextButton nextOrderButton;

    private World world;
    private int levelWidth;
    private int levelHeight;

    private Player player;
    public PlayerAttributes playerAttributes;
    private ArrayList<String> visibleQ;

    private final Popup popup;
    private Coin coin;
    private BuildingObject[] buildings;
    public int coinCounter;
    public Coin[] coins;

    private GameObject log;
    private GameObject rock;

    public Label warningLabel;
    public Label pickupLabel;
    public Label dropoffLabel;
    public Label autoDeclineLabel;

    private Timer timer;
    private TimerTask timerTask;
    private int[] timeCount;
    private int[] orderTimeLeft;

    //private TimedPopup incomingOrder;

    public GameObject pickupObject;
    public GameObject dropoffObject;

    private InputMultiplexer multiplexer;

    private final MoonshipGame GAME;
    private SpriteBatch batch;
    private Stage stage;
    private KeyProcessor keyProcessor;

    private PlayerCamera camera;

    private ShapeRenderer shapeRenderer;
    private boolean isBackButtonHovered = false;
    private boolean isOrderButtonHovered = false;
    float screenWidth;
    float screenHeight;
    private float sidePanelX;
    private float sidePanelY;
    private float sidePanelWidth;
    private float sidePanelHeight;
    private Color sidePanelColor;
    private BitmapFont font;

    /** The width/height of the virtual resolution of the screen. */
    private final int VIRTUAL_WIDTH = 480, VIRTUAL_HEIGHT = 270;

    private ArrayList<String> orderArray;
    private int count;
    public Order order;
    private String[] orderA;
    private ArrayList<Sprite> tileSprites;

    public GameplayScreen(final MoonshipGame game) throws IOException, ClassNotFoundException {
        this.GAME = game;
        batch = game.batch;

        timeCount = new int[5];
        orderTimeLeft = new int[5];
        Arrays.fill(timeCount, 0);
        Arrays.fill(orderTimeLeft, 6);

        tileSprites = new ArrayList<>();
        LevelData levelData = loadLevel();



        // TODO: Add serializable field to level data for the tilesize
        levelWidth = (levelData.width / 2) * 16;
        levelHeight = (levelData.height / 2) * 16;

        stage = new Stage();
        keyProcessor = new KeyProcessor();
        world = new World(new Vector2(0, 0), true);
        multiplexer = new InputMultiplexer();

        rock = new GameObject(world, 300, 300);
        rock.setSprite("rock.png");
        rock.sprite.setPosition(300, 300);

        log = new GameObject(world, VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2);
        log.setSprite("log.png");

        player = new Player(world, 150, 200);
        playerAttributes = new PlayerAttributes();

        camera = new PlayerCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        camera.update();

        // Define side panel properties
        sidePanelWidth = Gdx.graphics.getWidth() / 5; // Width
        sidePanelX = Gdx.graphics.getWidth() - sidePanelWidth; // Position the panel on the right side
        sidePanelY = 100; // Y position
        sidePanelHeight = Gdx.graphics.getHeight() - 110; // Height
        sidePanelColor = new Color(0.2f, 0.2f, 0.2f, 0.5f); // Background color (RGBA)

        coin = new Coin(world, 0, 0);
        coinCounter = 0;
        // making a coin array
        coins = new Coin[5];
        for (int i = 0; i < coins.length; i++) {
            int x = (int) ((Math.random() * (levelWidth - (-levelWidth))) + (-levelWidth));
            int y = (int) ((Math.random() * (levelHeight - (-levelHeight))) + (-levelHeight));
            coins[i] = new Coin(world, x, y);
            coins[i].getSprite().setPosition(x, y);
        }

        // making multiple buildings
        // TODO: change so its not a random location
        buildings = new BuildingObject[1];
        String spriteList[] = new String[3];
        spriteList[0] = "pmu.PNG";
        spriteList[1] = "haas.PNG";
        spriteList[2] = "msee.PNG";
        for (int i = 0; i < buildings.length; i++) {
            int x = (int) ((Math.random() * (levelWidth - (-levelWidth))) + (-levelWidth));
            int y = (int) ((Math.random() * (levelHeight - (-levelHeight))) + (-levelHeight));
            // most convoluted thing ever need to change
            if (x < 0) {
                x = x + 128;
            } else {
                x = x - 128;
            }
            if (y < 0) {
                y = y + 128;
            } else {
                y = y - 128;
            }
            buildings[i] = new BuildingObject(world, x, y);
            buildings[i].setSprite(spriteList[i]);
            buildings[i].sprite.setPosition(x, y);
        }

        visibleQ = new ArrayList<>();
        playerAttributes.setArray(visibleQ);
        playerAttributes.orderInProgress = false;

        order = new Order();
        orderArray = new ArrayList<>();
        order.setArray(orderArray);
        Collections.shuffle(orderArray);
        //System.out.println(orderArray);
        orderA = order.arrayToArray();
        order.seti(order.i++);
        int time = Integer.parseInt(orderA[3]);
        //int id = Integer.parseInt(orderA[0]);
        order = new Order(stage, game, orderA[0], orderA[1], orderA[2], time, orderArray);
        order.setDroppedOff(false);
        order.setPickedUp(false);
        popup = new Popup(this, order.arrayToString());
        order.setPickupBounds(-levelWidth + 50, -levelHeight + 50, 16, 16);
        order.setDropoffBounds(levelWidth - 100, levelHeight - 100, 16, 16);

//        order.setPickupBounds(10, 55, 16, 16);
//        order.setDropoffBounds(22, 102, 16, 16);

        pickupObject = new GameObject(world, order.getPickupBounds().getX(), order.getPickupBounds().getY());
        pickupObject.setSprite("borger.png");
        pickupObject.sprite.setPosition(order.getPickupBounds().getX(), order.getPickupBounds().getY());

        dropoffObject = new GameObject(world, order.getDropoffBounds().getX(), order.getDropoffBounds().getY());
        dropoffObject.setSprite("plate.png");
        dropoffObject.sprite.setPosition(order.getDropoffBounds().getX(), order.getDropoffBounds().getY());

        // Make button style
        Pixmap backgroundPixmap = createRoundedRectanglePixmap(200, 50, 10, new Color (0.9f, 0, 0.9f, 0.6f)); // Adjust size and color
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
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                isBackButtonHovered = true;
                backButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isBackButtonHovered = false;
                backButton.setColor(Color.WHITE);
            }
        });

        // Make next order button
        nextOrderButton = new TextButton("Next Order", buttonStyle);
        nextOrderButton.setPosition(Gdx.graphics.getWidth() - 220, Gdx.graphics.getHeight() - 60); // Adjust the position as necessary
        nextOrderButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    order.setArray(orderArray);
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
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                isOrderButtonHovered = true;
                nextOrderButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isOrderButtonHovered = false;
                nextOrderButton.setColor(Color.WHITE);
            }
        });

        stage.addActor(backButton);
        //stage.addActor(nextOrderButton);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(keyProcessor);
        multiplexer.addProcessor(popup.getStage());


        Gdx.input.setInputProcessor(multiplexer);

        // visual indicator boundary
        BitmapFont font = new BitmapFont(Gdx.files.internal("moonships_font.fnt"), Gdx.files.internal("moonships_font.png"), false);
        font.setColor(0, 0, 0, 1);
        font.getData().setScale(0.5f, 0.5f);
        Label.LabelStyle indicatorStyle = new Label.LabelStyle(font, Color.BLACK);
        warningLabel = new Label("Careful!", indicatorStyle);
        warningLabel.setVisible(false);
        stage.addActor(warningLabel);
        warningLabel.setSize(font.getScaleX() * 100, font.getScaleY() * 100);

        // pickup label
        pickupLabel = new Label("Press P to pickup!", indicatorStyle);
        //pickupLabel.setPosition(Gdx.graphics.getWidth() - pickupLabel.getWidth() - 550, 10);
        pickupLabel.setVisible(false);
        stage.addActor(pickupLabel);
        pickupLabel.setSize(font.getScaleX() * 16, font.getScaleY() * 16);

        // dropoff label
        dropoffLabel = new Label("Press O to dropoff!", indicatorStyle);
        //dropoffLabel.setPosition(Gdx.graphics.getWidth() - dropoffLabel.getWidth() - 550, 10);
        dropoffLabel.setVisible(false);
        stage.addActor(dropoffLabel);
        dropoffLabel.setSize(font.getScaleX() * 16, font.getScaleY() * 16);

        //auto decline after order timeout label
        autoDeclineLabel = new Label("Order Timeout! Declined.", indicatorStyle);
        autoDeclineLabel.setPosition(Gdx.graphics.getWidth() - autoDeclineLabel.getWidth(), 10);
        autoDeclineLabel.setVisible(false);
        stage.addActor(autoDeclineLabel);
        autoDeclineLabel.setSize(font.getScaleX() * 16, font.getScaleY() * 16);

        schedulePopupDisplay();

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
        //if (!popup.isVisible()) {
            player.update(delta, keyProcessor);
            player.checkBounds(levelWidth, levelHeight);
            world.step(1/60f, 6, 2); // Physics calculations

            camera.follow(player.position, levelWidth, levelHeight);

            // screen boundary collisions
            Rectangle playerBounds = player.sprite.getBoundingRectangle();
            float threshold = 50;

            // visual indicator that the player is almost off the screen
//            if (!pickupLabel.isVisible() && !dropoffLabel.isVisible()) {
                if (playerBounds.getX() <= -(levelWidth + threshold)) {
                    warningLabel.setPosition(levelWidth - (levelWidth - warningLabel.getWidth()), levelHeight / 2f);
                    warningLabel.setVisible(true);
                } else if (playerBounds.getY() >= (levelWidth - threshold)) {
                    warningLabel.setPosition((levelWidth - (3 * warningLabel.getWidth())), levelHeight / 2f);
                    warningLabel.setVisible(true);
                } else if ((playerBounds.getX() + player.getWidth()) <= (-levelHeight + threshold)) {
                    warningLabel.setPosition(levelWidth / 2f, levelHeight - (levelHeight - warningLabel.getHeight()));
                    warningLabel.setVisible(true);
                } else if ((playerBounds.getY() + player.getHeight()) >= (levelHeight - threshold)) {
                    warningLabel.setPosition(levelWidth / 2f, levelHeight - warningLabel.getHeight());
                    warningLabel.setVisible(true);
                } else {
                    // remove the label
                    warningLabel.setVisible(false);
                }
//            }
       // }
        batch.setProjectionMatrix(camera.combined);
        //popup.update(delta);

        stage.act(delta);

        /* ========================== DRAW ============================ */

        ScreenUtils.clear(Color.PINK);

        /* ===== Draw game objects ===== */
        batch.begin();

        for (Sprite sprite : tileSprites) {
            sprite.draw(batch);
        }


        player.render(batch);
        rock.render(batch, 20, -100);
        log.render(batch, 0, 10);

        // coin collision
        for (Coin coin : coins) {
            if (!coin.collected) {
                coin.render(batch, (int) coin.getSprite().getX(), (int) coin.getSprite().getY());
                if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), coin.getSprite().getBoundingRectangle())) {
                    coin.setCollected(true);
                    coinCounter++;
                }
            }
        }

        for (BuildingObject building : buildings) {
            building.render(batch, (int) building.sprite.getX(), (int) building.sprite.getY());
            Rectangle bounds = building.getBounds();
            bounds.setHeight(bounds.getHeight() / 2);
            if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), bounds)) {
                float playerTop = player.body.getPosition().y + player.sprite.getHeight();
                float buildingBottom = building.getBounds().y;
//                if (playerBottom + player.body.getLinearVelocity().y < buildingTop && playerBottom > buildingTop) {
//                    // Bounce off only if the player is moving downward
////                    if (player.body.getLinearVelocity().y < 0) {
//                        // Reverse the player's vertical velocity
//                        player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y * -1);
////                    }
//                    }

                int bounceThreshold = 0;
                if (buildingBottom < 0) {
                    bounceThreshold = 8;
                } else {
                    bounceThreshold = -8;
                }
                if ((playerTop > buildingBottom) && (playerTop < (buildingBottom + bounceThreshold))) {
                    System.out.println("yes");
                    player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y * -1);
                }
//                player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y * -1);
//                if ((player.getSprite().getX() >= (bounds.getX() - bounds.getWidth()) && player.getSprite().getX() <= bounds.getX())) {
//                    player.body.setLinearVelocity(player.body.getLinearVelocity().x * -1, player.body.getLinearVelocity().y);
//                }
                building.setTransparent(true);
            } else {
                building.setTransparent(false);
            }
        }


//        Rectangle rockBounds = rock.sprite.getBoundingRectangle();
////        float rockBottom = rock.sprite.getY();
//        Rectangle agg = new Rectangle(rock.sprite.getX(), rock.sprite.getY(), rock.sprite.getWidth(), rock.sprite.getHeight() / 2);
////        if ((player.getSprite().getX() >= rock.sprite.getX() - rock.sprite.getWidth()) && (player.getSprite().getX()) <= (rock.sprite.getX())) {
//////        if ((player.body.getPosition().x >= rockBounds.getX()) && ((player.body.getPosition().x) <= (rockBounds.getX() + rockBounds.getWidth()))) {
////            // if the player is within the width of the rock
////            if ((player.body.getPosition().y + player.getSprite().getHeight() >= rockBottom) && (player.body.getPosition().y <= rockBottom + 8 )) {
////                // if the player is trying to intersect from the bottom, bounce
////                player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y * -1);
////            }
////        }
//        // rock transparency and collisions (testing for buildings)
//        if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), agg)) {
//            player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y * -1);
//            if ((player.getSprite().getX() >= (agg.getX() - agg.getWidth()) && player.getSprite().getX() <= agg.getX())) {
//                player.body.setLinearVelocity(player.body.getLinearVelocity().x * -1, player.body.getLinearVelocity().y);
//            }
//            rock.sprite.setAlpha(0.4f);
//        } else {
//            rock.sprite.setAlpha(1);
//        }


        if (playerAttributes.orderInProgress) {
            for (int i = 1; i < playerAttributes.array.size(); i++ ) {
                String[] s = order.stringToArray(playerAttributes.array.get(i));
                int time;
                boolean twoName = false;
                try {
                    time = Integer.parseInt(s[4]);
                } catch (NumberFormatException num) {
                    time = Integer.parseInt(s[5]);
                    twoName = true;
                }
                if (time <= 0) {
                    if (i == 1) {
                        if (!order.isDroppedOff()) {
                            order.setDroppedOff(true);
                            dropoffLabel.setVisible(false);
                            pickupLabel.setVisible(false);
                            order.setPickedUp(false);
                        }
                        if (playerAttributes.array.size() <= 1) {
                            playerAttributes.orderInProgress = false;
                            order.setPickedUp(false);
                            dropoffLabel.setVisible(false);
                            pickupLabel.setVisible(false);
                        } else {
                            //System.out.println("hehe i am here");
                            pickupObject.sprite.draw(batch);
                            order.setDroppedOff(false);
                            order.setPickedUp(false);
                        }
                    }
                    playerAttributes.array.remove(i);
                } else {
                    if (timeCount[i - 1] % 40 == 0) {
                        time -= 1;
                        orderTimeLeft[i - 1] = time;
                    }
                    timeCount[i - 1]++;

                    if (!twoName) {
                        s[4] = String.valueOf(time);
                    } else {
                        s[5] = String.valueOf(time);
                    }

                    StringBuilder sb = new StringBuilder();
                    for (String thing : s) {
                        sb.append(thing);
                        sb.append(" ");
                    }
                    playerAttributes.array.set(i, sb.toString());
                }
            }
        }

        if (playerAttributes.array.size() > 1) {
            if (!order.isPickedUp()) {
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
            } else if (!order.isDroppedOff()) {
                dropoffObject.sprite.draw(batch);
                if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), order.getDropoffBounds())) {
                    dropoffLabel.setVisible(true);
                    if (keyProcessor.oPressed) {
                        order.setPickedUp(false);
                        order.setDroppedOff(true);
                        playerAttributes.array.remove(1);
                        if (playerAttributes.array.size() <= 1) {
                            playerAttributes.orderInProgress = false;
                        }
                        dropoffLabel.setVisible(false);
                    }
                } else {
                    dropoffLabel.setVisible(false);
                }
            }
        }

        batch.end();

        /* Draw UI elements */
        batch.begin();

        // Render the side panel
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(sidePanelColor);
        shapeRenderer.rect(sidePanelX, sidePanelY, sidePanelWidth, sidePanelHeight);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        String[] items = playerAttributes.array.toArray(new String[0]);

        font.setColor(Color.WHITE);
        font.draw(batch, "Order List:", sidePanelX + 10, sidePanelY + sidePanelHeight - 10);

        for (int i = 1; i < items.length; i++) {
            if (orderTimeLeft[i - 1] <= 5 && orderTimeLeft[i - 1] > 0) {
                //if (order.isPickedUp()) {
                    font.setColor(Color.RED);
               // }
            } else {
                font.setColor(Color.WHITE);
            }
            font.draw(batch, items[i], sidePanelX + 10, sidePanelY + sidePanelHeight - 70 * i);

        }
        stage.draw();
        popup.render();
        //popup.draw();

        batch.end();
    }

    // Trigger the timed popup to show
    public void showTimedPopup() {
        //popup.setPosition(Gdx.graphics.getWidth() - 300, 0);
        popup.show(); // Display the popup
        try {
            order.setArray(orderArray);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        order.seti(count);
        count++;
        popup.setMessage(order.arrayToString());
        popup.acceptClicked = false;
        popup.declineClicked = false;
    }

    public void hideTimedPopup() {
        popup.hide(); // Display the popup
    }

    // Schedule the popup to display every 1 minute
    private void schedulePopupDisplay() {
        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                showTimedPopup(); // Show the popup
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the popup
                        hideTimedPopup();
                        System.out.println("accepted?"+popup.acceptClicked());
                        System.out.println("declined?"+popup.declineClicked());
                        if (!popup.acceptClicked() && !popup.declineClicked()) {
                            autoDeclineLabel.setVisible(true);
                            scheduler.schedule(new Runnable() {
                                @Override
                                public void run() {
                                    autoDeclineLabel.setVisible(false); // Remove the label from the display
                                }
                            }, 4, TimeUnit.SECONDS);
                        }
                    }
                }, 10, TimeUnit.SECONDS); // Schedule to hide the popup after 10 seconds
            }
        }, 0, 15, TimeUnit.SECONDS); // Schedule the next popup 15 seconds after the first one
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
        //incomingOrder.dispose();
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
        pixmap.setBlending(Pixmap.Blending.None); // Makes it so elements of the pixmap don't overlap if transparency is on

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

    public LevelData loadLevel() throws IOException, ClassNotFoundException {
        InputStream fileStream = Files.newInputStream(new File(Gdx.files.internal("levels/level3.lvl").path()).toPath());
        ObjectInputStream inputStream = new ObjectInputStream(fileStream);

        LevelData levelData = (LevelData) inputStream.readObject();

        AssetManager manager = new AssetManager();

        // If the level data is properly deserialized
        if (levelData != null) {
            for (int i = 0; i < levelData.width; i++) {
                for (int j = 0; j < levelData.height; j++) {
                    int index = levelData.layers[0][i][j].getIndex();
                    if (index != 0) {
                        Sprite tileSprite = new Sprite(manager.getRegion(index));
                        tileSprite.setPosition((i - 15) * tileSprite.getWidth(), (15 - j) * tileSprite.getHeight());
                        tileSprites.add(tileSprite);
                    }
                }
            }
            System.out.println("level name: " + levelData.levelName);
        } else {
            System.err.println("Unable to load level.");
        }

        return levelData;
    }
}
