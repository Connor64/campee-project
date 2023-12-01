package com.campee.starship.managers;

import Serial.LevelData;
import com.badlogic.gdx.Gdx;

import java.io.*;
import java.util.*;

import com.badlogic.gdx.files.FileHandle;
import org.json.*;

public class DataManager {
    private final String UNLOCKED_LEVELS = "unlocked_levels";
    private final String PURCHASED_UPGRADES = "purchased_upgrades";
    private final String COIN_COUNT = "coins";
    private final String FILE_NAME = "save_data.json";

    public static final DataManager INSTANCE = new DataManager();

    private HashMap<String, Boolean> levelUnlocks;
    private ArrayList<String> purchasedUpgrades;
    private int coinCount;

    private DataManager() {
        levelUnlocks = new HashMap<>();
        purchasedUpgrades = new ArrayList<>();
        coinCount = 0;

        loadFile();
    }

    public boolean levelExists(String levelID) {
        return levelUnlocks.containsKey(levelID);
    }

    public boolean isLevelUnlocked(String levelID) {
        return levelUnlocks.get(levelID);
    }

    public boolean isUpgradePurchased(String upgradeID) {
        return purchasedUpgrades.contains(upgradeID);
    }

    public void setClearStatus(String levelID, boolean unlocked, boolean diskWrite) {
        if (!levelUnlocks.containsKey(levelID)) return;

        levelUnlocks.put(levelID, unlocked);

        if (diskWrite) {
            saveProgress();
        }
    }

    public void addPurchase(String upgradeID, boolean diskWrite) {
        if (purchasedUpgrades.contains(upgradeID)) return;

        purchasedUpgrades.add(upgradeID);

        if (diskWrite) {
            saveProgress();
        }
    }

    public void addCoins(int coinDiff, boolean diskWrite) {
        coinCount = Math.max(0, coinCount + coinDiff);

        if (diskWrite) {
            saveProgress();
        }
    }

    public int getCoinCount() {
        return coinCount;
    }

    private void loadFile() {
        try {
            File file = Gdx.files.local(FILE_NAME).file();
            JSONTokener tokener = new JSONTokener(file.toURI().toURL().openStream());
            JSONObject root = new JSONObject(tokener);

            // Load in the unlocked levels
            JSONObject unlockedLevelsObj = root.getJSONObject(UNLOCKED_LEVELS);
            Iterator<String> levelKeys = unlockedLevelsObj.keys();
            while (levelKeys.hasNext()) {
                String key = levelKeys.next();
                levelUnlocks.put(key, unlockedLevelsObj.getBoolean(key));
            }

            // Load in the purchased upgrades
            JSONArray purchasedUpgradesObj = root.getJSONArray(PURCHASED_UPGRADES);
            for (int i = 0; i < purchasedUpgradesObj.length(); i++) {
                purchasedUpgrades.add(purchasedUpgradesObj.getString(i));
            }

            coinCount = root.getInt(COIN_COUNT);

        } catch (IOException e) {
            // If the file doesn't exist (or something is fucked)
            generateFile();
        }
    }

    private void generateFile() {
        Set<Map.Entry<String, LevelData>> levelSet = AssetManager.INSTANCE.getLevels();
        for (Map.Entry<String, LevelData> entry : levelSet) {
            levelUnlocks.put(entry.getKey(), false);
        }

        purchasedUpgrades.clear(); // just in case

        saveProgress(); // Write to disk
    }

    private void saveProgress() {
        FileHandle handle = Gdx.files.local(FILE_NAME);

        // The outermost JSON object, contains all other objects and data
        JSONObject obj = new JSONObject();

        // Make JSON object containing level unlock data
        JSONObject unlockedLevelsObj = new JSONObject();
        for (Map.Entry<String, Boolean> entry : levelUnlocks.entrySet()) {
            unlockedLevelsObj.put(entry.getKey(), entry.getValue());
        }

        // Make JSON array to store all purchased upgrades
        JSONArray purchasedUpgradesObj = new JSONArray();
        for (String upgrade : purchasedUpgrades) {
            purchasedUpgradesObj.put(upgrade);
        }

        // Write to JSON outer JSON object
        obj.put(UNLOCKED_LEVELS, unlockedLevelsObj);
        obj.put(PURCHASED_UPGRADES, purchasedUpgradesObj);
        obj.put(COIN_COUNT, coinCount);

        handle.writeString(obj.toString(4), false); // Write to file
    }
}
