import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter {	private SpriteBatch batch;
    private List<Texture> images; // List of images to render
    private List<Float> xPositions; // List of X-coordinates for images
    private List<Float> yPositions; // List of Y-coordinates for images

    @Override
    public void create() {
        batch = new SpriteBatch();
        images = new ArrayList<Texture>();
        xPositions = new ArrayList<Float>();
        yPositions = new ArrayList<Float>();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for (int i = 0; i < images.size(); i++) {
            float x = xPositions.get(i);
            float y = yPositions.get(i);
            Texture image = images.get(i);
            float scaledWidth = image.getWidth() / 6;
            float scaledHeight = image.getHeight() / 6;
            batch.draw(image, x - scaledWidth / 2, y - scaledHeight / 2, scaledWidth, scaledHeight);
        }

        batch.end();

        if (Gdx.input.isTouched()) {
            float clickX = Gdx.input.getX();
            float clickY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invert the Y-axis

            // Add the image and its position to the lists
            Texture newImage = new Texture(Gdx.files.internal("connor_apple.jpg")); // Replace with your image path
            images.add(newImage);
            xPositions.add(clickX);
            yPositions.add(clickY);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();

        // Dispose of all images
        for (Texture image : images) {
            image.dispose();
        }
    }
}
