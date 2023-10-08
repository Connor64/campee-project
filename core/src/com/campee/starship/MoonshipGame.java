package com.campee.starship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MoonshipGame extends Game {
	 Order orderScreen;
	 TitleScreen titleScreen;
	 SpriteBatch batch;
	 Texture img;

	@Override
	public void create() {
//		orderScreen = new Order();
//		titleScreen = new TitleScreen();
//		setScreen(orderScreen);
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		setScreen((Screen) titleScreen);
		batch = new SpriteBatch();
		orderScreen = new Order();
		titleScreen = new TitleScreen();
	}

	public void render() {
		//Gdx.gl.glClearColor(1, 1, 1, 1);
		ScreenUtils.clear(Color.PURPLE);
		//batch.begin();
		//batch.draw(img, 400 - (img.getWidth() / 2), 300 - (img.getHeight() / 2));
		//batch.end();
		orderScreen.render();
		//titleScreen.render();

		if (orderScreen.press) {
			ScreenUtils.clear(Color.BLUE);
			orderScreen.dispose();
			titleScreen.render();
		}
		//titleScreen.render();
	}
	public void setScreen() {
		ScreenUtils.clear(Color.YELLOW);
		System.out.println("you got here");
	}

	public void dispose() {
		batch.dispose();
		//img.dispose();
	}

	// Other methods in your game class
}
