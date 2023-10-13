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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import java.io.FileNotFoundException;

public class MoonshipGame extends Game {
	//GameScreen gameScreen;
	//private Order order;
	private TitleScreen titleScreen;
	//private TestScreen testScreen;

	public SpriteBatch batch;
//	public BitmapFont font;
	static int interval;
	static Timer timer;


	@Override
	public void create() {
//		titleScreen = new TitleScreen(this);
//		//testScreen = new TestScreen(this);
//		//order = new Order(this);
////		gameScreen = new GameScreen(this);
////		setScreen(gameScreen);
//
		batch = new SpriteBatch();
//		setScreen(new TitleScreen(this));
		//setScreen(new GameplayScreen(this));
		setScreen(new SplashScreen(this));
		final Game game = this;
		timer = new Timer();
		interval = 2;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
//						setScreen(new TitleScreen((MoonshipGame) game));
						try {
							setScreen(new GameplayScreen((MoonshipGame) game));
						} catch (FileNotFoundException e) {
							throw new RuntimeException(e);
						}
					}
				});
				timer.cancel();
			}
		}, TimeUnit.SECONDS.toMillis(interval));
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