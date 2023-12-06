package com.campee.starship.userinterface;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class HoverableButton extends TextButton {

    private boolean hoverable = false;
    private Color background;

    public static HoverableButton generate(String text, boolean hoverable, Color background, Color textColor, float textScale) {
        BitmapFont buttonFont = new BitmapFont();
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        buttonFont.getData().setScale(textScale);
        style.font = buttonFont;
        style.fontColor = textColor;
        Pixmap backgroundPixmap = createRoundedRectanglePixmap(150, 60, 15, background);
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture(backgroundPixmap)));

        return new HoverableButton(text, style, hoverable, background);
    }

    private HoverableButton(String text, TextButtonStyle style, boolean _hoverable, Color background) {
        super(text, style);

        setColor(Color.WHITE);
        hoverable = _hoverable;

        this.background = background;

        addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (hoverable) {
                    setColor(Color.LIGHT_GRAY);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if (hoverable) {
                    setColor(Color.WHITE);
                }
            }
        });
    }

    private static Pixmap createRoundedRectanglePixmap(int width, int height, int cornerRadius, Color color) {
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

    public boolean isHoverable() {
        return hoverable;
    }

    public void setHoverable(boolean hoverable, boolean disabled) {
        this.hoverable = hoverable;

        setColor(disabled ? Color.LIGHT_GRAY : Color.WHITE);
        setDisabled(disabled);

        if (disabled) {
            setBackground(Color.DARK_GRAY);
        } else {
            setBackground(background);
        }
    }

    public void setBackground(Color color) {
        this.background = color;

        Pixmap backgroundPixmap = createRoundedRectanglePixmap(150, 60, 15, color);
        getStyle().up = new TextureRegionDrawable(new TextureRegion(new Texture(backgroundPixmap)));
    }
}
