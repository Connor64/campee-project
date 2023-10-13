package com.campee.starship;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;



public class GameScreen extends ApplicationAdapter implements Screen {
    int test;
    SpriteBatch batch;

    Texture img;
    private Game game;
    private Stage stage;
    private Popup popup;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private ShapeRenderer shapeRenderer;
    public int coinCounter = 0;

    private boolean screenClicked = false; // Add this variable
    private boolean prevClickState = false; // Add this variable to track the previous click state
    TextButton backButton;
    TextButton nextOrderButton;
    Player player;
    PlayerAttributes attributes;
    KeyProcessor keyProcessor;
    InputMultiplexer multiplexer;

    //OrderScreen orderScreen;
    Coin coin;
    Order order;
    float x;
    float y;
    float screenWidth;
    float screenHeight;
    int SPEED = 150;
    int move = 0;
    ArrayList<String> array;

    float sidePanelX;
    float sidePanelY;
    float sidePanelWidth;
    float sidePanelHeight;
    Color sidePanelColor;
    BitmapFont font;

    public GameScreen(final Game game) {
        ArrayList<String> array;
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
        test = 0;
        stage = new Stage();
        this.game = game;
        batch = new SpriteBatch();
        keyProcessor = new KeyProcessor();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        x = 100;
        y = 100;
        player = new Player(world, x, y);
        //orderScreen = new OrderScreen();
        coin = new Coin(world, x, y);
        attributes = new PlayerAttributes();

        Pixmap backgroundPixmap = createRoundedRectanglePixmap(200, 50, 10, Color.PURPLE); // Adjust size and color
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(backgroundPixmap)));
        BitmapFont buttonFont = new BitmapFont();
        buttonFont.getData().setScale(1.5f);
        buttonStyle.font = buttonFont;
        buttonStyle.fontColor = Color.BLACK;
        backButton = new TextButton("Back", buttonStyle);
        backButton.setPosition(10, Gdx.graphics.getHeight() - 60); // Adjust the position as necessary
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked back");
//                game.setScreen(new TitleScreen(game));
            }
        });

        nextOrderButton = new TextButton("Next Order", buttonStyle);
        nextOrderButton.setPosition(Gdx.graphics.getWidth() - 220, Gdx.graphics.getHeight() - 60); // Adjust the position as necessary
        nextOrderButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popup.show();
                popup.render();
                multiplexer.addProcessor(popup.getStage());
//                Gdx.input.setInputProcessor(popup.getStage());
                System.out.println("Order Menu");
            }
        });

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(keyProcessor);

        stage.addActor(nextOrderButton);
        stage.addActor(backButton);

        Gdx.input.setInputProcessor(multiplexer);


        array = attributes.array;
//        orderScreen = new OrderScreen();
//        orderScreen.visible = true;
        order = new Order(stage, game, 01, "Cosi", "walc", 7.00 );
//        popup = new Popup(this, order.toString());

        // Define side panel properties
        sidePanelWidth = screenWidth / 5; // Width
        sidePanelX = screenWidth - sidePanelWidth-10; // Position the panel on the right side
        sidePanelY = 10; // Y position
        sidePanelHeight = Gdx.graphics.getHeight() - 20; // Height
        sidePanelColor = new Color(0.2f, 0.2f, 0.2f, 0.8f); // Background color (RGBA)

        attributes = new PlayerAttributes();
        array = attributes.array;
        order = new Order(stage, game, 0, null, null, 0 );
//        popup = new Popup(this, order.toString());
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont(Gdx.files.internal("moonships_font.fnt"), Gdx.files.internal("moonships_font.png"), false);; // Define your BitmapFont
        // Set font color and scale
        font.setColor(1, 1, 0, 1);
        font.getData().setScale(0.5f);
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
    public void render(float delta) {
        ScreenUtils.clear(Color.PINK);
        float deltaTime =  Gdx.graphics.getDeltaTime();

        world.step(1 / 60f, 6, 2);
        float xMove = 0;
        float yMove = 0;

        // Render the side panel
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(sidePanelColor);
        shapeRenderer.rect(sidePanelX, sidePanelY, sidePanelWidth, sidePanelHeight);
        shapeRenderer.end();

        Vector2 vel = this.player.body.getLinearVelocity();
        Vector2 pos = this.player.body.getPosition();
        float MAX_VELOCITY = 100f;

        // Handle player movement
        if (!popup.isVisible()) {
            if (keyProcessor.upPressed && vel.y < MAX_VELOCITY) {
                this.player.body.applyLinearImpulse(0, 4f, pos.x, pos.y, true);
                yMove = 1;
                move = 0;
            } else if (keyProcessor.downPressed && vel.y > -MAX_VELOCITY) {
                this.player.body.applyLinearImpulse(0, -4f, pos.x, pos.y, true);
                yMove = -1;
                move = 1;
            } else if (keyProcessor.leftPressed && vel.x > -MAX_VELOCITY) {
                this.player.body.applyLinearImpulse(-4f, 0, pos.x, pos.y, true);
                xMove = -1;
                move = 2;
            } else if (keyProcessor.rightPressed && vel.x < MAX_VELOCITY) {
                this.player.body.applyLinearImpulse(4f, 0, pos.x, pos.y, true);
                xMove = 1;
                move = 3;
            } else {
                float linearDamping = 2;
                player.body.setLinearDamping(linearDamping);
            }
        }

        float newscreenWidth = screenWidth - sidePanelWidth - 10;
        // boundaries for player and screen
        Rectangle playerBounds = player.getBounds();
        Rectangle screenBounds = new Rectangle(0, 0, newscreenWidth, screenHeight);
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

        // float for new position (for screen collisions)
//        float newX = player.sprite.getX();
//        float newY = player.sprite.getY();

        // visual indicator stuff
//        Skin indicatorSkin = new Skin(Gdx.files.internal("skin/uiskin.json"),
//                new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        BitmapFont font = new BitmapFont(Gdx.files.internal("moonships_font.fnt"), Gdx.files.internal("moonships_font.png"), false);
        font.setColor(0, 0, 0, 1);
        font.getData().setScale(0.5f, 0.5f);
        Label.LabelStyle indicatorStyle = new Label.LabelStyle(font, Color.BLACK);
        Label label = new Label("Careful!", indicatorStyle);
        label.setVisible(false);

        label.setSize(font.getScaleX() * 100, font.getScaleY() * 100);
//        label.setPosition(screenWidth - (screenWidth - 15), screenHeight - label.getHeight());

        // visual indicator that the player is almost off the screen
        // left side
        if (!popup.isVisible()) {
            if (playerLeft <= screenLeft + (2 * halfWidth)) {
                label.setVisible(false);
                stage.clear();
                label.setPosition(newscreenWidth - (newscreenWidth - label.getWidth()), screenHeight / 2);
                label.setVisible(true);
                stage.addActor(label);
            } else if (playerRight >= screenRight - (2 * halfWidth)) {
                label.setVisible(false);
                stage.clear();
                label.setPosition((newscreenWidth - (3 * label.getWidth())), screenHeight / 2);
                label.setVisible(true);
                stage.addActor(label);
            } else if (playerBottom <= screenBottom + (2 * halfHeight)) {
                label.setVisible(false);
                stage.clear();
                label.setPosition(newscreenWidth / 2, screenHeight - (screenHeight - label.getHeight()));
                label.setVisible(true);
                stage.addActor(label);
            } else if (playerTop >= screenTop - (2 * halfHeight)) {
                label.setVisible(false);
                stage.clear();
                label.setPosition(newscreenWidth / 2, screenHeight - label.getHeight());
                label.setVisible(true);
                stage.addActor(label);
            } else {
                // remove the label
                stage.clear();
                label.setVisible(false);
            }
        }

        stage.act(delta);
//        // testing to make sure the player can't leave bounds
//        // TODO: fix bouncing :/
//        // horizontal axis
//        if (playerLeft < screenLeft) {
//            // clamp to left
//            newX = screenLeft + halfWidth;
//            player.body.setLinearVelocity(newX, player.body.getLinearVelocity().y);
//            xMove = 1;
//        } else if (playerRight > screenRight) {
//            // clamp to right
//            newX = screenRight - halfWidth;
//            player.body.setLinearVelocity(-newX, player.body.getLinearVelocity().y);
//            xMove = 1;
//        }
//        // vertical axis
//        if (playerBottom < screenBottom) {
//            // clamp to bottom
//            newY = screenBottom + halfHeight;
//            player.body.setLinearVelocity(player.body.getLinearVelocity().x, newY);
//            yMove = 1;
//        } else if(playerTop > screenTop) {
//            // clamp to top
//            newY = screenTop - halfHeight;
//            player.body.setLinearVelocity(player.body.getLinearVelocity().x, -newY);
//            yMove = 1;
//        }

        batch.begin();

        stage.draw();

        String[] items = {"", "Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};
        font.draw(batch, "Order List:", sidePanelX + 10, sidePanelY + sidePanelHeight - 10);
        font.draw(batch, "\n", sidePanelX + 10, sidePanelY + sidePanelHeight - 10);

        for (int i = 1; i < items.length; i++) {
            font.draw(batch, items[i], sidePanelX + 10, sidePanelY + sidePanelHeight - 30*i);
        }

//        player.render(batch, xMove, yMove, move);

        if (!coin.collected) {
            coin.render(batch, 50, 25);
            if (Intersector.overlaps(player.getBounds(), coin.getBounds())) {
                coin.setCollected(true);
                coinCounter++;
            }
        }

        // Gdx.input.setInputProcessor(keyProcessor);


//        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//            test++;
//            System.out.println(test);
//            if (test == 2) {
//                popup.show();
//                multiplexer.addProcessor(popup.getStage());
////                Gdx.input.setInputProcessor(popup.getStage());
//            }
//            if (test == 4) {
//                System.out.println(attributes.array);
//            }
//        }
//
//        if (popup.isVisible()) {
//            popup.render();
//        } else {
//            multiplexer.removeProcessor(popup.getStage());
////            Gdx.input.setInputProcessor(keyProcessor); // Enable arrow key input
//        }

        batch.end();
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }
}