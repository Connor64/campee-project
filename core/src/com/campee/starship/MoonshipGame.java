package com.campee.starship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.FileNotFoundException;

public class MoonshipGame extends Game {
	//GameScreen gameScreen;
	//private Order order;
	private TitleScreen titleScreen;
	//private TestScreen testScreen;

	public SpriteBatch batch;
//	public BitmapFont font;


	@Override
	public void create() {
		titleScreen = new TitleScreen(this);
		//testScreen = new TestScreen(this);
		//order = new Order(this);
//		gameScreen = new GameScreen(this);
//		setScreen(gameScreen);

		batch = new SpriteBatch();
		setScreen(new TitleScreen(this));
		//setScreen(new GameplayScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
//		font.dispose();
	}

}