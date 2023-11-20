package com.campee.starship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ScreenManager {
    private Game game;

    public ScreenManager(Game game) {
        this.game = game;
    }

    public void setScreen(Screen screen) {
        game.setScreen(screen);
    }
}
