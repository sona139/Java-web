package com.example.student.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.ui.Model;

public class StudentDAO {
	private static StudentService ss = new StudentService();
	private static Connection connection = ss.getConnection();
	private static PreparedStatement ps = null;
	
	private static final String SELECT_ALL = "SELECT id, name, DATE_FORMAT(dob, '%d/%m/%Y'), major, vaccinated FROM student";
	private static final String SELECT_BY_ID = "SELECT id, name, DATE_FORMAT(dob, '%d/%m/%Y'), major, vaccinated FROM student WHERE id = ?";
	private static final String SELECT_BY_ID_LARGEST = "SELECT id FROM student ORDER BY id DESC LIMIT 1";
	private static final String INSERT = "INSERT INTO student(name, dob, major, vaccinated) VALUES (?, STR_TO_DATE(?, '%d/%m/%Y'), ?, ?)";
	private static final String UPDATE = "UPDATE student SET name = ?, dob = STR_TO_DATE(?, '%d/%m/%Y'), major = ?, vaccinated = ? WHERE id = ?";
	private static final String IS_EXISTED = "SELECT * FROM student WHERE id != ? AND name = ? AND STR_TO_DATE(?, '%d/%m/%Y') = dob";
	private static final String DELETE = "DELETE FROM student WHERE id = ?";
	
	public StudentDAO() {}
	
	public static ArrayList<Student> selectAll() {
		ArrayList<Student> students = new ArrayList<>();
		
		try {
			ps = connection.prepareStatement(SELECT_ALL);
			ResultSet res = ps.executeQuery();
			
			while(res.next()) {
				int id = res.getInt(1);
				String name = res.getString(2);
				String dob = res.getString(3);
				String major = res.getString(4);
				boolean vaccinated = res.getInt(5) == 0 ? false : true;
				
				students.add(new Student(id, name, dob, major, vaccinated));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return students;
	}
	
	public static Student selectById(int _id) {
		Student student = new Student();
		int id = 0;
		String name = "";
		String dob = "";
		String major = "";
		boolean vaccinated = false;
		
		try {
			connection = ss.getConnection();
			if(_id == -1) {
				ps = connection.prepareStatement(SELECT_BY_ID_LARGEST);
				ResultSet res = ps.executeQuery();
				if(res.next()) {
					id = res.getInt(1) + 1;
				}
				if(id == 0) id = 1;
			}
			else {
				ps = connection.prepareStatement(SELECT_BY_ID);
				ps.setInt(1, _id);
				
				ResultSet res = ps.executeQuery();
				if(res.next()) {
					id = res.getInt(1);
					name = res.getString(2);
					dob = res.getString(3);
					major = res.getString(4);
					vaccinated = res.getInt(5) == 0 ? false : true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		student = new Student(id, name, dob, major, vaccinated);
		return student;
	}

	private static Model checkValidation(Model model, Student student) {
		int id = student.getId();
		String name = student.getName();
		String dob = student.getDob();
		String major = student.getMajor();
		
		String _name = getText(name);
		String _dob = getDateString(dob);
		String _major = getTextUpper(major);
		
		String msgName = checkValidString(_name, "Name");
		String msgDob = checkValidDate(dob, "Date");
		String msgMajor = checkValidString(_major, "Major");
		
		if(!msgName.equals("") || !msgDob.equals("") || !msgMajor.equals("")) {
			model.addAttribute("msgName", msgName);
			model.addAttribute("msgDob", msgDob);
			model.addAttribute("msgMajor", msgMajor);
			model.addAttribute("student", student);
			model.addAttribute("status", 415);
			return model;
		}
		
		int status = checkDataExisted(id, _name, _dob);
		model.addAttribute("status", status);
		
		if(status != 200) {
			String msg = status == 403 ? "Dữ liệu bị trùng" : "Lỗi";
			model.addAttribute("student", student);
			model.addAttribute("msg", msg);
		}
		
		return model;
	}
	
	public static Model insertStudent(Model model, Student student) {
		model = checkValidation(model, student);
		int status = (int) model.getAttribute("status");
		if(status != 200) return model;
		
		String name = getText(student.getName());
		String dob = getDateString(student.getDob());
		String major = getTextUpper(student.getMajor());
		boolean vaccinated = student.isVaccinated();
		
		insert(name, dob, major, vaccinated);
		
		return model;
	}
	
	private static void insert(String name, String dob, String major, boolean vaccinated) {
		try {
			ps = connection.prepareStatement(INSERT);
			ps.setString(1, name);
			ps.setString(2, dob);
			ps.setString(3, major);
			ps.setInt(4, vaccinated ? 1 : 0);
			
			int res = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Model updateStudent(Model model, Student student) {
		model = checkValidation(model, student);
		int status = (int) model.getAttribute("status");
		if(status != 200) return model;
		
		int id = student.getId();
		String name = getText(student.getName());
		String dob = getDateString(student.getDob());
		String major = getTextUpper(student.getMajor());
		boolean vaccinated = student.isVaccinated();
		
		update(id, name, dob, major, vaccinated);
		
		return model;
	}
	
	private static void update(int id, String name, String dob, String major, boolean vaccinated) {
		try {
			ps = connection.prepareStatement(UPDATE);
			ps.setString(1, name);
			ps.setString(2, dob);
			ps.setString(3, major);
			ps.setInt(4, vaccinated ? 1 : 0);
			ps.setInt(5, id);
			
			int res = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static int checkDataExisted(int _id, String _name, String _dob) {
		try {
			Connection connection = ss.getConnection();
			PreparedStatement ps = connection.prepareStatement(IS_EXISTED);
			ps.setInt(1, _id);
			ps.setString(2, _name);
			ps.setString(3, _dob);
			
			ResultSet res = ps.executeQuery();
			if(res.next()) {
				return 403;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 500;
		}
		return 200;
	}
	
	private static String checkValidString(String str, String title) {
		str = str.trim().toLowerCase();
		if(str.equals("")) return title + " không được để trống";
		
		//String khong chua so va ki tu dac biet;
		String regex = "^[\\p{L}\\s]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if (!matcher.matches()) {
			return title + " không bao gồm số và kí tự đặc biệt";
		}
		return "";
	}
	
	private static String checkValidDate(String str, String title) {
		str = str.trim();
		if(str.equals("")) return title + " không được để trống";
		
		String date = getDateString(str);
		if(date.equals("")) return title + " phải có định dạng dd/mm/yyyy";
		
		return "";
	}
	
	private static String getText(String str) {
		if(str.trim().equals("")) return "";
		String[] strings = str.trim().toLowerCase().split("\\s+");
		for (int i = 0; i < strings.length; i++)
			strings[i] = strings[i].substring(0, 1).toUpperCase() + strings[i].substring(1);
		return String.join(" ", strings);
	}
	
	private static String getTextUpper(String str) {
		String[] strings = str.trim().toUpperCase().split("\\s+");
		return String.join(" ", strings);
	}
	
	private static String getDateString(String str) {
		str = str.trim();
		DateTimeFormatter inputFormater = new DateTimeFormatterBuilder()
				.appendPattern("[d/M/uuuu][dd/M/uuuu][d/M/uuuu][dd/MM/uuuu]")
				.toFormatter();
		
		DateTimeFormatter outputFormater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		LocalDate localDate = null;
		String date = "";
		
		try {
			localDate = LocalDate.parse(str, inputFormater);
			date = localDate.format(outputFormater);
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
		return date;
	}

	public static void deleteStudent(int id) {
		try {
			ps = connection.prepareStatement(DELETE);
			ps.setInt(1, id);
			int res = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
