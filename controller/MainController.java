package com.nep.itn08.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nep.itn08.model.Admin;
import com.nep.itn08.model.Product;
import com.nep.itn08.model.Total;
import com.nep.itn08.repository.AdminDAO;
import com.nep.itn08.repository.ProductDAO;
import com.nep.itn08.repository.TotalDAO;
import com.nep.itn08.repository.UserDAO;


@Controller
public class MainController {

	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private TotalDAO totalDAO;
	
	@Autowired
	private UserDAO userDAO;



	Total addTotal;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	String date = sdf.format(new Date());
	Admin adminList = new Admin(1,"100","admin","admin",date);
	Total total = new Total (1,0,0,0,0,0,0);
	int token = 0;

	//index page for login system----------------------------------------------------------------------------------------------------------
	
	@RequestMapping(value="/")
	public String index (@RequestParam(required=false) String userName,@RequestParam(required=false) String userPassword, ModelMap modelMap) {
		
		if (!adminDAO.existsById(1)) {      //if administrator data is not available by id 1
			adminDAO.save(adminList);
		}
		
		if (!totalDAO.existsById(1)) {		//if total_product data is not available by id 1
			totalDAO.save(total);
		}
		
		String warn;
		if (adminDAO.existsByUserName(userName)) {				//if user name enter is exist in the Administrator database
			if (adminDAO.existsByUserPassword(userPassword)) {  //if user password entered is exist in Administrator database
				
				return "redirect:adminSearch";  				//redirect to administrator search page
			}
			else {
				warn = "Incorrect Username/Password";  			//if incorrect this message will appear
				modelMap.put("Warn", warn);
				return "index";									//load index page if incorrect
			}
		} else if (userDAO.existsByUserName(userName)){			//if user name is exist in user database
			if (userDAO.existsByUserPassword(userPassword)) {	//if user password is exist in user database
				return "redirect:verify";   					// redirect to user search page
			} else {
				warn = "Incorrect Username/Password"; 			
				modelMap.put("Warn", warn);
				return "index";
			}
			 
		}else {													//if user name and password is not exist in any database and refers to empty or null
			warn = "Please Enter Your Username and Password!";
			modelMap.put("Warn", warn);
			return "index";										//load index page again!
		}

	}
	
	

//	@RequestMapping(value="/verify" ,method=RequestMethod.GET)
//	public String verify (@RequestParam(required=false) String id,ModelMap modelMap) {
//
//		Product product;
//		String warningSearch = null;
//		
//
//		if (!productDAO.existsByRid(id) || id == null || id.isEmpty()) {
//			product = new Product(0,"Not Found",0, "Not Found", 0,0,0,date);
//			//					(List<Product>) productDAO.findAll();
//			warningSearch = "Enter the Product ID";
//		} else{
//			product = productDAO.findByRid(id).get();
//		}
//		
//		double Price = 0;
//		int quantity = 0;
//		
//		if (totalDAO.existsById(1)) {
//			addTotal = totalDAO.findById(1).get();
//			Price = addTotal.getTotalPrice();
//			quantity = addTotal.getTotalQuantity();
//		}
//		
//		modelMap.put("products", product);
//		modelMap.put("warn", warningSearch);
//		modelMap.put("price", Price);
//		modelMap.put("quantity", quantity);
//
//
//		return "verify";	
//
//	}
//
//	@RequestMapping (value="/adminSearch")
//	public String adminSearch(@RequestParam(required=false) String id,ModelMap modelMap) {
//		Product product ;
//		String warningSearch = null ;
//
//
//		if (!productDAO.existsByRid(id) || id == null || id.isEmpty()) {
//			product = new Product(0,"Not Found",0, "Not Found", 0,0,0,date);
//			//					(List<Product>) productDAO.findAll();
//			warningSearch = "Please Enter the Product ID";
//		} else {
//
//			product = productDAO.findByRid(id).get();
//		}
//		double Price = 0;
//		int quantity = 0;
//	
//		if (totalDAO.existsById(1)) {
//			addTotal = totalDAO.findById(1).get();
//			Price = addTotal.getTotalPrice();
//			quantity = addTotal.getTotalQuantity();
//		}
//		
//
//
//		modelMap.put("products", product);
//		modelMap.put("warn", warningSearch);
//		modelMap.put("price", Price);
//		modelMap.put("quantity", quantity);
//		return "adminSearch";
//	}
//
//
//
}
