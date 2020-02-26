package net.jfabricationgames.go.react_test;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jfabricationgames.go.db.repository.GameRepository;
import net.jfabricationgames.go.db.repository.UserRepository;
import net.jfabricationgames.go.game.Game;
import net.jfabricationgames.go.game.GameState;
import net.jfabricationgames.go.game.Move;
import net.jfabricationgames.go.server.data.User;

@RestController("react_test_controller")//name of the bean is needed here!
@RequestMapping(path = "/react_test", produces = "application/json")
@AllArgsConstructor
@Slf4j
public class ReactTestController {
	
	/**
	 * a default user id for the react test (because this user is added in resources/data.sql)
	 */
	private static final int defaultUserId = 1;
	private static final double defaultComi = 5.5;
	private static final int defaultBoardSize = 9;
	
	@Autowired
	private GameRepository gameRepo;
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("/games/{id}")
	public ResponseEntity<GameState> getGameState(@PathVariable("id") Integer id) {
		log.info("loading game with id: {}", id);
		try {
			Game game = gameRepo.findById(id);
			GameState gameState = game.toGameState();
			
			ResponseEntity<GameState> entity = new ResponseEntity<GameState>(gameState, HttpStatus.OK);
			return entity;
		}
		catch (DataAccessException dae) {
			return new ResponseEntity<GameState>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/games")
	@ResponseStatus(HttpStatus.CREATED)
	public GameState createGame() {
		LocalDate now = LocalDate.now();
		
		User defaultUser = userRepo.findById(defaultUserId).get();
		log.info("user found: {}", defaultUser);
		
		Game game = new Game(0, now, now, defaultUser, defaultUser, "", null, defaultBoardSize, false, false, 0, defaultComi, 0);
		log.info("game created: {}", game);
		
		gameRepo.create(game);
		log.info("stored game in database");
		
		GameState gameState = game.toGameState();
		log.info("created game state: {}", gameState);
		
		return gameState;
	}
	
	@DeleteMapping("/games/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteGame(@PathVariable("id") Integer id) {
		log.info("deleting game: id={}", id);
		gameRepo.delete(id);
	}
	
	@RequestMapping(value = "/games/{id}/move", method = {RequestMethod.POST, RequestMethod.PUT})
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<GameState> makeMove(@PathVariable("id") int id, @RequestBody Move move) {
		log.info("received move: id={} move={}", id, move);
		Game game = gameRepo.findById(id);
		try {
			game.makeMove(move);
		}
		catch (IllegalArgumentException iae) {
			log.error("error while making the move", iae);
			return new ResponseEntity<GameState>(HttpStatus.BAD_REQUEST);
		}
		log.info("move executed");
		
		gameRepo.update(game);
		GameState gameState = game.toGameState();
		ResponseEntity<GameState> entity = new ResponseEntity<>(gameState, HttpStatus.CREATED);
		return entity;
	}
}