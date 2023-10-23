package com.campee.starship;

import com.badlogic.gdx.Gdx;
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

public class Popup {
    private Stage stage;
    public boolean visible;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private Label messageLabel;
    private boolean acceptClicked;
    private boolean declineClicked;
    private boolean isAcceptButtonHovered = false;
    private boolean isDeclineButtonHovered = false;

    public Popup(final GameplayScreen screen, final String notificationMessage) {
        stage = new Stage();
        shapeRenderer = new ShapeRenderer();
        visible = false;
        acceptClicked = false;
        declineClicked = false;

        font = new BitmapFont();

        // Set font color and scale
        font.setColor(1, 1, 0, 1);
        font.getData().setScale(1.5f);

        messageLabel = new Label(notificationMessage, new Label.LabelStyle(font, Color.WHITE));
        messageLabel.setFontScale(1.5f);
        messageLabel.setPosition(Gdx.graphics.getWidth() / 2 - messageLabel.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 100);



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
        final TextButton acceptButton = new TextButton("Accept", acceptButtonStyle);
        final TextButton declineButton = new TextButton("Decline", declineButtonStyle);

        acceptButton.setWidth(100);
        acceptButton.setHeight(50);
        declineButton.setWidth(100);
        declineButton.setHeight(50);

        // Set button positions
        acceptButton.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 25);
        declineButton.setPosition(Gdx.graphics.getWidth() / 2 + 20, Gdx.graphics.getHeight() / 2 - 25);

        // Add click listeners to buttons
        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                acceptClicked = true;
                screen.playerAttributes.orderInProgress = true;
                screen.playerAttributes.array.add(screen.order.arrayToString());



                visible = false;
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
                declineClicked = true;
                screen.playerAttributes.orderInProgress = false;
                //use this for the other thing
                //screen.playerAttributes.array.remove(1);
                visible = false;
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
        stage.addActor(acceptButton);
        stage.addActor(declineButton);
        stage.addActor(messageLabel);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void show() {
        visible = true;
    }

    public void hide() {
        visible = false;
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
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.7f));
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();

            stage.act();
            stage.draw();
        }
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