package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private DataSource dataSource;
    private PlayerDao playerDao;

    public GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO game_state (current_map, saved_at, player_id) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, state.getCurrentMap());
            statement.setDate(2, state.getSavedAt());
            statement.setInt(3, state.getPlayer().getId());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            state.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE game_state SET current_map = ?, saved_at = ?, player_id = ? WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, state.getCurrentMap());
            st.setDate(2, state.getSavedAt());
            st.setInt(3, state.getPlayer().getId());
            st.setInt(4, state.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM game_state where id = ?";
            PreparedStatement secureQuery = conn.prepareStatement(sql);
            secureQuery.setInt(1, id);
            ResultSet result = secureQuery.executeQuery();
            if(!result.next()){
                return null;
            }

            String currentMap = result.getString(2);
            Date saveDate = result.getDate(3);
            int playerID = result.getInt(4);
            PlayerModel player = playerDao.get(playerID);

            GameState loadedGameState = new GameState(currentMap, saveDate, player);

            return loadedGameState;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameState> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM game_state";

            PreparedStatement secureQuery = conn.prepareStatement(sql);
            ResultSet result = secureQuery.executeQuery();
            List<GameState> gameStates = new ArrayList<>();
            List<PlayerModel> players = playerDao.getAll();

            while(result.next()){
                int playerId = result.getInt(4);
                String currentMap = result.getString(2);
                Date saveDate = result.getDate(3);
                for(PlayerModel player : players){
                    if(player.getId() == playerId){
                        GameState state = new GameState(currentMap,saveDate,player);
                        gameStates.add(state);
                    }
                }
            }
            return gameStates;

        } catch (SQLException e) {
            throw new RuntimeException("Error gamestate", e);
        }
    }
}
