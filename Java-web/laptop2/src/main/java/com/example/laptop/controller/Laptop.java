package com.example.laptop.controller;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Laptop {
	@Id
	private int id;
	private String name;
	private String date;
	private String branch;
	private boolean sold;
	
	public Laptop() {
	}
	public Laptop(int id, String name, String date, String branch, boolean sold) {
		super();
		this.id = id;
		this.name = name;
		this.date = date;
		this.branch = branch;
		this.sold = sold;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public boolean isSold() {
		return sold;
	}
	public void setSold(int sold) {
		this.sold = sold == 1 ? true : false;
	}
}
