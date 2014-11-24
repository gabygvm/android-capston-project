package org.magnum.symptoms.service.repository;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"patient_id", "doctor_id"}))
public class PatientRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "patient_id")
	private Patient patient;
	
	@ManyToOne
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;
	
	@OneToMany(mappedBy = "patRecord")
	private List<Recipe> recipeList;

	@OneToMany(mappedBy = "patRecord")
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
