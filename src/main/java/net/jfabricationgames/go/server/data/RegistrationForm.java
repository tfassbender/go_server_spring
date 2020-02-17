package net.jfabricationgames.go.server.data;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Data
public class RegistrationForm {
	
	private String username;
	private String password;
	
	public User toUser(PasswordEncoder encoder) {
		return new User(0, username, encoder.encode(password));
	}
}