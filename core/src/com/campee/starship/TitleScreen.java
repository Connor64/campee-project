package com.campee.starship;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

public class TitleScreen implements Screen {
    private Game game;
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout glyphLayout;
    private Texture img;
    private Stage stage;

    public TitleScreen(final Game game) {
        this.game = game;
        batch = new SpriteBatch();
        //font = new BitmapFont(Gdx.files.internal("myFont.fnt"), Gdx.files.internal("myFont.png"), false);
        font = new BitmapFont();

        // Set font color and scale
        font.setColor(1, 1, 0, 1);
        font.getData().setScale(3);

        img = new Texture(Gdx.files.internal("IMG_0339.PNG"));

        glyphLayout = new GlyphLayout();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        ScreenUtils.clear(1, 0.8f, 1, 1);

        BitmapFont font = new BitmapFont();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        font.getData().setScale(1.5f);
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.BLACK;

        // Create a pixmap for the background (rounded rectangle)
        Pixmap backgroundPixmap = createRoundedRectanglePixmap(300, 120, 15, Color.valueOf("98FF98")); // Light green color

        // Set the background of the button
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(backgroundPixmap)));

        TextButton button = new TextButton("PLAY", textButtonStyle);
        button.setColor(Color.WHITE);
        float buttonWidth = 200;
        float buttonHeight = 80;
        float buttonX = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        float buttonY = (Gdx.graphics.getHeight() - buttonHeight + 100) / 2;
        button.setPosition(buttonX, buttonY);
        button.setSize(buttonWidth, buttonHeight);
        button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Switch to another screen when the button is clicked
                game.setScreen(new TestScreen(game)); // Change to the screen you want
                return true;
            }
        });

        stage.addActor(button);
    }

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

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0.8f, 1, 1);

        batch.begin();
        glyphLayout.setText(font, "MOONSHIPS");
        float textX = (Gdx.graphics.getWidth() - glyphLayout.width) / 2;
        float textY = Gdx.graphics.getHeight() * 3 / 4 + glyphLayout.height;
        font.draw(batch, glyphLayout, textX, textY);

        float scaleFactor = 0.7f;
        float imgWidth = img.getWidth() * scaleFactor;
        float imgHeight = img.getHeight() * scaleFactor;
        float imgX = (Gdx.graphics.getWidth() - imgWidth) / 2;
        float imgY = 30; // Adjust this value to move the image up or down
        batch.draw(img, imgX, imgY, imgWidth, imgHeight);


        batch.end();
        stage.act(delta);
        stage.draw();
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
        batch.dispose();
        font.dispose();
        img.dispose();
        stage.dispose();
    }
}
