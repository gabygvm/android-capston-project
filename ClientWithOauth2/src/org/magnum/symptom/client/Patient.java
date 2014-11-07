package org.magnum.symptom.client;


/**
 * A simple object to represent a Patient
 * 
 * @author Gabriela Vera
 */
public class Patient extends Person /* implements UserDetails */{

	private long id;

	public Patient() {
		super();
	}

	public Patient(String name, String lastName, String birthDate,
			boolean isFemale, String username, String password) {
		super(name, lastName, birthDate, isFemale, username, password);
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
