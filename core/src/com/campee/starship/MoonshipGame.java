package com.campee.starship;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;


public class MoonshipGame extends ApplicationAdapter {
	KeyProcessor keyProcessor;
	SpriteBatch batch;
	Texture img;

	Player player;
	Coin coin;
	float x;
	float y;
	float screenWidth;
	float screenHeight;
	int SPEED = 150;
	int move = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		keyProcessor = new KeyProcessor();
		Gdx.input.setInputProcessor(keyProcessor);

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		x = 100;
		y = 100;
		player = new Player();
		coin = new Coin();
	}

	@Override
	public void render () {
		ScreenUtils.clear(155f/255f, 150f/255f, 10f/25f, 0);
		float deltaTime =  Gdx.graphics.getDeltaTime();

		boolean accelerating = false;
		//implement KeyProcessor
		if (keyProcessor.upPressed) {
			y += SPEED * deltaTime;
			move = 0;
			accelerating = true;
		} else if (keyProcessor.downPressed) {
			y -= SPEED * deltaTime;
			move = 1;
			accelerating = true;
		} else if (keyProcessor.leftPressed) {
			x -= SPEED * deltaTime;
			move = 2;
			accelerating = true;
		} else if (keyProcessor.rightPressed) {
			x += SPEED * deltaTime;
			move = 3;
			accelerating = true;
		}


		//batch.draw(img, 400 - (img.getWidth() / 2), 300 - (img.getHeight() / 2));
		batch.begin();
		player.render(batch, x, y, move, accelerating);

		//ensure sprite stays within screen bounds
		if (x < 0) {
			x = 0;
		} else if (x > screenWidth - player.getWidth()) {
			x = screenWidth - player.getWidth();
		}

		if (y < 0) {
			y = 0;
		} else if (y > screenHeight - player.getHeight()) {
			y = screenHeight - player.getHeight();
		}

		if (!coin.collected) {
			coin.render(batch, 0, 0);
		}
		if (Intersector.overlaps(player.getBounds(), coin.getBounds())) {
			coin.setCollected(true);
		}
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}