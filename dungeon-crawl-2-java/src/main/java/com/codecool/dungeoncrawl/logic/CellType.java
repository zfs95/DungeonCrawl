package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    SWORD("sword"),
    KEY("key"),
    POTION("potion"),
    DOOR("door"),
    OPENDOOR("openDoor"),
    NEXTLEVEL("nextLevel"),
    TREE("tree"),
    WATER("water"),
    MUD("mud");

    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
