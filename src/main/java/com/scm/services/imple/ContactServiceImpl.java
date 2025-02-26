package com.scm.services.imple;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entity.Contact;
import com.scm.entity.User;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepositories;
import com.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactRepositories contactRepositories;

	@Override
	public Contact save(Contact contact) {
		String contactId = UUID.randomUUID().toString();
		contact.setId(contactId);
		return this.contactRepositories.save(contact);
	}

	@Override
	public Contact update(Contact contact) {
		
	Contact contactOld = contactRepositories.findById(contact.getId()).orElseThrow(() -> new ResourceNotFoundException("Contact Not Found!"));
		
	contactOld.setName(contact.getName());
	contactOld.setEmail(contact.getEmail());
	contactOld.setPhoneNumber(contact.getPhoneNumber());
	contactOld.setAddress(contact.getAddress());
	contactOld.setDescription(contact.getDescription());
	contactOld.setFavorite(contact.isFavorite());
	contactOld.setWebsiteLink(contact.getWebsiteLink());
	contactOld.setLinkedInLink(contact.getLinkedInLink());
	contactOld.setPicture(contact.getPicture());
	contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());
	contact.setLinks(contact.getLinks());
	
	return contactRepositories.save(contactOld);
	}

	@Override
	public List<Contact> getAll() {
		return this.contactRepositories.findAll();
	}

	@Override
	public Contact getById(String id) {
		return this.contactRepositories.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact not Found with given " + id + " id"));
	}

	@Override
	public void delete(String id) {

		Contact contact = this.contactRepositories.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact Not Foun!"));

		this.contactRepositories.delete(contact);
	}

	@Override
	public List<Contact> getByUserId(String userid) {
		return this.contactRepositories.findBYUserId(userid);
	}

	@Override
	public Page<Contact> getByUser(User user, int Page, int size, String sortBY, String direction) {

		Sort sort = direction.equals("desc") ? Sort.by(sortBY).descending() : Sort.by(sortBY).ascending();

		Pageable pageable = PageRequest.of(Page, size, sort);

		return contactRepositories.findByUser(user, pageable);

	}

	@Override
	public Page<Contact> searchByName(String nameKeyword, int size, int Page, String sortBy, String order, User user) {

		Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(Page, size, sort);

		return contactRepositories.findByUserAndNameContaining(user, nameKeyword, pageable);
	}

	@Override
	public Page<Contact> searchByEmail(String emailKeyword, int size, int Page, String sortBy, String order,
			User user) {

		Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(Page, size, sort);

		return contactRepositories.findByUserAndEmailContaining(user, emailKeyword, pageable);
	}

	@Override
	public Page<Contact> searchByPhone(String phoneNumberKeyword, int size, int Page, String sortBy, String order,
			User user) {

		Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(Page, size, sort);

		return contactRepositories.findByUserAndPhoneNumberContaining(user, phoneNumberKeyword, pageable);
	}

}
