package com.local.ysf.AppUser.business;

import com.local.ysf.AppUser.Entity.BookReaderRole;
import com.local.ysf.AppUser.Entity.BookReaderUser;

public interface BookReaderUserService {
	
	public BookReaderUser saveUser(BookReaderUser user);
	
	public BookReaderRole saveRole(BookReaderRole role);
	
	public BookReaderUser getUserByUsername(String username);
	
	void addRoleToUser(String username, String role);

}
