package net.jfabricationgames.go.db.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.jfabricationgames.go.game.Game;

@Repository
public class GameRepository implements IGameRepository {
	
	private static final String GAME_FIELDS = "id, player_black, player_white, moves, started_on, last_played_on, "
			+ "board_size, resign, points, over, handycap, comi";
	
	private final JdbcTemplate jdbc;
	
	@Autowired
	public GameRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	@Override
	public List<Game> findAll() {
		return jdbc.query("SELECT " + GAME_FIELDS + " FROM games", this::mapRowToGame);
	}
	
	@Override
	public List<Game> findByUser(long userId) {
		return jdbc.query("SELECT " + GAME_FIELDS + " FROM games WHERE id = ?", this::mapRowToGame, userId);
	}
	
	@Override
	public List<Game> findByUser(String username) throws SQLException {
		long userId = jdbc.queryForObject("SELECT id FROM users WHERE name = ?", (rs, row) -> rs.getLong("id"), username);
		return findByUser(userId);
	}
	
	@Override
	public Game findById(long gameId) {
		return jdbc.queryForObject("SELECT " + GAME_FIELDS + " FROM games WHERE id = ?", this::mapRowToGame, gameId);
	}
	
	@Override
	public void save(Game game) {
		jdbc.update("INSERT INTO games (" + GAME_FIELDS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", game.getId(), game.getPlayerBlack(),
				game.getPlayerWhite(), game.getMovesAsString(), game.getStarted(), game.getLastPlayed(), game.getBoardSize(), game.isResigned(),
				game.getPoints(), game.isOver(), game.getHandycap(), game.getComi());
		
	}
	
	/**
	 * Build a Game object from a result set in the database
	 */
	private Game mapRowToGame(ResultSet rs, int row) throws SQLException {
		return new Game(rs.getLong("id"), rs.getDate("started_on").toLocalDate(), rs.getDate("last_played_on").toLocalDate(),
				rs.getLong("player_black"), rs.getLong("player_white"), Game.fromMoveString(rs.getString("moves")), rs.getInt("board_size"),
				rs.getBoolean("resigned"), rs.getBoolean("over"), rs.getDouble("points"), rs.getDouble("comi"), rs.getInt("handycap"));
	}
}