package com.campee.starship.screens;

import Serial.LevelData;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.campee.starship.managers.AssetManager;
import com.campee.starship.managers.DataManager;
import com.campee.starship.userinterface.CustomScrollPane;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class LevelScreen extends ScreenAdapter {
    public static String nameOfFile;
    private final Game game;
    private Stage stage;
    private CustomScrollPane scrollBar;
    private BitmapFont font;
    private TextButton backButton; // Add a TextButton for the "BACK" button
    private TextButton settingsButton;
    private TextButton storeButton;
    private TextButton levelButton;
    private ExtendViewport viewport;
    private Stage stage2;
    //private String prevLevelName;

    public LevelScreen(final Game game) {
        this.game = game;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        //stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        DataManager.INSTANCE.isLevelUnlocked("0");

        Table outerTable = new Table();
        outerTable.setFillParent(true);
        stage.addActor(outerTable);

        Table innerTable = new Table();
        outerTable.add(innerTable).center();

        Label titleLabel = new Label("LEVEL SELECT SCREEN", createTitleLabelStyle(Color.BLACK));

        // Add title label to the top of the window with some padding
        innerTable.add(titleLabel).padTop(50).colspan(3).center().row();

        int index = 0;
        Set<Map.Entry<String, LevelData>> levelSet = AssetManager.INSTANCE.getLevels();
        Table rowTable = new Table(); // Create a new table for each row

        for (Map.Entry<String, LevelData> entry : levelSet) {
            Table levelWidget = createLevelWidget(entry.getKey());
            rowTable.add(levelWidget).pad(40).center();

            index++;

            // Add up to three level widgets in this row
            if (index >= 3) {
                // Add the rowTable to the innerTable
                innerTable.add(rowTable).row();
                rowTable = new Table();
                index = 0;
            }
        }

        if (index != 0) innerTable.add(rowTable).row();

        CustomScrollPane customScrollPane = new CustomScrollPane(innerTable, stage);
        customScrollPane.setScrollingDisabled(true, false);
        customScrollPane.setFillParent(true);

        //customScrollPane.setTouchable(Touchable.enabled);

        stage.addActor(customScrollPane);
        stage.setScrollFocus(customScrollPane);
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

                game.setScreen(new TitleScreen((MoonshipGame) game)); // Change to the screen you want
                return true;
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                backButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                backButton.setColor(Color.WHITE);
            }
        });
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

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                settingsButton.setColor(Color.LIGHT_GRAY);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                settingsButton.setColor(Color.WHITE);
            }
        });

        TextButton.TextButtonStyle storeButtonStyle = new TextButton.TextButtonStyle();
        storeButtonStyle.font = buttonFont;
        storeButtonStyle.fontColor = Color.BLACK;
        Pixmap storeButtonPixmap = createRoundedRectanglePixmap(150, 60, 15, Color.valueOf("98FF98"));
        storeButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(storeButtonPixmap)));
        storeButton = new TextButton("STORE", storeButtonStyle);
        storeButton.setPosition(30, Gdx.graphics.getHeight() - 150); // Adjust Y-coordinate as needed
        storeButton.setSize(150, 60);
        storeButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new StoreScreen(game));
                return true;
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                storeButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                storeButton.setColor(Color.WHITE);
            }
        });


        stage.addActor(storeButton);
        stage.addActor(settingsButton);
        stage.addActor(backButton);

        //scrollPane.debug();
        //innerTable.debug();
        //outerTable.debug();
    }

    private Label.LabelStyle createTitleLabelStyle(Color color) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.fontColor = color;
        return style;
    }

    private Table createLevelWidget(final String levelNumber) {
        Table levelWidget = new Table();
//        levelWidget.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(createPixmap(new Color(0.8f, 0.6f, 1f, 1f))))));

        // Create a label
        Label label = new Label("Level " + levelNumber, createLabelStyle(Color.BLACK));
        label.setFontScale(1.2f);


        Label orderLabel = new Label("2 orders", createLabelStyle(Color.BLACK));
        orderLabel.setFontScale(1f);

        // Create a button
        final TextButton.TextButtonStyle levelButtonStyle = new TextButton.TextButtonStyle();
        BitmapFont levelButtonFont = new BitmapFont();
        levelButtonFont.getData().setScale(1f);
        levelButtonStyle.font = levelButtonFont;
        levelButtonStyle.fontColor = Color.BLACK;
        levelButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(createRoundedRectanglePixmap(100, 45, 15, Color.DARK_GRAY))));
        //levelButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(createRoundedRectanglePixmap(150, 60, 15, Color.YELLOW))))


        ImageButton.ImageButtonStyle lockButtonStyle = new ImageButton.ImageButtonStyle();
        lockButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("sprites/lock_icon.png")));
        ImageButton lockButton = new ImageButton(lockButtonStyle);
        lockButton.getImageCell().size(20, 30);

        ImageButton.ImageButtonStyle unlockButtonStyle = new ImageButton.ImageButtonStyle();
        unlockButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("sprites/unlock_icon.png")));
        ImageButton unlockButton = new ImageButton(unlockButtonStyle);
        unlockButton.getImageCell().size(30, 40);


        boolean isLocked = false;
        final TextButton levelButton = new TextButton("LOCKED", levelButtonStyle);

        if (DataManager.INSTANCE.levelExists(levelNumber)) {
            if (DataManager.INSTANCE.isLevelUnlocked(levelNumber)) {
                levelButtonStyle.up = new TextureRegionDrawable(
                        new TextureRegion(new Texture(createRoundedRectanglePixmap(100, 45, 15, Color.GREEN))));
                levelButton.setText("UNLOCKED");
                levelWidget.add(unlockButton).padBottom(5).colspan(3).center().row();
            } else {
                levelButtonStyle.up = new TextureRegionDrawable(
                        new TextureRegion(new Texture(createRoundedRectanglePixmap(100, 45, 15, Color.DARK_GRAY))));
                levelButton.setText("LOCKED");
                levelWidget.add(lockButton).padBottom(5).colspan(3).center().row();
                isLocked = true;
            }
        }
        //final TextButton levelButton = new TextButton("LOCKED", levelButtonStyle);

        levelButton.setSize(10, 20);
        levelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if (levelButton.getText().toString().equals("UNLOCKED")) {
                        nameOfFile = "Level " + levelNumber;
//                        game.setScreen(new GameplayScreen((MoonshipGame) game, levelName));
                        game.setScreen(new GameplayScreen((MoonshipGame) game, "level_" + levelNumber));
                        //System.out.println("hereeee");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
//                return true;
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
//                isButtonHovered = true;
                //levelButton.setColor(Color.LIGHT_GRAY);
                if (levelButton.getText().toString().equals("UNLOCKED")) {
                    levelButton.setColor(Color.LIGHT_GRAY);
                }
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
//                isButtonHovered = false;
                //levelButton.setColor(Color.WHITE);
                if (levelButton.getText().toString().equals("UNLOCKED")) {
                    levelButton.setColor(Color.WHITE);
                }
            }
        });

        // Add the label to the top center of the table
        levelWidget.add(label).padBottom(30).colspan(3).center().row();
        levelWidget.add(orderLabel).padBottom(10).colspan(3).center().row();
        // Add the button slightly towards the bottom of the rectangle
        levelWidget.add(levelButton).padBottom(30).colspan(3).center().row();

        // Set background for the table
        levelWidget.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(createPixmap(new Color(0.8f, 0.6f, 1f, 1f))))));

        // Adjust padding and other properties as needed
        levelWidget.pad(30);

        return levelWidget;

    }

    private Label.LabelStyle createLabelStyle(Color color) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.fontColor = color;
        return style;
    }

    private TextButton.TextButtonStyle createButtonStyle(Color upColor, Color downColor) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont();
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture(createPixmap(upColor))));
        style.down = new TextureRegionDrawable(new TextureRegion(new Texture(createPixmap(downColor))));
        return style;
    }

    private Pixmap createPixmap(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        return pixmap;
    }
    private Pixmap createPixmap(Color color, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        return pixmap;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.7f, 0.9f, 1f, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 80f));
        //stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        backButton.setPosition(30, viewport.getWorldHeight() - 80); // Update button position on resize
        settingsButton.setPosition(viewport.getWorldWidth() - 180, viewport.getWorldHeight() - 80);
        storeButton.setPosition(viewport.getWorldWidth() - 340, viewport.getWorldHeight() - 80);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
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