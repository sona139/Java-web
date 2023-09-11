package com.example.phone.controller;

import java.math.BigInteger;
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

public class PhoneDAO {
	private static PhoneService ss = new PhoneService();
	private static Connection connection = ss.getConnection();
	private static PreparedStatement ps = null;
	
	private static final String SELECT_ALL = "SELECT * FROM phone";
	private static final String SELECT_BY_ID = "SELECT * FROM phone WHERE id = ?";
	private static final String SELECT_BY_ID_LARGEST = "SELECT id FROM phone ORDER BY id DESC LIMIT 1";
	private static final String INSERT = "INSERT INTO phone(name, price, brand, sold) VALUES (?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE phone SET name = ?, price = ?, brand = ?, sold = ? WHERE id = ?";
	private static final String IS_EXISTED = "SELECT * FROM phone WHERE id != ? AND name = ? AND brand = ?";
	private static final String DELETE = "DELETE FROM phone WHERE id = ?";
	
	public PhoneDAO() {}
	
	public static ArrayList<Phone> selectAll() {
		ArrayList<Phone> phones = new ArrayList<>();
		
		try {
			ps = connection.prepareStatement(SELECT_ALL);
			ResultSet res = ps.executeQuery();
		
			while(res.next()) {
				int id = res.getInt(1);
				String name = res.getString(2);
				String price = res.getString(3);
				String brand = res.getString(4);
				boolean sold = res.getInt(5) == 0 ? false : true;
				
				phones.add(new Phone(id, name, price, brand, sold));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return phones;
	}
	
	public static Phone selectById(int _id) {
		Phone phone = new Phone();
		int id = 0;
		String name = "";
		String price = "";
		String brand = "";
		boolean sold = false;
		
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
					price = res.getString(3);
					brand = res.getString(4);
					sold = res.getInt(5) == 0 ? false : true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		phone = new Phone(id, name, price, brand, sold);
		return phone;
	}

	private static Model checkValidation(Model model, Phone phone) {
		int id = phone.getId();
		String name = phone.getName();
		String price = phone.getPrice();
		String brand = phone.getBrand();
		
		System.out.println("name: " + name + " price : " + price + " brand: " + brand);
		
		String _name = getText(name);
		String _price = getText(price);
		String _brand = getText(brand);
		
		String msgName = checkValidString(_name, "Name");
		String msgPrice = checkValidNumber(price, "Price");
		String msgBrand = checkValidString(_brand, "Brand");
		
		if(!msgName.equals("") || !msgPrice.equals("") || !msgBrand.equals("")) {
			model.addAttribute("msgName", msgName);
			model.addAttribute("msgPrice", msgPrice);
			model.addAttribute("msgBrand", msgBrand);
			model.addAttribute("phone", phone);
			model.addAttribute("status", 415);
			return model;
		}
		
		int status = checkDataExisted(id, _name, _brand);
		model.addAttribute("status", status);
		
		if(status != 200) {
			String msg = status == 403 ? "Dữ liệu bị trùng" : "Lỗi";
			model.addAttribute("phone", phone);
			model.addAttribute("msg", msg);
		}
		
		return model;
	}
	
	public static Model insertPhone(Model model, Phone phone) {
		model = checkValidation(model, phone);
		int status = (int) model.getAttribute("status");
		if(status != 200) return model;
		
		String name = getText(phone.getName());
		String price = getText(phone.getPrice());
		String brand = getText(phone.getBrand());
		boolean sold = phone.isSold();
		
		insert(name, price, brand, sold);
		
		return model;
	}
	
	private static void insert(String name, String price, String brand, boolean sold) {
		try {
			ps = connection.prepareStatement(INSERT);
			ps.setString(1, name);
			ps.setString(2, price);
			ps.setString(3, brand);
			ps.setInt(4, sold ? 1 : 0);
			
			int res = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Model updatePhone(Model model, Phone phone) {
		model = checkValidation(model, phone);
		int status = (int) model.getAttribute("status");
		if(status != 200) return model;
		
		int id = phone.getId();
		String name = getText(phone.getName());
		String price = getText(phone.getPrice());
		String brand = getText(phone.getBrand());
		boolean sold = phone.isSold();
		
		update(id, name, price, brand, sold);
		
		return model;
	}
	
	private static void update(int id, String name, String price, String brand, boolean sold) {
		try {
			ps = connection.prepareStatement(UPDATE);
			ps.setString(1, name);
			ps.setString(2, price);
			ps.setString(3, brand);
			ps.setInt(4, sold ? 1 : 0);
			ps.setInt(5, id);
			
			int res = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static int checkDataExisted(int _id, String _name, String _brand) {
		try {
			Connection connection = ss.getConnection();
			PreparedStatement ps = connection.prepareStatement(IS_EXISTED);
			ps.setInt(1, _id);
			ps.setString(2, _name);
			ps.setString(3, _brand);
			
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
		
		//String khong chua ki tu dac biet;
		String regex = "^[a-zA-Z0-9\\s]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if (!matcher.matches()) {
			return title + " không bao gồm kí tự đặc biệt";
		}
		return "";
	}
	
	private static String checkValidNumber(String str, String title) {
		if(str.trim().equals("")) return title + " không được để trống";
		try {
			BigInteger res = new BigInteger(str);
			if(res.compareTo(new BigInteger("0")) <= 0) return title + " phải lớn hơn 0";
			if(res.compareTo(new BigInteger("2147483647")) > 0) return title + " không được lớn hơn 2.147.483.647";
		} catch (Exception e) {
			return title + " chỉ bao gồm chữ số";
		}
		return "";
	}
	
	private static String getText(String str) {
		if(str.trim().equals("")) return "";
		String[] strings = str.trim().toLowerCase().split("\\s+");
		for (int i = 0; i < strings.length; i++)
			strings[i] = strings[i].substring(0, 1).toUpperCase() + strings[i].substring(1);
		return String.join(" ", strings);
	}

	public static void deletePhone(int id) {
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
