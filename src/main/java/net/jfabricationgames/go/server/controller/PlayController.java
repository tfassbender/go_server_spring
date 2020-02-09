package net.jfabricationgames.go.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import net.jfabricationgames.go.db.repository.GameRepository;

@Controller
@RequestMapping("/play")
@SessionAttributes("game")
public class PlayController {
	
	@SuppressWarnings("unused")
	private GameRepository gameRepository;
	
	@Autowired
	public PlayController(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}
	
	@GetMapping
	public String showCurrentGame() {
		return "current_game";
	}
}