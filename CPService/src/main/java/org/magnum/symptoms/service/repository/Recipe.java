package org.magnum.symptoms.service.repository;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Recipe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "patRecord_id")
	private PatientRecord patRecord;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<Medicine> medicines;
	
	private String time;
	
	public Recipe() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Recipe(PatientRecord patRecord, List<Medicine> medicines, String time) {
		super();
		this.patRecord = patRecord;
		this.medicines = medicines;
		this.time = time;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PatientRecord getPatRecord() {
		return patRecord;
	}

	public void setPatRecord(PatientRecord patRecord) {
		this.patRecord = patRecord;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<Medicine> getMedicines() {
		return medicines;
	}

	public void setMedicines(List<Medicine> medicines) {
		this.medicines = medicines;
	}
	
}
