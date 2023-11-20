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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class OrderScreen extends Actor {
    private String[] items;
    private BitmapFont font;
    boolean visible;
    private Stage stage;



    public OrderScreen(float x, float y, String[] items, BitmapFont font) {
        this.setPosition(x, y);
        this.items = items;
        this.font = font;
        this.visible= true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float itemHeight = font.getLineHeight();
        if (visible) {
            // Draw each item in the list
            for (int i = 0; i < items.length; i++) {
                float itemY = getY() + getHeight() - (i + 1) * itemHeight;
                font.draw(batch, items[i], getX(), itemY);
            }
        }

    }



//    public Pixmap createRoundedRectanglePixmap(int width, int height, int cornerRadius, Color color) {
//        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
//        pixmap.setColor(color);
//
//        // Draw rounded rectangle
//        pixmap.fillRectangle(cornerRadius, 0, width - 2 * cornerRadius, height);
//        pixmap.fillRectangle(0, cornerRadius, width, height - 2 * cornerRadius);
//        pixmap.fillCircle(cornerRadius, cornerRadius, cornerRadius);
//        pixmap.fillCircle(cornerRadius, height - cornerRadius - 1, cornerRadius);
//        pixmap.fillCircle(width - cornerRadius - 1, cornerRadius, cornerRadius);
//        pixmap.fillCircle(width - cornerRadius - 1, height - cornerRadius - 1, cornerRadius);
//
//        return pixmap;
//    }
public void show() {
    visible = true;
}
    public void hide() {
        visible = false;
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void render() {

    }
}