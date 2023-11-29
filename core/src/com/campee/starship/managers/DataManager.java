package com.campee.starship.managers;

import Serial.LevelData;
import com.badlogic.gdx.Gdx;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import org.json.*;

public class DataManager {
    private final String UNLOCKED_LEVELS = "UnlockedLevels";
    private final String FILE_NAME = "save_data.json";

    public static final DataManager INSTANCE = new DataManager();

    private HashMap<String, Boolean> levelUnlocks;

    private DataManager() {
        levelUnlocks = new HashMap<>();

        loadFile();
    }

    public boolean levelExists(String levelID) {
        return levelUnlocks.containsKey(levelID);
    }

    public boolean isLevelUnlocked(String levelID) {
        return levelUnlocks.get(levelID);
    }

    public void setClearStatus(String levelID, boolean unlocked) {
        if (!levelUnlocks.containsKey(levelID)) return;

        levelUnlocks.put(levelID, unlocked);
    }

    private void loadFile() {
        try {
            System.out.println("Beginning to read...");

            File file = Gdx.files.local(FILE_NAME).file();
            JSONTokener tokener = new JSONTokener(file.toURI().toURL().openStream());
            JSONObject root = new JSONObject(tokener);

            JSONObject unlockedLevelsObj = root.getJSONObject(UNLOCKED_LEVELS);

            Iterator<String> levelKeys = unlockedLevelsObj.keys();
            while (levelKeys.hasNext()) {
                String key = levelKeys.next();

                levelUnlocks.put(key, unlockedLevelsObj.getBoolean(key));
            }
        } catch (IOException e) {
            // If the file doesn't exist (or something is fucked)
            generateFile();
        }
    }

    private void generateFile() {
        FileHandle handle = Gdx.files.local(FILE_NAME);

        // The outermost JSON object, contains all other objects and data
        JSONObject obj = new JSONObject();

        // Make JSON array object containing level unlock data
        JSONObject subObj = new JSONObject();
        for (Map.Entry<String, LevelData> entry : AssetManager.INSTANCE.getLevels().entrySet()) {
            subObj.put(entry.getKey(), false);
            levelUnlocks.put(entry.getKey(), false);
        }

        obj.put(UNLOCKED_LEVELS, subObj);

        handle.writeString(obj.toString(4), false);
    }
}
