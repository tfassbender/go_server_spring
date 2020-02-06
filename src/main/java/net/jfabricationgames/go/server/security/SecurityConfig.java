package net.jfabricationgames.go.server.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import net.jfabricationgames.go.server.Page;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	//	@Autowired
	//	private DataSource dataSource;
	
	public static final String ROLE_USER = "USER";
	
	@Override
	protected void configure(AuthenticationManagerBuilder authentication) throws Exception {
		//	authentication.jdbcAuthentication().dataSource(dataSource);
		
		//add a default in-memory root user
		authentication.inMemoryAuthentication().withUser("root").password("asdf").authorities(ROLE_USER);
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		//find pages that need to be secured by specific roles and secure the pages
		String[] userPages = Page.getByRequiredRoles(Role.USER).stream().map(page -> page.getPageName()).toArray(size -> new String[size]);
		httpSecurity.authorizeRequests()//
				.antMatchers(userPages).hasRole(ROLE_USER)//
				.antMatchers("/", "/**").permitAll()
				
				//add a login form
				.and().formLogin().loginPage(Page.LOGIN.getPageName()).defaultSuccessUrl(Page.MENU.getPageName());
	}
}