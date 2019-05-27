package com.local.ysf.AppUser.JWT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.local.ysf.AppUser.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;

public class JWTAutorizationFilter extends OncePerRequestFilter {

	
	@Override
	protected void doFilterInternal(HttpServletRequest request , HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		 response.addHeader("Access-Control-Allow-Origin", "*");
		 response.addHeader("Access-Control-Allow-Headers",
		            		"Content-type , Origin , Accept ,X-Request-With,Access-Control-Request-Method,Access-Control-Request-Method ,Access-Control-Request-Headers ,authorization");
		 response.addHeader("access-Control-Expose-Headers", "Access-Control-Allow-Origin , Access-Control-Allow-Credentaials , authorization");
		 
		 if(request.getMethod().equals(HttpMethod.OPTIONS.toString()))
			 response.setStatus(HttpServletResponse.SC_OK);
		 else{
			 String jwtToken = request.getHeader(SecurityConstants.HEADER_STRING);
			 if(jwtToken==null || !jwtToken.startsWith(SecurityConstants.TOKEN_PREFIX)){
				 chain.doFilter(request, response);
				 return ;
			 }
				 System.out.println("|"+jwtToken+"|");
				 System.out.println("|"+jwtToken.replace(SecurityConstants.TOKEN_PREFIX, "")+"|");
				 Claims claims = Jwts.parser()
						 			.setSigningKey(Encoders.BASE64.encode(SecurityConstants.SECRET.getBytes()))
						 			.parseClaimsJws(jwtToken.replace(SecurityConstants.TOKEN_PREFIX, ""))
						 			.getBody();
				 String username = claims.getSubject();
				 ArrayList<Map<String , String>> roles = (ArrayList<Map<String, String>>) claims.get("roles");
				 Collection<GrantedAuthority> authorities = new ArrayList<>();
				 roles.forEach(role->{
					 authorities.add(new SimpleGrantedAuthority(role.get("authority")));
				 });
				
				 UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(username, null ,authorities);
				 SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
				 chain.doFilter(request, response); 
			 
			 
		 }
		
	}

}
