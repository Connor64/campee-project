package com.campee.starship.managers;

import Serial.LevelData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.campee.starship.objects.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;

public class AssetManager {
    public static final AssetManager INSTANCE;

    static {
        try {
            INSTANCE = new AssetManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HashMap<String, TextureRegion[]> tilesets;
    private HashMap<String, GameObject> gameObjects;
    private HashMap<String, LevelData> levels;

    private final int TILE_SIZE = 16;

    private AssetManager() throws IOException {
        tilesets = new HashMap<>();
        gameObjects = new HashMap<>();
        levels = new HashMap<>();
        
        loadTileset("1_terrain.png", "tileset_test_1");
        loadTileset("street.png", "modern_tileset");
        loadTileset("caves.png", "caves_tileset");

        gameObjects.put("building_haas", new BuildingObject("sprites/buildings/haas.png", "HAAS", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_pmu", new BuildingObject("sprites/buildings/pmu.png", "PMU", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_msee", new BuildingObject("sprites/buildings/msee.png", "MSEE", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_fountain", new BuildingObject("sprites/buildings/fountain.png", "fountain", 0, 0, 0, 1));
        gameObjects.put("building_panera", new BuildingObject("sprites/buildings/panera.png", "Panera", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_bepsi", new BuildingObject("sprites/buildings/bepsi_machine.png", "Bepsi Machine", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_kola", new BuildingObject("sprites/buildings/koca_kola_machine.png", "Kola Machine", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_turkstra", new BuildingObject("sprites/buildings/prof_turkstra.png", "Turkstra", 0, 0, 0.5f, 0.75f));
        gameObjects.put("building_police", new BuildingObject("sprites/buildings/police_officer.png", "Officer", 0, 0, 0.5f, 0.75f));
        gameObjects.put("coin", new CoinObject(0, 0));

        FileHandle levelsFolder = Gdx.files.internal("levels");
        FileHandle[] levelFiles = levelsFolder.list();

        for (int i = 0; i < levelFiles.length; i++) {
            String[] nameFields = levelFiles[i].nameWithoutExtension().split("_");

            InputStream fileStream = Files.newInputStream(new File(levelFiles[i].path()).toPath());
            ObjectInputStream inputStream = new ObjectInputStream(fileStream);

            try {
                LevelData levelData = (LevelData) inputStream.readObject();
                levels.put(nameFields[1], levelData);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public HashMap<String, LevelData> getLevels() {
        return levels;
    }

    public LevelData getLevel(String key) {
        return levels.get(key);
    }

    public int getNumLevels() {
        return levels.size();
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
