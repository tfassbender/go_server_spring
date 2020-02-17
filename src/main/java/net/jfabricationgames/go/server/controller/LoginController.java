package net.jfabricationgames.go.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.jfabricationgames.go.Page;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@GetMapping
	public String login() {
		return Page.LOGIN.getTemplateName();
	}
}