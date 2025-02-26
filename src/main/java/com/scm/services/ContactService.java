package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scm.entity.Contact;
import com.scm.entity.User;

public interface ContactService {
	// save Contact
	Contact save(Contact contact);

	// Update Contact

	Contact update(Contact contact);

	List<Contact> getAll();

	// get contact ById

	Contact getById(String id);

	// delete contact

	void delete(String id);

	// search contact
	// public List<Contact> search(String name, String email, String phoneNumber)

	Page<Contact> searchByName(String nameKeyword, int size, int Page, String sortBy, String order, User user);

	Page<Contact> searchByEmail(String emailKeyword, int size, int Page, String sortBy, String order, User user);

	Page<Contact> searchByPhone(String phoneNumberKeyword, int size, int Page, String sortBy, String order, User user);

	// get contact by userId

	List<Contact> getByUserId(String userid);

	Page<Contact> getByUser(User user, int Page, int size, String sortBy, String direction);

}
