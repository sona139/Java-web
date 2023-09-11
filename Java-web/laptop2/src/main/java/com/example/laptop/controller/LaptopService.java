package com.example.laptop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaptopService {
	@Autowired
	private LaptopRepository repo;
	
	public List<Laptop> getLaptops() {
		return repo.findAll();
	}
}
