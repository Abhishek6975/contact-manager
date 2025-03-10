package com.scm.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scm.entity.Contact;
import com.scm.entity.User;

@Repository
public interface ContactRepositories extends JpaRepository<Contact, String> {

	Page<Contact> findByUser(User user, Pageable pageable);
	
	@Query("SELECT c FROM Contact c WHERE c.user.id =:userId")
	List<Contact> findBYUserId(String userId);

	Page<Contact> findByUserAndNameContaining(User user, String nameKeyword, Pageable pageable); // this is Custom Finder
																								// Method's &
	// Containing used For Liked Query.

	Page<Contact> findByUserAndEmailContaining(User user, String emailkeyword, Pageable pageable);

	Page<Contact> findByUserAndPhoneNumberContaining(User user, String phoneNumberKeyword, Pageable pageable);

}
