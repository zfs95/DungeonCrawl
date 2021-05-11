package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Player extends Actor {

    private String playerName;
    private boolean haveSword;
    private boolean haveKey;
    private boolean havePotion;


    public Player(Cell cell, int attackDamage) {
        super(cell, attackDamage);
        this.havePotion = false;
        this.haveSword = false;
        this.haveKey = false;
    }

    public String getTileName() {
        return "player";
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int checkCellType(){
        if (this.getCell().getType().equals(CellType.SWORD))
            return 1;
        else if(this.getCell().getType().equals(CellType.POTION))
            return 2;
        else if(this.getCell().getType().equals(CellType.KEY))
            return 3;
        else
            return 0;
    }

    public void pickUpItem(int item){
        if(item == 1){
            haveSword = true;
            this.setAttackDamage(10);
            this.getCell().setType(CellType.FLOOR);
            System.out.println("You picked up the sword");
        }  else if(item == 2){
            havePotion = true;
            this.getCell().setType(CellType.FLOOR);
            System.out.println("You picked up the potion");
        } else if(item == 3){
            haveKey = true;
            this.getCell().setType(CellType.FLOOR);
            System.out.println("You picked up the key");
        } else {
            System.out.println("You are not currently on any item");
        }
    }


    @Override
    public boolean canMove(Cell nextCell) {
        boolean isCellAWall = nextCell.getType().equals(CellType.WALL);
        boolean hasCellAnEnemy = nextCell.getActor() != null;
        boolean isCellAClosedDoor = nextCell.getType().equals(CellType.DOOR);
        boolean isCellAnOpenDoor = nextCell.getType().equals(CellType.OPENDOOR);
        boolean isCellADoor = isCellAClosedDoor || isCellAnOpenDoor;
        boolean canPassDoor = (isCellAClosedDoor && haveKey) || isCellAnOpenDoor || !isCellADoor;

        return (!(isCellAWall || hasCellAnEnemy || !canPassDoor));

    }

    @Override
    public String toString() {
        int key = 0;
        int sword = 0;
        int potion = 0;

        if(haveKey) key = 1;
        if(haveSword) sword = 1;
        if(havePotion) {
            setHealth1(10);
        }
        return "Inventory key" + "["+ key + "]" + "Sword" + "["+ sword + "]";
    }

}


