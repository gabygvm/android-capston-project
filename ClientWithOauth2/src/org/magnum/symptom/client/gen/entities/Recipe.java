package org.magnum.symptom.client.gen.entities;

import java.util.List;

public class Recipe {
	
	private long id;
	private PatientRecord patRecord;
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
