package net.jfabricationgames.go.react_test;

import java.time.LocalDate;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import net.jfabricationgames.go.db.repository.GameCrudRepository;
import net.jfabricationgames.go.db.repository.GameRepository;
import net.jfabricationgames.go.db.repository.UserRepository;
import net.jfabricationgames.go.game.Game;
import net.jfabricationgames.go.game.GameState;
import net.jfabricationgames.go.game.Move;
import net.jfabricationgames.go.game.PlayerColor;

@RestController("react_test_controller")//name of the bean is needed here!
@RequestMapping(path = "/react_test", produces = "application/json")
@AllArgsConstructor
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
	private GameCrudRepository gameCrudRepo;
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("/games/{id}")
	public GameState getGameState(@PathParam("id") int id) {
		return gameRepo.findById(id).toGameState();
	}
	
	@PostMapping("/games/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public GameState createGame(@PathParam("id") int id) {
		Game game = new Game(id, LocalDate.now(), LocalDate.now(), userRepo.findById(defaultUserId).get(), userRepo.findById(defaultUserId).get(), "",
				null, defaultBoardSize, false, false, 0, defaultComi, 0);
		gameRepo.create(game);
		return game.toGameState();
	}
	
	@DeleteMapping("/games/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteGame(@PathParam("id") int id) {
		gameCrudRepo.deleteById(id);
	}
	
	@RequestMapping(value = "/games/{id}/move/{row}/{col}", method = {RequestMethod.POST, RequestMethod.PUT})
	@ResponseStatus(HttpStatus.CREATED)
	public GameState makeMove(@PathParam("id") int id, @PathParam("row") int row, @PathParam("col") int col) {
		Game game = gameRepo.findById(id);
		List<Move> moveList = game.getMoveList();
		PlayerColor playersTurn = PlayerColor.getOpposizeColor(moveList.get(moveList.size()- 1).getColor());
		if (playersTurn == null) {
			playersTurn = PlayerColor.BLACK;//black player starts
		}
		
		Move move = new Move(row, col, playersTurn);
		game.makeMove(move);
		
		gameRepo.update(game);
		return game.toGameState();
	}
}