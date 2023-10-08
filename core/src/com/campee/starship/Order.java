package com.campee.starship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
public class Order {
    Boolean press = false;
    Stage stage;
    TitleScreen titleScreen;
    MoonshipGame game;
    public Order () {
        stage = new Stage();
        game = new MoonshipGame();
        titleScreen = new TitleScreen();
        Gdx.input.setInputProcessor(stage);
        //ScreenUtils.clear(1, 0.8f, 1, 1);
        BitmapFont font = new BitmapFont();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        textButtonStyle.fontColor = Color.BLACK;
        TextButton button = new TextButton("poke the button!", textButtonStyle);
        button.setColor(Color.WHITE);
        button.setX(250);
        button.setY(250);
        button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen();
                press = true;

                System.out.println("oop! you poked me!");
                return true;
            }
        });

        stage.addActor(button);
    }

    public void render() {
        // stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
