package com.campee.starship;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class StarshipGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		Pixmap pix_big = new Pixmap(Gdx.files.internal("connor_apple.jpg"));
		Pixmap pix_small = new Pixmap(400, 400, pix_big.getFormat());
		pix_small.drawPixmap(pix_big,
				0, 0, pix_big.getWidth(), pix_big.getHeight(),
				0, 0, pix_small.getWidth(), pix_small.getHeight()
		);
		img = new Texture(pix_small);
		pix_small.dispose();
		pix_big.dispose();

	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 100, 100, 1);
		batch.begin();
		batch.draw(img, 400 - (img.getWidth() / 2), 300 - (img.getHeight() / 2));
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
