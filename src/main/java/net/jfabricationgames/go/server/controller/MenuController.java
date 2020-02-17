package net.jfabricationgames.go.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.jfabricationgames.go.Page;

@Controller
@RequestMapping("/menu")
public class MenuController {
	
	@GetMapping
	public String menu() {
		return Page.MENU.getTemplateName();
	}
}
