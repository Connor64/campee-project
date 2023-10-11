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

	private Order order;
	private TitleScreen titleScreen;

	@Override
	public void create() {
		//titleScreen = new TitleScreen(this);
		order = new Order(this);
		setScreen(order);
	}

}
