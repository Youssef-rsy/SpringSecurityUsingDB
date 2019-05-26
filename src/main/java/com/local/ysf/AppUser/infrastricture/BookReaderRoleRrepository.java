package com.local.ysf.AppUser.infrastricture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.local.ysf.AppUser.Entity.BookReaderRole;
@Repository
public interface BookReaderRoleRrepository extends JpaRepository<BookReaderRole, Long> {

	public BookReaderRole findByRole(String role);
}
