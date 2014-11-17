package org.magnum.symptoms.service.repository;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonBackReference;

/**
 * A simple object to represent a Patient
 * 
 * @author Gabriela Vera
 */
@Entity
public class Doctor extends Person{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToMany(mappedBy = "doctor")
	private List<PatientRecord> patientRecord;

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
	
	public List<PatientRecord> getPatientRecord() {
		return patientRecord;
	}

	public void setPatientRecord(List<PatientRecord> patientRecord) {
		this.patientRecord = patientRecord;
	}
}
