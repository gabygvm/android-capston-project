package org.magnum.symptoms.service.repository;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * A simple object to represent a Person
 * 
 * @author Gabriela Vera
 */
@MappedSuperclass
public class Person {

	private String name;
	private String lastName;
	private String birthDate;
	private boolean isFemale;

/*	@Lob
	@Column(length=10*1024*1024)
	@JsonIgnore
	private byte[] image;
*/	@Column(length=Integer.MAX_VALUE)
	private String imageBase64;
	
	@Column(unique=true)
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
	/*
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
*/
	public String getImageBase64() {
		return imageBase64;
	}

	public void setImageBase64(String imageBase64) {
		this.imageBase64 = imageBase64;
	}

	@PrePersist
	protected void MedicinePrePersist()
	{
		this.name = this.name.toUpperCase();
		this.lastName = this.lastName.toUpperCase();
	}
}
