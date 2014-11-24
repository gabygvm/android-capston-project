
package org.magnum.symptom.client.gen.entities;


public class Medicine {
	
	private long id;
	private String medicine;

	
	public Medicine() {
		super();
	}

	public Medicine(String medicine) {
		super();
		this.medicine = medicine;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMedicine() {
		return medicine;
	}

	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}
}
