package com.campee.starship.screens;

import Serial.LevelData;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.campee.starship.managers.AssetManager;
import com.campee.starship.managers.DataManager;
import com.campee.starship.userinterface.CustomScrollPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LevelScreen extends ScreenAdapter {
    public static String nameOfFile;
    private final Game game;
    private Stage stage;
    private ScrollPane scrollPane;
    private CustomScrollPane scrollBar;
    private BitmapFont font;
    private TextButton backButton; // Add a TextButton for the "BACK" button
    private TextButton settingsButton;
    private TextButton levelButton;
    private ExtendViewport viewport;
    private boolean isBackButtonHovered = false;
    private boolean isSettingButtonHovered = false;
    private boolean isButtonHovered = false;
    private Stage stage2;
    Music music;

    public LevelScreen(final Game game) throws FileNotFoundException {
        this.game = game;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        //stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        music = Gdx.audio.newMusic(Gdx.files.internal("audio/menu screen sound.mp3"));
        music.setLooping(true);
        music.setVolume(0.35f);

        DataManager.INSTANCE.isLevelUnlocked("0");

        Table outerTable = new Table();
        outerTable.setFillParent(true);
        stage.addActor(outerTable);

        Table innerTable = new Table();
        outerTable.add(innerTable).center();

//        FileHandle levelsFolder = Gdx.files.internal("levels");
//        FileHandle[] levelFiles = levelsFolder.list();

        Label titleLabel = new Label("LEVEL SELECT SCREEN", createTitleLabelStyle(Color.BLACK));
        titleLabel.setFontScale(2.5f);

        // Add title label to the top of the window with some padding
        innerTable.add(titleLabel).padTop(50).colspan(3).center().row();


        int index = 0;
        HashMap<String, LevelData> levels = AssetManager.INSTANCE.getLevels();
        Table rowTable = new Table(); // Create a new table for each row

        for (Map.Entry<String, LevelData> entry : levels.entrySet()) {
            Table levelWidget = createLevelWidget(entry.getKey());
            rowTable.add(levelWidget).pad(20).center();

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

//        for (int i = 0; i < levelFiles.length; i += 3) {
//            Table rowTable = new Table(); // Create a new table for each row
//
//            // Add up to three level widgets in this row
//            for (int j = i; j < Math.min(i + 3, levelFiles.length); j++) {
//                String[] level_name = levelFiles[j].nameWithoutExtension().split("_");
//                Table levelWidget = createLevelWidget(level_name[1]);
//                rowTable.add(levelWidget).pad(20).center();
//            }
//
//            // Add the rowTable to the innerTable
//            innerTable.add(rowTable).row();
//        }

        CustomScrollPane customScrollPane = new CustomScrollPane(innerTable, stage);
        customScrollPane.setScrollingDisabled(true, false);
        customScrollPane.setFillParent(true);

        //customScrollPane.setTouchable(Touchable.enabled);

        stage.addActor(customScrollPane);
        stage.setScrollFocus(scrollPane);
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
                music.pause();
                game.setScreen(new TitleScreen((MoonshipGame) game)); // Change to the screen you want
                return true;
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
                music.pause();
                game.setScreen(new SettingsScreen(game));
                return true;
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                isSettingButtonHovered = true;
                settingsButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isSettingButtonHovered = false;
                settingsButton.setColor(Color.WHITE);
            }
        });

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
    private Table createLevelWidget(final String levelNumber) throws FileNotFoundException {
        Table levelWidget = new Table();
//        levelWidget.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(createPixmap(new Color(0.8f, 0.6f, 1f, 1f))))));

        // Create a label
        Label label = new Label("Level " + levelNumber, createLabelStyle(Color.BLACK));
        label.setFontScale(1.2f);


        // read in a thumbnail and time limit
        Scanner scanner = new Scanner(new File("level_displays/" + "level_" + levelNumber + "_display" + ".txt"));
        String thumbnailPath = "thumbnails/" + scanner.nextLine();
        int timeMinutes = Integer.parseInt(scanner.nextLine());
        int timeSeconds = Integer.parseInt(scanner.nextLine());
        String levelTitle = scanner.nextLine();
        int numOrders = Integer.parseInt(scanner.nextLine());

        if ((!DataManager.INSTANCE.isLevelUnlocked(levelNumber))) {
            // set thumbnail to lock icon
            thumbnailPath = "sprites/locked_thumbnail.png";
//            timeMinutes = " ";
        } else {
            label = new Label(levelTitle, createLabelStyle(Color.BLACK));
        }

        // thumbnail
        Pixmap pix_big = new Pixmap(Gdx.files.internal(thumbnailPath));
        Pixmap pix_small = new Pixmap(300, 300, pix_big.getFormat());
        pix_small.drawPixmap(pix_big,
                0, 0, pix_big.getWidth(), pix_big.getHeight(),
                0, 0, pix_small.getWidth(), pix_small.getHeight()
        );
        Texture thumbTexture = new Texture(pix_small);
        Image thumbnail = new Image(thumbTexture);

        // time limits
        Label timeLabel;
        if (timeSeconds < 10) {
            timeLabel = new Label("Time Limit: " + timeMinutes + ":0" + timeSeconds, createLabelStyle(Color.BLACK));
        } else {
            timeLabel = new Label("Time Limit: " + timeMinutes + ":" + timeSeconds, createLabelStyle(Color.BLACK));
        }
        timeLabel.setFontScale(1f);


        // number of orders
        Label ordersLabel;
        if ((DataManager.INSTANCE.isLevelUnlocked(levelNumber))) {
            // set thumbnail to lock icon
            ordersLabel = new Label(numOrders + " orders", createLabelStyle(Color.BLACK));
        } else {
            ordersLabel = new Label("   ", createLabelStyle(Color.BLACK));
        }

        ordersLabel.setFontScale(1f);



        // Create a button
        TextButton.TextButtonStyle levelButtonStyle = new TextButton.TextButtonStyle();
        BitmapFont levelButtonFont = new BitmapFont();
        levelButtonFont.getData().setScale(1f);
        levelButtonStyle.font = levelButtonFont;
        levelButtonStyle.fontColor = Color.BLACK;
        levelButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(createRoundedRectanglePixmap(100, 45, 15, Color.YELLOW))));
        //levelButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(createRoundedRectanglePixmap(150, 60, 15, Color.YELLOW))));

        ImageButton.ImageButtonStyle lockButtonStyle = new ImageButton.ImageButtonStyle();
        lockButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("sprites/lock_icon.png")));
        ImageButton lockButton = new ImageButton(lockButtonStyle);
        lockButton.getImageCell().size(40, 40);

        ImageButton.ImageButtonStyle unlockButtonStyle = new ImageButton.ImageButtonStyle();
        unlockButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("sprites/unlock_icon.png")));
        ImageButton unlockButton = new ImageButton(unlockButtonStyle);
        unlockButton.getImageCell().size(40, 40);


        boolean isLocked = false;
        //System.out.println(prevLevelName);
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

        levelButton.setSize(10, 20);
        levelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if (levelButton.getText().toString().equals("UNLOCKED")) {
                        nameOfFile = "Level " + levelNumber;
//                        game.setScreen(new GameplayScreen((MoonshipGame) game, levelName));
                        music.pause();
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
                isButtonHovered = true;
                //levelButton.setColor(Color.LIGHT_GRAY);
                if (levelButton.getText().toString().equals("UNLOCKED")) {
                    levelButton.setColor(Color.LIGHT_GRAY);
                }
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isButtonHovered = false;
                //levelButton.setColor(Color.WHITE);
                if (levelButton.getText().toString().equals("UNLOCKED")) {
                    levelButton.setColor(Color.WHITE);
                }
            }
        });

        // Add the label to the top center of the table
        levelWidget.add(label).padBottom(30).colspan(3).center().row();
        levelWidget.add(thumbnail).padBottom(30).colspan(3).center().row();
        levelWidget.add(ordersLabel).padBottom(10).colspan(3).center().row();
        if ((DataManager.INSTANCE.isLevelUnlocked(levelNumber))) {
            levelWidget.add(timeLabel).padBottom(10).colspan(3).center().row();
        } else {
            timeLabel = new Label(" ", createLabelStyle(Color.BLACK));
            levelWidget.add(timeLabel).padBottom(10).colspan(3).center().row();
        }
        // Add the button slightly towards the bottom of the rectangle
        levelWidget.add(levelButton).padBottom(30).colspan(3).center().row();

//        batch.begin();
//        batch.draw(thumbnail, label.getX(), label.getY() - thumbnail.getHeight());
//        batch.end();

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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.7f, 0.9f, 1f, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 80f));
        music.play();
        //stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        backButton.setPosition(30, viewport.getWorldHeight() - 80); // Update button position on resize
        settingsButton.setPosition(viewport.getWorldWidth() - 180, viewport.getWorldHeight() - 80);
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