package com.local.ysf.AppUser.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.local.ysf.AppUser.Entity.BookReaderUser;
import com.local.ysf.AppUser.business.BookReaderUserService;


@Configuration
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private BookReaderUserService service;
	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		BookReaderUser user = service.getUserByUsername(username);
		Collection<GrantedAuthority> roles = new ArrayList<>();
		if (user == null)
				throw new UsernameNotFoundException(username);
		user.getRoles().forEach(role ->{
			roles.add(new SimpleGrantedAuthority(role.getRole()));
		});
		return new User(user.getUsername(), user.getPassword(),roles);
	}

}
