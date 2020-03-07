package net.jfabricationgames.go.server.security;

public enum Role {
	
	USER("USER");
	
	private final String name;
	
	private Role(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}