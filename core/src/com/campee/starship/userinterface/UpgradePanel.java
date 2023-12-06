package com.campee.starship.userinterface;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.campee.starship.managers.DataManager;
import com.campee.starship.objects.Upgrade;

import java.util.ArrayList;

public class UpgradePanel extends Table {
    private HoverableButton purchaseButton, toggleButton;
    private Upgrade upgrade;
    private ArrayList<UpgradePanel> panels;

    public UpgradePanel(Upgrade _upgrade, final ArrayList<UpgradePanel> panels) {
        upgrade = _upgrade;
        this.panels = panels;

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

        purchaseButton = HoverableButton.generate("PURCHASE", true, Color.GREEN, Color.BLACK, 1.5f);
        purchaseButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!purchaseButton.isDisabled() && DataManager.INSTANCE.getCoinCount() >= upgrade.getCost()) {
                    DataManager.INSTANCE.addCoins(upgrade.getCost() * -1, true);
                    DataManager.INSTANCE.addPurchase(upgrade.getID(), true);
                    DataManager.INSTANCE.toggleUpgrade(upgrade.getID(), true);

                    for (UpgradePanel panel : panels) {
                        panel.updateButtons();
                    }
                }
                return true;
            }
        });

        toggleButton = HoverableButton.generate("DISABLED", true, Color.GREEN, Color.BLACK, 1.5f);
        toggleButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (DataManager.INSTANCE.isUpgradePurchased(upgrade.getID())) {
                    DataManager.INSTANCE.toggleUpgrade(upgrade.getID(), true);

                    for (UpgradePanel panel : panels) {
                        panel.updateButtons();
                    }
                }
                return true;
            }
        });

        updateButtons();

        setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(createPixmap(new Color(0.8f, 0.6f, 1f, 1f))))));

        add(nameLabel).padTop(5).colspan(3).center().row();
        add(upgrade.getIcon()).pad(50).colspan(3).center().row();
        add(costlabel).padBottom(15).colspan(3).center().row();
        add(purchaseButton).padBottom(5).colspan(3).center().row();
        add(toggleButton).padBottom(0).colspan(3).center().row();

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

    public void updateButtons() {
        if (DataManager.INSTANCE.isUpgradePurchased(upgrade.getID())) {
            purchaseButton.setText("PURCHASE");
            purchaseButton.setHoverable(false, true);

            if (DataManager.INSTANCE.isUpgradeActive(upgrade.getID())) {
                toggleButton.setText("ENABLED");
                toggleButton.setBackground(Color.GREEN);
                toggleButton.setHoverable(true, false);
            } else {
                toggleButton.setText("DISABLED");
                toggleButton.setHoverable(false, true);
            }

        } else {
            toggleButton.setHoverable(false, true);

            if (DataManager.INSTANCE.getCoinCount() >= upgrade.getCost()) {
                purchaseButton.setText("PURCHASE");
                purchaseButton.setBackground(Color.GREEN);
                purchaseButton.setHoverable(true, false);
            } else {
                purchaseButton.setText("NEED FUNDS");
                purchaseButton.setBackground(Color.RED);
                purchaseButton.setHoverable(false, false);
            }
        }
    }
}
