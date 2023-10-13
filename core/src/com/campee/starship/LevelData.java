package com.campee.starship;

import java.io.Serializable;
import java.util.ArrayList;

public class LevelData implements Serializable {
    ArrayList<Tile[][]> layers;
    String levelName;
    int width;
    int height;

    public LevelData(ArrayList<Tile[][]> layers, String levelName, int width, int height) {
        this.layers = layers;
        this.levelName = levelName;
        this.width = width;
        this.height = height;
    }
}
