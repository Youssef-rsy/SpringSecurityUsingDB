package com.local.ysf.AppUser.JWT;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;

import java.io.IOException;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.local.ysf.AppUser.Entity.BookReaderUser;
import com.local.ysf.AppUser.exposition.model.userModel;
import com.local.ysf.AppUser.security.SecurityConstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	
	private AuthenticationManager authenticationManager;
	@Autowired
	private ObjectMapper objectMapper;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {//deserialize data using jackson objectMapper to match the BookReaderUser type -getting username password -
		BookReaderUser user = null;
			try {
				user = new ObjectMapper().readValue(request.getInputStream(), BookReaderUser.class);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		return authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		User springUser = (User)authResult.getPrincipal();
		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512
		String base64Key = Encoders.BASE64.encode(SecurityConstants.SECRET.getBytes());
		String jwtToken = Jwts.builder()
							.setSubject(springUser.getUsername())
							.setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.Expiration_TIME))
							.signWith(SignatureAlgorithm.HS256 , base64Key)
							.claim("roles",authResult.getAuthorities())
							.compact();
		
		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX+jwtToken);
	}

}
