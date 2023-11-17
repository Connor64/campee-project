package com.campee.starship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class AssetManager {
    private HashMap<String, TextureRegion[]> tilesets;
    private HashMap<String, GameObject> gameObjects;

    private final int TILE_SIZE = 16;

    public AssetManager() throws IOException {
        tilesets = new HashMap<>();
        gameObjects = new HashMap<>();
        
        loadTileset("1_terrain.png", "tileset_test_1");
        loadTileset("street.png", "modern_tileset");
        loadTileset("caves.png", "caves_tileset");

        gameObjects.put("building_haas", new BuildingObject("HAAS.PNG", "HAAS", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_pmu", new BuildingObject("PMU.PNG", "PMU", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_msee", new BuildingObject("MSEE.PNG", "MSEE", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_fountain", new BuildingObject("fountain.PNG", "fountain", 0, 0, 0, 1));
        gameObjects.put("building_panera", new BuildingObject("Panera.PNG", "Panera", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_bepsi", new BuildingObject("Bepsi Machine.PNG", "Bepsi Machine", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_kola", new BuildingObject("Koca Kola Machine.PNG", "Kola Machine", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_turkstra", new BuildingObject("Prof. Turkstra.PNG", "Turkstra", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_police", new BuildingObject("Police Officer.PNG", "Officer", 0, 0, 0.5f, 0.75f));
        gameObjects.put("coin", new CoinObject(0, 0));
    }

    /**
     * Loads the specified tileset and adds it to the asset manager's list of tilesets
     *
     * @param fileName The filename of the tileset image.
     * @param tilesetID The identifier of the tileset (used to load sprites later).
     */
    private void loadTileset(String fileName, String tilesetID) {
        Texture source = new Texture(Gdx.files.internal("tilesets/" + fileName));
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

        tilesets.put(tilesetID, tiles);
    }

    /**
     * Loads the specified tile sprite from the asset manager's hash map.
     *
     * @param tilesetID The source tileset of the sprite.
     * @param index The index of the sprite within the tileset (top left is index 0).
     * @return A TextureRegion object from the tileset
     */
    public TextureRegion getTileSprite(String tilesetID, int index) {
        TextureRegion[] textures = tilesets.get(tilesetID);
        if (textures == null) return null;

        return textures[index];
    }

    public GameObject loadGameObject(String objectID) {
        return gameObjects.get(objectID);
    }
}
