package com.example.patient.controller;

public class Patient {
	public int id;
	public String name;
	public String dob;
	public String disease;
	public boolean vaccinated;
	
	public Patient() {
	}

	public Patient(int id, String name, String dob, String disease, boolean vaccinated) {
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.disease = disease;
		this.vaccinated = vaccinated;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public boolean isVaccinated() {
		return vaccinated;
	}

	public void setVaccinated(boolean vaccinated) {
		this.vaccinated = vaccinated;
	}
	
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", dob=" + dob + ", disease=" + disease + ", vaccinated="
				+ vaccinated + "]";
	}
}
