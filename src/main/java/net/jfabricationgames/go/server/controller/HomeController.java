package net.jfabricationgames.go.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.jfabricationgames.go.Page;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@GetMapping
	public String home() {
		return Page.WELCOME.getTemplateName();
	}
}