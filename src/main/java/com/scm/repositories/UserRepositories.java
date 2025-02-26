package com.scm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entity.User;
import java.util.List;

@Repository
public interface UserRepositories extends JpaRepository<User, String> {

	// extra methods of db related Operations
	// custom Query Methods

	// custom Finder Methods
	Optional<User> findByEmail(String email);

	Optional<User> findByEmailAndPassword(String email, String password);
	
	Optional<User> findByEmailToken(String emailToken);
	
	public User findByEmailAndPhoneNumber(String email , String phoneNumber);

}
