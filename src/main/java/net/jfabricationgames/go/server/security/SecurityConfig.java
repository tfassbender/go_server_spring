package net.jfabricationgames.go.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.jfabricationgames.go.Page;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	//	@Autowired
	//	private DataSource dataSource;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//find pages that need to be secured by specific roles and secure the pages
		String[] userPages = Page.getByRequiredRoles(Role.USER).stream().map(page -> page.getPageName()).toArray(size -> new String[size]);
		http.authorizeRequests()//
				.antMatchers(userPages).hasAuthority(Role.USER.getName())//
				.antMatchers("/", "/**").permitAll()
				
				//add a login form
				.and().formLogin().loginPage(Page.LOGIN.getPageName()).defaultSuccessUrl(Page.MENU.getPageName())
				
				//add a logout form
				.and().logout().logoutSuccessUrl("/logged_out");

		//TODO re-enable CSRF after tests
		http.csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)//
				.passwordEncoder(encoder());
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	//	@Override
	//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	//		auth.jdbcAuthentication().dataSource(dataSource)//
	//				.usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")//
	//				.authoritiesByUsernameQuery(
	//						"SELECT users.username, auth.authority FROM users JOIN user_authorities AS auth ON users.id = auth.user_id WHERE users.username = ?")//
	//				.passwordEncoder(new BCryptPasswordEncoder());
	//	}
	
	//	@Bean
	//	@Override
	//	public UserDetailsService userDetailsService() {
	//		//withDefaultPasswordEncoder should only be used for testing
	//		@SuppressWarnings("deprecation")
	//		UserDetails user = User.withDefaultPasswordEncoder().username("root").password("asdf").roles(Role.USER.getName()).build();
	//		
	//		return new InMemoryUserDetailsManager(user);
	//	}
}