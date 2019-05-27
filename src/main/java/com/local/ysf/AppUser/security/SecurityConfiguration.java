package com.local.ysf.AppUser.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.local.ysf.AppUser.JWT.JWTAuthenticationFilter;
import com.local.ysf.AppUser.JWT.JWTAutorizationFilter;

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
		/*
		http
		.csrf().disable()
		    // make sure we use stateless session; session won't be used to store user's state.
	 	    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) 	
		.and()
		    // handle an authorized attempts 
		    .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)) 	
		.and()
		   // Add a filter to validate the tokens with every request
		   .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
		// authorization requests config
		.authorizeRequests()
		   // allow all who are accessing "auth" service
		   .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()  
		   // must be an admin if trying to access admin area (authentication is also required here)
		   .antMatchers("/gallery" + "/admin/**").hasRole("ADMIN")
		   // Any other request must be authenticated
		   .anyRequest().authenticated();
		*/
		http.csrf().disable() .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) ;
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
            .invalidateHttpSession(true);
		http.addFilter(new JWTAuthenticationFilter(authenticationManager()))
			.addFilterBefore(new JWTAutorizationFilter(), UsernamePasswordAuthenticationFilter.class);

	}
}
