package com.campee.starship;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends ApplicationAdapter implements Screen {
    SpriteBatch batch;
    Texture img;
    private Game game;
    private Stage stage;

    Player player;
    Coin coin;
    float x;
    float y;
    int SPEED = 150;
    int move = 0;

    public GameScreen(final Game game) {
        stage = new Stage();
        this.game = game;
        batch = new SpriteBatch();
        x = 100;
        y = 100;
        player = new Player();
        coin = new Coin();
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //ScreenUtils.clear(Color.PINK);


        ScreenUtils.clear(Color.PINK);

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            y += SPEED * Gdx.graphics.getDeltaTime();
            move = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            y -= SPEED * Gdx.graphics.getDeltaTime();
            move = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= SPEED * Gdx.graphics.getDeltaTime();
            move = 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += SPEED * Gdx.graphics.getDeltaTime();
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
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }
}