package org.magnum.symptom.client.gen.entities;


public class Answer {
	
	private long id;	
	private AnswerType answerType;
	private AnswerValue answerValue;
	private String dateAndTimeTaken;
	private Medicine medTaken;
	
	
	public Answer() {
		super();
	}
	public Answer(AnswerType answerType, AnswerValue answerValue,
			String dateAndTimeTaken, Medicine medTaken) {
		super();
		this.answerType = answerType;
		this.answerValue = answerValue;
		this.dateAndTimeTaken = dateAndTimeTaken;
		this.medTaken = medTaken;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public AnswerType getAnswerType() {
		return answerType;
	}
	public void setAnswerType(AnswerType answerType) {
		this.answerType = answerType;
	}
	
	public AnswerValue getAnswerValue() {
		return answerValue;
	}
	public void setAnswerValue(AnswerValue answerValue) {
		this.answerValue = answerValue;
	}
	
	public String getDateAndTimeTaken() {
		return dateAndTimeTaken;
	}
	public void setDateAndTimeTaken(String dateAndTimeTaken) {
		this.dateAndTimeTaken = dateAndTimeTaken;
	}
	
	public Medicine getMedTaken() {
		return medTaken;
	}
	public void setMedTaken(Medicine medTaken) {
		this.medTaken = medTaken;
	}
}

