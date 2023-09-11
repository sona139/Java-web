package com.example.book.controller;

public class Book {
	private int bookcode;
	private String title;
	private String author;
	private String category;
	private boolean sold;
	
	public Book() {
	}

	public Book(int bookcode, String title, String author, String category, boolean sold) {
		this.bookcode = bookcode;
		this.title = title;
		this.author = author;
		this.category = category;
		this.sold = sold;
	}

	public int getBookcode() {
		return bookcode;
	}

	public void setBookCode(int bookcode) {
		this.bookcode = bookcode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean getSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}

	@Override
	public String toString() {
		return "Book [bookcode=" + bookcode + ", title=" + title + ", author=" + author + ", category=" + category
				+ ", sold=" + sold + "]";
	}
}
