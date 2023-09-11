package com.example.book.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
	@CrossOrigin
	@GetMapping("/")
	public ArrayList<Book> getBooks(Model model) {
		Connection con = null;
		Statement statement = null;
		ResultSet res = null;
		ArrayList<Book> books = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/book", "root", "Iuhuyennhat1-");
			statement = con.createStatement();
			
			res = statement.executeQuery("SELECT * FROM book.book");
			while(res.next()) {
				int bookcode = res.getInt("bookcode");
				String title = res.getString("title");
				String author = res.getString("author");
				String category = res.getString("category");
				boolean sold = res.getInt("sold") != 0 ? true : false;
				
				books.add(new Book(bookcode, title, author, category, sold));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("books", books);
		return books;
	}
	
	@GetMapping("/book/{code}")
	public String getBook(Model model, @PathVariable int code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		
		Book book = new Book();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/book", "root", "Iuhuyennhat1-");
			
			int bookcode = 0;
			String title = null;
			String author = null;
			String category = null;
			boolean sold = false;
			
			String sqlQuery = null;
			
			if(code == -1) {
				sqlQuery = "SELECT * FROM book.book ORDER BY bookcode DESC LIMIT 1";
				
				ps = con.prepareStatement(sqlQuery);
				
				res = ps.executeQuery();
				
				while(res.next()) {
					bookcode = res.getInt(1)+1;
				}
			} else {
				sqlQuery = "SELECT * FROM book.book WHERE bookcode = ?";

				ps = con.prepareStatement("SELECT * FROM book.book WHERE bookcode = ?");
				ps.setInt(1, code);
				
				res = ps.executeQuery();
				
				while(res.next()) {
					bookcode = res.getInt(1);
					title = res.getString(2);
					author = res.getString(3);
					category = res.getString(4);
					sold = res.getInt(5) != 0 ? true : false;
				}
			}
			
			book = new Book(bookcode, title, author, category, sold);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("book", book);
		return "book-detail";
	}
	
	@PostMapping("/book/save/{code}")
	public String addBook(Model model, Book book, @PathVariable int code) {
		Connection con = null;
		PreparedStatement ps = null;
		String sqlQuery = "";
		
		int res = 0;
		
		System.out.println(book);
		
		//validate
		int bookcode = book.getBookcode();
		String title = getText(book.getTitle());
		String author = getText(book.getAuthor());
		String category = getText(book.getCategory());
		boolean sold = book.getSold();
		
		String msgTitle = validString(title, "Title");
		String msgAuthor = validString(author, "Author");
		String msgCategory = validString(category, "Category");
		
		model.addAttribute("msgTitle", msgTitle);
		model.addAttribute("msgAuthor", msgAuthor);
		model.addAttribute("msgCategory", msgCategory);

		if(!msgTitle.equals("") || !msgAuthor.equals("") || !msgCategory.equals("")) {
			model.addAttribute("book", book);
			return "book-detail";
		}
		
		if(checkData(bookcode, title, author)) {
			String msg = "Dữ liệu đã tồn tại";
			model.addAttribute("msg", msg);
			model.addAttribute("book", book);
			return "book-detail";
		}
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/book", "root", "Iuhuyennhat1-");
			
			sqlQuery = code == -1 
				? "INSERT INTO book.book(title, author, category, sold) VALUES (?, ?, ?, ?)" 
				: "UPDATE book.book SET title = ?, author = ?, category = ?, sold = ? WHERE (`bookcode` = ?)";
			ps = con.prepareStatement(sqlQuery);
			ps.setString(1, title);
			ps.setString(2, author);
			ps.setString(3, category);
			ps.setInt(4, sold ? 1 : 0);
			
			if(code != -1) ps.setInt(5, code);
			
			res = ps.executeUpdate();
			ps.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/";
	}
	
	private String getText(String str) {
		if(str.equals("")) return str;
		str = str.trim().toLowerCase();
		String[] ss = str.split("\\s+");
		for (int i = 0; i < ss.length; i++)
			ss[i] = ss[i].substring(0, 1).toUpperCase() + ss[i].substring(1);
		return String.join(" ", ss);
	}
	
	private String validString(String str, String title) {
		if(str.equals("")) return title + " không được để trống";
		return "";
	}
	
	private boolean checkData(int _bookcode, String _title, String _author) {
		Connection con = null;
		ResultSet result = null;
		Statement statement = null;
		ArrayList<Book> books = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/phone", "root", "Iuhuyennhat1-");
			
			statement = con.createStatement();
			
			result = statement.executeQuery("SELECT * FROM book.book");
			while(result.next()) {
				int bookcode = result.getInt(1);
				String title = result.getString(2);
				String author = result.getString(3);
				
				if(bookcode != _bookcode && title.toLowerCase().equals(_title.toLowerCase()) && author.toLowerCase().equals(_author.toLowerCase()))
					return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@GetMapping("/book/delete/{bookcode}")
	public String deleteBook(@PathVariable int bookcode) {
		Connection con = null;
		PreparedStatement ps = null;
		int res = 0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/book", "root", "Iuhuyennhat1-");
			
			ps = con.prepareStatement("DELETE FROM book.book WHERE bookcode = ?");
			ps.setInt(1, bookcode);
			
			res = ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "redirect:/";
	}
}
