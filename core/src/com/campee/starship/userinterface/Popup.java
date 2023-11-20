package com.campee.starship.userinterface;

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
import com.campee.starship.screens.GameplayScreen;
import com.campee.starship.objects.*;

public class Popup implements Screen {
    private Stage stage;
    //private float duration; // Duration of the popup in seconds
    //private float timeElapsed; // Time elapsed since the popup was shown
    public boolean visible;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private Label messageLabel;
    public boolean acceptClicked;
    public boolean declineClicked;
    private boolean isAcceptButtonHovered = false;
    private boolean isDeclineButtonHovered = false;
    public TextButton acceptButton;
    public TextButton declineButton;

    float popupWidth;
    float popupHeight;
    float popupX;
    float popupY;
    private ExtendViewport viewport;

    public Popup(final GameplayScreen screen, final String notificationMessage) {
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        //stage = new Stage();
        //this.duration = duration;
        //timeElapsed = 0;

        shapeRenderer = new ShapeRenderer();
        visible = false;
        acceptClicked = false;
        declineClicked = false;

        font = new BitmapFont();

        // Set font color and scale
        font.setColor(1, 1, 0, 1);
        font.getData().setScale(1.25f);

        popupWidth = (float)(Gdx.graphics.getWidth() / 4.21);
        popupHeight = 100; // Set the height of the popup
        popupX = Gdx.graphics.getWidth() - popupWidth; // Position the popup at the right edge
        popupY = 0; // Position the popup at the bottom


        messageLabel = new Label(notificationMessage, new Label.LabelStyle(font, Color.WHITE));
        messageLabel.setFontScale(1f);
        messageLabel.setPosition(670, popupY + 15);

        //add(messageLabel).padRight(100).padTop(5);

        Pixmap acceptBackgroundPixmap = createRoundedRectanglePixmap(200, 50, 10, Color.GREEN); // Adjust size and color
        TextButton.TextButtonStyle acceptButtonStyle = new TextButton.TextButtonStyle();
        acceptButtonStyle.font = font;
        acceptButtonStyle.fontColor = Color.BLACK;
        acceptButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(acceptBackgroundPixmap)));


        Pixmap declineBackgroundPixmap = createRoundedRectanglePixmap(200, 50, 10, Color.RED); // Adjust size and color
        TextButton.TextButtonStyle declineButtonStyle = new TextButton.TextButtonStyle();
        declineButtonStyle.font = font;
        declineButtonStyle.fontColor = Color.BLACK;
        declineButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(declineBackgroundPixmap)));

        // Create buttons
        //final TextButton acceptButton = new TextButton("Accept", acceptButtonStyle);
        //final TextButton declineButton = new TextButton("Decline", declineButtonStyle);
        acceptButton = new TextButton("Accept", acceptButtonStyle);
        declineButton = new TextButton("Decline", declineButtonStyle);

        acceptButton.setWidth(75);
        acceptButton.setHeight(25);
        declineButton.setWidth(75);
        declineButton.setHeight(25);


        // Set button positions
        popupWidth = (float)(Gdx.graphics.getWidth() / 4.21);
        popupHeight = 100; // Set the height of the popup
        popupX = Gdx.graphics.getWidth() - popupWidth; // Position the popup at the right edge
        popupY = 0; // Position the popup at the bottom
        float acceptX = (float)(Gdx.graphics.getWidth() / 1.29);
        float declineX = (float)(Gdx.graphics.getWidth() / 1.11);
//        acceptButton.setPosition(620, popupY);
//        declineButton.setPosition(720, popupY);
        acceptButton.setPosition(acceptX, popupY);
        declineButton.setPosition(declineX, popupY);



            // Add click listeners to buttons
            acceptButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (visible) {
                        acceptClicked = true;
                        screen.playerAttributes.orderInProgress = true;
                        screen.playerAttributes.array.add(screen.order.arrayToString());

                        visible = false;
                    }
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    isAcceptButtonHovered = true;
                    acceptButton.setColor(Color.LIGHT_GRAY);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    isAcceptButtonHovered = false;
                    acceptButton.setColor(Color.WHITE);
                }
            });

            declineButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (visible) {
                        declineClicked = true;
                        if (screen.playerAttributes.array.size() <= 1) {
                            screen.playerAttributes.orderInProgress = false;
                        }
                        //use this for the other thing
                        //screen.playerAttributes.array.remove(1);
                        visible = false;
                    }
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    isDeclineButtonHovered = true;
                    declineButton.setColor(Color.LIGHT_GRAY);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    isDeclineButtonHovered = false;
                    declineButton.setColor(Color.WHITE);
                }
            });

        // Add buttons to the stage
        //if (messageLabel == "No more orders!")) {
            stage.addActor(acceptButton);
            stage.addActor(declineButton);
            stage.addActor(messageLabel);

    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void show() {
        visible = true;
        //timeElapsed = 0;
        //stage.act();
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        float acceptX = (float)(Gdx.graphics.getWidth() / 1.29);
        float declineX = (float)(Gdx.graphics.getWidth() / 1.11);
//        acceptButton.setPosition(620, popupY);
//        declineButton.setPosition(720, popupY);
        acceptButton.setPosition(acceptX, 0);
        declineButton.setPosition(declineX, 0);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

//    public void update(float delta) {
//        if (visible) {
//            timeElapsed += delta;
//            if (timeElapsed >= duration) {
//                hide();
//            }
//        }
//    }

    public void hide() {
        visible = false;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isVisible() {
        return visible;
    }

    public Stage getStage() {
        return stage;
    }

    public boolean acceptClicked() {
        return acceptClicked;
    }
    public boolean declineClicked() {
        return declineClicked;
    }

    public void render() {
        if (visible) {
            // Clear the background
//            Gdx.gl.glEnable(GL20.GL_BLEND);
//            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            shapeRenderer.setColor(new Color(0, 0, 0, 0.7f));
//            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//            shapeRenderer.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.5f));
            shapeRenderer.rect(popupX, popupY, popupWidth, popupHeight); // Define the popup area
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);


//            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            shapeRenderer.setColor(new Color(0, 0, 0, 0.5f)); // Adjust the alpha (transparency) value here
//            //popupWidth = 190; // Set the width of the popup
//            popupWidth = (float)(Gdx.graphics.getWidth() / 4.21);
//            popupHeight = 100; // Set the height of the popup
//            System.out.println(popupWidth);
//            popupX = Gdx.graphics.getWidth() - popupWidth; // Position the popup at the right edge
//            //popupX = Gdx.graphics.getWidth();
//            popupY = 0; // Position the popup at the bottom
//            shapeRenderer.rect(popupX, popupY, popupWidth, popupHeight); // Define the popup area
//            shapeRenderer.end();
            //Gdx.gl.glDisable(GL20.GL_BLEND);


            stage.act();
            stage.draw();
        }
    }


//    public void draw() {
//        if (visible) {
//            stage.draw();
//        }
//    }

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


//    public void dispose() {
//        stage.dispose();
//    }
}