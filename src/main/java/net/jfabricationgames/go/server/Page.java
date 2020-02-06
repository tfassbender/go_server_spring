package net.jfabricationgames.go.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.jfabricationgames.go.server.security.Role;

public enum Page {
	
	WELCOME("/"),//
	LOGIN("/login"),//
	MENU("/menu", Role.USER),//
	PLAY("/play", Role.USER),//
	START_GAME("/start_game", Role.USER);
	
	private final String pageName;
	//all roles that can access this page (one of them is required)
	private final List<Role> requiredRoles;
	
	private Page(String pageName, Role... roles) {
		this.pageName = pageName;
		this.requiredRoles = Arrays.asList(roles);
	}
	
	public String getPageName() {
		return pageName;
	}
	public List<Role> getRequiredRoles() {
		return requiredRoles;
	}
	
	public static List<Page> getByRequiredRoles(Role... roles) {
		List<Role> roleList = Arrays.asList(roles);
		
		//!Collections.disjoint(A, B) = A.containsAny(B)
		List<Page> containedRequiredRoles = Arrays.stream(values()).filter(page -> !Collections.disjoint(page.requiredRoles, roleList))
				.collect(Collectors.toList());
		
		return containedRequiredRoles;
	}
}