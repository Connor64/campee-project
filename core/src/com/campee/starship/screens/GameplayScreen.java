package com.campee.starship.screens;

import Serial.LevelData;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.campee.starship.userinterface.TutorialPopups;
import com.campee.starship.objects.*;
import com.campee.starship.managers.*;
import com.campee.starship.userinterface.*;

import java.util.*;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameplayScreen extends ApplicationAdapter implements Screen {

    private TextButton backButton;
    public TextButton doneButton;
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
    public boolean ordersDone = false;

    //pause screen
    private TextButton pauseButton;
    //public boolean resume = true;
    private boolean isPauseButtonHovered = false;
    public boolean gamePaused = false;
    public boolean outOfOrders = false;


    private Player player;
    public PlayerAttributes playerAttributes;
    private ArrayList<String> visibleQ;
    private ArrayList<String> deliveredOrderIDs;
    private ArrayList<String> outOfTimeOrdersIDs;

    private final Popup popup;
    private final GamePopup gamepopup;
    public TutorialPopups tutorialPopups;
    public boolean isTutorialPopupsVisible;
    private ArrayList<BuildingObject> buildings;
    public ArrayList<CoinObject> coins;
    private final KeepPlayingPopup keepplayingpopup;
    private final PauseScreen pauseScreen;
    public int coinCounter;
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
    public Label coinCollectLabel;
    public Label mainTimer;
    public Label lowTimer;

    private Timer timer;
    private TimerTask timerTask;
    private int[] timeCount;
    private int[] orderTimeLeft;
    float messageTimer = 0.0f;
    final float MESSAGE_DURATION = 3.0f;

    // Declare variables for the countdown timer
    public int countdownMinutes = 3; // 2 minutes
    public int countdownSeconds = 0;
    private Timer countdownTimer = new Timer();

    // music and sound
    Music gameplayMusic;
    Music fastMusic;

    Sound coinCollect;
    Skin skin;
    Slider musicSlider;
    Slider soundSlider;
    long soundId;
    //private boolean coinCollectPlayed;
    //boolean soundPlayed;
    Sound dropoffSuccess;
    Sound pie;
    Sound pickupSuccess;
    Sound levelWin;
    Sound levelFail;
    Sound newOrderNotif;
    int hide_popup;


    // Create a TimerTask to decrement the countdown timer
    private TimerTask countdownTask = new TimerTask() {
        @Override
        public void run() {
            if (!popupInAction && !gamePaused && !isTutorialPopupsVisible) {
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
    private float timeSinceLastMove;

    private PlayerCamera camera;

    private ShapeRenderer shapeRenderer;
    private boolean isBackButtonHovered = false;
    private boolean isDoneButtonHovered = false;
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
        world = new World(new Vector2(0, 0), true);
        multiplexer = new InputMultiplexer();

        countdownMinutes = getCountdownMinutes(fileName);
        countdownSeconds = getCountdownSeconds(fileName);

        player = new Player(world, 150, 200);
        playerAttributes = new PlayerAttributes();

        camera = new PlayerCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        camera.update();

        stage = new Stage();
        keyProcessor = new KeyProcessor(this);

        // Create a Timer object to schedule the TimerTask
        countdownTimer.scheduleAtFixedRate(countdownTask, 500, 500);

        timeCount = new int[5];
        orderTimeLeft = new int[5];
        Arrays.fill(timeCount, 0);
        Arrays.fill(orderTimeLeft, 6);
        //gameStatsMessage = "";

        win = false;

        layers = new ArrayList<>();
        loadLevel(fileName);
//        System.out.println(fileName);

        //Level information

        if ("level_6".equals(fileName)) {
            GameDifficulty.easy = true;
            //System.out.println("easy");
        }

        if ("level_5".equals(fileName)) {
            GameDifficulty.medium = true;
            //System.out.println("medium");
        }
        if ("level_4".equals(fileName)) {
            GameDifficulty.hard = true;
            //System.out.println("hard");
        }

        if (GameDifficulty.tutorial) {
            minOrders = 2   /*levelData.minOrders*/;
            goalTime = 300 /*levelData.goalTime*/;
            countdownMinutes = 15;
        }

        if (GameDifficulty.easy) {
            minOrders = 2;
            goalTime = 400;
            countdownMinutes = 3;
        }
        if (GameDifficulty.medium) {
            minOrders = 3;
            goalTime = 500;
            countdownMinutes = 4;
        }
        if (GameDifficulty.hard) {
            minOrders = 5;
            goalTime = 300;
            countdownMinutes = 6;
        }

        // Define side panel properties
        sidePanelWidth = Gdx.graphics.getWidth() / 5; // Width
        sidePanelX = Gdx.graphics.getWidth() - sidePanelWidth; // Position the panel on the right side
        sidePanelY = 100; // Y position
        sidePanelHeight = Gdx.graphics.getHeight() - 110; // Height
        sidePanelColor = new Color(0.2f, 0.2f, 0.2f, 0.5f); // Background color (RGBA)

        coinCounter = 0;

        visibleQ = new ArrayList<>();
        playerAttributes.setArray(visibleQ);
        playerAttributes.orderInProgress = false;
        deliveredOrderIDs = new ArrayList<>();
        outOfTimeOrdersIDs = new ArrayList<>();

        totalOrdersCompleted = 0;
        order = new Order();
        orderArray = new ArrayList<>();
        order.setArray(orderArray, fileName + "_orders");
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

        gameplayMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/we like this one.mp3"));
        fastMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/Calmer Fast.mp3"));
        fastMusic.setLooping(true);
        fastMusic.setVolume(.5f);
        gameplayMusic.setLooping(true);
        gameplayMusic.setVolume(0.5f);

        dropoffSuccess = Gdx.audio.newSound(Gdx.files.internal("audio/successful dropoff.mp3"));
        pie = Gdx.audio.newSound(Gdx.files.internal("audio/pumpkin pie.mp3"));
        pickupSuccess = Gdx.audio.newSound(Gdx.files.internal("audio/pickup success.mp3"));
        newOrderNotif = Gdx.audio.newSound(Gdx.files.internal("audio/new order notification.mp3"));
        coinCollect = Gdx.audio.newSound(Gdx.files.internal("audio/coin_collect.mp3"));
        gamepopup = new GamePopup(this, "", game, fileName);
        keepplayingpopup = new KeepPlayingPopup(this, "", game, fileName);
        int hide_popup = 0;
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        //musicSlider = pauseScreen.getMusicSlider();
        musicSlider = new Slider(0f, 1f, 0.1f, false, skin);
        musicSlider.setValue(1f);
        musicSlider.setSize(250f, 25f);
        musicSlider.setPosition(200f, Gdx.graphics.getHeight() - 300f);

        //soundSlider = pauseScreen.getSoundSlider();
        soundSlider = new Slider(0f, 1f, 0.1f, false, skin);
        soundSlider.setValue(1f);
        soundSlider.setSize(250f, 25f);
        soundSlider.setPosition(200f, Gdx.graphics.getHeight() - 400f);

        pauseScreen = new PauseScreen(this, "", game, skin, musicSlider, soundSlider);
        //coinCollectPlayed = false;
        //soundPlayed = false;
        long soundId = 0;


        // Make button style
        Pixmap backgroundPixmap = createRoundedRectanglePixmap(100, 50, 10, new Color(0.9f, 0, 0.9f, 0.6f)); // Adjust size and color
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(backgroundPixmap)));
        BitmapFont buttonFont = new BitmapFont();
        buttonFont.getData().setScale(1.5f);
        buttonStyle.font = buttonFont;
        buttonStyle.fontColor = Color.BLACK;

        if ("level_5".equals(fileName)) {
            System.out.println("in tutorial");
            tutorialPopups = new TutorialPopups(this);
            //isTutorialPopupsVisible = true;
            GameDifficulty.tutorial = true;
            minOrders = 1;
            countdownMinutes = 15;

            //make done button
            doneButton = new TextButton("Done!", buttonStyle);
            doneButton.setVisible(false);
            doneButton.setPosition(0, 30); // Adjust the position as necessary
            doneButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //System.out.println("clicked back");
                    isTutorialPopupsVisible = true;
                    doneButton.setVisible(false);
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    isDoneButtonHovered = true;
                    doneButton.setColor(Color.LIGHT_GRAY);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    isDoneButtonHovered = false;
                    doneButton.setColor(Color.WHITE);
                }
            });

            if(tutorialPopups.OKClicked) {
                doneButton.setVisible(true);
            }
            stage.addActor(doneButton);

        } else {
            tutorialPopups = null; // If fileName is not "Level 5", set tutorialPopups to null
            //isTutorialPopupsVisible = false;
        }



        // Make back button
        backButton = new TextButton("Back", buttonStyle);
        backButton.setPosition(10, Gdx.graphics.getHeight() - 60); // Adjust the position as necessary
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //System.out.println("clicked back");
                try {
                    game.setScreen(new LevelScreen(game));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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


        // Make pause button
        pauseButton = new TextButton("| |", buttonStyle);
        pauseButton.setPosition(120, Gdx.graphics.getHeight() - 60); // Adjust the position as necessary
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //System.out.println("clicked back");
                //pauseMusic();
                pauseScreen();
                gamePaused = true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                isPauseButtonHovered = true;
                pauseButton.setColor(Color.LIGHT_GRAY);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isPauseButtonHovered = false;
                pauseButton.setColor(Color.WHITE);
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

                String gameStatsMessage = "GAME OVER! \nTotal Coins Collected: " + coinCounter + "\nTotal Orders Completed: " + totalOrdersCompleted;

                gamepopup.showGameStatsMessage(gameStatsMessage);
                gamepopup.showOrderCompletedList(orderIDsMessage);
                gamepopup.showOutoffTimeList(notInTimeorderIDsMessage);
                gamepopup.show();
                gamepopup.render();
                multiplexer.addProcessor(gamepopup.getStage());
            }
        });

        stage.addActor(backButton);
        stage.addActor(pauseButton);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(keyProcessor);
        multiplexer.addProcessor(popup.getStage());
        if (tutorialPopups != null) {
            multiplexer.addProcessor(tutorialPopups.getStage());
        }

        Gdx.input.setInputProcessor(multiplexer);

        // visual indicators stuff
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/moonships_font.fnt"), Gdx.files.internal("fonts/moonships_font.png"), false);
        font.setColor(0, 0, 0, 1);
        font.getData().setScale(0.5f, 0.5f);
        Label.LabelStyle indicatorStyle = new Label.LabelStyle(font, Color.BLACK);
        Label.LabelStyle warningStyle = new Label.LabelStyle(font, Color.MAROON);

        // pickup label
        pickupLabel = new Label("Press P to pickup!", indicatorStyle);
        pickupLabel.setVisible(false);
        stage.addActor(pickupLabel);
        pickupLabel.setSize(font.getScaleX() * 16, font.getScaleY() * 16);

        // dropoff label
        dropoffLabel = new Label("Press O to dropoff!", indicatorStyle);
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

        indicatorStyle = new Label.LabelStyle(font, Color.YELLOW);
        coinCollectLabel = new Label("Coins Collected: " + coinCounter, indicatorStyle);
        coinCollectLabel.setSize(font.getScaleX() * 16, font.getScaleY() * 16);
        coinCollectLabel.setPosition(Gdx.graphics.getWidth() / 2 - 90, Gdx.graphics.getHeight() - coinCollectLabel.getHeight() - 36);
        coinCollectLabel.setVisible(true);
        stage.addActor(coinCollectLabel);
        //minOrderLabel.setText("Orders Completed: "+playerAttributes.ordersCompleted+"/"+minOrders);

        indicatorStyle = new Label.LabelStyle(font, Color.BLACK);
        orderTimeoutLabel = new Label("Time ran out. Begin next delivery!", warningStyle);
        orderTimeoutLabel.setPosition(Gdx.graphics.getWidth() / 2 - 180, Gdx.graphics.getHeight() - minOrderLabel.getHeight() - 52);
        orderTimeoutLabel.setVisible(false);
        stage.addActor(orderTimeoutLabel);
        orderTimeoutLabel.setSize(font.getScaleX() * 16, font.getScaleY() * 16);

        String str = "Time remaining:\n" + "     " + String.valueOf(countdownMinutes) + ":" + String.valueOf(countdownSeconds);
        mainTimer = new Label(str, indicatorStyle);
        mainTimer.setPosition(25, Gdx.graphics.getHeight() - 90);
        mainTimer.setVisible(true);
        stage.addActor(mainTimer);
        mainTimer.setSize(font.getScaleX() * 16, font.getScaleY() * 16);

        //String str = "Time remaining:\n" + "     " + String.valueOf(countdownMinutes) + ":" + String.valueOf(countdownSeconds);
        lowTimer = new Label(str, warningStyle);
        lowTimer.setPosition(25, Gdx.graphics.getHeight() - 90);
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
        if (tutorialPopups != null) {
            tutorialPopups.scheduleStepDelay();
        }
        //tutorialPopups.scheduleStep();

    }

    @Override
    public void create() {
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont(Gdx.files.internal("fonts/moonships_font.fnt"), Gdx.files.internal("fonts/moonships_font.png"), false);
        ; // Define your BitmapFont
        // Set font color and scale
        font.setColor(1, 1, 0, 1);
        font.getData().setScale(0.5f);

    }


    public boolean isTutorialPopupsVisible() {
        return isTutorialPopupsVisible;
    }

    public void setTutorialPopupsVisible(boolean visible) {
        isTutorialPopupsVisible = visible;
    }

    @Override
    public void render(float delta) {
        /* ========================== UPDATE ============================ */

        //If game stats screen is not visible, keep the game going (else pause)
        //if (!gameStatsScreen.isVisible()) {
//        if (coinCollected && !coinCollectPlayed) {
//            soundId = coinCollect.play();
//            coinCollect.setVolume(soundId, soundSlider.getValue());
//            coinCollectPlayed = true; // Mark that the sound has been played
//        }
        //long soundId = coinCollect.play();
        gameplayMusic.setVolume(musicSlider.getValue());

        if (!gamepopup.isVisible()) {
            if (!isTutorialPopupsVisible && !gamePaused){

                player.update(delta, keyProcessor);
            }
            player.checkBounds(levelWidth, levelHeight);
            for (BuildingObject buildingObject : buildings) {
                player.checkCollision(buildingObject, true);
            }

            world.step(1 / 60f, 6, 2); // Physics calculations

            camera.follow(player.getPosition(), levelWidth, levelHeight);

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

            player.draw(batch);
//            rock.draw(batch);
//            log.draw(batch);

            // start music
            gameplayMusic.play();

            if (keepplayingpopup.isVisible()) {
                gameplayMusic.pause();
                newOrderNotif.pause();
            }

            // coin collision
            for (CoinObject coin : coins) {
                if (!coin.isCollected()) {
                    coin.draw(batch);
                    if (player.checkCollision(coin, false)) {
                        soundId = coinCollect.play();
                        coinCollect.setVolume(soundId, soundSlider.getValue());
                        //System.out.println("volume set!");
                        gameplayMusic.pause();
                        newOrderNotif.pause();
                        //coinCollect.play();
                        coin.setCollected(true);
                        //soundPlayed = true;
                        coinCounter++;
                        coinCollectLabel.setText("Coins Collected: " + coinCounter);
                    }
                }
            }

            // Inside the render method
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
                gameplayMusic.pause();
                gameplayMusic = fastMusic;
                gameplayMusic.play();
                lowTimer.setVisible(true);
                mainTimer.setVisible(false);
            }
            if (countdownMinutes == 0 && countdownSeconds == 0) {
                gameplayMusic.pause();
            }

            // building collisions and transparency
            for (BuildingObject building : buildings) {
                building.draw(batch);
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
                    StringBuilder sb = new StringBuilder();
                    String[] currentOrder = playerAttributes.array.get(1).split("\\\n");
                    for (String s : currentOrder)
                    {
                        sb.append(s);
                        sb.append("\t");
                    }
//                    System.out.println("sb: " + sb.toString());
                    String orderString = sb.toString();
                    String[] splitArray = orderString.split("\\\t");
                    String orderID = splitArray[0].substring(4);
//                    System.out.println(orderID);
                    String currPick = splitArray[1].substring(3);
                    String currDrop = splitArray[2].substring(3);

//                    String[] s = order.stringToArray(playerAttributes.array.get(1));
//                    String str3 = s[3];
//                    String str2 = s[2];
//                    String currDrop = str3.substring(0, str3.length() - 6);
//                    String currPick = str2.substring(0, str2.length() - 3);
//                    System.out.println("pick:" + currPick);
                    for (BuildingObject building : buildings) {
//                        System.out.println("name: " + building.getName());
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
                            order.dropoffBuilding = building;
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
                                gameplayMusic.pause();
                                newOrderNotif.pause();

                                long id = pickupSuccess.play();
                                pickupSuccess.setVolume(id, 0.5f);

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
                                gameplayMusic.pause();
                                newOrderNotif.pause();

                                if (order.dropoffBuilding.getName().equals("Turkstra")) {
                                    if (playerAttributes.ordersCompleted == minOrders - 1) {
                                        long id = pie.play();
                                        pie.setVolume(id, .8f);
                                    }
                                } else {
                                    long id = dropoffSuccess.play();
                                    dropoffSuccess.setVolume(id, 0.5f);
                                }


                                playerAttributes.ordersCompleted++;
                                minOrderLabel.setText("Orders Completed: " + playerAttributes.ordersCompleted + "/" + minOrders);
                                totalOrdersCompleted++;

//                                String orderID = playerAttributes.array.get(1).substring(4,9)/*order.getOrderString()*/;
                                //if (!deliveredOrderIDs.contains(orderID)) {
                                deliveredOrderIDs.add(orderID);
//                                System.out.println("Order " + orderID + " (before removing) has been delivered and added to the list.");

                                //}
                                playerAttributes.array.remove(1);
                                //System.out.println("array after removing: "+playerAttributes.array);
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
            } else {
                // set everything to false
                order.setPickedUp(false);
                order.setDroppedOff(false);
                for (BuildingObject building : buildings) {
                    building.setDropoffLocation(false);
                    building.setPickupLocation(false);
                }
            }


            if (playerAttributes.orderInProgress) {
                for (int i = 1; i < playerAttributes.array.size(); i++) {
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
                        if (!(playerAttributes.ordersCompleted >= minOrders)) {
                            outOfTimeOrdersIDs.add(orderID);
                        }
                        orderTimeoutLabel.setVisible(true);
                        playerAttributes.array.remove(i);
                        messageTimer = 0.0f;
                    } else {
                        if (!popupInAction && !isTutorialPopupsVisible && !gamePaused) {

                            if (timeCount[i - 1] % 60 == 0) {
                                time -= 1;
                                orderTimeLeft[i - 1] = time;
                            }
                            timeCount[i - 1]++;
                        }

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
            float coinCountTextX = Gdx.graphics.getWidth() / 2 - 90; // X position within the side panel
            float coinCountTextY = Gdx.graphics.getHeight() - minOrderLabel.getHeight() - 25;

            for (int i = 1; i < items.length; i++) {
                if (orderTimeLeft[i - 1] <= 5 && orderTimeLeft[i - 1] > 0) {
                    //if (order.isPickedUp()) {
                    font.setColor(Color.RED);
                    // }
                } else if (i == 1 && order.isPickedUp()){
                    font.setColor(Color.GREEN);
                } else {
                    font.setColor(Color.WHITE);
                }
                font.draw(batch, items[i], sidePanelX + 10, sidePanelY + sidePanelHeight - 70 * i);

            }
            //}

            stage.draw();
            popup.render();
            gamepopup.render();
            if (this.tutorialPopups != null) {
                tutorialPopups.render();
            }
            pauseScreen.render();
            batch.end();
        } else {
            gameplayMusic.pause();
            newOrderNotif.pause();
        }

        boolean timeLeft = true;
        if (countdownSeconds == 0 && countdownMinutes == 0) {
            if (playerAttributes.orderInProgress) {
                // go through all orders and add timed out orders to game stats
                for (int i = 1; i < playerAttributes.array.size(); i++) {
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
                //pauseScreen();
                popupInAction = true;
            }
        }
    }

//    public void pauseMusic() {
//        if (gameplayMusic.isPlaying()) {
//            System.out.println("Music paused");
//            gameplayMusic.pause();
//        }
//    }
//
//    public void resumeMusic() {
//        if (!gameplayMusic.isPlaying()) {
//            System.out.println("Music resumed");
//            gameplayMusic.play();
//        }
//    }

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

        String gameStatsMessage = "GAME STATS: \nTotal Coins Collected: " + coinCounter + "\nTotal Orders Completed: " + totalOrdersCompleted;
        gamepopup.showGameStatsMessage(gameStatsMessage);
        gamepopup.showOrderCompletedList(orderIDsMessage);
        gamepopup.showOutoffTimeList(notInTimeorderIDsMessage);
        gamepopup.showLevelResultMessage(levelResult);
        gamepopup.setWin(win);
        gamepopup.show();
        gamepopup.render();
        multiplexer.addProcessor(gamepopup.getStage());
        //System.out.println("Level completed!");
    } //render

    // keep playing pop up
    public void keepPlayingPopup() {
        visibleText = false;
        String message = "Level Complete!\n";
        String option = "Keep Playing or End Game?";
        keepplayingpopup.setMessageLabel(message);
        keepplayingpopup.setOptionLabel(option);
        keepplayingpopup.show();
        keepplayingpopup.render();
        multiplexer.addProcessor(keepplayingpopup.getStage());
    }

    // pause screen pop up
    public void pauseScreen() {
        visibleText = false;
        String message = "Game Paused!\nSettings:\n";
        //String option = "Keep Playing or End Game?";
        pauseScreen.setMessageLabel(message);
        //stage.addActor(musicSlider);
        //stage.addActor(soundSlider);
        pauseScreen.show();
        pauseScreen.render();
        multiplexer.addProcessor(pauseScreen.getStage());
    }

    // Trigger the timed popup to show
    public void showTimedPopup() {
        popup.show(); // Display the popup
        /*try {
            order.setArray(orderArray);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        order.seti(count);
        count++;
        if (count < orderArray.size() + 1) {
            popup.setMessage(order.arrayToString());
            popup.acceptClicked = false;
            popup.declineClicked = false;
        } else {
            popup.setMessage("No more orders!");
            outOfOrders = true;
            popup.declineButton.remove();
            popup.acceptButton.remove();
            if (ordersDone) {
                popup.hide();
            }
            ordersDone = true;


        }
    }

    public void hideTimedPopup() {
        popup.hide(); // Display the popup
    }


    // Schedule the popup to display every 1 minute
    private void schedulePopupDisplay() {
        //System.out.println("hi in schedule");
        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//        int hide_popup = 0;
        //int show_popup = 0;

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (GameDifficulty.tutorial || GameDifficulty.easy || GameDifficulty.medium) {
                    hide_popup = 5;
                    //show_popup = 10;
                } else if (GameDifficulty.hard) {
                    hide_popup = new Random().nextInt(6)+1;
                    System.out.println("random num: "+hide_popup);
                    //show_popup = 10;
//            show_popup = new Random().nextInt(5) + 6;
                    //System.out.println("random num2: "+show_popup);
                }
                if (!popupInAction && !isTutorialPopupsVisible && !gamePaused) {

                    showTimedPopup(); // Show the popup
                    if (!outOfOrders) {
                        soundId = newOrderNotif.play();
                        //newOrderNotif.setVolume(id, 0.9f);
                        newOrderNotif.setVolume(soundId, soundSlider.getValue());
                    }
                    scheduler.schedule(new Runnable() {
                        @Override
                        public void run() {
                            if (!popupInAction && !isTutorialPopupsVisible && !gamePaused) {

                                // Hide the popup
                                hideTimedPopup();
                                if (!popup.acceptClicked() && !popup.declineClicked() && !ordersDone) {
                                    //lose coins if do not manually accept or decline 3 times
                                    if(GameDifficulty.hard) {
                                        popup.nothingClicked++;
                                        if (popup.nothingClicked >= 3) {
                                            if (coinCounter > 1) {
                                                coinCounter -= 2;
                                            }
                                            else if (coinCounter == 1) {
                                                coinCounter -= 1;
                                            }
                                            coinCollectLabel.setText("Coins Collected: " + coinCounter);
                                        }
                                    }
                                    autoDeclineLabel.setVisible(true);
                                    scheduler.schedule(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!popupInAction && !isTutorialPopupsVisible && !gamePaused) {

                                                autoDeclineLabel.setVisible(false); // Remove the label from the display
                                            }
                                        }
                                    }, 4, TimeUnit.SECONDS);
                                }
                                if(GameDifficulty.hard) {
                                    if (popup.declineCount == 3) {
                                        if (coinCounter > 1) {
                                            coinCounter -= 2;
                                        }
                                        if (coinCounter == 1) {
                                            coinCounter -= 1;
                                        }
                                    }
                                    coinCollectLabel.setText("Coins Collected: " + coinCounter);
                                }
                            }
                        }
                    }, hide_popup, TimeUnit.SECONDS); // Schedule to hide the popup after _ seconds
                }
            }
        }, 0, 10, TimeUnit.SECONDS); // Schedule the next popup _ seconds after the first one
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
        gameplayMusic.dispose();
        newOrderNotif.dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameplayMusic.dispose();
        newOrderNotif.dispose();
        //incomingOrder.dispose();
        multiplexer.removeProcessor(stage);
    }

    /**
     * Creates a rectangular panel with rounded corners.
     *
     * @param width        Width of the panel.
     * @param height       Height of the panel.
     * @param cornerRadius The radius of the corners.
     * @param color        The background color of the panel.
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

    private void loadLevel(String fileName) throws IOException, ClassNotFoundException {
        InputStream fileStream = Files.newInputStream(new File(Gdx.files.internal("levels/" + fileName + ".lvl").path()).toPath());
        ObjectInputStream inputStream = new ObjectInputStream(fileStream);

        LevelData levelData = (LevelData) inputStream.readObject();

        if (levelData == null) return;

//        System.out.println(levelData.levelName);
//        System.out.println(levelData.tileSize);
//        System.out.println(levelData.difficulty);
//        System.out.println(levelData.width);
//        System.out.println(levelData.height);

        levelWidth = levelData.width * levelData.tileSize;
        levelHeight = levelData.height * levelData.tileSize;

        buildings = new ArrayList<>();
        coins = new ArrayList<>();

        for (int layerNum = 0; layerNum < levelData.tileData.size(); layerNum++) {
            LevelData.TileData[] tileData = levelData.tileData.get(layerNum);

            // TODO: spawn in the objects
            LevelData.ObjectData[] objectData = levelData.objectData.get(layerNum);

            Tile[] tiles = new Tile[tileData.length];

            for (int i = 0; i < tileData.length; i++) {
                tiles[i] = new Tile(
                        AssetManager.INSTANCE.getTileSprite(tileData[i].tilesetID,
                                tileData[i].spriteIndex),
                        tileData[i].x * levelData.tileSize,
                        levelHeight - tileData[i].y * levelData.tileSize
                );
            }

            for (int i = 0; i < objectData.length; i++) {
                GameObject object = AssetManager.INSTANCE.loadGameObject(objectData[i].objectID);
                if (object == null) continue;

                if (object instanceof BuildingObject) {
                    BuildingObject building = new BuildingObject((BuildingObject) object);
                    building.setPosition(
                            objectData[i].x * levelData.tileSize,
                            // Setting the y-position is like this bc libgdx is stupid :)
                            levelHeight - (objectData[i].y + 1) * levelData.tileSize - building.getBounds().height
                    );
//                    System.out.println(objectData[i].objectID);
                    buildings.add(building);
                } else if (object instanceof CoinObject) {
                    CoinObject coin = new CoinObject((CoinObject) object);
                    coin.setPosition(
                            objectData[i].x * levelData.tileSize,
                            // Setting the y-position is like this bc libgdx is stupid :)
                            levelHeight - (objectData[i].y + 1) * levelData.tileSize - coin.getBounds().height
                    );
                    coins.add(coin);
                }
            }

            layers.add(tiles);

        }
    }

    private int getCountdownMinutes(String filename) throws FileNotFoundException {
        String file = "level_displays/" + filename + "_display.txt";

        Scanner scanner = new Scanner(new File(file));
        String thumbnailPath = scanner.nextLine();
        int timeMinutes = Integer.parseInt(scanner.nextLine());
        int timeSeconds = Integer.parseInt(scanner.nextLine());
        return timeMinutes;
    }

    private int getCountdownSeconds(String filename) throws FileNotFoundException {
        String file = "level_displays/" + filename + "_display.txt";

        Scanner scanner = new Scanner(new File(file));
        String thumbnailPath = scanner.nextLine();
        int timeMinutes = Integer.parseInt(scanner.nextLine());
        int timeSeconds = Integer.parseInt(scanner.nextLine());
        return timeSeconds;
    }

    private int getMinOrders(String filename) throws FileNotFoundException {
        String file = "level_displays/" + filename + "_display.txt";

        Scanner scanner = new Scanner(new File(file));
        String thumbnailPath = scanner.nextLine();
        int timeMinutes = Integer.parseInt(scanner.nextLine());
        int timeSeconds = Integer.parseInt(scanner.nextLine());
        String name = scanner.nextLine();
        scanner.nextLine();
        int minOrders = Integer.parseInt(scanner.nextLine());
        return minOrders;
    }
}
