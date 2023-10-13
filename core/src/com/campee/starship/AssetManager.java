package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class AssetManager {
    private HashMap<String, TextureRegion[]> tilesets;

    private final int TILE_SIZE = 16;

    public AssetManager() throws IOException {
        tilesets = new HashMap<>();

        Texture source = new Texture(Gdx.files.internal("tilesets/1_terrain.png"));
        source.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        if (!source.getTextureData().isPrepared())
            source.getTextureData().prepare();

        Pixmap pixmap = source.getTextureData().consumePixmap();

        int rows = source.getHeight() / TILE_SIZE;
        int columns = source.getWidth() / TILE_SIZE;

        TextureRegion[] tiles = new TextureRegion[rows * columns];

        int spriteIndex = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                TextureRegion subTexture = new TextureRegion(source, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                // Check if the sub-image is empty
                boolean empty = true;
                for (int pixX = 0; (pixX < TILE_SIZE) && empty; pixX++) {
                    for (int pixY = 0; pixY < TILE_SIZE; pixY++) {
                        Color color = new Color(pixmap.getPixel((x * TILE_SIZE) + pixX, (y * TILE_SIZE) + pixY));
                        if (color.a != 0) {
                            empty = false;
                            break;
                        }
                    }
                }

                if (empty) continue; // If the sub-image was empty, continue to the next one

                tiles[spriteIndex] = subTexture;
                spriteIndex++;
            }
        }

        // If there were some tiles in the image which were completely empty, cull them from the end of the array.
        if (spriteIndex != rows * columns) {
            tiles = Arrays.copyOf(tiles, spriteIndex);
        }

        tilesets.put("test", tiles);
//        System.out.println("tiles i guess: " + spriteIndex);

    }

    public TextureRegion getRegion(int index) {
        return tilesets.get("test")[index];
    }
}
