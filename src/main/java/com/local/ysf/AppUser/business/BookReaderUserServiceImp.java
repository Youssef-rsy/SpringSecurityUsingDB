package com.local.ysf.AppUser.business;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.local.ysf.AppUser.Entity.BookReaderRole;
import com.local.ysf.AppUser.Entity.BookReaderUser;
import com.local.ysf.AppUser.infrastricture.BookReaderRoleRrepository;
import com.local.ysf.AppUser.infrastricture.BookReaderUserRepository;
@Service
@Transactional
public class BookReaderUserServiceImp implements BookReaderUserService {

	@Autowired
	private BookReaderUserRepository userRepository;
	@Autowired
	private BookReaderRoleRrepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bcpe;
	
	
	@Override
	public BookReaderUser saveUser(BookReaderUser user) {
		String hashedPassword = bcpe.encode(user.getPassword());
		user.setPassword(hashedPassword);
		return userRepository.save(user);
	}

	@Override
	public BookReaderRole saveRole(BookReaderRole role) {
		return roleRepository.save(role);
	}

	@Override
	public BookReaderUser getUserByUsername(String username) {
		BookReaderUser user = userRepository.findByUsername(username);
		return user;
	}

	@Override
	public void addRoleToUser(String username, String role) {
		BookReaderUser user = getUserByUsername(username);
		BookReaderRole userRole = roleRepository.findByRole(role);
		user.getRoles().add(userRole);
	}

}
