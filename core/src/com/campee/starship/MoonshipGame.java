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
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MoonshipGame extends Game {
    //GameScreen gameScreen;
	//private Order order;
	private TitleScreen titleScreen;
	//private ExtendViewport viewport;
	//private TestScreen testScreen;

	@Override
	public void create() {
		titleScreen = new TitleScreen(this);
		//testScreen = new TestScreen(this);
		//order = new Order(this);
		//gameScreen = new GameScreen(this);
		//viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		setScreen(titleScreen);
	}

}
