package com.nep.itn08.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

@Entity
@Table (name="CashierStorage")
public class CashierStorage {

	@Id
	private int id;
	
	
	private String rid;
	
	@NotNull
	private int quantity;
	
	@NotNull
	private String brand;
	
	@NotNull
	private double price;
	
	@NotNull
	private double discount;
	
	@NotNull
	private double totalPrice;
	
	@NotNull
	private String date;
	
	@NotNull
	private String action;
	
	@NotNull
	private String user;
	
	public CashierStorage() {}

	public CashierStorage(int id, String rid, int quantity, String brand, double price, double discount, double totalPrice,
			String date, String action, String user) {
		
		this.id = id;
		this.rid = rid;
		this.quantity = quantity;
		this.brand = brand;
		this.price = price;
		this.discount = discount;
		this.totalPrice = totalPrice;
		this.date = date;
		this.action = action;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	
}
