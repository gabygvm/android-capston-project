package org.magnum.symptoms.service.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * A simple object to represent a Patient
 * 
 * @author Gabriela Vera
 */
@Entity
public class Reminder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String time;
	
	public Reminder(String time) {
		super();
		this.time = time;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
