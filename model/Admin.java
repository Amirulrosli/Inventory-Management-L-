package com.nep.itn08.model;



import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table (name="AdminLogin")
public class Admin {
	
	@Id
	private int id;
	
	
	private String rid;
	
	@NotNull
	private String userName;

	@NotNull
	private String userPassword;
	
	private String date;

	

	public Admin() {}



	public Admin(int id, String rid, @NotNull String userName, @NotNull String userPassword, String date) {
		super();
		this.id = id;
		this.rid = rid;
		this.userName = userName;
		this.userPassword = userPassword;
		this.date = date;
	}
	
	

	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
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


}
