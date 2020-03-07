package net.jfabricationgames.go.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/react")
public class ReactTestController {
	
	@GetMapping
	public String home() {
		//not working because it's no template
		return "index.html";
	}
}