package org.magnum.mobilecloud.video.repository;

import java.util.Date;

/**
 * A simple object to represent a Person
 * @author Gabriela Vera
 */

public class Person {

	private String name;
	private String lastName;
	private Date birthDate;
	private boolean isFemale;

	public Person(){
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
	
	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthdate) {
		this.birthDate = birthdate;
	}
	
	public boolean getIsFemale(){
		return this.isFemale;
	}
	public void setIsFemale(boolean isFemale){
		this.isFemale = isFemale;
	}
}
