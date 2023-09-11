package com.example.bookJPA.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
	@Autowired
	private BookRepository repo;
	
	public List<Book> getBooks() {
		return repo.findAll();
	}
}
