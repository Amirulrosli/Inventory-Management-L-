package com.nep.itn08.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nep.itn08.model.CashierStorage;
import com.nep.itn08.model.Product;
import com.nep.itn08.model.Total;
import com.nep.itn08.model.User;
import com.nep.itn08.repository.AdminDAO;
import com.nep.itn08.repository.CashierDAO;
import com.nep.itn08.repository.ProductDAO;
import com.nep.itn08.repository.TotalDAO;
import com.nep.itn08.repository.UserDAO;

@Controller
public class AdminController {
	
	// Declaration of Data Repository--------------------------------------------------------------------------------------

	@Autowired
	private AdminDAO adminDAO;
	
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private TotalDAO totalDAO;
	
	@Autowired
	private CashierDAO cashierDAO;
	
	// Initialization of Required data needed for every Request Mapping--------------------------------------------------------

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	String date = sdf.format(new Date()); 
	ArrayList <String> sourceID = new ArrayList<>(); 
	Product product;
	String newID;
	Total total;

	//adminID Authorization Page-------------------------------------------------------------------------------------------------
	
	@RequestMapping (value ="/adminID", method = RequestMethod.GET)
	public String adminID(@RequestParam(required=false) String id, ModelMap modelMap) {

		String warn = null;

		if (!adminDAO.existsByRid(id)||id==null || id.isEmpty()) {                       //[Not Authorized] if entered data is not match with any of the existed adminID
			warn = "Please Enter Your Admin ID";

		}
		else {	
			return "redirect:adminSearch";												// if match: redirect page to adminsearch.html
		}

		modelMap.put("Warn", warn); 													//link warn to web
		return "adminID";
	}
	
	//addProduct Page (for add, create and Update product page)--------------------------------------------------------------------------

	@RequestMapping (value="/addProduct")
	public String addProduct(@RequestParam (required= false) String id, 
			@RequestParam (required= false) String quantity, 
			@RequestParam (required= false) String brand,
			@RequestParam (required= false) String price,
			@RequestParam (required= false) String discount,
			@RequestParam (required= false) String date,
			@RequestParam (required=false) String search,
			@RequestParam (required=false) String UPquantity, 
			@RequestParam (required=false) String UPbrand,
			@RequestParam (required=false) String UPprice,
			@RequestParam (required=false) String UPdiscount,
			@RequestParam (required=false) String UPdate,
			ModelMap modelMap) {



		boolean addSuccessful ;
		boolean addFailed ;
		boolean addExist;
		String message = null;
		Product product = null;
		double totalPrice = 0;
		int parseQuantity = 0;
		
		//----------Add Product CODE-------------------------------------------------------------------------------------
		
		if (productDAO.existsByRid(id)) {  //if product already exist
			addExist = true;
			addFailed = false;
			addSuccessful = false;
			message = "Product is already Existed, Please Select Update Products To Continue.";

		} else if (id==null && brand==null && price==null && discount==null ) { // if all products field is empty
			addSuccessful = false;
			addFailed = false;
			addExist = true;
			message = "Please Select Card Above to Add/Update Products";


		}
		else if (id==null || brand == null || price ==null || discount == null) {  //if any product field is empty
			addSuccessful = false;
			addFailed = true;
			addExist= false;

		}
		else { 																	  //if product is not exist and field is not empty!
			try {

				if (date.isEmpty()) {											  //if date field is empty, retrieve default (current) date
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					date = sdf.format(new Date());
				}
				//Save product to database using data repository-------------------------------------------
				
				parseQuantity = Integer.parseInt(quantity);
				double parsePrice = Double.parseDouble(price);
				double parseDiscount = Double.parseDouble(discount);

				double newDiscount = (parseDiscount/100)*parsePrice;
				double newPrice = parsePrice - newDiscount;
				totalPrice = (newPrice*parseQuantity); 
				double roundTotalPrice = Math.round(totalPrice*100.0)/100.0;
				int newID = Integer.parseInt(id);
				product = new Product (newID,id,parseQuantity,brand,newPrice,parseDiscount,roundTotalPrice,date); //set Product To POJO
				productDAO.save(product); // save to database using POJO variables
				sourceID.add(id); //add id to arrayList

				addSuccessful = true;
				addFailed = false;
				addExist = false;


			} catch (NumberFormatException e) {
				addSuccessful = false;
				addFailed = true;
				addExist = false;

			}
		}


		//-----------------------------UPDATE PRODUCT CODE--------------------------------------------------------------------

		Product storedProduct = null;
		String showRID = null;
		int IDid= 0;
		int showQuantity = 0;
		String showBrand = null;
		double showPrice = 0;
		double showDiscount = 0;
		String showDate = null;
		String emptyWarn = null;
		boolean UPSuccessful = false;
		boolean UPFailed = false;
		boolean UPExist = false;
		boolean show = false;



		if (search==null || search.equalsIgnoreCase("")||search.isEmpty() && UPquantity.isEmpty()) {               //if updateSearch field is empty
			emptyWarn = "Please Enter Product ID for Update";
			UPFailed = false;
			UPSuccessful = false;

		} else if (productDAO.existsByRid(search)) {															  //if product is exist (comparing data in database with the updateSearch field)
			storedProduct = productDAO.findByRid(search).get();
			showRID = storedProduct.getRid();
			showQuantity = storedProduct.getQuantity();
			showBrand = storedProduct.getBrand();
			showPrice = storedProduct.getPrice();
			showDiscount = storedProduct.getDiscount();
			showDate = storedProduct.getDate();
			IDid = storedProduct.getId();
			show = true;

			if (UPquantity==null && UPbrand==null && UPprice==null && UPdiscount==null ) {						 //if product is exist but update fields is empty
				UPSuccessful = false;
				UPFailed = true;

			} else if (UPquantity==null || UPbrand == null || UPprice ==null || UPdiscount == null) {           //if product is exist and any update fields is empty
				UPSuccessful = false;
				UPFailed = true;

			}else {																								//else if product is exist and any update fields is not empty (update value)
				try {


					parseQuantity = Integer.parseInt(UPquantity);
					double parsePrice = Double.parseDouble(UPprice);
					double parseDiscount = Double.parseDouble(UPdiscount);
					double newDiscount = (parseDiscount/100)*parsePrice;
					double newPrice = parsePrice - newDiscount;
					totalPrice = (newPrice*parseQuantity); 
					product = new Product (IDid,showRID,parseQuantity,UPbrand,newPrice,parseDiscount,totalPrice,UPdate); //save product to POJO
					productDAO.save(product); 																			// save POJO variables to database
					UPSuccessful = true;
					UPFailed = false;
					UPExist = false;
					show = false;



				} catch (NumberFormatException e) { 																	//handling NumberFormat exception
					UPSuccessful = false;
					UPFailed = true;

				}
			} 
		}
		else {
			UPExist=true;
			emptyWarn = "Product is not exists";
		}
		
		// save total quantity and total price to Total Database----------------------------------------------------------------------
		
		if (sourceID.size()!=0 || sourceID.size()>0) {									//if source ID size (from section add product) is not equal to 0 and source ID size is bigger than 0 
		Product reProduct;
		double newRePrice = 0;
		int newQuantity = 0;
		int k = 0;
	
		for (int i = 0 ; i<sourceID.size();i++) {    									//loops to calculate the total Price and total quantity
			String index = sourceID.get(i);
			if (productDAO.existsByRid(index)) {
				reProduct = productDAO.findByRid(index).get();
				int reQuantity = reProduct.getQuantity();
				double rePrice = reProduct.getTotalPrice();
				double round = Math.round(rePrice*100.0)/100.0;
				newRePrice += round;
				newQuantity += reQuantity;
				k = sourceID.size();
			}
			
		}

		
			total = totalDAO.findById(1).get();											// Retrieving default data in total database
			total.setTotalPrice(newRePrice);											// set new total Price
			total.setTotalQuantity(newQuantity);										// set new total quantity
			totalDAO.save(total);														// save total POJO to database
		
		
		}

		modelMap.put("id", showRID);
		modelMap.put("quantity", showQuantity);
		modelMap.put("brand", showBrand);
		modelMap.put("price", showPrice);
		modelMap.put("discount", showDiscount);
		modelMap.put("date", showDate);
		modelMap.put("UPsuccessful",UPSuccessful);
		modelMap.put("UPfailed", UPFailed);
		modelMap.put("show", show);
		modelMap.put("UPexist", UPExist);
		modelMap.put("warn", emptyWarn);
		modelMap.put("UPsuccessful",UPSuccessful);
		modelMap.put("UPfailed", UPFailed);
		modelMap.put("UPexist", UPExist);
		modelMap.put("warn", emptyWarn);
		modelMap.put("idid", IDid);



		modelMap.put("successful",addSuccessful);
		modelMap.put("failed", addFailed);
		modelMap.put("exist", addExist);
		modelMap.put("Message", message);

		return "AddProduct";
	}

	//Product List and Calculation Page------------------------------------------------------------------------------------------------------------
	
	@RequestMapping (value="/List")
	public String productList (@RequestParam (required=false)String label,ModelMap modelMap) {
		int i;
		String warn;
		boolean Show;
		String option1 = "ID";
		String option2 = "Brand";
		String option3 = "Price";
		List <Product> listProduct = (List<Product>) productDAO.findAll();					//retrieving Product Repository

	
		//-----------------Show Lists------------------------------
		if (listProduct.isEmpty() || listProduct == null) {									//if product list is empty or null
			i=0;
			warn = i+" Result Found"; 														//0 result found
			Show = false;		

		}
		else {																				//if product is available
			i = listProduct.size();															//Retrieve Array listProduct Size
			warn = i+" Result Found"; 														//size Result found		
			Show = true;
		}

		// initialization for retrieving total Repository--------------
		int k = 1;
		double Price = 0;
		int quantity = 0;
		int soldQuantity = 0;
		double userCommission = 0;
		double sales = 0;
		double profit = 0;
		Total total1 = new Total();
		
		// Retrieving Total Repository---------------------------------
		
		if (totalDAO.existsById(k)) {
			total1 = totalDAO.findById(k).get();
			Price = total1.getTotalPrice();
			quantity = total1.getTotalQuantity();
			soldQuantity = total1.getSoldQuantity();
			userCommission = total1.getTotalCommission();
			sales = total1.getTotalSales();
			profit = total1.getTotalProfit();
		}
		
		// Sort Label in term of ID, Brand, Price----------------------
		
		if (label==null || label.isEmpty() || label.equalsIgnoreCase("")) {
			listProduct = (List<Product>) productDAO.findAll();
		}
		else if (label.equalsIgnoreCase(option1) ) {
			listProduct = productDAO.findAll(Sort.by(Sort.Direction.ASC,"id")); 
		}
		else if (label.equalsIgnoreCase(option2)) {
			listProduct = productDAO.findAll(Sort.by(Sort.Direction.ASC,"brand"));
		} else if (label.equalsIgnoreCase(option3)) {
			listProduct = productDAO.findAll(Sort.by(Sort.Direction.ASC,"price"));
		}



		modelMap.put("total", total1);
		modelMap.put("product", listProduct);
		modelMap.put("warn", warn);
		modelMap.put("show", Show);
		modelMap.put("price", Price);
		modelMap.put("quantity", quantity);
		modelMap.put("id", option1);
		modelMap.put("brand", option2);
		modelMap.put("priceOption", option3);
		modelMap.put("commission", userCommission);
		modelMap.put("sales", sales);
		modelMap.put("soldQuantity", soldQuantity);
		modelMap.put("profit", profit);





		return "List";
	}
	
	//Verify page (User Search page)----------------------------------------------------------------------------

	@RequestMapping(value="/verify" ,method=RequestMethod.GET)
	public String verify (@RequestParam(required=false) String id,ModelMap modelMap) {

		Product product;
		String warningSearch = null;
		String result = null;


		if (!productDAO.existsByRid(id) || id == null || id.isEmpty()) {						//Product Repository is not exist by ID (search) and search field is null or empty
			product = null;																		// table will be empty
			//					(List<Product>) productDAO.findAll();
			warningSearch = "Enter the Product ID";
			result = "No Result Found";
		} else{																					// if Product in product repository is exist
			product = productDAO.findByRid(id).get();											// retrieve all available product
		}
		
		
		//Retrieving data from database for automated calculation summarization----------------------------------------------------
		
		Total newTotal;
		int soldQuantity = 0;
		double sales = 0;
		int allQuantity = 0;
		double allSales = 0;
		double ReCommission = 0;
		double allCommission = 0;
		double upTotal = 0;
		String name = null;
		long Numbercashier = cashierDAO.count(); //count the data in the database
		User user;
		
		if (Numbercashier != 0) {               // if count is not equal to zero
		for (int i = 1 ; i<=Numbercashier;i++) {                                             //retrieving data (cashierDAO repository): cashier_storage using loops
			if (cashierDAO.existsById(i)) {													 //if cashier_storage database is exist by the i
			CashierStorage cashier = cashierDAO.findById(i).get();                           // retrieve cashier data by i
			sales = cashier.getTotalPrice();												 // retrieve sales from cashier_storage
			soldQuantity = cashier.getQuantity();											 // retrieve sold quantity from cashier storage
			name = cashier.getUser();														 // retrieve user (name) from cashier storage
			if (userDAO.existsByUserName(name)) {										     // if name from above is exist in database
				user = userDAO.findByUserName(name).get();									 // retrieve data from user database
				double comm = user.getCommission(); 										 //retrieve commission (%)
				ReCommission = (comm/100) * sales; 										   	// calculate total commission from sales
			}
			upTotal = sales - ReCommission; 												//retrieve profit from sales - commission
			allCommission += ReCommission;  												// all commission for certain user
			allQuantity += soldQuantity;    												// all quantity from sold quantity
			allSales += sales;																// all sales from sales
			}
		}
		double newAllSales = Math.round(allSales*100.0)/100.0; 								// round 2 decimal places for all sales
		double newAllCommission = Math.round(allCommission*100.0)/100.0; 					//round 2 decimal places for commission
		newTotal = totalDAO.findById(1).get(); 												//retrieve total repository by id=1
		newTotal.setSoldQuantity(allQuantity); 												//set allQuantity for POJO
		newTotal.setTotalSales(newAllSales);   												//set total sales for POJO
		newTotal.setTotalCommission(newAllCommission); 										//set total commission to POJO
		totalDAO.save(newTotal); 															//save POJO to database
		}
		
		int totalQuantity = 0;
		double totalPrice = 0;
		double totalSales = 0;
	    int sQuantity = 0;
	    double commission = 0;
	    double totalProfit = 0;
	    double roundtotalProfit = 0;
		double newTotalPrice = 0;
	    
		// Retrieving data from Total repository that had been saved before (line 413 to 437)---------------------------------------------------------------
		
		if (totalDAO.existsById(1)) {
		newTotal = totalDAO.findById(1).get();
		totalPrice = newTotal.getTotalPrice();
		newTotalPrice = Math.round(totalPrice * 100.0)/100.0;
		totalQuantity = newTotal.getTotalQuantity();
		sQuantity = newTotal.getSoldQuantity();
		totalSales = newTotal.getTotalSales();
		commission = newTotal.getTotalCommission();
		totalProfit = totalSales - commission;
		roundtotalProfit = Math.round(totalProfit * 100.0)/100.0;
		newTotal.setTotalProfit(roundtotalProfit);
		totalDAO.save(newTotal);
		}
		

		modelMap.put("products", product);
		modelMap.put("warn", warningSearch);
		modelMap.put("price", newTotalPrice);
		modelMap.put("quantity", totalQuantity);
		modelMap.put("sales", totalSales);
		modelMap.put("currentsales", sales);
		modelMap.put("currentsold",soldQuantity);
		modelMap.put("squantity", sQuantity);
		modelMap.put("totalProfit", roundtotalProfit);
		modelMap.put("commission", commission);
		modelMap.put("currentcommission", ReCommission);
		modelMap.put("UpTotal", upTotal);
		modelMap.put("Result", result);
		return "verify";	

	}
	
	
	//administrator Search page------------------------------------------------------------------------------------------------
	
	
	@RequestMapping (value= {"/adminSearch",""})
	public String adminSearch(@RequestParam(required=false) String id,ModelMap modelMap) {
		Product product ;
		String warningSearch = null ;
		String result = null;

		if (!productDAO.existsByRid(id) || id == null || id.isEmpty()) {                  //if search id is not exist in product repository or search field is empty or null
			product = null;																  // product is equal to null
			//					(List<Product>) productDAO.findAll();
			warningSearch = "Please Enter the Product ID";
			result = "No Result Found";
		} else {																		  // if search id is exist

			product = productDAO.findByRid(id).get();                                     // retrieve product from product Database
		}
		
		//Retrieving data from database for automated calculation summarization-----------------------------------------------------------------
		
		Total newTotal;
		int soldQuantity = 0;
		double sales = 0;
		int allQuantity = 0;
		double allSales = 0;
		double ReCommission = 0;
		double allCommission = 0;
		double upTotal = 0;
		String name = null;
		long Numbercashier = cashierDAO.count();
		User user;
		if (Numbercashier != 0) { //if count is not equal to zero
		for (int i = 1 ; i<=Numbercashier;i++) {                 					//loops for retrieving Cashier_storage data                          
			if (cashierDAO.existsById(i)) {                      					//cashierDAO is available by id
			CashierStorage cashier = cashierDAO.findById(i).get(); 					//retrieve cashier from cashier_storage database
			sales = cashier.getTotalPrice(); 										//retrieve total price
			soldQuantity = cashier.getQuantity(); 									//retrieve quantity
			name = cashier.getUser();												//retrieve user (name) from user database
			if (userDAO.existsByUserName(name)) {									// if name is exist in user database
				user = userDAO.findByUserName(name).get();							//retrieve user by name
				double comm = user.getCommission();									//retrieve user commission
				ReCommission = (comm/100) * sales;             						// calculate commissions from sales for certain user
			}
			upTotal = sales - ReCommission;                     					//Calculate profit from sales - commission
			allCommission += ReCommission;											//calculate total commission
			allQuantity += soldQuantity;											//calculate total quantity
			allSales += sales;														//calculate total sales
			
			}
			
		}
		double newAllSales = Math.round(allSales*100.0)/100.0;  					//round to 2 decimals for total sales
		double newAllCommission = Math.round(allCommission*100.0)/100.0; 			//round to 2 decimals for total commission
		newTotal = totalDAO.findById(1).get(); 										//retrieve data in total_product database
		newTotal.setSoldQuantity(allQuantity); 										//set sold quantity to POJO
		newTotal.setTotalSales(newAllSales);   										//set total sales to POJO
		newTotal.setTotalCommission(newAllCommission); 								//set all commission to POJO
		totalDAO.save(newTotal); 													//save POJO to total_product database
		}
		
		
		// Retrieving data from Total repository that had been saved before (line 503 to 543)-------------------------------------------------
		
		int totalQuantity = 0;
		double totalPrice = 0;
		double totalSales = 0;
	    int sQuantity = 0;
	    double commission = 0;
	    double totalProfit = 0;
	    double roundtotalProfit = 0;
		double newTotalPrice = 0;
		if (totalDAO.existsById(1)) {
		newTotal = totalDAO.findById(1).get();
		totalPrice = newTotal.getTotalPrice();
		newTotalPrice = Math.round(totalPrice * 100.0)/100.0;
		totalQuantity = newTotal.getTotalQuantity();
		sQuantity = newTotal.getSoldQuantity();
		totalSales = newTotal.getTotalSales();
		commission = newTotal.getTotalCommission();
		totalProfit = totalSales - commission;
		roundtotalProfit = Math.round(totalProfit * 100.0)/100.0;
		newTotal.setTotalProfit(roundtotalProfit);
		totalDAO.save(newTotal);
		}
		

		modelMap.put("products", product);
		modelMap.put("warn", warningSearch);
		modelMap.put("price", newTotalPrice);
		modelMap.put("quantity", totalQuantity);
		modelMap.put("sales", totalSales);
		modelMap.put("currentsales", sales);
		modelMap.put("currentsold",soldQuantity);
		modelMap.put("squantity", sQuantity);
		modelMap.put("totalProfit", roundtotalProfit);
		modelMap.put("commission", commission);
		modelMap.put("currentcommission", ReCommission);
		modelMap.put("UpTotal", upTotal);
		modelMap.put("Result", result);
		return "adminSearch";
	}
	
	//delProduct page---------------------------------------------------------------------------------------------------------------------------------------------------

	@RequestMapping (value="/delProduct")
	public String DelProduct(@RequestParam (required=false) String search, @RequestParam (required=false) String IDID, ModelMap modelMap) {
		String warn = null;
		String Exist = null;
		boolean show = false;
		boolean delSuccessful = false;
		boolean delFailed = false;
		boolean enter = false;
		
		//Search and notification---------------------------------------------------------
	
		if (search==null || search.isEmpty() || search.equalsIgnoreCase("")) {             
			warn = "Please Enter Product ID";
			show = false;
			delFailed=false;

			
		} else if (productDAO.existsByRid(search)) {
			
				product = productDAO.findByRid(search).get();
				show = true;
				newID = search;		
				delFailed=false;
				
				if (IDID==null || IDID.isEmpty()|| IDID.equalsIgnoreCase("")) {
					warn = "Please Enter Product ID";
					delFailed=false;
				}
				
		} else {
			Exist = "Product Does Not Exist";
			show = false;
			delFailed=false;
			
		}
		
		//-------------------AdminID Verification-------------------------------------
		
		if (newID == null) {
			warn = "Please Enter Product ID";
			delFailed=false;
		}
		else if (adminDAO.existsByRid(IDID)) { 												//id is existed in the database
			product = productDAO.findByRid(newID).get();
			
			total = totalDAO.findById(1).get();
			int reQuantity = 0;
			double rePrice = 0;
			double round = 0;
			if (total.getTotalPrice() >= product.getTotalPrice()) {
			reQuantity = total.getTotalQuantity() - product.getQuantity();
			rePrice = total.getTotalPrice() - product.getTotalPrice();
			round = Math.round(rePrice * 100.0)/100.0;
			} 
			total.setTotalPrice(round);
			total.setTotalQuantity(reQuantity);
			delSuccessful = true;
			productDAO.delete(product);	
		} else if (!adminDAO.existsByRid(IDID)|| IDID==null){
			delFailed = false;
			enter = true;
		} else {
			delFailed = true;
		}
		
		
		
		
		String id = search;
		modelMap.put("id", id);
		modelMap.put("product", product);
		modelMap.put("warn", warn);
		modelMap.put("exist", Exist);
		modelMap.put("show", show);
		modelMap.put("delSuccess", delSuccessful);
		modelMap.put("delFail", delFailed);
		modelMap.put("enter", enter);
		
		return "delProduct";
	}








}





