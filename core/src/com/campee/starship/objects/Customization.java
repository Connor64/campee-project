package com.campee.starship.objects;

public class Customization extends Upgrade {

    private String suffix;

    public Customization(String id, String name, int cost, String spritePath, String suffix) {
        super(id, name, cost, spritePath);

        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

}
