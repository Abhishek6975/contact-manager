package com.scm.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
	
	@Id
	private String id;
	private String name;
	private String email;
	private String phoneNumber;
	private String address;
	private String picture;
	
	@Column(length = 1000)
	private String description;
	private boolean favorite;
	
	private String websiteLink;
	private String linkedInLink;
	
	private String cloudinaryImagePublicId;
	
	@ManyToOne
	@JsonIgnore  // used Because When ever get Data on View that time it's provide data in the form of Recursion ,now provide only Contact Data  
	private User user;
	
	@OneToMany(mappedBy = "contact",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
	private List<SocialLink> links = new ArrayList<>();

}
