package com.campee.starship;

import Serial.LevelData;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.*;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameplayScreen extends ApplicationAdapter implements Screen {

    private TextButton backButton;
    private TextButton nextOrderButton;
    private TextButton gameStatsButton;

    private World world;
    private int levelWidth;
    private int levelHeight;
    public int minOrders;
    public float goalTime;

    public boolean win;
    public boolean keepPlaying = true;
    public boolean popupInAction = false;


    private Player player;
    public PlayerAttributes playerAttributes;
    private ArrayList<String> visibleQ;
    private ArrayList<String> deliveredOrderIDs;
    private ArrayList<String> outOfTimeOrdersIDs;

    private final Popup popup;
    private final GamePopup gamepopup;
    private final KeepPlayingPopup keepplayingpopup;
    private Coin coin;
    private BuildingObject[] buildings;
    public int coinCounter;
    public Coin[] coins;
    private boolean visibleText;

    private int totalOrdersCompleted;


    private GameObject log;
    private GameObject rock;

    public Label warningLabel;
    public Label pickupLabel;
    public Label dropoffLabel;
    public Label minOrderLabel;
    public Label autoDeclineLabel;
    public Label orderTimeoutLabel;
    public Label mainTimer;
    public Label lowTimer;

    private Timer timer;
    private TimerTask timerTask;
    private int[] timeCount;
    private int[] orderTimeLeft;
    float messageTimer = 0.0f;
    final float MESSAGE_DURATION = 3.0f;

    // Declare variables for the countdown timer
    int countdownMinutes = 3; // 2 minutes
    int countdownSeconds = 0;
    private Timer countdownTimer = new Timer();


    // Create a TimerTask to decrement the countdown timer
    private TimerTask countdownTask = new TimerTask() {
        @Override
        public void run() {
            if (!popupInAction) {
                if (countdownSeconds > 0) {
                    countdownSeconds--;
                } else {
                    if (countdownMinutes > 0) {
                        countdownMinutes--;
                        countdownSeconds = 59;
                    } else {
                        this.cancel(); // Stop the timer
                    }
                }
            }
        }
    };




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
    //final String gameStatsMessage;

    /**
     * The width/height of the virtual resolution of the screen.
     */
    private final int VIRTUAL_WIDTH = 480, VIRTUAL_HEIGHT = 270;

    private ArrayList<String> orderArray;
    private int count;
    public Order order;
    private String[] orderA;
    private ArrayList<Tile[]> layers;
    private AssetManager assetManager;

    public GameplayScreen(final MoonshipGame game, String fileName) throws IOException, ClassNotFoundException {
        this.GAME = game;
        batch = game.batch;
        visibleText = true;
        assetManager = new AssetManager();
        world = new World(new Vector2(0, 0), true);
        multiplexer = new InputMultiplexer();

        player = new Player(world, 150, 200);
        playerAttributes = new PlayerAttributes();

        camera = new PlayerCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        camera.update();

//        stage = new Stage(new ExtendViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera));
        stage = new Stage();
        keyProcessor = new KeyProcessor(this);

//        Table uiTable = new Table();
//        uiTable.setFillParent(true);
//        stage.addActor(uiTable);

        // Create a Timer object to schedule the TimerTask
        countdownTimer.scheduleAtFixedRate(countdownTask, 500, 500);

        timeCount = new int[5];
        orderTimeLeft = new int[5];
        Arrays.fill(timeCount, 0);
        Arrays.fill(orderTimeLeft, 6);
        //gameStatsMessage = "";

        win = false;

        layers = new ArrayList<>();
        LevelData levelData = loadLevel(fileName);

        minOrders = 2/*levelData.minOrders*/;
        goalTime = 300/*levelData.goalTime*/;

        rock = new GameObject(world, 300, 300);
        rock.setSprite("rock.png");
        rock.sprite.setPosition(300, 300);

        log = new GameObject(world, VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2);
        log.setSprite("log.png");

        // Define side panel properties
        sidePanelWidth = Gdx.graphics.getWidth() / 5; // Width
        sidePanelX = Gdx.graphics.getWidth() - sidePanelWidth; // Position the panel on the right side
        sidePanelY = 100; // Y position
        sidePanelHeight = Gdx.graphics.getHeight() - 110; // Height
        sidePanelColor = new Color(0.2f, 0.2f, 0.2f, 0.5f); // Background color (RGBA)

        coin = new Coin(world, 0, 0);
        coinCounter = 0;

        // making a coin array
        coins = new Coin[20];
        for (int i = 0; i < coins.length; i++) {
            Random random = new Random();
            int x = (random.nextInt((levelWidth - 16)+ 1));
            int y = (random.nextInt((levelHeight - 16) + 1));
            x = (x / 16) * 16;
            y = (y / 16) * 16;
            coins[i] = new Coin(world, x, y);
            coins[i].getSprite().setPosition(x, y);
        }

        // making multiple buildings
        // TODO: change so its not a random location
        buildings = new BuildingObject[3];
        String[] spriteList = new String[3];
        spriteList[0] = "PMU.PNG";
        spriteList[1] = "HAAS.PNG";
        spriteList[2] = "MSEE.PNG";
        for (int i = 0; i < buildings.length; i++) {
            Random random = new Random();
            int x = (random.nextInt((levelWidth - 128)+ 1));
            int y = (random.nextInt((levelHeight - 128) + 1));
            x = (x / 16) * 16;
            y = (y / 16) * 16;
            buildings[i] = new BuildingObject(world, x, y);
//            for (int k = i; k < buildings.length - i; k++) {
//                while (Intersector.overlaps(buildings[i].getBounds(), buildings[k].getBounds())) {
//                    x = (random.nextInt((levelWidth - 128)+ 1));
//                    y = (random.nextInt((levelHeight - 128) + 1));
//                    x = (x / 16) * 16;
//                    y = (y / 16) * 16;
//                    buildings[i] = new BuildingObject(world, x, y);
//                }
//            }
            buildings[i].setSprite(spriteList[i]);
            String sprite = spriteList[i];
            buildings[i].setName(sprite.substring(0, (sprite.length() - 4)));
            System.out.println(buildings[i].getName());
            buildings[i].sprite.setPosition(x, y);
        }

        visibleQ = new ArrayList<>();
        playerAttributes.setArray(visibleQ);
        playerAttributes.orderInProgress = false;
        deliveredOrderIDs = new ArrayList<>();
        outOfTimeOrdersIDs = new ArrayList<>();

        totalOrdersCompleted = 0;
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

        gamepopup = new GamePopup(this, "", game, fileName);
        keepplayingpopup = new KeepPlayingPopup(this, "", game, fileName);

        pickupObject = new GameObject(world, order.getPickupBounds().getX(), order.getPickupBounds().getY());
        pickupObject.setSprite("borger.png");
        pickupObject.sprite.setPosition(order.getPickupBounds().getX(), order.getPickupBounds().getY());

        dropoffObject = new GameObject(world, order.getDropoffBounds().getX(), order.getDropoffBounds().getY());
        dropoffObject.setSprite("plate.png");
        dropoffObject.sprite.setPosition(order.getDropoffBounds().getX(), order.getDropoffBounds().getY());

        // Make button style
        Pixmap backgroundPixmap = createRoundedRectanglePixmap(200, 50, 10, new Color(0.9f, 0, 0.9f, 0.6f)); // Adjust size and color
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

        //Make game stats button
        gameStatsButton = new TextButton("Game Stats", buttonStyle);
        gameStatsButton.setPosition(Gdx.graphics.getWidth() - 600, 10);
        gameStatsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                visibleText = false;
                StringBuilder orderIDsStringBuilder = new StringBuilder("Successfully Delivered:\n");
                for (String orderID : deliveredOrderIDs) {
                    orderIDsStringBuilder.append(orderID).append("\n");
                }
                String orderIDsMessage = orderIDsStringBuilder.toString();

                StringBuilder timeOrderIDsStringBuilder = new StringBuilder("Out of Time Orders:\n");
                for (String orderID : outOfTimeOrdersIDs) {
                    timeOrderIDsStringBuilder.append(orderID).append("\n");
                }
                String notInTimeorderIDsMessage = timeOrderIDsStringBuilder.toString();

                String gameStatsMessage = "GAME OVER! \nTotal Coins Collected: " + coinCounter
                        + "\nTotal Orders Completed: " + totalOrdersCompleted;

                gamepopup.showGameStatsMessage(gameStatsMessage);
                gamepopup.showOrderCompletedList(orderIDsMessage);
                gamepopup.showOutoffTimeList(notInTimeorderIDsMessage);
                gamepopup.show();
                gamepopup.render();
                multiplexer.addProcessor(gamepopup.getStage());
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
        //stage.addActor(gameStatsButton);
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
        Label.LabelStyle warningStyle = new Label.LabelStyle(font, Color.MAROON);
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

        // min orders label
        minOrderLabel = new Label("Orders Completed: " + playerAttributes.ordersCompleted + "/" + minOrders, indicatorStyle);
        minOrderLabel.setSize(font.getScaleX() * 16, font.getScaleY() * 16);
        //minOrderLabel.setPosition(Gdx.graphics.getWidth() / 2 - minOrderLabel.getWidth() / 2, Gdx.graphics.getHeight() - minOrderLabel.getHeight());
        minOrderLabel.setPosition(Gdx.graphics.getWidth() / 2 - 120, Gdx.graphics.getHeight() - minOrderLabel.getHeight() - 17);
        minOrderLabel.setVisible(true);
        stage.addActor(minOrderLabel);
        //minOrderLabel.setText("Orders Completed: "+playerAttributes.ordersCompleted+"/"+minOrders);

        orderTimeoutLabel = new Label("Time ran out. Begin next delivery!", warningStyle);
        orderTimeoutLabel.setPosition(Gdx.graphics.getWidth() / 2 - 180, Gdx.graphics.getHeight() - minOrderLabel.getHeight() - 52);
        orderTimeoutLabel.setVisible(false);
        stage.addActor(orderTimeoutLabel);
        orderTimeoutLabel.setSize(font.getScaleX() * 16, font.getScaleY() * 16);

        String str = "Time remaining:\n" + "     " + String.valueOf(countdownMinutes) + ":" + String.valueOf(countdownSeconds);
        mainTimer = new Label(str, indicatorStyle);
        mainTimer.setPosition(25,Gdx.graphics.getHeight() - 90);
        mainTimer.setVisible(true);
        stage.addActor(mainTimer);
        mainTimer.setSize(font.getScaleX() * 16, font.getScaleY() * 16);

        //String str = "Time remaining:\n" + "     " + String.valueOf(countdownMinutes) + ":" + String.valueOf(countdownSeconds);
        lowTimer = new Label(str, warningStyle);
        lowTimer.setPosition(25,Gdx.graphics.getHeight() - 90);
        lowTimer.setVisible(true);
        stage.addActor(lowTimer);
        lowTimer.setSize(font.getScaleX() * 16, font.getScaleY() * 16);



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
        font = new BitmapFont(Gdx.files.internal("moonships_font.fnt"), Gdx.files.internal("moonships_font.png"), false);
        ; // Define your BitmapFont
        // Set font color and scale
        font.setColor(1, 1, 0, 1);
        font.getData().setScale(0.5f);
    }

    @Override
    public void render(float delta) {
        /* ========================== UPDATE ============================ */

        //If game stats screen is not visible, keep the game going (else pause)
        //if (!gameStatsScreen.isVisible()) {
        if (!gamepopup.isVisible()) {
            player.update(delta, keyProcessor);
            player.checkBounds(levelWidth, levelHeight);
            world.step(1 / 60f, 6, 2); // Physics calculations

            camera.follow(player.position, levelWidth, levelHeight);

        batch.setProjectionMatrix(camera.combined);
        //popup.update(delta);

        stage.act(delta);

        /* ========================== DRAW ============================ */

        ScreenUtils.clear(Color.PINK);

        /* ===== Draw game objects ===== */
        batch.begin();

        for (Tile[] layer : layers) {
            for (Tile tile : layer) {
                tile.draw(batch);
            }
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
        // Inside the render metho
        String sec;
        if (countdownSeconds < 10) {
            sec = "0" + String.valueOf(countdownSeconds);
        } else {
            sec = String.valueOf(countdownSeconds);
        }
        String str = "Time remaining:\n" + "     " + String.valueOf(countdownMinutes) + ":" + sec;
        if (countdownMinutes >= 1) {
            mainTimer.setText(str);
            mainTimer.setVisible(true);
            lowTimer.setVisible(false);
        } else {
            lowTimer.setText(str);
            lowTimer.setVisible(true);
            mainTimer.setVisible(false);
        }


        // building collisions and transparency
        for (BuildingObject building : buildings) {
            building.render(batch, (int) building.sprite.getX(), (int) building.sprite.getY());
            if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), building.getBounds())) {
                building.setTransparent(true);
                Rectangle collisionBounds = new Rectangle(building.getBounds());
                collisionBounds.setHeight(collisionBounds.getHeight() / 2);
                if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), collisionBounds)) {
                    if (Math.abs(player.body.getLinearVelocity().x) > 0) {
                        player.body.setLinearVelocity(player.body.getLinearVelocity().x * -1, player.body.getLinearVelocity().y);
                    }
                    if (Math.abs(player.body.getLinearVelocity().y) > 0) {
                        player.body.setLinearVelocity(player.body.getLinearVelocity().x, player.body.getLinearVelocity().y * -1);
                    }
                }
            } else {
                building.setTransparent(false);
            }
        }


            // set all buildings to not pickup/dropoff if no queued orders
            if (playerAttributes.array.size() <= 1) {
                for (BuildingObject building : buildings) {
                    building.setDropoffLocation(false);
                    building.setPickupLocation(false);
                }
            }

            //  building dropoff and pickup pin stuff
            if (playerAttributes.orderInProgress) {
                for (int i = 1; i < playerAttributes.array.size(); i++) {
                    String[] s = order.stringToArray(playerAttributes.array.get(1));
                    String str3 = s[3];
                    String str2 = s[2];
                    String currDrop = str3.substring(0, str3.length() - 6);
                    String currPick = str2.substring(0, str2.length() - 3);
                    for (BuildingObject building : buildings) {
                        // assign the building to the correct order
                        if (building.getName().equals(currPick) && !order.isPickedUp()) {
                            building.setPickupLocation(true);
                            order.setPickupBounds(building.getBounds().getX(), building.getBounds().getY(), building.getWidth(), building.getHeight());
                        } else {
                            building.setPickupLocation(false);
                        }
                        if (building.getName().equals(currDrop) && !order.isDroppedOff() && order.isPickedUp()) {
                            building.setDropoffLocation(true);
                            order.setDropoffBounds(building.getBounds().getX(), building.getBounds().getY(), building.getWidth(), building.getHeight());
                        } else {
                            building.setDropoffLocation(false);
                        }
                    }
                    if (!order.isPickedUp()) {
                        if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), order.getPickupBounds())) {
                            pickupLabel.setPosition(16, 16);
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
                        if (Intersector.overlaps(player.getSprite().getBoundingRectangle(), order.getDropoffBounds())) {
                            dropoffLabel.setPosition(16, 16);
                            dropoffLabel.setVisible(true);
                            if (keyProcessor.oPressed) {
                                order.setPickedUp(false);
                                order.setDroppedOff(true);
                                playerAttributes.ordersCompleted++;
                                minOrderLabel.setText("Orders Completed: "+playerAttributes.ordersCompleted+"/"+minOrders);
//                                playerAttributes.array.remove(1);
//                                if (playerAttributes.array.size() <= 1) {
//                                    playerAttributes.orderInProgress = false;
//                                }
                                playerAttributes.array.remove(1);
                                totalOrdersCompleted++;
                                String orderID = order.getOrderString();
                                System.out.println("order id: " + orderID);
                                //if (!deliveredOrderIDs.contains(orderID)) {
                                    deliveredOrderIDs.add(orderID);
                                    System.out.println("Order " + orderID + " has been delivered and added to the list.");
                                //}
                                System.out.println("Order List: " + deliveredOrderIDs);
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
            }  else {
                // set everything to false
                order.setPickedUp(false);
                order.setDroppedOff(false);
                for (BuildingObject building : buildings) {
                    building.setDropoffLocation(false);
                    building.setPickupLocation(false);
                }
            }



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
                            order.setDroppedOff(false);
                            order.setPickedUp(false);
                        }
                    }
                    String orderID = s[1];
                    orderID = orderID.substring(0, orderID.length() - 3);
                    outOfTimeOrdersIDs.add(orderID);
                    orderTimeoutLabel.setVisible(true);
                    playerAttributes.array.remove(i);
                    messageTimer = 0.0f;
                } else {
                    if (timeCount[i - 1] % 60 == 0) {
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
        if (orderTimeoutLabel.isVisible()) {
            messageTimer += delta;
            if (messageTimer >= MESSAGE_DURATION) {
                orderTimeoutLabel.setVisible(false);
            }
        }


//        if (playerAttributes.ordersCompleted == minOrders){
//            //show game stats screen, pause game as part of this (if condition above)
//            System.out.println("Level completed!");
//        }

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

            //if (visibleText) {
            font.setColor(Color.WHITE);
            font.draw(batch, "Order List:", sidePanelX + 10, sidePanelY + sidePanelHeight - 10);
            float coinCountTextX = Gdx.graphics.getWidth() / 2 - 90/*sidePanelX - font.getRegion().getRegionWidth() - 110*/;
            float coinCountTextY = Gdx.graphics.getHeight() - minOrderLabel.getHeight() - 25/*sidePanelY - 20*/;
            font.draw(batch, "Coins Collected: " + coinCounter, coinCountTextX, coinCountTextY);
            //System.out.println("orders completed:"+playerAttributes.getOrdersCompleted());


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
            //}

            stage.draw();
            popup.render();
            gamepopup.render();
            //popup.draw();
            batch.end();
        }
        boolean timeLeft = true;
        if (countdownSeconds == 0 && countdownMinutes == 0) {
            if (playerAttributes.orderInProgress) {
                // go through all orders and add timed out orders to game stats
                for (int i = 1; i < playerAttributes.array.size(); i++ ) {
                    String[] s = order.stringToArray(playerAttributes.array.get(i));
                    String orderID = s[1];
                    orderID = orderID.substring(0, orderID.length() - 3);
                    if (!outOfTimeOrdersIDs.contains(orderID)) {
                        outOfTimeOrdersIDs.add(orderID);
                    }
                }
            }
            timeLeft = false;
            if (playerAttributes.ordersCompleted >= minOrders) {
                win = true;
            } else {
                win = false;
            }
            showGameResult();
        }
        if (playerAttributes.ordersCompleted >= minOrders) {
            win = true;
            if (!timeLeft) {
                showGameResult();
            }
            if (playerAttributes.ordersCompleted == minOrders && keepPlaying) {
                keepPlayingPopup();
                popupInAction = true;
            }
        }
    }

    //show game stats screen
    public void showGameResult() {
        visibleText = false;
        StringBuilder orderIDsStringBuilder = new StringBuilder("Successfully Delivered:\n");
        for (String orderID : deliveredOrderIDs) {
            orderIDsStringBuilder.append(orderID).append("\n");
        }
        String orderIDsMessage = orderIDsStringBuilder.toString();
        StringBuilder timeOrderIDsStringBuilder = new StringBuilder("Out of Time Orders:\n");
        for (String orderID : outOfTimeOrdersIDs) {
            timeOrderIDsStringBuilder.append(orderID).append("\n");
        }
        String notInTimeorderIDsMessage = timeOrderIDsStringBuilder.toString();

        String levelResult;
        if (win) {
            levelResult = "Congrats, level completed!";
        } else {
            levelResult = "Game Over! :(";
        }

        String gameStatsMessage = "GAME STATS: \nTotal Coins Collected: " + coinCounter
                + "\nTotal Orders Completed: " + totalOrdersCompleted;
        gamepopup.showGameStatsMessage(gameStatsMessage);
        gamepopup.showOrderCompletedList(orderIDsMessage);
        gamepopup.showOutoffTimeList(notInTimeorderIDsMessage);
        gamepopup.showLevelResultMessage(levelResult);
        gamepopup.show();
        gamepopup.render();
        multiplexer.addProcessor(gamepopup.getStage());
        //System.out.println("Level completed!");
    } //render

    // keep playing pop up
    public void keepPlayingPopup() {
        visibleText = false;
        String message = "Do you want to keep playing or end the game?";
        keepplayingpopup.setMessageLabel(message);
        keepplayingpopup.show();
        keepplayingpopup.render();
        multiplexer.addProcessor(keepplayingpopup.getStage());
        //System.out.println("Level completed!");
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
                if (!popupInAction) {
                    showTimedPopup(); // Show the popup
                    scheduler.schedule(new Runnable() {
                        @Override
                        public void run() {
                            if (!popupInAction) {
                                // Hide the popup
                                hideTimedPopup();
                                if (!popup.acceptClicked() && !popup.declineClicked()) {
                                    autoDeclineLabel.setVisible(true);
                                    scheduler.schedule(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!popupInAction) {
                                                autoDeclineLabel.setVisible(false); // Remove the label from the display
                                            }
                                        }
                                    }, 4, TimeUnit.SECONDS);
                                }
                            }

                        }
                    }, 10, TimeUnit.SECONDS); // Schedule to hide the popup after 10 seconds
                }
            }
        }, 0, 15, TimeUnit.SECONDS); // Schedule the next popup 15 seconds after the first one
    }





    @Override
    public void resize(int width, int height) {
        if (camera != null) camera.setToOrtho(false, width, height);

        stage.getViewport().update(width, height, true);
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

    private LevelData loadLevel(String fileName) throws IOException, ClassNotFoundException {
        InputStream fileStream = Files.newInputStream(new File(Gdx.files.internal("levels/" + fileName + ".lvl").path()).toPath());
        ObjectInputStream inputStream = new ObjectInputStream(fileStream);

        LevelData levelData = (LevelData) inputStream.readObject();

        if (levelData == null) return null;

        System.out.println(levelData.levelName);
        System.out.println(levelData.tileSize);
        System.out.println(levelData.difficulty);
        System.out.println(levelData.width);
        System.out.println(levelData.height);

        levelWidth = levelData.width * levelData.tileSize;
        levelHeight = levelData.height * levelData.tileSize;

        for (int layerNum = 0; layerNum < levelData.tileData.size(); layerNum++) {
            LevelData.TileData[] tileData = levelData.tileData.get(layerNum);

            // TODO: spawn in the objects
            LevelData.ObjectData[] objectData = levelData.objectData.get(layerNum);

            Tile[] tiles = new Tile[tileData.length];

            for (int i = 0; i < tileData.length; i++) {
                System.out.println("tileset: " + tileData[i].tilesetID);
                tiles[i] = new Tile(
                        assetManager.getTileSprite(tileData[i].tilesetID, tileData[i].spriteIndex),
                        tileData[i].x * levelData.tileSize,
                        levelHeight - tileData[i].y * levelData.tileSize
                );
            }

            layers.add(tiles);

        }

        // tileSprite.setPosition((i - 15) * tileSprite.getWidth(), (15 - j) * tileSprite.getHeight());


        return levelData;
    }
}