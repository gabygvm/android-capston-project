package org.magnum.mobilecloud.video.repository;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * A simple object to represent a Doctor
 * @author Gabriela Vera
 */
@Entity
public class DoctorInfo extends Person{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private Collection<UserInfo> patients;
	private Collection<MedicalRecord> medicalRecords;

	
	public DoctorInfo() {
		super();
	}
	public DoctorInfo(Collection<UserInfo> patients,
			Collection<MedicalRecord> medicalRecords) {
		super();
		this.patients = patients;
		this.medicalRecords = medicalRecords;
	}

	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public Collection<UserInfo> getPatients() {
		return patients;
	}
	public void setPatients(Collection<UserInfo> patients) {
		this.patients = patients;
	}

	public Collection<MedicalRecord> getMedicalRecords() {
		return medicalRecords;
	}
	public void setMedicalRecords(Collection<MedicalRecord> medicalRecord) {
		this.medicalRecords = medicalRecord;
	}
	
}
