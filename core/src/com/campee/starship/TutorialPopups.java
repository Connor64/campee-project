package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TutorialPopups implements Screen {
    private GameplayScreen screen;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    ScheduledExecutorService scheduler;
    private BitmapFont font;
    private boolean OKClicked;
    private boolean countDone;
    private Label messageLabel;
    private TextButton OKButton;
    private int currentStep;
    private String[] tutorialMessages = {
            "Step 1: Move the WASD or \narrow keys to move around the map!",
            "Step 2: Click the accept button\nto accept an order!",
            "Step 3: Travel to the pickup building and \n press P to pick up the order!",
            "Step 4: Make sure to collect coins \n on the way!",
            "Step 5: Travel to the destination building and \n press d to drop off the order!"
    };

    private float popupWidth;
    private float popupHeight;
    private float popupX;
    private float popupY;
    private ExtendViewport viewport;

    public TutorialPopups(final GameplayScreen screen) {
        this.screen = screen;
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        scheduler = Executors.newSingleThreadScheduledExecutor();

        shapeRenderer = new ShapeRenderer();
        OKClicked = false;
        countDone = false;
        currentStep = 0;

        font = new BitmapFont();
        font.setColor(1, 1, 1, 1); // White color

        popupWidth = (float) (Gdx.graphics.getWidth() / 4.21);
        popupHeight = 100;
        popupX = 0;
        popupY = 0;

        messageLabel = new Label(tutorialMessages[currentStep], new Label.LabelStyle(font, Color.WHITE));
        messageLabel.setFontScale(1f);
        messageLabel.setPosition(popupX + 10, popupY + 50);

        Pixmap OKBackgroundPixmap = createRoundedRectanglePixmap(200, 50, 10, Color.GREEN);
        TextButton.TextButtonStyle OKButtonStyle = new TextButton.TextButtonStyle();
        OKButtonStyle.font = font;
        OKButtonStyle.fontColor = Color.BLACK;
        OKButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(OKBackgroundPixmap)));

        OKButton = new TextButton("OK", OKButtonStyle);
        OKButton.setWidth(75);
        OKButton.setHeight(25);
        OKButton.setPosition(popupX + 10, popupY + 5);

        OKButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(screen.isTutorialPopupsVisible()) {
                    //System.out.println("OK Button Clicked!");
                    OKClicked = true;
                    if (currentStep < tutorialMessages.length - 1) {
                        // If there are more steps, increment the step and update the label
                        currentStep++;
                        //count 15 seconds and let them play, then display next step
                        hideStepPopup();
                        messageLabel.setText(tutorialMessages[currentStep]);
                        scheduleStep();

                    } else {
                        // If no more steps, close the pop-up
                        screen.setTutorialPopupsVisible(false);
                    }
                }
            }
        });

        stage.addActor(OKButton);
        stage.addActor(messageLabel);
    }

    @Override
    public void show() {
        screen.isTutorialPopupsVisible = true;
    }
    public Stage getStage() {
        return stage;
    }

    @Override
    public void render(float delta) {

    }

    public void render() {
        if (screen.isTutorialPopupsVisible()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.5f));
            shapeRenderer.rect(popupX, popupY, popupWidth, popupHeight);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            stage.act();
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    private void scheduleStep() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                //if (!popupInAction) {
                showStepPopup();
                stopScheduler();
                //}
            }
        }, 15, TimeUnit.SECONDS); // Schedule the next popup 15 seconds after the first one
    }

    public void stopScheduler() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    public void hideStepPopup() {
        screen.tutorialPopups.hide(); // Hide the popup
    }

    public void showStepPopup() {
        screen.tutorialPopups.show(); // Display the popup
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        screen.isTutorialPopupsVisible = false;
    }

    @Override
    public void dispose() {

    }

    // Other methods...

    private Pixmap createRoundedRectanglePixmap(int width, int height, int cornerRadius, Color color) {
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
