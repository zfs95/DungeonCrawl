package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.SaveModel;

import java.util.List;

public interface SaveDAO {
    void addSave(String name);
    public List<SaveModel> getAll();
    void update(String name);
    String get(String name);
}
