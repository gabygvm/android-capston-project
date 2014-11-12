package org.magnum.mobilecloud.repositories;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MedicalRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id; //medical record number
	
	private UserInfo patient;
	private DoctorInfo doctor;
	private Collection<Recipe> recipes;

	public MedicalRecord() {
		super();
	}
	
	public MedicalRecord(UserInfo patient, DoctorInfo doctor,
			Collection<Recipe> recipes) {
		super();
		this.patient = patient;
		this.doctor = doctor;
		this.recipes = recipes;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public UserInfo getPatient() {
		return patient;
	}
	public void setPatient(UserInfo patient) {
		this.patient = patient;
	}
	public DoctorInfo getDoctor() {
		return doctor;
	}
	public void setDoctor(DoctorInfo doctor) {
		this.doctor = doctor;
	}
	public Collection<Recipe> getRecipes() {
		return recipes;
	}
	public void setRecipes(Collection<Recipe> recipes) {
		this.recipes = recipes;
	}
	
	
}
