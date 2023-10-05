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
	int SPEED = 150;
	int move = 0;



	@Override
	public void create () {
		batch = new SpriteBatch();
		keyProcessor = new KeyProcessor();
		Gdx.input.setInputProcessor(keyProcessor);
//		Pixmap pix_big = new Pixmap(Gdx.files.internal("connor_apple.jpg"));
//		Pixmap pix_small = new Pixmap(400, 400, pix_big.getFormat());
//		pix_small.drawPixmap(pix_big,
//				0, 0, pix_big.getWidth(), pix_big.getHeight(),
//				0, 0, pix_small.getWidth(), pix_small.getHeight()
//		);
//		img = new Texture(pix_small);
//		pix_small.dispose();
//		pix_big.dispose();

		x = 100;
		y = 100;
		player = new Player();
		coin = new Coin();
	}

	@Override
	public void render () {
		ScreenUtils.clear(155f/255f, 150f/255f, 10f/25f, 0);
		float deltaTime =  Gdx.graphics.getDeltaTime();

		//implement KeyProcessor
		if (keyProcessor.upPressed) {
			y += SPEED * deltaTime;
			move = 0;
		}
		if (keyProcessor.downPressed) {
			y -= SPEED * deltaTime;
			move = 1;
		}
		if (keyProcessor.leftPressed) {
			x -= SPEED * deltaTime;
			move = 2;
		}
		if (keyProcessor.rightPressed) {
			x += SPEED * deltaTime;
			move = 3;
		}
//		batch.draw(img, 400 - (img.getWidth() / 2), 300 - (img.getHeight() / 2));
		batch.begin();
		player.render(batch, x, y, move);
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
		img.dispose();
	}
}