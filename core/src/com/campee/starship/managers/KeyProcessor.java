package com.campee.starship;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class KeyProcessor implements InputProcessor {
    //arrow keys
    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;
    public boolean oPressed;
    public boolean pPressed;
    GameplayScreen screen;

    public KeyProcessor(GameplayScreen screen) {
        this.screen = screen;
    }


    @Override
    public boolean keyDown(int keycode) {
        if (!screen.popupInAction) {

            switch (keycode) {
                case Input.Keys.UP:
                case Input.Keys.W:
                    upPressed = true;
                    downPressed = false;
                    leftPressed = false;
                    rightPressed = false;
                    break;
                case Input.Keys.DOWN:
                case Input.Keys.S:
                    downPressed = true;
                    upPressed = false;
                    leftPressed = false;
                    rightPressed = false;
                    break;
                case Input.Keys.LEFT:
                case Input.Keys.A:
                    leftPressed = true;
                    rightPressed = false;
                    upPressed = false;
                    downPressed = false;
                    break;
                case Input.Keys.RIGHT:
                case Input.Keys.D:
                    rightPressed = true;
                    leftPressed = false;
                    upPressed = false;
                    downPressed = false;
                    break;
            }
        }
            switch (keycode) {
                case Input.Keys.P:
                    pPressed = true;
                    break;
                case Input.Keys.O:
                    oPressed = true;
                    break;
            }
       // }
            return false;

    }

    @Override
    public boolean keyUp(int keycode) {
         if (!screen.popupInAction) {
             switch (keycode) {
                 case Input.Keys.UP:
                 case Input.Keys.W:
                     upPressed = false;
                     break;
                 case Input.Keys.DOWN:
                 case Input.Keys.S:
                     downPressed = false;
                     break;
                 case Input.Keys.LEFT:
                 case Input.Keys.A:
                     leftPressed = false;
                     break;
                 case Input.Keys.RIGHT:
                 case Input.Keys.D:
                     rightPressed = false;
                     break;
             }
         }
            switch (keycode) {
                case Input.Keys.P:
                    pPressed = false;
                    break;
                case Input.Keys.O:
                    oPressed = false;
                    break;
            }
        //}
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
