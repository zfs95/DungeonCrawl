package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap() {
        InputStream is = MapLoader.class.getResourceAsStream("/map.txt");
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            new Skeleton(cell, 2);
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell, 5));
                            break;
                        case 'k':
                            cell.setType(CellType.KEY);
                            break;
                        case 'w':
                            cell.setType(CellType.SWORD);
                            break;
                        case 'p':
                            cell.setType(CellType.POTION);
                            break;
                        case 'd':
                            cell.setType(CellType.DOOR);
                            break;
                        case 'o':
                            cell.setType(CellType.OPENDOOR);
                            break;
                        case 'n':
                            cell.setType(CellType.NEXTLEVEL);
                            break;
                        case 't':
                            cell.setType(CellType.TREE);
                            break;
                        case 'r':
                            cell.setType(CellType.WATER);
                            break;
                        case 'm':
                            cell.setType(CellType.MUD);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

}
