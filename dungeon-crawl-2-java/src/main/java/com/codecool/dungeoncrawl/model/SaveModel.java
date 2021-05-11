package com.codecool.dungeoncrawl.model;

import java.sql.Date;

public class SaveModel{
    private int id;
    private String name;
    private String path;
    private Date saveDate;

    public SaveModel(int id, String name, String path, Date saveDate) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.saveDate = saveDate;
    }

    public void setSaveDate(Date saveDate) {
        this.saveDate = saveDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Date getSaveDate() {
        return saveDate;
    }
}
