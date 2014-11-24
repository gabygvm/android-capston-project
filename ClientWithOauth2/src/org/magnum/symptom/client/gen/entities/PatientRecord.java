package org.magnum.symptom.client.gen.entities;

import java.util.List;

public class PatientRecord {

	private long id;
	private Patient patient;
	private Doctor doctor;
	private List<Recipe> recipeList;
	private List<CheckIn> checkInList;
	
	public PatientRecord() {
		super();
	}
	public PatientRecord(Patient patient, Doctor doctor) {
		super();
		this.patient = patient;
		this.doctor = doctor;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public List<Recipe> getRecipeList() {
		return recipeList;
	}
	public void setRecipeList(List<Recipe> recipeList) {
		this.recipeList = recipeList;
	}
}
