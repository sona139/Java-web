package com.example.HelloWorld.book;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookController {
	@GetMapping("/books")
	public String getBooks(Model model) throws IOException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList<Book> books = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/books", "root", "Iuhuyennhat1-");
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select bookcode, title, author, CategoryName, approved FROM book inner join category on category = CategoryID order by bookcode;");
			while (resultSet.next()) {
				int bookcode = resultSet.getInt("bookcode");
				String title = resultSet.getString("title");
				String author = resultSet.getString("author");
				String category = resultSet.getString("CategoryName");
				int approved = resultSet.getInt("approved");
				books.add(new Book(bookcode, title, author, category, approved == 0 ? false : true));
			}
		} // End of try block
		catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("books", books);
		return "books";
	}
	
	@GetMapping("/book/{bookcode}")
	public String getBook(Model model, @PathVariable String bookcode) {
		model.addAttribute("bookcode", bookcode);
		Connection connection = null;
		PreparedStatement ps = null;
//		Statement statement = null;
		ResultSet result = null;
		Book book = new Book();
//		ArrayList<Category> categorys = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/books", "root", "Iuhuyennhat1-");
			ps = connection.prepareStatement("select bookcode, title, author, CategoryName, approved FROM book inner join category on category = CategoryID where bookcode = ?");
			ps.setInt(1, Integer.valueOf(bookcode));
			result = ps.executeQuery();
			while (result.next()) {
				book.setBookcode(result.getInt("bookcode"));
				book.setTitle(result.getString("title"));
				book.setAuthor(result.getString("author"));
				book.setCategory(result.getString("CategoryName"));
				book.setApproved(result.getInt("approved") != 0 ? true : false);
			}
		} // End of try block
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		model.addAttribute("book", book);
		
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/books", "root", "Iuhuyennhat1-");
//			statement = connection.createStatement();
//			result = statement.executeQuery("SELECT CategoryName FROM category;");
//			while (result.next()) {
//				int id = result.getInt("CategoryId");
//				String name = result.getString("CategoryName");
//				categorys.add(new Category(id, name));
//			}
//		} // End of try block
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		model.addAttribute("categorys", categorys);
		
		return "book-detail";
	}
	
	@PostMapping("/book/save/{bookcode}")
	public String addBook(Book book, @PathVariable String bookcode) {
		Connection connection = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/books", "root", "Iuhuyennhat1-");
			ps = connection.prepareStatement("insert into books(title, author, category, approved) values (?, ?, ?, ?)");
			ps.setString(2, book.getTitle());
			ps.setString(3, book.getAuthor());
			ps.setString(4, book.getCategory());
			ps.setInt(5, book.isApproved() ? 1 : 0);
			result = ps.executeUpdate();
			ps.close();
			connection.close();
			// Redirect the response to success page
			return "redirect:/books2";
		} // End of try block
		catch (Exception e) {
			e.printStackTrace();
		}
		return "error"; // tạo trang Error
	}


}
