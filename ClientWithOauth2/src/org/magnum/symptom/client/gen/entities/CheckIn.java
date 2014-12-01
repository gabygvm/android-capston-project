package org.magnum.symptom.client.gen.entities;

import java.util.List;

public class CheckIn {

	private long id;
	private PatientRecord patRecord;
	private List<Answer> answers;

	
	public CheckIn() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CheckIn(PatientRecord patRecord, List<Answer> answers) {
		super();
		this.patRecord = patRecord;
		this.answers = answers;
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
	
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
}
