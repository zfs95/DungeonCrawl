package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.SaveModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaveDAOJdbc implements SaveDAO {
    private DataSource dataSource;

    public SaveDAOJdbc(DataSource dataSource){
        this.dataSource = dataSource;
    }
    @Override
    public void addSave(String name){
        String path = "src/main/saves/"+ name +".txt";
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO saved_games (name, path) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, path);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SaveModel> getAll(){
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM saved_games";

            PreparedStatement secureQuery = conn.prepareStatement(sql);
            ResultSet result = secureQuery.executeQuery();

            List<SaveModel> savedGames = new ArrayList<>();


            while(result.next()){
                int saveId = result.getInt(1);
                String name = result.getString(2);
                String path = result.getString(3);
                Date saveDate = result.getDate(4);
                SaveModel newSave = new SaveModel(saveId, name, path, saveDate);
                savedGames.add(newSave);
            }
            return savedGames;

        } catch (SQLException e) {
            throw new RuntimeException("Error gamestate", e);
        }
    }

    @Override
    public void update(String name){
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE saved_games SET save_at = ? WHERE name = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            long millis =System.currentTimeMillis();
            st.setDate(1,new java.sql.Date(millis));

            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String get(String name){
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM saved_games WHERE name = ?";
            PreparedStatement secureQuery = conn.prepareStatement(sql);
            secureQuery.setString(1, name);
            ResultSet result = secureQuery.executeQuery();
            if(!result.next()){
                return null;
            }

            String newName = result.getString(2);


            return newName;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





}
