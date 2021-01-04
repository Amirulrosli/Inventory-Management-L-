package com.nep.itn08.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table (name = "User")
public class User {
	@Id
	private int id;
	
	
	private String rid;
	
	@NotNull
	private String userName;

	@NotNull
	private String userPassword;
	
	@NotNull
	private String date;
	
	@NotNull
	private double commission;
	
	@NotNull
	private double totalCommission;
	
	
	public User() {}

	public User(int id, String rid, @NotNull String userName, @NotNull String userPassword, @NotNull String date, @NotNull double commission) {
	
		this.id = id;
		this.rid = rid;
		this.userName = userName;
		this.userPassword = userPassword;
		this.date = date;
		this.commission = commission;
	}
	
	

	public double getTotalCommission() {
		return totalCommission;
	}

	public void setTotalCommission(double totalCommission) {
		this.totalCommission = totalCommission;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
