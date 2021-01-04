package com.nep.itn08.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nep.itn08.model.CashierStorage;
import com.nep.itn08.model.Product;
import com.nep.itn08.model.TemporaryStorage;
import com.nep.itn08.model.Total;
import com.nep.itn08.model.User;


import com.nep.itn08.repository.CashierDAO;
import com.nep.itn08.repository.ProductDAO;
import com.nep.itn08.repository.TemporaryStorageDAO;
import com.nep.itn08.repository.TotalDAO;
import com.nep.itn08.repository.UserDAO;

@Controller
public class CashierApiController {
	
	//Declaration of data repository needed------------------------------------
	
	@Autowired
	private UserDAO userDAO;
	User user;

	@Autowired
	private ProductDAO productDAO;
	Product product;

	@Autowired
	private TemporaryStorageDAO temporaryDAO;

	@Autowired
	private CashierDAO cashierDAO;
	

	@Autowired
	private TotalDAO totalDAO;
	
	// initialization required for page function--------------------------------------------------------
	
	Total total;
	
	TemporaryStorage newActivity;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	String date = sdf.format(new Date());
	ArrayList <Integer> indexID = new ArrayList<>();
	ArrayList <Double> priceList = new ArrayList <>();
	ArrayList <String> nameList = new ArrayList <>();
	ArrayList <Integer> quantityList = new ArrayList <>();
	double clientPrice = 0;
	int num = 0;
	double newPriceAvail = 0;
	String selectedUser = null;
	
	// Cashier Page for User-------------------------------------------------------------------------------
	
	@RequestMapping (value="/CashierAPI")
	public String CashierAPI(@RequestParam (required=false)String label1,
			@RequestParam (required=false)String productID,
			@RequestParam (required=false)String payment,
			@RequestParam (required=false)String submit,
			@RequestParam (required=false)String quantity,
			ModelMap modelMap) {
		
		
		//initialization----------------------------------------------
		
		boolean show = false;
		boolean showReceipt = false;
		boolean addSuccessful = false;
		boolean confirm = false;
		int ID;
		String RID = null;
		String brand;
		double price;
		double Totalprice;
		int Quantity;
		double discount;
		String CommissionTo = null;
		String purchase=null;
		String message = null;

		double cashTotal = 0;
		int cashQuantity = 0;
		double addNewPrice = 0;
		int indextoSize = 0;
		String CompareID = null;
		String Warn = null;
		List <User> userAcc = (List<User>) userDAO.findAll();
		List <TemporaryStorage> activity = new ArrayList <>();
		List <TemporaryStorage> storage = new ArrayList <>();
		List <CashierStorage> cashierList = new ArrayList <>();
		double clientChange = 0;
		double parsePayment = 0;


		TemporaryStorage action;
		CashierStorage cashier = null;
		
		//Transaction Process (Failed & Success Transaction)------------------------
		
	 if (label1==null || productID==null || quantity==null) {
		 
			if (payment==null || payment.equalsIgnoreCase("") ) {
				confirm  = false;
				showReceipt = false;
				message = "Failed to Process Transaction";
			} else {
				
				parsePayment = Double.parseDouble(payment);
				
				//Calculation and save data to cashier_storage database------------------
				
				if (parsePayment >= clientPrice) {								//if payment is bigger and equal to the price
				for (int i = 0 ; i<nameList.size();i++) {
					String byId = nameList.get(i);
					int byId1 = Integer.parseInt(byId);
					product = productDAO.findByRid(byId).get();
					action = temporaryDAO.findById(byId1).get();
					int pQuantity = product.getQuantity() - action.getQuantity();
					double pPrice = product.getTotalPrice() - action.getTotalPrice();
					if (pQuantity ==0 || pPrice==0) {
						productDAO.delete(product);
					}
					product.setQuantity(pQuantity);
					product.setTotalPrice(pPrice);
					int id = action.getId();
					String cRid = action.getRid();
					String cbrand = action.getBrand();
					double cprice = action.getPrice();
					int cquantity = action.getQuantity();
					double cDiscount = action.getDiscount();
					double ctotalPrice = action.getTotalPrice();
					String cdate = action.getDate();
					String caction = action.getAction();
					String cuser = action.getUser();
					
					productDAO.save(product);	
					num++;
					
					
					while (true) {
						if (temporaryDAO.existsById(num) && id!=num) {
							num++;
						} else {
							if (id==num) {	
								num = id;
							}
							if (cashierDAO.existsById(num)) {
								num++;
							} else {
								cashier = new CashierStorage (num,cRid,cquantity,cbrand,cprice,cDiscount,ctotalPrice,cdate,caction,cuser);
								cashierDAO.save(cashier);
								break;
							}
						
							}
					}
					
					
					// Retrieving and calculating data from total_product and cashier_storage database-----------------------------
					
					total = totalDAO.findById(1).get();
					double newTotalPrice = total.getTotalPrice() - ctotalPrice;
					int newTotalQuantity = total.getTotalQuantity() - cquantity;
					total.setTotalPrice(newTotalPrice);
					total.setTotalQuantity(newTotalQuantity);
					totalDAO.save(total);
				}
				
				storage = (List<TemporaryStorage>) temporaryDAO.findAll();
				show = true;
				showReceipt = true;
				clientChange = parsePayment - clientPrice;
				message = "Transaction Complete!, Please Print/show Receipt Below";
				}
				else {
					confirm  = false;
					showReceipt = false;
					activity = (List<TemporaryStorage>) temporaryDAO.findAll();
					temporaryDAO.deleteAll(activity);
					nameList.clear();
					priceList.clear();
					indexID.clear();
					indextoSize = 0; 
				}
			}
			
			//deleting data temporary_storage database
			
			activity = (List<TemporaryStorage>) temporaryDAO.findAll();
			temporaryDAO.deleteAll(activity);

			nameList.clear();
			priceList.clear();
			indexID.clear();
			indextoSize = 0; 
			
			show = false;
			Warn = "Please fill out the required Field";
			
		} else if (userDAO.existsByUserName(label1)) {
			CommissionTo = label1;
			
			
			if (productDAO.existsByRid(productID) && quantity != null) {

				show = true;
				product = productDAO.findByRid(productID).get();
				ID = product.getId();
				RID = product.getRid();
				brand = product.getBrand();
				price = product.getPrice();
				Totalprice = product.getPrice();
				Quantity = product.getQuantity();
				discount = product.getDiscount();
				


				int parseQuantity = Integer.parseInt(quantity);

				if (parseQuantity <= Quantity) {						//if entered quantity is less or equal to the quantity in the database (to avoid error)
					
					confirm = true;
					cashTotal = price*parseQuantity;
					cashQuantity = parseQuantity; 
					
					if (temporaryDAO.existsByRid(productID)) {          //if product ID is exist in the temporary storage database
						int index = 0;
						for (int i= 0; i<nameList.size();i++) {
							if (nameList.get(i).equalsIgnoreCase(productID)) {
								index = i;
							}
						}
						priceList.set(index, cashTotal);
					} else {
						nameList.add(indextoSize, productID);
						priceList.add(indextoSize, cashTotal);
						indexID.add(indextoSize,ID);
					}
					
					//save data to temporary and cashier storage-------------------------------------
					
					indextoSize++;
					addSuccessful = true;
					purchase = "Cash Purchase";
					action = new TemporaryStorage (ID,RID,cashQuantity,brand,price,discount,cashTotal,date,purchase,CommissionTo);
					temporaryDAO.save(action);
					activity = (List<TemporaryStorage>) temporaryDAO.findAll();

					
					for (int i = 0; i < priceList.size();i++) {
						
						double setPrice = priceList.get(i);
						addNewPrice += setPrice;
					}
					clientPrice = addNewPrice;

				} else {
					show = false;
					Warn = "Quantity details is incorrect! (Quantity Exceeded)";
				}


			} 
		}
		else {
			Warn = "Product Details is incorrect!";
			addSuccessful = false;
		}
	 
	//data for show receipt----------------------------
		
		Date dateInvoice = new Date();
		Long random = (long) (Math.random()*99999999);
		double newChange = Math.round(clientChange*100.0)/100.0;
		double newPrice = Math.round(clientPrice*100.0)/100.0;
		
		modelMap.put("userComm", CommissionTo);
		modelMap.put("selectedUser", selectedUser);
		modelMap.put("cashPrice", cashTotal);
		modelMap.put("user", userAcc);
		modelMap.put("show", show);
		modelMap.put("activity", activity);
		modelMap.put("warn", Warn);
		modelMap.put("AllPrice", addNewPrice);
		modelMap.put("clientPrice", newPrice);
		modelMap.put("purchase", purchase);
		modelMap.put("clientchange", newChange);
		modelMap.put("showReceipt", showReceipt);
		modelMap.put("cashier", cashierList);
		modelMap.put("dateInvoice", dateInvoice);
		modelMap.put("invoice", random);
		modelMap.put("cashier", cashierList);
		modelMap.put("payment", parsePayment);
		modelMap.put("store", storage);
		modelMap.put("message", message);

		return "CashierAPI";
	}
}
