package com.campee.starship.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class Upgrade {
    private final String ID;
    private final String NAME;
    private final int COST;
    private Image icon;

    public Upgrade(String id, String name, int cost, String spritePath) {
        ID = id;
        NAME = name;
        COST = cost;

        Texture texture = new Texture(Gdx.files.internal("sprites/" + spritePath));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
        icon = new Image(new SpriteDrawable(new Sprite(region)));
        icon.setOrigin(icon.getWidth() / 2, icon.getHeight() / 2);
        icon.scaleBy(3);
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return NAME;
    }

    public int getCost() {
        return COST;
    }

    public Image getIcon() {
        return icon;
    }
}
