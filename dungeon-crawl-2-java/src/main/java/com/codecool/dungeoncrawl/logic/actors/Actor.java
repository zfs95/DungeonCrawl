package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.io.Serializable;

public abstract class Actor implements Drawable, Serializable {
    private Cell cell;
    private int health = 10;
    private int attackDamage;

    public Actor(Cell cell, int attackDamage) {
        this.cell = cell;
        this.cell.setActor(this);
        this.attackDamage = attackDamage;
    }

    public abstract boolean canMove(Cell nextCell);

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        boolean isNotEnemy = nextCell.getActor() == null;

        if(canMove(nextCell)){
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
            if(nextCell.getType().equals(CellType.DOOR)){
                nextCell.setType(CellType.OPENDOOR);
            }
            if(nextCell.getType().equals(CellType.NEXTLEVEL)){
                System.out.println("You escaped!");
            }

        } else if(!isNotEnemy){
            Actor enemy = nextCell.getActor();
            int playerDamage = this.getAttackDamage();

            if(enemy.getHealth()<=playerDamage){
                nextCell.setActor(null);
                System.out.println("You have killed the enemy.");
            } else {
                int enemyDamage = enemy.getAttackDamage();
                enemy.setHealth(playerDamage);
                this.setHealth(enemyDamage);
                System.out.println("You hit the enemy for " + playerDamage);
                System.out.println("The enemy hit you for " + enemyDamage);
            }
        }
        if(getHealth() ==0){
            System.out.println("You died! Game Over!");
//            TimeUnit.SECONDS.sleep(3);
            System.exit(0);
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health -= health;
    }
    public void setHealth1(int health1){
        this.health = health1;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

}
