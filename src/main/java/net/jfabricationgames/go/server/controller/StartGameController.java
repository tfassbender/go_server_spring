package net.jfabricationgames.go.server.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import net.jfabricationgames.go.server.Page;
import net.jfabricationgames.go.server.data.GameCreation;

@Slf4j
@Controller
@RequestMapping("/start_game")
public class StartGameController {
	
	@GetMapping
	public String showStartGameForm(Model model) {
		configureModel(model);
		return Page.START_GAME.getPageName();
	}
	
	@PostMapping
	public String processGameCreation(@Valid GameCreation gameCreation, Errors errors, Model model) {
		if (errors.hasErrors()) {
			//stay on the page if there are errors
			configureModel(model);
			return Page.START_GAME.getPageName();
		}
		
		log.info("received game creation {}", gameCreation);
		
		return "redirect:/play";
	}
	
	private void configureModel(Model model) {
		//add the game_creation attribute that is needed for the form
		model.addAttribute("game_creation", new GameCreation());
	}
}