package com.campee.starship.objects;

public class Upgrade {
    private final String ID;
    private final String NAME;
    private final int COST;
    private final String DESCRIPTION;

    public Upgrade(String id, String name, int cost, String description) {
        ID = id;
        NAME = name;
        COST = cost;
        DESCRIPTION = description;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return NAME;
    }

    public int getCost() {
        return COST;
    }

    public String getDescription() {
        return DESCRIPTION;
    }
}
