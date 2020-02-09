package net.jfabricationgames.go;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.jfabricationgames.go.server.security.Role;

public enum Page {
	
	WELCOME("/", "home"),//
	LOGIN("/login", "login"),//
	REGISTER("/register", "register"),//
	MENU("/menu", "menu", Role.USER),//
	PLAY("/play", "play", Role.USER),//
	START_GAME("/start_game", "start_game", Role.USER);
	
	private final String pageName;
	private final String templateName;
	
	//all roles that can access this page (one of them is required)
	private final List<Role> requiredRoles;
	
	private Page(String pageName, String templateName, Role... roles) {
		this.pageName = pageName;
		this.templateName = templateName;
		this.requiredRoles = Arrays.asList(roles);
	}
	
	public String getPageName() {
		return pageName;
	}
	public String getTemplateName() {
		return templateName;
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