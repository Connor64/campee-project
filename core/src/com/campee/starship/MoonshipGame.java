package com.campee.starship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MoonshipGame extends Game {
    //GameScreen gameScreen;
	//private OrderScreen order;
	//private TitleScreen titleScreen;
	private TestScreen testScreen;

	@Override
	public void create() {
		//titleScreen = new TitleScreen(this);
		testScreen = new TestScreen(this);
		//order = new OrderScreen(this);
		//gameScreen = new GameScreen(this);
		setScreen(testScreen);
	}

}
