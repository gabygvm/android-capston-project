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
import javax.persistence.OneToMany;

@Entity
public class CheckIn {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "patRecord_id")
	private PatientRecord patRecord;
	
	@OneToMany(fetch=FetchType.EAGER)
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
