package org.magnum.symptoms.service.repository;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.security.oauth2.common.util.JsonDateSerializer;

/**
 * A simple object to represent a Person
 * 
 * @author Gabriela Vera
 */
@MappedSuperclass
public class Person {

	private String name;
	private String lastName;
	//@JsonSerialize(using=JsonDateSerializer.class)
	//private Date birthDate;
	private String birthDate;
	private boolean isFemale;

	private String username;
	private String password;

	public Person() {
		super();
	}

	public Person(String name, String lastName, String birthDate,
			boolean isFemale, String username, String password) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.isFemale = isFemale;
		this.username = username;
		this.password = password;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String name) {
		this.lastName = name;
	}

	public String getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(String birthdate) {
		this.birthDate = birthdate;
	}

	public boolean getIsFemale() {
		return this.isFemale;
	}

	public void setFemale(boolean isFemale) {
		this.isFemale = isFemale;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
