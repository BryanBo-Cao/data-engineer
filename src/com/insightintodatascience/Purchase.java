package com.insightintodatascience;

public class Purchase {
	
	public Purchase(String timestamp, int id, double amount) {
		this.setTimestamp(timestamp);
		this.setId(id);
		this.setAmount(amount);
	}
	
	private int id;
	private String timestamp;	//should be Date, for the simplicity of this 
	private double amount;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
