package net.jfabricationgames.go.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.jfabricationgames.go.Page;
import net.jfabricationgames.go.db.repository.UserRepository;
import net.jfabricationgames.go.server.data.RegistrationForm;

@Controller
@RequestMapping("/register")
public class RegisterController {
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public RegisterController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@GetMapping
	public String register() {
		return Page.REGISTER.getTemplateName();
	}
	
	@PostMapping
	public String processRegistrationString(RegistrationForm form) {
		userRepository.save(form.toUser(passwordEncoder));
		return "redirect:/login";
	}
}