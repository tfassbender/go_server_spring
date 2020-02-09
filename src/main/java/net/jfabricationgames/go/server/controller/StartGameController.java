package net.jfabricationgames.go.server.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import net.jfabricationgames.go.Page;
import net.jfabricationgames.go.db.repository.GameRepository;
import net.jfabricationgames.go.db.repository.UserRepository;
import net.jfabricationgames.go.game.Game;
import net.jfabricationgames.go.game.PlayerColor;
import net.jfabricationgames.go.server.data.GameCreation;
import net.jfabricationgames.go.server.data.User;

@Slf4j
@Controller
@RequestMapping("/start_game")
@SessionAttributes("game")
public class StartGameController {
	
	private final GameRepository gameRepository;
	private final UserRepository userRepository;
	
	@Autowired
	public StartGameController(GameRepository gameRepository, UserRepository userRepository) {
		this.gameRepository = gameRepository;
		this.userRepository = userRepository;
	}
	
	@ModelAttribute(name = "game")
	public Game game() {
		return new Game();
	}
	
	@GetMapping
	public String showStartGameForm(Model model) {
		//add the game_creation attribute that is needed for the form
		model.addAttribute("game_creation", new GameCreation());
		return Page.START_GAME.getPageName();
	}
	
	@PostMapping
	public String processGameCreation(@Valid @ModelAttribute("game_creation") GameCreation gameCreation, Errors errors, Model model,
			@ModelAttribute Game game, @AuthenticationPrincipal User user) {
		if (errors.hasErrors()) {
			//stay on the page if there are errors
			return Page.START_GAME.getPageName();
		}
		
		log.info("received game creation {}", gameCreation);
		
		User player2 = userRepository.findByUsername(gameCreation.getOpponentPlayer());
		if (player2 == null) {
			log.info("player2 not found");
			//set the error and stay on the page
			errors.rejectValue("opponentPlayer", "error.opponentPlayer", "A user whith this name doesn't exist");
			return Page.START_GAME.getPageName();
		}
		else {
			log.info("found player2: {}", player2);
		}
		
		game.startGame(gameCreation);
		if (gameCreation.getColor().equalsIgnoreCase(PlayerColor.BLACK.name())) {
			game.setPlayerBlack(user);
			game.setPlayerWhite(player2);
		}
		else {
			game.setPlayerWhite(user);
			game.setPlayerBlack(player2);
		}
		log.info("game started {}", game);
		
		gameRepository.create(game);
		log.info("game created in database");
		
		return "redirect:/play";
	}
}