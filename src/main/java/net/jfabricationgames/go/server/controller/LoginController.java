package net.jfabricationgames.go.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/login", produces = "text/html;charset=utf-8")
public class LoginController {
	
	@GetMapping
	public String login() {
		return "login";
	}
}