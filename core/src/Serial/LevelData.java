package com.campee.starship.serial;

import java.io.Serializable;
import java.util.ArrayList;

public class LevelData implements Serializable {
    private static final long serialVersionUID = 2L;

    public String levelName;
    public int difficulty;
    public int tileSize;
    public int width;
    public int height;
    public ArrayList<TileData[]> tileData;
    public ArrayList<ObjectData[]> objectData;

    public class TileData implements Serializable {
        private static final long serialVersionUID = 2L;

        public String tilesetID;
        public int spriteIndex;
        public int layerIndex;
        public int x, y;
        public boolean collision;

        public TileData(String tilesetID, int spriteIndex, int layerIndex, int x, int y, boolean collision) {
            this.tilesetID = tilesetID;
            this.spriteIndex = spriteIndex;
            this.layerIndex = layerIndex;
            this.x = x;
            this.y = y;
            this.collision = collision;
        }
    }

    public class ObjectData implements Serializable {
        private static final long serialVersionUID = 2L;

        public String objectID;
        public int layerIndex;
        public int x, y;

        public ObjectData(String objectID, int layerIndex, int x, int y) {
            this.objectID = objectID;
            this.layerIndex = layerIndex;
            this.x = x;
            this.y = y;
        }
    }
}
