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

public class TutorialPopups implements Screen {
    private GameplayScreen screen;
    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private boolean OKClicked;
    public boolean visible;
    private Label messageLabel;
    private TextButton OKButton;
    private int currentStep;
    private String[] tutorialMessages = {
            "Step 1: Move the WASD or \narrow keys to move!",
            "Step 2: Press P to \npick up an order now!",
            "Step 3: Deliver orders \nto marked locations.",
            "Step 4: Avoid obstacles \nto keep your health up.",
            "Step 5: Collect coins \nfor extra points!"
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

        shapeRenderer = new ShapeRenderer();
        OKClicked = false;
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
                if(visible) {
                    System.out.println("OK Button Clicked!");
                    OKClicked = true;
                    if (currentStep < tutorialMessages.length - 1) {
                        // If there are more steps, increment the step and update the label
                        currentStep++;
                        messageLabel.setText(tutorialMessages[currentStep]);
                    } else {
                        // If no more steps, close the pop-up
                       visible = false;
                    }
                }
            }
        });

        stage.addActor(OKButton);
        stage.addActor(messageLabel);
    }

    @Override
    public void show() {
    }

    public boolean isVisible() {
        return visible;
    }


    @Override
    public void render(float delta) {

    }

    public void render() {
        if (visible) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.5f));
            shapeRenderer.rect(popupX, popupY, popupWidth, popupHeight);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            stage.act();
            stage.draw();

            System.out.println("TutorialPopups.render() - OKClicked: " + OKClicked);

            if (Gdx.input.isTouched()) {
                float touchX = Gdx.input.getX();
                float touchY = Gdx.input.getY();
                System.out.println("Touch coordinates: " + touchX + ", " + touchY);
            }
        }
    }

    @Override
    public void resize(int width, int height) {

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
