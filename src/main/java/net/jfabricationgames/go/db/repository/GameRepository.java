package net.jfabricationgames.go.db.repository;

import java.sql.SQLException;
import java.util.List;

import net.jfabricationgames.go.game.Game;

public interface GameRepository {
	
	public List<Game> findAll();
	public List<Game> findByUser(int userId);
	public List<Game> findByUser(String username) throws SQLException;
	public Game findById(int gameId);
	
	public Game create(Game game);
	public void update(Game game);
}