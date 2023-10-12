package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class OrderScreen extends Actor {
    public boolean visible;
    private Stage stage;
    private Table table;
    private TextButton acceptButton;
    KeyProcessor keyProcessor;

    //public PlayerAttributes playerAttributes;

    public OrderScreen() {
        keyProcessor = new KeyProcessor();

        //visible = false;

        // Create a stage with a FitViewport for managing UI elements
        stage = new Stage(new FitViewport((float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight()));

        // Create a table to organize UI elements
        table = new Table();
        table.setFillParent(true);

        BitmapFont font = new BitmapFont();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        font.getData().setScale(1.5f);
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.BLACK;

        // Create a 1x1 pixel texture with a color (e.g., light gray)
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY); // Set the desired color
        pixmap.fill();
        Texture backgroundTexture = new Texture(pixmap);

        // Create a NinePatch from the background texture
        NinePatchDrawable backgroundDrawable = new NinePatchDrawable(new NinePatch(backgroundTexture, 10, 10, 10, 10));
        this.table.setBackground(backgroundDrawable);

        // Create and configure your popup's content, buttons, and options.
        TextButton.TextButtonStyle acceptButtonStyle = new TextButton.TextButtonStyle();
        acceptButtonStyle.font = new BitmapFont();
        acceptButtonStyle.fontColor = Color.BLACK;

        Pixmap buttonBackground = createRoundedRectanglePixmap(200, 80, 15, Color.LIGHT_GRAY);

        acceptButtonStyle.up = new TextureRegionDrawable(new Texture(buttonBackground));

        acceptButton = new TextButton("Accept", acceptButtonStyle); // Replace 'skin' with your skin instance.
        acceptButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                visible = false;
                Gdx.input.setInputProcessor(keyProcessor);


            }
        });

        this.table.add(acceptButton).pad(10.0F);
        Gdx.input.setInputProcessor(this.stage);
        this.stage.addActor(this.table);

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
    public void draw(Batch batch, float parentAlpha) {
        // Draw your popup's content here.
        if (visible) {
                stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
                stage.draw();
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
