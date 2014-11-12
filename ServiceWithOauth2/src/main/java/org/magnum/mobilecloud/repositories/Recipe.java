package org.magnum.mobilecloud.repositories;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Recipe Object. It has the doctor that prescribed it, the patient it was for, and
 * the medicines that were prescribed.
 * @author Gabriela Vera
 */
@Entity
public class Recipe {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private DoctorInfo doctor;
	private UserInfo patient;
	
	private Collection<Medicine> medicines;
	
	public Recipe() {
		super();
	}
	public Recipe(DoctorInfo doctor, UserInfo patient, Collection<Medicine> medicines) {
		super();
		this.doctor = doctor;
		this.patient = patient;
		this.medicines = medicines;
	}
	
	public DoctorInfo getDoctor(){
		return this.doctor;
	}
	public UserInfo getPatient(){
		return this.patient;
	}
	public Collection<Medicine> getMedicines(){
		return this.medicines;
	}
}
