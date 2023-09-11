package com.example.laptop.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LaptopController {

	@Autowired
	private LaptopService service;
	
	@GetMapping("/laptops")
	public List<Laptop> getLaptops() throws IOException {
		List<Laptop> laptops = service.getLaptops();
		return laptops;
	}
}
