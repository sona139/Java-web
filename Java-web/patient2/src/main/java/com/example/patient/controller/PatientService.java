package com.example.patient.controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class PatientService {
	private String jdbcURL = "jdbc:mysql://localhost:3306/patient";
	private String jdbcUsername = "root";
	private String jdbcPassword = "Iuhuyennhat1-";
	
	public PatientService() {}
	
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
