package com.scm.controller;

import java.util.List;
import java.util.UUID;

import javax.naming.Binding;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entity.Contact;
import com.scm.entity.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.repositories.ContactRepositories;
import com.scm.helpers.Message.MessageBuilder;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

	@Autowired
	private ContactService contactService;

	@Autowired
	private UserService userService;

	@Autowired
	private ImageService imageService;

	// add contact page handler
	@RequestMapping("/add")
	public String addContactView(Model model) {

		ContactForm contactForm = new ContactForm();

		model.addAttribute("contactForm", contactForm);
		return "user/add_contact";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult,
			Authentication authentication, HttpSession session) {

		if (bindingResult.hasErrors()) {
//			session.setAttribute("message", Message.builder()
//					.content("Contact Not Added")
//					.type(MessageType.red)
//					.build());
			session.setAttribute("message", "Contact Not Added");

			return "user/add_contact";
		}

		String username = Helper.getEmailOfLoggedInUser(authentication);
		User user = userService.getUserByEmail(username);

		// upload karne ka code

		Contact contact = new Contact();
		contact.setName(contactForm.getName());
		contact.setEmail(contactForm.getEmail());
		contact.setPhoneNumber(contactForm.getPhoneNumber());
		contact.setAddress(contactForm.getAddress());
		contact.setDescription(contactForm.getDescription());
		contact.setWebsiteLink(contactForm.getWebsiteLink());
		contact.setLinkedInLink(contactForm.getLinkedinLink());
		contact.setFavorite(contactForm.isFavorite());
		contact.setUser(user);

		// in addContact when user have option to select or not select Contact Image
		if (contactForm.getContactImage() != null || !contactForm.getContactImage().isEmpty()) {
			String filename = UUID.randomUUID().toString();
			String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);
			contact.setPicture(fileURL);
			contact.setCloudinaryImagePublicId(filename);
		}

		contactService.save(contact);
		System.out.println(contactForm);

//		session.setAttribute("message", Message.builder()
//				.content("You Contact Successfully added")
//				.type(MessageType.green)
//				.build());
		// MessageBuilder messageBuilder = Message.builder().content("You Contact
		// Successfully added").type(MessageType.green);

		session.setAttribute("message", "You Contact Successfully added");

		return "redirect:/user/contacts/add";
	}

	@RequestMapping()
	public String viewContacts(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
			@RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
			@RequestParam(value = "direction", defaultValue = "asc") String direction, Authentication authentication,
			Model model) {

		// load all the user contacts
		String username = Helper.getEmailOfLoggedInUser(authentication);
		
		User user = userService.getUserByEmail(username);
		List<Contact> userHiddenData = contactService.getByUserId(user.getUserId());
		
		Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);
		
		
		model.addAttribute("userHidden", userHiddenData);
		
		model.addAttribute("pageContact", pageContact);

		model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

		model.addAttribute("contactSearchForm", new ContactSearchForm());

		return "user/contacts";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchHandler(@ModelAttribute ContactSearchForm contactSearchForm,
			@RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
			@RequestParam(value = "direction", defaultValue = "asc") String direction, Authentication authentication,
			Model model) {

		// System.out.println("field :" + field);
		// System.out.println("Keyword :" + Keyword);
		System.out.println("field :" + contactSearchForm.getField());
		System.out.println("keyword :" + contactSearchForm.getValue());

		User user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

		Page<Contact> pageContact = null;
		
		if (contactSearchForm.getField().equalsIgnoreCase("name")) {

			pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,
					user);
		} else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
			pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,
					user);
		} else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
			pageContact = contactService.searchByPhone(contactSearchForm.getValue(), size, page, sortBy, direction,
					user);
		}

		System.out.println("pageContact :" + pageContact);

		model.addAttribute("contactSearchForm", contactSearchForm);

		model.addAttribute("pageContact", pageContact);

		model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

		return "user/search";
	}

	@RequestMapping("/delete/{contactid}")
	public String deleteContact(@PathVariable("contactid") String contactid, HttpSession httpSession) {

		contactService.delete(contactid);

		httpSession.setAttribute("message", "Contact Deleted Successfully!");

		return "redirect:/user/contacts";
	}

	@GetMapping("/view/{contactid}")
	public String updateContactFormView(@PathVariable("contactid") String contactid, Model model) {

		Contact contact = contactService.getById(contactid);

		ContactForm contactForm = new ContactForm();

		contactForm.setName(contact.getName());
		contactForm.setEmail(contact.getEmail());
		contactForm.setPhoneNumber(contact.getPhoneNumber());
		contactForm.setAddress(contact.getAddress());
		contactForm.setDescription(contact.getDescription());
		contactForm.setFavorite(contact.isFavorite());
		contactForm.setWebsiteLink(contact.getWebsiteLink());
		contactForm.setLinkedinLink(contact.getLinkedInLink());
		contactForm.setPicture(contact.getPicture());

		model.addAttribute("contactForm", contactForm);

		model.addAttribute("contactId", contactid);

		return "user/update_contact_view";
	}

	@RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
	public String updateCotact(@PathVariable("contactId") String contactId,
			@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "user/update_contact_view";
		}

		Contact contact = contactService.getById(contactId);

		contact.setId(contactId);
		contact.setName(contactForm.getName());
		contact.setEmail(contactForm.getEmail());
		contact.setPhoneNumber(contactForm.getPhoneNumber());
		contact.setAddress(contactForm.getAddress());
		contact.setDescription(contactForm.getDescription());
		contact.setFavorite(contactForm.isFavorite());
		contact.setWebsiteLink(contactForm.getWebsiteLink());
		contact.setLinkedInLink(contactForm.getLinkedinLink());
		// contact.setPicture(contactForm.getPicture());

		if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {

			String fileName = UUID.randomUUID().toString();
			String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
			contact.setCloudinaryImagePublicId(fileName);
			contact.setPicture(imageUrl);
			contactForm.setPicture(imageUrl);
		}

		contactService.update(contact);

		model.addAttribute("message", "Contact Updated!");

		return "user/update_contact_view";
	}

}
