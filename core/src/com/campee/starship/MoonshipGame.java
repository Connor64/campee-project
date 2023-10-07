package com.campee.starship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class MoonshipGame extends Game {
	 Order orderScreen;
	 TitleScreen titleScreen;

	@Override
	public void create() {
		orderScreen = new Order();
		titleScreen = new TitleScreen();
		setScreen(orderScreen);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setScreen((Screen) titleScreen);
	}

	// Other methods in your game class
}
