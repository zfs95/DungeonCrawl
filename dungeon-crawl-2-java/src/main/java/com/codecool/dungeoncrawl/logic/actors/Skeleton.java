package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Skeleton extends Actor {
    
    public Skeleton(Cell cell, int attackDamage) {
        super(cell, attackDamage);
    }

    @Override
    public boolean canMove(Cell nextCell) {
        return false;
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
