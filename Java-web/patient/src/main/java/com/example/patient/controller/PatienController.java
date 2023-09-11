package com.example.patient.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class PatienController {
	@GetMapping("/")
	public String getPatients(Model model) {
		ArrayList<Patient> patients = PatientDAO.selectAll();
		model.addAttribute("patients", patients);
		return "patients";
	}
	
	@GetMapping("/patient/{code}")
	public String getPatient(Model model, @PathVariable int code) {
		Patient patient = PatientDAO.selectById(code);
		model.addAttribute("patient", patient);
		return "patient";
	}

	@PostMapping("/patient/save/{code}")
	public String insertPatient(Model model, Patient patient, @PathVariable int code) {
		model = PatientDAO.insertPatient(model, patient);
		int status = (int) model.getAttribute("status");
		return status == 200 ? "redirect:/" : "/patient";
	}
	
	@PutMapping("/patient/save/{code}")
	public String updatePatient(Model model, Patient patient, @PathVariable int code ) {
		model = PatientDAO.updatePatient(model, patient);
		int status = (int) model.getAttribute("status");
		return status == 200 ? "redirect:/" : "/patient";
	}
	
	@DeleteMapping("/patient/delete/{code}")
	public String deletePatient(@PathVariable int code) {
		PatientDAO.deletePatient(code);
		return "redirect:/";
	}
}
