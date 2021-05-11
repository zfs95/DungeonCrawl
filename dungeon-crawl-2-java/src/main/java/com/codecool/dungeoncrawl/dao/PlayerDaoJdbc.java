package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {
    private DataSource dataSource;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO player (player_name, hp, x, y) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getHp());
            statement.setInt(3, player.getX());
            statement.setInt(4, player.getY());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            player.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE player SET player_name = ?, hp = ?, x = ?,y = ? WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, player.getPlayerName());
            st.setInt(2, player.getHp());
            st.setInt(3, player.getX());
            st.setInt(4, player.getY());
            st.setInt(5, player.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerModel get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM player WHERE id = ?";
            PreparedStatement secureQuery = conn.prepareStatement(sql);
            secureQuery.setInt(1, id);
            ResultSet result = secureQuery.executeQuery();

            if(!result.next()){
                return null;
            }

            String playerName = result.getString(2);
            int playerHealth = result.getInt(3);
            int positionX = result.getInt(4);
            int positionY = result.getInt(5);

            PlayerModel player = new PlayerModel(playerName, positionX, positionY);
            player.setHp(playerHealth);

            return player;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PlayerModel> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM player";
            PreparedStatement secureQuery = conn.prepareStatement(sql);
            ResultSet result = secureQuery.executeQuery();
            List<PlayerModel> playerModels = new ArrayList<>();
            while(result.next()){
                String playerName = result.getString(2);
                int playerHealth = result.getInt(3);
                int positionX = result.getInt(4);
                int positionY = result.getInt(5);
                PlayerModel player = new PlayerModel(playerName, positionX, positionY);
                player.setHp(playerHealth);
                playerModels.add(player);

            }
            return playerModels;
        } catch (SQLException e) {
            throw new RuntimeException("Error gamestate", e);
        }
    }
}
