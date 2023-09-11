package com.example.phone.controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class PhoneService {
	private String jdbcURL = "jdbc:mysql://localhost:3306/phone";
	private String jdbcUsername = "root";
	private String jdbcPassword = "Iuhuyennhat1-";
	
	public PhoneService() {}
	
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
