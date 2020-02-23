package net.jfabricationgames.go.db.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import net.jfabricationgames.go.game.Game;
import net.jfabricationgames.go.server.data.User;

@Repository
public class GameRepositoryImpl implements GameRepository {
	
	private static final String GAME_FIELDS = "id, player_black, player_white, moves, started_on, last_played_on, "
			+ "board_size, resign, points, over, handycap, comi";
	private static final String GAME_FIELDS_NO_ID = "player_black, player_white, moves, started_on, last_played_on, "
			+ "board_size, resign, points, over, handycap, comi";
	
	private final JdbcTemplate jdbc;
	private final UserRepository userRepository;
	
	@Autowired
	public GameRepositoryImpl(JdbcTemplate jdbc, UserRepository userRepository) {
		this.jdbc = jdbc;
		this.userRepository = userRepository;
	}
	
	@Override
	public List<Game> findAll() {
		return jdbc.query("SELECT " + GAME_FIELDS + " FROM games", this::mapRowToGame);
	}
	
	@Override
	public List<Game> findByUser(int userId) {
		return jdbc.query("SELECT " + GAME_FIELDS + " FROM games WHERE id = ?", this::mapRowToGame, userId);
	}
	
	@Override
	public List<Game> findByUser(String username) throws SQLException {
		int userId = jdbc.queryForObject("SELECT id FROM users WHERE name = ?", (rs, row) -> rs.getInt("id"), username);
		return findByUser(userId);
	}
	
	@Override
	public Game findById(int gameId) {
		return jdbc.queryForObject("SELECT " + GAME_FIELDS + " FROM games WHERE id = ?", this::mapRowToGame, gameId);
	}
	
	@Override
	public Game create(Game game) {
		game.setStarted(LocalDate.now());
		
		//insert the game and get the id in the database (therefore there must be NO ID TO BE INSERTED here)
		PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
				"INSERT INTO games (" + GAME_FIELDS_NO_ID + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Types.INTEGER, Types.INTEGER, Types.VARCHAR,
				Types.DATE, Types.DATE, Types.INTEGER, Types.BOOLEAN, Types.FLOAT, Types.BOOLEAN, Types.INTEGER, Types.FLOAT);
		
		//return the key that was generated to set the id
		factory.setReturnGeneratedKeys(true);
		
		PreparedStatementCreator psc = factory.newPreparedStatementCreator(
				Arrays.asList(game.getPlayerBlack().getId(), game.getPlayerWhite().getId(), game.getMoves(), game.getStarted(), game.getLastPlayed(),
						game.getBoardSize(), game.isResigned(), game.getPoints(), game.isOver(), game.getHandycap(), game.getComi()));
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(psc, keyHolder);
		
		game.setId(keyHolder.getKey().intValue());
		
		return game;
	}
	
	@Override
	public void update(Game game) {
		jdbc.update("UPDATE games SET moves = ?, last_played_on = ?, resign = ?, points = ?, over = ? WHERE id = ?", game.getMoves(),
				game.getLastPlayed(), game.isResigned(), game.getPoints(), game.isOver(), game.getId());
	}
	
	/**
	 * Build a Game object from a result set in the database
	 */
	private Game mapRowToGame(ResultSet rs, int row) throws SQLException {
		int playerBlackId = rs.getInt("player_black");
		int playerWhiteId = rs.getInt("player_white");
		
		//get the players from the database
		User playerBlack = userRepository.findById(playerBlackId)
				.orElseThrow(() -> new SQLException("User for player_black not found. Id was " + playerBlackId));
		User playerWhite = userRepository.findById(playerWhiteId)
				.orElseThrow(() -> new SQLException("User for player_white not found. Id was " + playerWhiteId));
		
		//create the game from the database entry
		return new Game(rs.getInt("id"), rs.getDate("started_on").toLocalDate(), rs.getDate("last_played_on").toLocalDate(), playerBlack, playerWhite,
				rs.getString("moves"), null, rs.getInt("board_size"), rs.getBoolean("resigned"), rs.getBoolean("over"), rs.getDouble("points"),
				rs.getDouble("comi"), rs.getInt("handycap"));
	}
}