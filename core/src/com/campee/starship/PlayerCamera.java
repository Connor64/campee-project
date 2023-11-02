package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PlayerCamera extends OrthographicCamera {
    public float lerpSpeed = 0.1f;
    public float interpolateSpeed = 1.0f;
    private final int VIRTUAL_WIDTH, VIRTUAL_HEIGHT;

    /**
     * Initializes the camera object based on provided width and height.
     *
     * @param virtualWidth The width of the camera's viewable area.
     * @param virtualHeight The height of the camera's viewable area.
     */
    public PlayerCamera(int virtualWidth, int virtualHeight) {
        VIRTUAL_WIDTH = virtualWidth;
        VIRTUAL_HEIGHT = virtualHeight;

        setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        position.set(0, 0, 0);
    }

    /**
     * Linearly interpolates the camera's position to follow the specified position within the constrained bounds. Must
     * be called every frame.
     *
     * @param target The position the camera is to follow.
     * @param xBound The horizontal boundary of the camera (positive and negative).
     * @param yBound The vertical boundary of the camera (positive and negative).
     */
    public void follow(Vector2 target, int xBound, int yBound) {
        float deltaLerp = lerpSpeed;

        float smoothX = (position.x * (interpolateSpeed - deltaLerp)) + (target.x * deltaLerp);
        float smoothY = (position.y * (interpolateSpeed - deltaLerp)) + (target.y * deltaLerp);

        smoothX = MathUtils.clamp(smoothX, (viewportWidth / 2), xBound - (viewportWidth / 2));
        smoothY = MathUtils.clamp(smoothY, (viewportHeight / 2) + 16, yBound - 16 - (viewportHeight / 2));

        position.set(smoothX, smoothY, 0);
        update();
    }

    @Override
    public void setToOrtho(boolean yDown, float viewportWidth, float viewportHeight) {
        float ratio = VIRTUAL_WIDTH / viewportWidth;

        super.setToOrtho(yDown, VIRTUAL_WIDTH, viewportHeight * ratio);
    }
}
