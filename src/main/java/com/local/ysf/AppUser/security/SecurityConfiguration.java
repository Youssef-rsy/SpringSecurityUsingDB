package com.local.ysf.AppUser.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.formLogin();
		http.authorizeRequests().antMatchers("/*").permitAll();
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/data/onlyAdmin")
			.hasAuthority("ADMIN")
			.and()
			.authorizeRequests()
			.antMatchers(HttpMethod.GET,"/data/allRoles")
			.permitAll()
			.and()
            .logout()
            .logoutUrl("/logout")
           // .logoutSuccessHandler(new AuthentificationLogoutSuccessHandler())
            .invalidateHttpSession(true);
		//http.authorizeRequests().anyRequest().permitAll();

		//http.authorizeRequests().antMatchers("/").permitAll().and().authorizeRequests().antMatchers("/h2/**").permitAll();
		// http.csrf().disable();
		//http.headers().frameOptions().disable();
	}
}
