package com.example.phone.controller;

public class Phone {
	private int id;
	private String name;
	private String price;
	private String brand;
	private boolean sold;
	
	public Phone(int id, String name, String price, String brand, boolean sold) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.brand = brand;
		this.sold = sold;
	}

	public Phone() {
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}
}
