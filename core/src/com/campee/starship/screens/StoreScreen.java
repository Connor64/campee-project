package com.campee.starship.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.campee.starship.managers.AssetManager;
import com.campee.starship.managers.DataManager;
import com.campee.starship.objects.Upgrade;
import com.campee.starship.userinterface.CustomScrollPane;
import com.campee.starship.userinterface.HoverableButton;
import com.campee.starship.userinterface.UpgradePanel;

import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

public class StoreScreen implements Screen {
    private final Game game;
    private Stage stage;
    private HoverableButton backButton;
    private ExtendViewport viewport;

    private Label shopBanner, coinLabel;

    public StoreScreen (final Game game) {
        this.game = game;
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        backButton = HoverableButton.generate("BACK", true, Color.valueOf("98FF98"), Color.BLACK, 1.5f);
        backButton.setPosition(30, Gdx.graphics.getHeight() - 80);
        backButton.setSize(150, 60);
        backButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    game.setScreen(new LevelScreen(game));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
        });

        Label.LabelStyle coinStyle = new Label.LabelStyle();
        BitmapFont coinFont = new BitmapFont();
        coinFont.getData().scale(1.5f);
        coinStyle.font = coinFont;
        coinStyle.fontColor = Color.BLACK;

        coinLabel = new Label("Coins " + DataManager.INSTANCE.getCoinCount(), coinStyle);
        coinLabel.setAlignment(Align.right);
        coinLabel.setSize(Gdx.graphics.getWidth(), 120);
        coinLabel.setPosition(-10, Gdx.graphics.getHeight() - coinLabel.getHeight());

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        BitmapFont labelFont = new BitmapFont();
        labelFont.getData().scale(5);
        titleStyle.font = labelFont;
        titleStyle.fontColor = Color.BLACK;

        shopBanner = new Label("THE BITS SHOP", titleStyle);
        shopBanner.setAlignment(Align.center);
        shopBanner.setSize(Gdx.graphics.getWidth(), 240);
        shopBanner.setPosition(0, Gdx.graphics.getHeight() - shopBanner.getHeight());

        Table upgradeRow = new Table();

        Set<Map.Entry<String, Upgrade>> upgradeSet = AssetManager.INSTANCE.getUpgrades();
        for (Map.Entry<String, Upgrade> entry : upgradeSet) {
            UpgradePanel panel = new UpgradePanel("sprites/coin.png", entry.getValue());
            upgradeRow.add(panel).pad(15);
        }

        CustomScrollPane customScrollPane = new CustomScrollPane(upgradeRow, stage);
        customScrollPane.setScrollingDisabled(false, true);
        customScrollPane.setFillParent(true);

        stage.addActor(customScrollPane);
        stage.addActor(shopBanner);
        stage.addActor(coinLabel);
        stage.addActor(backButton);
    }

    @Override
    public void show() {
        // This method is called when the screen becomes the active screen.
        // You can use it to set up any initial state or resources.
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a background color
        Gdx.gl.glClearColor(0.7f, 0.9f, 1f, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);

        coinLabel.setText("Coins: " + DataManager.INSTANCE.getCoinCount());

//        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 80f));
        // Update and render game elements
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void pause() {
        // Handle pausing the screen (if necessary)
    }

    @Override
    public void resume() {
        // Handle resuming the screen (if necessary)
    }

    @Override
    public void hide() {
        // This method is called when the screen is no longer the active screen.
        // You can use it to dispose of resources or clean up.
    }

    @Override
    public void dispose() {
        // Dispose of any resources (textures, sounds, stages, etc.) used by the screen
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        backButton.setPosition(30, viewport.getWorldHeight() - 80); // Update button position on resize

        shopBanner.setPosition(
                Math.max(0.0f, (viewport.getWorldWidth() - shopBanner.getWidth()) / 2.0f),
                viewport.getWorldHeight() - shopBanner.getHeight()
        );

        coinLabel.setWidth(viewport.getWorldWidth());
        coinLabel.setPosition(-10, viewport.getWorldHeight() - coinLabel.getHeight());

        stage.getViewport().update(width, height, true);
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
