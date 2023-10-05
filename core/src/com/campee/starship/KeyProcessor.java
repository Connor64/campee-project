package com.campee.starship;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class KeyProcessor implements InputProcessor {
    //arrow keys
    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            upPressed = true;
        }
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            downPressed = true;
        }
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            leftPressed = true;
        }
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            rightPressed = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            upPressed = false;
        }
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            downPressed = false;
        }
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            leftPressed = false;
        }
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            rightPressed = false;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
