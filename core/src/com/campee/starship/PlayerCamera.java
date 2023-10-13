package com.campee.starship;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class PlayerCamera extends OrthographicCamera {
    public float lerpSpeed = 0.1f;
    public float interpolateSpeed = 1.0f;

    /**
     * Initializes the camera object based on provided width and height.
     *
     * @param viewportWidth The width of the camera's viewable area.
     * @param viewportHeight The height of the camera's viewable area.
     */
    public PlayerCamera(int viewportWidth, int viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.near = 0;

        position.set(0, 0, 0);
        setToOrtho(false, viewportWidth, viewportHeight);
    }

    /**
     * Linearly interpolates the camera's position to follow the specified position. Must be called every frame.
     *
     * @param target The position the camera is to follow.
     * @param deltaTime The time, in seconds, since the last frame.
     */
    public void follow(Vector2 target, float deltaTime) {
        float deltaLerp = lerpSpeed;

        float smoothX = (position.x * (interpolateSpeed - deltaLerp)) + (target.x * deltaLerp);
        float smoothY = (position.y * (interpolateSpeed - deltaLerp)) + (target.y * deltaLerp);

        position.set(smoothX, smoothY, 0);
        System.out.println("pos: " + position);
        update();
    }
}
