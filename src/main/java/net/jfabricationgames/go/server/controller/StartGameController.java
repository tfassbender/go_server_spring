package net.jfabricationgames.go.server.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import net.jfabricationgames.go.game.Game;
import net.jfabricationgames.go.server.data.GameCreation;

@Slf4j
@Controller
@RequestMapping("/start_game")
@SessionAttributes("game")
public class StartGameController {
	
	private final GameRepository gameRepository;
	
	@Autowired
	public StartGameController(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
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
	public String processGameCreation(@Valid @ModelAttribute("game_creation") GameCreation gameCreation, Errors errors, Model model, @ModelAttribute Game game) {
		if (errors.hasErrors()) {
			//stay on the page if there are errors
			return Page.START_GAME.getPageName();
		}
		
		log.info("received game creation {}", gameCreation);
		
		game.startGame(gameCreation);
		log.info("game started {}", game);
		
		gameRepository.create(game);
		log.info("game created in database");
		
		return "redirect:/play";
	}
}