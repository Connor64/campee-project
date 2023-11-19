package com.campee.starship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.FileNotFoundException;
import java.io.IOException;

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

    public LevelScreen(final Game game) {
        this.game = game;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        //stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Table outerTable = new Table();
        outerTable.setFillParent(true);
        stage.addActor(outerTable);

        Table innerTable = new Table();
        outerTable.add(innerTable).center();

        FileHandle levelsFolder = Gdx.files.internal("levels");
        FileHandle[] levelFiles = levelsFolder.list();

        Label titleLabel = new Label("LEVEL SELECT SCREEN", createTitleLabelStyle(Color.BLACK));

        // Add title label to the top of the window with some padding
        innerTable.add(titleLabel).padTop(50).colspan(3).center().row();

        for (int i = 0; i < levelFiles.length; i += 3) {
            Table rowTable = new Table(); // Create a new table for each row

            // Add up to three level widgets in this row
            for (int j = i; j < Math.min(i + 3, levelFiles.length); j++) {
                Table levelWidget = createLevelWidget(levelFiles[j].nameWithoutExtension());
                rowTable.add(levelWidget).pad(40).center();
            }

            // Add the rowTable to the innerTable
            innerTable.add(rowTable).row();
        }

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
    private Table createLevelWidget(final String levelName) {
        Table levelWidget = new Table();
//        levelWidget.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(createPixmap(new Color(0.8f, 0.6f, 1f, 1f))))));

        // Create a label
        Label label = new Label(levelName, createLabelStyle(Color.BLACK));
        label.setFontScale(1.2f);

        Label orderLabel = new Label("2 orders", createLabelStyle(Color.BLACK));
        orderLabel.setFontScale(1f);

        // Create a button
        TextButton.TextButtonStyle levelButtonStyle = new TextButton.TextButtonStyle();
        BitmapFont levelButtonFont = new BitmapFont();
        levelButtonFont.getData().setScale(1f);
        levelButtonStyle.font = levelButtonFont;
        levelButtonStyle.fontColor = Color.BLACK;
        levelButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(createRoundedRectanglePixmap(100, 45, 15, Color.YELLOW))));
        //levelButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(createRoundedRectanglePixmap(150, 60, 15, Color.YELLOW))));

        final TextButton levelButton = new TextButton("UNLOCKED", levelButtonStyle);
        levelButton.setSize(10, 20);
        levelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    nameOfFile = levelName;
                    game.setScreen(new GameplayScreen((MoonshipGame) game, levelName));
                    //System.out.println("hereeee");
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
                levelButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isButtonHovered = false;
                levelButton.setColor(Color.WHITE);
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