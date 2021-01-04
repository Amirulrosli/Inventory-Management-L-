package com.nep.itn08.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

@Entity
@Table (name="totalProduct")
public class Total {
	
	@Id
	private int id;
	
	@NotNull
	private double totalPrice;
	
	@NotNull
	private int totalQuantity;
	
	@NotNull
	private double totalSales;
	
	@NotNull
	private int soldQuantity;
	
	@NotNull
	private double totalProfit;
	
	@NotNull
	private double totalCommission;
	
	
	
	
	public Total() {}
	
	

	
	


	public Total(int id, double totalPrice, int totalQuantity, double totalSales, int soldQuantity, double totalProfit,
			double totalCommission) {
		super();
		this.id = id;
		this.totalPrice = totalPrice;
		this.totalQuantity = totalQuantity;
		this.totalSales = totalSales;
		this.soldQuantity = soldQuantity;
		this.totalProfit = totalProfit;
		this.totalCommission = totalCommission;
	}


	public double getTotalSales() {
		return totalSales;
	}



	public void setTotalSales(double totalSales) {
		this.totalSales = totalSales;
	}




	public int getSoldQuantity() {
		return soldQuantity;
	}


	public void setSoldQuantity(int soldQuantity) {
		this.soldQuantity = soldQuantity;
	}


	public double getTotalProfit() {
		return totalProfit;
	}




	public void setTotalProfit(double totalProfit) {
		this.totalProfit = totalProfit;
	}



	public double getTotalCommission() {
		return totalCommission;
	}



	public void setTotalCommission(double totalCommission) {
		this.totalCommission = totalCommission;
	}


	public double getTotalPrice() {
	
		return totalPrice;
	}
	

	public void setTotalPrice(double totalPrice) {
		
		this.totalPrice = totalPrice;
		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	
}
