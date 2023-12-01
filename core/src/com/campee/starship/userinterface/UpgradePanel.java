package com.campee.starship.userinterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.campee.starship.managers.DataManager;
import com.campee.starship.objects.Upgrade;

public class UpgradePanel extends Table {
    private HoverableButton purchaseButton;
    private Upgrade upgrade;

    public UpgradePanel(Upgrade _upgrade) {
        upgrade = _upgrade;

        // Create name label
        Label.LabelStyle nameStyle = new Label.LabelStyle();
        BitmapFont nameFont = new BitmapFont();
        nameFont.getData().scale(2);
        nameStyle.font = nameFont;
        nameStyle.fontColor = Color.BLACK;
        Label nameLabel = new Label(upgrade.getName(), nameStyle);

        Label.LabelStyle costStyle = new Label.LabelStyle();
        costStyle.font = new BitmapFont();
        costStyle.fontColor = Color.BLACK;
        Label costlabel = new Label("Cost: " + upgrade.getCost(), costStyle);

        if (DataManager.INSTANCE.getCoinCount() >= upgrade.getCost()) {
            purchaseButton = HoverableButton.generate("PURCHASE", true, Color.GREEN, Color.BLACK, 1.5f);
        } else {
            purchaseButton = HoverableButton.generate("NEED FUNDS", true, Color.RED, Color.BLACK, 1.5f);
            purchaseButton.setHoverable(false, false);
        }

        purchaseButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!purchaseButton.isDisabled() && DataManager.INSTANCE.getCoinCount() >= upgrade.getCost()) {
                    DataManager.INSTANCE.addCoins(upgrade.getCost() * -1, true);
                    DataManager.INSTANCE.addPurchase(upgrade.getID(), true);
                    purchaseButton.setHoverable(false, true);
                }
                return true;
            }
        });
        if (DataManager.INSTANCE.isUpgradePurchased(upgrade.getID())) {
            purchaseButton.setHoverable(false, true);
        }

        setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(createPixmap(new Color(0.8f, 0.6f, 1f, 1f))))));

        add(nameLabel).padTop(5).colspan(3).center().row();
        add(upgrade.getIcon()).pad(50).colspan(3).center().row();
        add(costlabel).padBottom(15).colspan(3).center().row();
        add(purchaseButton).padBottom(5).colspan(3).center().row();

        pad(50);
    }

    public Upgrade getUpgrade() {
        return upgrade;
    }

    private Pixmap createPixmap(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        return pixmap;
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
