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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class LevelScreen extends ScreenAdapter {
    private final Game game;
    private Stage stage;

    public LevelScreen(final Game game) {
        this.game = game;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

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

        // Create a ScrollPane with the innerTable as its content
        ScrollPane scrollPane = new ScrollPane(innerTable);
        scrollPane.setScrollingDisabled(true, false); // Disable horizontal scrolling, enable vertical scrolling
        scrollPane.setForceScroll(false, true); // Allow scrolling only when content overflows vertically

        // Set the ScrollPane to fill the outerTable and expand both vertically and horizontally
        scrollPane.setFillParent(true);

        // Add the ScrollPane to the outerTable
        outerTable.add(scrollPane).expand().fill().pad(20);

        Gdx.input.setInputProcessor(stage);
    }

    private Label.LabelStyle createTitleLabelStyle(Color color) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.fontColor = color;
        return style;
    }
    private Table createLevelWidget(String levelName) {
        Table levelWidget = new Table();

        // Create a label
        Label label = new Label(levelName, createLabelStyle(Color.BLACK));

        // Create a button
        TextButton button = new TextButton("LOCKED", createButtonStyle(Color.DARK_GRAY, Color.GRAY));

        // Add the label to the top center of the table
        levelWidget.add(label).padBottom(30).colspan(3).center().row();

        // Add the button slightly towards the bottom of the rectangle
        levelWidget.add(button).padBottom(30).colspan(3).center().row();

        // Set background for the table
        levelWidget.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(createPixmap(Color.LIGHT_GRAY)))));

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

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}






/*
package com.campee.starship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class LevelScreen extends ScreenAdapter {
    private final Game game;
    private Stage stage;

    public LevelScreen(final Game game) {
        this.game = game;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Table table = new Table();
        table.top().left();

        FileHandle levelsFolder = Gdx.files.internal("levels");
        FileHandle[] levelFiles = levelsFolder.list();

        for (final FileHandle levelFile : levelFiles) {
            // Create level name label
            Label levelLabel = new Label(levelFile.nameWithoutExtension(), createLabelStyle(Color.PURPLE));

            // Create LOCKED button
            TextButton lockedButton = new TextButton("LOCKED", createButtonStyle(Color.DARK_GRAY, Color.GRAY));
            lockedButton.setDisabled(true);

            // Create a horizontal container to hold the level name label and LOCKED button
            HorizontalGroup group = new HorizontalGroup();
            group.space(10);
            group.addActor(levelLabel);
            group.addActor(lockedButton);

            // Add the container to the table
            table.add(group).pad(10).row();
        }

        // Add the table to a ScrollPane for scrolling
        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFillParent(true);
        stage.addActor(scrollPane);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.7f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private Label.LabelStyle createLabelStyle(Color color) {
        BitmapFont font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        style.background = new TextureRegionDrawable(new TextureRegion(createRoundedRectangleTexture(300, 60, 15, color)));
        return style;
    }

    private TextButton.TextButtonStyle createButtonStyle(Color upColor, Color downColor) {
        BitmapFont font = new BitmapFont();
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = new TextureRegionDrawable(new TextureRegion(createRoundedRectangleTexture(150, 60, 15, upColor)));
        style.down = new TextureRegionDrawable(new TextureRegion(createRoundedRectangleTexture(150, 60, 15, downColor)));
        style.fontColor = Color.WHITE;
        return style;
    }

    private Texture createRoundedRectangleTexture(int width, int height, int cornerRadius, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(cornerRadius, 0, width - 2 * cornerRadius, height);
        pixmap.fillRectangle(0, cornerRadius, width, height - 2 * cornerRadius);
        pixmap.fillCircle(cornerRadius, cornerRadius, cornerRadius);
        pixmap.fillCircle(cornerRadius, height - cornerRadius - 1, cornerRadius);
        pixmap.fillCircle(width - cornerRadius - 1, cornerRadius, cornerRadius);
        pixmap.fillCircle(width - cornerRadius - 1, height - cornerRadius - 1, cornerRadius);

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
*/

/*
package com.campee.starship;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.table;

public class LevelScreen implements Screen {
    private final MoonshipGame game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;
    private TextButton backButton; // Add a TextButton for the "BACK" button
    private TextButton beginButton;
    private TextButton settingsButton;
    private ExtendViewport viewport;
    private boolean isBackButtonHovered = false;
    private boolean isSettingButtonHovered = false;
    private boolean isDemoButtonHovered = false;
    private ArrayList<TextButton> levelButtons;
    private ArrayList<Label> levelLabels;


    public LevelScreen(final Game game) {
        this.game = (MoonshipGame) game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        //Gdx.input.setInputProcessor(stage);


        // Create BACK button similar to PLAY button in TitleScreen
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
        //stage.addActor(backButton);
        TextButton.TextButtonStyle beginButtonStyle = new TextButton.TextButtonStyle();
        beginButtonStyle.font = buttonFont;
        beginButtonStyle.fontColor = Color.BLACK;
        Pixmap beginButtonPixmap = createRoundedRectanglePixmap(150, 60, 15, Color.valueOf("98FF98"));
        beginButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(beginButtonPixmap)));


        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float buttonX = 30;
        //float buttonY = (screenHeight - buttonHeight) / 2;
//        float buttonHeight = 60;
//        float buttonWidth = 150;
//        float screenWidth = Gdx.graphics.getWidth();
//        float screenHeight = Gdx.graphics.getHeight();
//        float buttonX = 30;
//        float buttonY = (screenHeight - buttonHeight) / 2;

        beginButton = new TextButton("BEGIN DEMO", beginButtonStyle);
        //beginButton.setPosition(buttonX, buttonY);
        beginButton.setPosition(30, Gdx.graphics.getHeight() - 80);
        //beginButton.setPosition(30, Gdx.graphics.getHeight() - 80);
        //beginButton.setSize(Gdx.graphics.getWidth() - 180, Gdx.graphics.getHeight() - 80);
        beginButton.setSize(150, 60);
        beginButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Switch back to the title screen when the BACK button is clicked
                try {
                    game.setScreen(new GameplayScreen((MoonshipGame) game)); // Change to the screen you want
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                isDemoButtonHovered = true;
                beginButton.setColor(Color.LIGHT_GRAY);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                isDemoButtonHovered = false;
                beginButton.setColor(Color.WHITE);
            }
        });
        stage.addActor(beginButton);

        levelButtons = new ArrayList<>();
        levelLabels = new ArrayList<>();
        loadLevelButtons();

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
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    private void loadLevelButtons() {
        FileHandle levelsFolder = Gdx.files.internal("levels");
        FileHandle[] levelFiles = levelsFolder.list();

        Table levelTable = new Table();
        levelTable.defaults().pad(20);
        int buttonsPerRow = 3;
        int buttonsPerColumn = 3;

//        float buttonWidthPercentage = 0.2f; // 20% of the viewport width
//        float buttonHeightPercentage = 0.3f; // 10% of the viewport height
//
//        float buttonWidth = viewport.getWorldWidth() * buttonWidthPercentage;
//        float buttonHeight = viewport.getWorldHeight() * buttonHeightPercentage;
//        float paddingX = (viewport.getWorldWidth() - buttonsPerRow * buttonWidth) / (buttonsPerRow + 1);
//        float paddingY = (viewport.getWorldHeight() - buttonsPerColumn * buttonHeight) / (buttonsPerColumn + 1);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float buttonWidthPercentage = 0.2f; // 20% of the initial screen width
        float buttonHeightPercentage = 0.3f; // 30% of the initial screen height
        float buttonWidth = screenWidth * buttonWidthPercentage;
        float buttonHeight = screenHeight * buttonHeightPercentage;
        float paddingX = (screenWidth - buttonsPerRow * buttonWidth) / (buttonsPerRow + 1);
        float paddingY = (screenHeight - buttonsPerColumn * buttonHeight) / (buttonsPerColumn + 1);

        for (int i = 0; i < levelFiles.length; i++) {
            final FileHandle levelFile = levelFiles[i]; // Retrieve the current level file

            Table buttonTable = new Table();
            TextButton.TextButtonStyle levelButtonStyle = new TextButton.TextButtonStyle();
            levelButtonStyle.font = font;
            levelButtonStyle.fontColor = Color.BLACK;
            levelButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(createRoundedRectanglePixmap(250, (int) buttonHeight, 15, Color.PURPLE))));

            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
            final Label levelNameLabel = new Label(levelFile.nameWithoutExtension(), labelStyle);
            String levelName = levelFile.nameWithoutExtension();

            final TextButton levelButton = new TextButton(levelName, levelButtonStyle);
            levelButton.setDisabled(true);
            buttonTable.add(levelButton).row();
            buttonTable.pack();

            final Button button = new Button(new TextureRegionDrawable(new TextureRegion(new Texture(createRoundedRectanglePixmap((int) buttonTable.getWidth(), (int) buttonTable.getHeight(), 15, Color.PURPLE)))));
            float labelX = button.getX() + button.getWidth() / 2 - levelNameLabel.getWidth() / 2;
            float labelY = button.getY() + button.getHeight() - levelNameLabel.getHeight() - 10; // Adjust the padding as needed
            levelNameLabel.setPosition(labelX, labelY);

            Container<Table> buttonContainer = new Container<>(buttonTable);
            float buttonX = paddingX + (paddingX + buttonWidth) * (i % buttonsPerRow);
            float buttonY = paddingY + (paddingY + buttonHeight) * (i / buttonsPerRow);
            buttonContainer.setSize(buttonWidth, buttonHeight);
            buttonContainer.setPosition(buttonX, buttonY);
            buttonContainer.setActor(button);
            buttonContainer.fill(); // Make the container fill its parent

            button.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    button.setColor(Color.LIGHT_GRAY);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    button.setColor(Color.WHITE);
                    //levelNameLabel.remove(); // Remove the label when not hovering over the button
                }
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    // Handle level button click here
                    // For example, you can load the selected level: game.setScreen(new GameplayScreen((MoonshipGame) game, levelFile));
                    return true;
                }
            });

            levelTable.add(buttonContainer).width(buttonWidth).height(buttonHeight).padLeft(paddingX).padRight(paddingX).padTop(paddingY).padBottom(paddingY);

            if ((i + 1) % buttonsPerRow == 0) {
                levelTable.row();
            }
            stage.addActor(levelNameLabel);
        }


        ScrollPane scrollPane = new ScrollPane(levelTable);
        scrollPane.setFillParent(true);
        scrollPane.setCancelTouchFocus(false); // Disable touch focus cancellation
        scrollPane.setScrollingDisabled(true, false); // Enable only vertical scrolling
        stage.addActor(scrollPane);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.7f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Filled);

        float boxWidth = 200;
        //float boxHeight = 150;
        float boxHeight = 200;
        float boxSpacing = 50;
        float startY = (camera.viewportHeight - boxHeight) / 2;

        shapeRenderer.setColor(new Color(0.8f, 0.6f, 1f, 1f));
        shapeRenderer.rect(boxSpacing, startY, boxWidth, boxHeight); // Level 1 box
        shapeRenderer.rect(boxSpacing * 2 + boxWidth, startY, boxWidth, boxHeight); // Level 2 box
        shapeRenderer.rect(boxSpacing * 3 + boxWidth * 2, startY, boxWidth, boxHeight); // Level 3 box
        shapeRenderer.end();
        batch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < levelButtons.size(); i++) {
            TextButton button = levelButtons.get(i);
            Label label = levelLabels.get(i);

            float labelX = button.getX() + button.getWidth() / 2 - label.getWidth() / 2;
            float labelY = button.getY() + button.getHeight() / 2 + label.getHeight() / 2; // Adjust the padding as needed
            label.setPosition(labelX, labelY);
        }

        batch.begin();
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, "LEVEL SELECT SCREEN");
        float textX = (camera.viewportWidth - layout.width) / 2;
        float textY = camera.viewportHeight - 50;
        font.draw(batch, "LEVEL SELECT SCREEN", textX, textY);

        layout.setText(font, "LEVEL 1");
        float level1X = boxSpacing + (boxWidth - layout.width) / 2;
        //float level1Y = startY + boxHeight / 2 + font.getLineHeight() / 2;
        float level1Y = startY + boxHeight / 2 + font.getLineHeight() / 2 + 50;
        font.getData().setScale(2f);
        font.draw(batch, "LEVEL 1", level1X, level1Y);

        layout.setText(font, "LEVEL 2");
        float level2X = boxSpacing * 2 + boxWidth + (boxWidth - layout.width) / 2;
        //float level2Y = startY + boxHeight / 2 + font.getLineHeight() / 2;
        float level2Y = startY + boxHeight / 2 + font.getLineHeight() / 2 + 50;
        font.draw(batch, "LEVEL 2", level2X, level2Y);

        layout.setText(font, "LEVEL 3");
        float level3X = boxSpacing * 3 + boxWidth * 2 + (boxWidth - layout.width) / 2;
        //float level3Y = startY + boxHeight / 2 + font.getLineHeight() / 2;
        float level3Y = startY + boxHeight / 2 + font.getLineHeight() / 2 + 50;
        font.draw(batch, "LEVEL 3", level3X, level3Y);

        // Update label positions to match their corresponding boxes
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Label) {
                String actorName = actor.getName();
                if (actorName != null && actorName.startsWith("levelLabel")) {
                    String boxName = "box" + actorName.substring(10);
                    Actor box = stage.getRoot().findActor(boxName);
                    if (box != null) {
                        float labelX = box.getX() + box.getWidth() / 2 - actor.getWidth() / 2;
                        float labelY = box.getY() + box.getHeight() + 10;
                        actor.setPosition(labelX, labelY);
                    }
                }
            }
        }

        for (int i = 0; i < levelButtons.size(); i++) {
            TextButton button = levelButtons.get(i);
            Label label = levelLabels.get(i);
            String levelName = button.getText().toString();

            layout = new GlyphLayout();
            layout.setText(font, levelName);

            float labelX = button.getX() + button.getWidth() / 2 - layout.width / 2;
            float labelY = button.getY() + button.getHeight() / 2 + layout.height / 2;

            font.draw(batch, levelName, labelX, labelY);
        }

        batch.end();
        stage.act(delta);
        stage.draw();
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        backButton.setPosition(30, viewport.getWorldHeight() - 80); // Update button position on resize
        //beginButton.setPosition(viewport.getWorldWidth() - 180, viewport.getWorldHeight() - 80);
        float buttonWidth = 150;
        float buttonHeight = 60;
        float buttonXPercentage = 0.5f; // 90% from the left side of the screen
        float buttonYPercentage = 0.4f;
        float buttonX = viewport.getWorldWidth() * buttonXPercentage - buttonWidth / 2;
        float buttonY = viewport.getWorldHeight() * buttonYPercentage - buttonHeight / 2;

        beginButton.setSize(buttonWidth, buttonHeight);
        beginButton.setPosition(buttonX, buttonY);
        //beginButton.setPosition(viewport.getWorldWidth() - 700, viewport.getWorldHeight() - 80);
        settingsButton.setPosition(viewport.getWorldWidth() - 180, viewport.getWorldHeight() - 80);

        stage.getViewport().update(width, height, true);
        //loadLevelButtons();
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
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
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


 */
