package com.campee.starship.managers;

import Serial.LevelData;
import com.badlogic.gdx.Gdx;

import java.io.*;
import java.util.*;

import com.badlogic.gdx.files.FileHandle;
import com.campee.starship.objects.Customization;
import org.json.*;

public class DataManager {
    private final String UNLOCKED_LEVELS = "unlocked_levels";
    private final String PURCHASED_UPGRADES = "purchased_upgrades";
    private final String COIN_COUNT = "coins";
    private final String GAMEPLAY_MUSIC = "gameplay_music_volume";
    private final String GAMEPLAY_SFX = "gameplay_sfx_volume";
    private final String MENU_MUSIC = "menu_music_volume";
    private final String MENU_SFX = "menu_sfx_volume";
    private final String HIGH_SCORES = "high_scores";
    private final String FILE_NAME = "save_data.json";

    public static final DataManager INSTANCE = new DataManager();

    private HashMap<String, Boolean> levelUnlocks;
    private HashMap<String, Integer> highScores;
    private HashMap<String, Boolean> activeUpgrades;
    private int coinCount;

    private float gameplayMusicVolume;
    private float gameplaySFXVolume;
    private float menuMusicVolume;
    private float menuSFXVolume;

    private DataManager() {
        levelUnlocks = new HashMap<>();
        highScores = new HashMap<>();
        activeUpgrades = new HashMap<>();
        coinCount = 0;

        loadFile();
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

            // Load in the level high scores
            JSONObject highScoresObj = root.getJSONObject(HIGH_SCORES);
            Iterator<String> scoreKeys = highScoresObj.keys();
            while (scoreKeys.hasNext()) {
                String key = scoreKeys.next();
                highScores.put(key, highScoresObj.getInt(key));
            }

            // Load in the purchased upgrades (with active status)
            JSONObject upgradesObj = root.getJSONObject(PURCHASED_UPGRADES);
            Iterator<String> upgradeKeys = upgradesObj.keys();
            while (upgradeKeys.hasNext()) {
                String key = upgradeKeys.next();
                activeUpgrades.put(key, upgradesObj.getBoolean(key));
            }

            coinCount = root.getInt(COIN_COUNT);

            gameplayMusicVolume = root.getFloat(GAMEPLAY_MUSIC);
            gameplaySFXVolume = root.getFloat(GAMEPLAY_SFX);
            menuMusicVolume = root.getFloat(MENU_MUSIC);
            menuSFXVolume = root.getFloat(MENU_SFX);

        } catch (IOException e) {
            // If the file doesn't exist (or something is fucked)
            generateFile();
        }
    }

    private void generateFile() {
        Set<Map.Entry<String, LevelData>> levelSet = AssetManager.INSTANCE.getLevels();
        for (Map.Entry<String, LevelData> entry : levelSet) {
            levelUnlocks.put(entry.getKey(), false);
            highScores.put(entry.getKey(), 0);
        }

        activeUpgrades.clear(); // just in case

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

        // Make JSON object containing level high scores
        JSONObject highScoresObj = new JSONObject();
        for (Map.Entry<String, Integer> entry : highScores.entrySet()) {
            highScoresObj.put(entry.getKey(), entry.getValue());
        }

        // Make JSON object to store all purchased upgrades and active status
        JSONObject upgradesObj = new JSONObject();
        for (Map.Entry<String, Boolean> entry : activeUpgrades.entrySet()) {
            upgradesObj.put(entry.getKey(), entry.getValue());
        }

        // Write to JSON outer JSON object
        obj.put(UNLOCKED_LEVELS, unlockedLevelsObj);
        obj.put(HIGH_SCORES, highScoresObj);
        obj.put(PURCHASED_UPGRADES, upgradesObj);
        obj.put(COIN_COUNT, coinCount);
        obj.put(GAMEPLAY_MUSIC, gameplayMusicVolume);
        obj.put(GAMEPLAY_SFX, gameplaySFXVolume);
        obj.put(MENU_MUSIC, menuMusicVolume);
        obj.put(MENU_SFX, menuSFXVolume);

        handle.writeString(obj.toString(4), false); // Write to file
    }

    public void setClearStatus(String levelID, boolean unlocked, boolean diskWrite) {
        if (!levelUnlocks.containsKey(levelID)) return;

        levelUnlocks.put(levelID, unlocked);

        if (diskWrite) {
            saveProgress();
        }
    }

    public boolean levelExists(String levelID) {
        return levelUnlocks.containsKey(levelID);
    }

    public boolean isLevelUnlocked(String levelID) {
        return levelUnlocks.get(levelID);
    }

    public void addPurchase(String upgradeID, boolean diskWrite) {
        if (activeUpgrades.containsKey(upgradeID)) return;

        activeUpgrades.put(upgradeID, false);

        if (diskWrite) {
            saveProgress();
        }
    }

    public int getHighScore(String levelID) {
        return highScores.get(levelID);
    }

    public void setHighScore(String levelID, int score, boolean diskWrite) {
        if (!highScores.containsKey(levelID)) return;

        if (highScores.get(levelID) < score) {
            highScores.put(levelID, score);
        }

        if (diskWrite) {
            saveProgress();
        }
    }

    public boolean isUpgradePurchased(String upgradeID) {
        return activeUpgrades.containsKey(upgradeID);
    }

    public boolean isUpgradeActive(String upgradeID) {
        if (!activeUpgrades.containsKey(upgradeID)) return false;

        return activeUpgrades.get(upgradeID);
    }

    public void toggleUpgrade(String upgradeID, boolean diskWrite) {
        if (!activeUpgrades.containsKey(upgradeID)) return;

        if (activeUpgrades.get(upgradeID) && (AssetManager.INSTANCE.getUpgrade(upgradeID) instanceof Customization)) {
            for (Map.Entry<String, Boolean> entry : activeUpgrades.entrySet()) {
                if (AssetManager.INSTANCE.getUpgrade(entry.getKey()) instanceof Customization) {
                    System.out.println(upgradeID + "  vs  " + entry.getKey());
                    if (!entry.getKey().equals(upgradeID)) {
                        System.out.println("NOT the upgrade");
                        activeUpgrades.put(entry.getKey(), false);
                    }
                }
            }
        }

        activeUpgrades.put(upgradeID, !activeUpgrades.get(upgradeID));

        if (diskWrite) {
            saveProgress();
        }
    }

    public Set<Map.Entry<String, Boolean>> getPurchases() {
        return activeUpgrades.entrySet();
    }

    public void addCoins(int coinDiff, boolean diskWrite) {
        System.out.println("adding coins!! current: " + coinCount + "  diff: " + coinDiff);
        coinCount = Math.max(0, coinCount + coinDiff);

        if (diskWrite) {
            saveProgress();
        }
    }

    public int getCoinCount() {
        return coinCount;
    }

    public void setGameplayMusicVolume(float volume, boolean diskWrite) {
        gameplayMusicVolume = volume;

        if (diskWrite) saveProgress();
    }

    public void setGameplaySFXVolume(float volume, boolean diskWrite) {
        gameplaySFXVolume = volume;

        if (diskWrite) saveProgress();
    }

    public void setMenuMusicVolume(float volume, boolean diskWrite) {
        menuMusicVolume = volume;

        if (diskWrite) saveProgress();
    }

    public void setMenuSFXVolume(float volume, boolean diskWrite) {
        menuSFXVolume = volume;

        if (diskWrite) saveProgress();
    }

    public float getGameplayMusicVolume() {
        return gameplayMusicVolume;
    }

    public float getGameplaySFXVolume() {
        return gameplaySFXVolume;
    }

    public float getMenuMusicVolume() {
        return menuMusicVolume;
    }

    public float getMenuSFXVolume() {
        return menuMusicVolume;
    }
}
