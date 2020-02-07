package net.jfabricationgames.go.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import net.jfabricationgames.go.server.Page;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String home() {
		return Page.WELCOME.getPageName();
	}
}