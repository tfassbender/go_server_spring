package net.jfabricationgames.go.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/npe")
public class NullPointerController {
	
	@GetMapping
	public String npe() {
		throw new NullPointerException("NPE just for testing");
	}
}