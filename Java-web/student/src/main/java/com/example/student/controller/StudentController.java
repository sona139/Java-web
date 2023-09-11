package com.example.student.controller;


import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class StudentController {
	@GetMapping("/")
	public String getStudents(Model model) {
		ArrayList<Student> students = StudentDAO.selectAll();
		model.addAttribute("students", students);
		return "students";
	}
	
	@GetMapping("/student/{code}")
	public String getStudent(Model model, @PathVariable int code) {
		Student student = StudentDAO.selectById(code);
		model.addAttribute("student", student);
		return "student";
	}

	@PostMapping("/student/save/{code}")
	public String insertStudent(Model model, Student student, @PathVariable int code) {
		model = StudentDAO.insertStudent(model, student);
		int status = (int) model.getAttribute("status");
		return status == 200 ? "redirect:/" : "/student";
	}
	
	@PutMapping("/student/save/{code}")
	public String updateStudent(Model model, Student student, @PathVariable int code ) {
		model = StudentDAO.updateStudent(model, student);
		int status = (int) model.getAttribute("status");
		return status == 200 ? "redirect:/" : "/student";
	}
	
	@DeleteMapping("/student/delete/{code}")
	public String deleteStudent(Student student, @PathVariable int code) {
		StudentDAO.deleteStudent(code);
		return "redirect:/";
	}
}
