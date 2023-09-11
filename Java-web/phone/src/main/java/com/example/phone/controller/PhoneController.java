package com.example.phone.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class PhoneController {
	@GetMapping("/")
	public String getPhones(Model model) {
		ArrayList<Phone> phones = PhoneDAO.selectAll();
		model.addAttribute("phones", phones);
		return "phones";
	}
	
	@GetMapping("/phone/{code}")
	public String getPhone(Model model, @PathVariable int code) {
		Phone phone = PhoneDAO.selectById(code);
		model.addAttribute("phone", phone);
		return "phone";
	}
	
	@PostMapping("/phone/save/{code}")
	public String addPhone(Model model, Phone phone, @PathVariable int code) {
		model = PhoneDAO.insertPhone(model, phone);
		int status = (int) model.getAttribute("status");
		return status == 200 ? "redirect:/" : "phone";
	}
	
	@PutMapping("/phone/save/{code}")
	public String updatePhone(Model model, Phone phone, @PathVariable int code) {
		model = PhoneDAO.insertPhone(model, phone);
		int status = (int) model.getAttribute("status");
		return status == 200 ? "redirect:/" : "phone";
	}
	
	@DeleteMapping("/phone/delete/{code}")
	public String deletePhone(@PathVariable int code) {
		PhoneDAO.deletePhone(code);
		return "redirect:/";
	}
}
