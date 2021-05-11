package com.codecool.dungeoncrawl.savemodule;

import com.codecool.dungeoncrawl.logic.GameMap;

import java.io.*;

public class Save {

    public static void saveState(GameMap map, String saveName){
        String filename =  "src/main/saves/"+saveName +".txt";
        try{

            FileOutputStream file =  new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(map);
            out.close();
            file.close();

            System.out.println("Object has been serialized\n");
        }
        catch (IOException e){
            System.out.println("IOException is cought.");
        }

    }

    public static GameMap loadState(String filename){
        GameMap map = null;

        try{

            FileInputStream file =  new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            map = (GameMap) in.readObject();


            in.close();
            file.close();
            System.out.println("File has been Deserialized");

        }
        catch (IOException | ClassNotFoundException me){
            System.out.println("IOException is cought.");
        }
        return map;
    }
}
