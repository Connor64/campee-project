package com.campee.starship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class CustomScrollPane extends ScrollPane {

    public CustomScrollPane(Actor widget, Stage stage) {
        super(widget);
        setupScrollBar();
        stage.addActor(this);
//        widget.setScrollFocus(this);
    }

    private void setupScrollBar() {
        // Create a new skin for the scroll bar
        Skin skin = new Skin();

        // Create a pixmap for the scroll bar track
        Pixmap trackPixmap = new Pixmap(10, 1, Pixmap.Format.RGB888);
        trackPixmap.setColor(Color.LIGHT_GRAY);
        trackPixmap.fill();
        skin.add("track", new Texture(trackPixmap));

        // Create a pixmap for the scroll bar knob
        Pixmap knobPixmap = new Pixmap(10, 10, Pixmap.Format.RGB888);
        knobPixmap.setColor(Color.DARK_GRAY);
        knobPixmap.fill();
        skin.add("knob", new Texture(knobPixmap));

        // Create a ScrollBar widget using the skin
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScroll = skin.getDrawable("track");
        scrollPaneStyle.vScrollKnob = skin.getDrawable("knob");

        setFadeScrollBars(false);

        setStyle(scrollPaneStyle);
    }
}