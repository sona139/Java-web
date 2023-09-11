package com.example.student.controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class StudentService {
	private String jdbcURL = "jdbc:mysql://localhost:3306/student";
	private String jdbcUsername = "root";
	private String jdbcPassword = "Iuhuyennhat1-";
	
	public StudentService() {}
	
	public Connection getConnection() {
		Connection connection = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return connection;
	}
}
