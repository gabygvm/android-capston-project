package org.magnum.symptom.client.gen.entities;



/**
 * A simple object to represent a Patient
 * 
 * @author Gabriela Vera
 */
public class Doctor extends Person /* implements UserDetails */{

	private long id;
	public Doctor() {
		super();
	}

	public Doctor(String name, String lastName, String birthDate,
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
