package net.jfabricationgames.go.server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import net.jfabricationgames.go.server.Page;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	//	@Autowired
	//	private DataSource dataSource;
	
	public static final String ROLE_USER = "USER";
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//find pages that need to be secured by specific roles and secure the pages
		String[] userPages = Page.getByRequiredRoles(Role.USER).stream().map(page -> page.getPageName()).toArray(size -> new String[size]);
		http.authorizeRequests()//
				.antMatchers(userPages).hasRole(Role.USER.getName())//
				.antMatchers("/", "/**").permitAll()
				
				//add a login form
				.and().formLogin().loginPage(Page.LOGIN.getPageName()).defaultSuccessUrl(Page.MENU.getPageName())
				
				//add a logout form
				.and().logout().logoutSuccessUrl("/logged_out");
	}
	
	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		//withDefaultPasswordEncoder should only be used for testing
		@SuppressWarnings("deprecation")
		UserDetails user = User.withDefaultPasswordEncoder().username("root").password("asdf").roles(Role.USER.getName()).build();
		
		return new InMemoryUserDetailsManager(user);
	}
}