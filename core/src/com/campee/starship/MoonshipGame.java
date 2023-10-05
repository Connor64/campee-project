package com.campee.starship;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;
import java.util.List;

public class MoonshipGame extends ApplicationAdapter {
	SpriteBatch batch;
	//Texture img;
	TitleScreen titleScreen;
	BitmapFont font;
	GlyphLayout glyphLayout;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(1, 1, 0, 1);
		font.getData().setScale(3);
		glyphLayout = new GlyphLayout();
		//Pixmap pix_big = new Pixmap(Gdx.files.internal("connor_apple.jpg"));
		//Pixmap pix_small = new Pixmap(400, 400, pix_big.getFormat());
		//pix_small.drawPixmap(pix_big,
		//		0, 0, pix_big.getWidth(), pix_big.getHeight(),
		//		0, 0, pix_small.getWidth(), pix_small.getHeight()
		//);
		//img = new Texture(pix_small);
		//pix_small.dispose();
		//pix_big.dispose();

		titleScreen = new TitleScreen();

	}

	@Override
	public void render () {
		//ScreenUtils.clear(255, 0, 255, 0);
		//batch.begin();
		//batch.draw(img, 400 - (img.getWidth() / 2), 300 - (img.getHeight() / 2));
		//TitleScreen titleScreen = new TitleScreen();
		//batch.end();
		Gdx.gl.glClearColor(1, 0.8f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		titleScreen.render();

		batch.begin();
		glyphLayout.setText(font, "MOONSHIPS");
		float textX = (Gdx.graphics.getWidth() - glyphLayout.width) / 2;
		float textY = Gdx.graphics.getHeight() * 2 / 3 + glyphLayout.height;
		font.draw(batch, glyphLayout, textX, textY);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		//img.dispose();
		//titleScreen.dispose();
	}
}
