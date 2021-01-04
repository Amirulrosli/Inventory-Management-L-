package com.nep.itn08.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nep.itn08.model.Admin;
import com.nep.itn08.model.CashierStorage;
import com.nep.itn08.model.Product;
import com.nep.itn08.model.User;
import com.nep.itn08.repository.AdminDAO;
import com.nep.itn08.repository.CashierDAO;
import com.nep.itn08.repository.ProductDAO;
import com.nep.itn08.repository.UserDAO;

@Controller
public class UserController {

	//initialization of data repository----------------------------------------------------------
	
	@Autowired
	AdminDAO adminDAO;
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	CashierDAO cashierDAO;
	
	@Autowired
	ProductDAO productDAO;
	
	//Declaration and initialization required for functions-----------------------------------------------------------------
	
	Admin admin;
	User user;
	int privilegesNum = 0;
	int privilegesAdmin = 1;
	String newID = null;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	String date = sdf.format(new Date());
	
	//user management page---------------------------------------------------------------------------------------------------

	@RequestMapping (value="/usermanage")																		
	public String UserManagement(@RequestParam (required=false) String optionList,  
			@RequestParam (required=false) String username,
			@RequestParam (required=false) String password, 
			@RequestParam (required=false) String repassword, 
			@RequestParam (required=false) String accounttype, 
			
			ModelMap modelMap) {
		
	//initialization---------------------------------------
		
		String option1 = "Admin";
		String option2 = "User";
		String warn = null;
		String result= null;
		boolean showAdmin = false;
		boolean showUser = false;
		boolean show = false;
		String UserID = null;
		String idWarning = "ID is Auto-generated";

		List <Admin> AdminList = null ;
		List <User> userList = null;
		String Privileges = null;
		String bigResult = null;
		userList  = (List<User>) userDAO.findAll();
		AdminList = (List<Admin>) adminDAO.findAll(); 

		//------------------------Search User---------------------
		if (optionList == null || optionList.isEmpty()) {					 // if optionList field is null or empty
			warn="Please select the User Option!";
		} else if (optionList.equalsIgnoreCase(option1)) {                   //selected optionList is equal to option1

			showAdmin = true;												//show administrator form
			int i = AdminList.size();
			bigResult = "Result Found";
			result = i + " Result Found";
			Privileges = "Administrator";
			show = true;
			UserID = "Auto-Generated";



		} else if (optionList.equalsIgnoreCase(option2)){					//if option list is equal to option 2
			userList  = (List<User>) userDAO.findAll();						//retrieve all user available using userList
			showUser = true;												//show user form
			int i = userList.size();										// retrieve size for user database
			bigResult = "Result Found";										
			result = i + " Result Found";										
			Privileges = "User Only";
			show = true;
			UserID = "Auto-Generated";

		}else {																//if option List is not equal to any of the option
			warn = "Please Select The User Option!";
		}

		//--------------------Add USER-------------------
		
		//boolean and initialization required for function------------------------------------
		
		boolean addSuccessful = false;
		boolean addFailed = false;
		boolean addNull = false;
		String message = null;
		Admin newAdmin = null;

		if (username == null && password==null && repassword==null && accounttype==null) {						//if all field is null / empty
			addNull = true;
		} else if (username==null || password==null || repassword==null || accounttype==null) {					//if any of this field is empty or null
			addNull = true;

		} else {																							   // if all field is not empty
			if (!password.equalsIgnoreCase(repassword)) {													   // password is not equal to re enter password = failed
				addFailed = true;
			}
			else {																							
				if (accounttype.equalsIgnoreCase(option1)){														//if account type = Administrator
					if (adminDAO.existsByUserName(username) || userDAO.existsByUserName(username)) {			//if user name or password is already existed = failed
						addFailed = true;
					} else {																					//if name and password is not exist = true (success)
						addSuccessful = true;

						admin = adminDAO.findById(1).get();														//retrieve retrieve administrator data at id 1
						String adminNumber = admin.getRid();													//Automatically set administrator ID to default
						privilegesAdmin++;																		//increment number for account

						newAdmin = new Admin (privilegesAdmin,adminNumber,username,password,date);				//set data to POJO
						adminDAO.save(newAdmin);																//Save POJO to Administrator database
						message = "Add Administrator Successfully | Rid: "+adminNumber;
					}
				} else if (accounttype.equalsIgnoreCase(option2)){												//if account type = User
					if (userDAO.existsByUserName(username) || adminDAO.existsByUserName(username)) {			// if name and password is already existed in user database = failed
						addFailed = true;
					} else {																				    //if name and password is not exist = true (success)
						privilegesNum++;																		//increment on number for account
						String strNum = ""+privilegesNum;
						User newUser = new User (privilegesNum,strNum,username,password,date,0);				//set data to POJO
						addSuccessful = true;
						userDAO.save(newUser);																	//Save POJO to user Database
						message = "Add User Successfully | id: "+privilegesNum;
					}	


				} else {																						
					addNull=true;
				}
			}
		}

		

	modelMap.put("admin", AdminList);
	modelMap.put("user", userList);
	modelMap.put("showAdmin", showAdmin);
	modelMap.put("showUser", showUser);
	modelMap.put("result", result);
	modelMap.put("bigresult", bigResult);
	modelMap.put("warn", warn);
	modelMap.put("option1", option1);
	modelMap.put("option2", option2);
	modelMap.put("privilege", Privileges);
	modelMap.put("Auto", UserID);
	modelMap.put("show", show);
	modelMap.put("idwarn", idWarning);
	modelMap.put("idwarn", idWarning);
	modelMap.put("addSuccess", addSuccessful);
	modelMap.put("addFailed", addFailed);
	modelMap.put("message", message);
	



	return "usermanage";
}


	
	//Update User page----------------------------------------------------------------------------------------------------
	
	@RequestMapping (value="/UpdateUser")
	public String UpdateUser (@RequestParam (required=false) String search,
			@RequestParam (required=false) String UPid, 
			@RequestParam (required=false) String UPusername, 
			@RequestParam (required=false) String UPpassword, 
			@RequestParam (required=false) String UPadmin, 
			@RequestParam (required=false) String UPcommission, 
			@RequestParam (required=false) String UPdate, 
			ModelMap modelMap) {
		
		
		//-----------------------------UPDATE User--------------------------------------
		
		// Initialization required for function------------------------------

				boolean showUpdate = false;
				boolean showAdminID = false;
				boolean updateFailed = false;
				boolean updateSuccess = false;
				boolean Warning = false;
				String retrieveName = null;
				String retrievePassword = null;
				int retrieveID = 0; 
				String retrieveRid = null; 
				String retrieveDate = null; 
				double retrieveCommission = 0;
				Admin admin1;
				User user1;

				if (search == null || search.equalsIgnoreCase("") || search.isEmpty() && UPusername==null) {		//if search field is null or empty 
					showUpdate = false;																				//show update is false and warning will appear
					Warning = true;
				} else if (adminDAO.existsByUserName(search)){														// if search value is exist in administrator database
					showUpdate = true;
					admin1 = adminDAO.findByUserName(search).get();
					
					//retrieve data from administrator database--------------------
					
					retrieveName = admin1.getUserName();
					retrievePassword = admin1.getUserPassword();
					retrieveID = admin1.getId();
					retrieveRid = admin1.getRid();
					retrieveDate = admin1.getDate();
					showAdminID = true;
					
					if (UPusername==null || UPpassword==null || UPdate==null || UPadmin==null) {					//if any field in the form is empty
						updateFailed=false;																			//failed to update + warning will appear
						Warning = true;
						updateSuccess = false;
					} else {																						
						admin = new Admin (retrieveID,UPadmin,UPusername,UPpassword,UPdate);						//set data to POJO
						adminDAO.save(admin);																		//save POJO to Administrator database
						updateSuccess = true;																		//update successful
						showUpdate = false;
					}
					
					
				}else if (userDAO.existsByUserName(search)) {														// else if search value is exist in User database
						showUpdate = true;																			//show update is true (appeared)
						user1 = userDAO.findByUserName(search).get();
						
						//retrieve data from User database based on ID------------------------
						
						retrieveName = user1.getUserName();
						retrievePassword = user1.getUserPassword();
						retrieveID = user1.getId();
						retrieveRid = user1.getRid();
						retrieveDate = user1.getDate();
						retrieveCommission = user1.getCommission();
						
						if (UPusername==null || UPpassword==null || UPdate==null) {									//if any of this field is empty / null
							updateFailed = false;																	//Update will be failed and warning will appear
							Warning = true;
						} else {																					//if the field is not empty
						updateSuccess = true;																		//update will success
						double dCommission;
						if (UPcommission == null) {																	//if commission is empty ; it will save it as 0
							UPcommission = "0";
							dCommission = Double.parseDouble(UPcommission);
						} else {																					//if commission is not empty; string will be parse into integer
							dCommission = Double.parseDouble(UPcommission);
						}
						
						user = new User (retrieveID,retrieveRid,UPusername,UPpassword,UPdate,dCommission);			//set data to POJO
						userDAO.save(user);																			//save POJO to user Database
						showUpdate = false;
					} 
				}
				
				else {																								//if all condition is not met ; update will be failed and warning message will appear
							updateFailed = false;
							Warning = true;
				}	

				String input = search;
				
				modelMap.put("showAdminID", showAdminID);
				modelMap.put("showUpdate", showUpdate);
				modelMap.put("name", retrieveName);
				modelMap.put("password", retrievePassword);
				modelMap.put("ID", retrieveID);
				modelMap.put("Rid", retrieveRid);
				modelMap.put("date", retrieveDate);
				modelMap.put("commission", retrieveCommission);
				modelMap.put("updateSuccess", updateSuccess);
				modelMap.put("updateFailed", updateFailed);
				modelMap.put("input", input);
				modelMap.put("warning", Warning);
				
		return "UPdateUser";
	}

	
	//delUser page-----------------------------------------------------------------------------------------------------------------------------------
	
	
	@RequestMapping(value="/delUser")
	public String delUser (@RequestParam (required=false) String search,@RequestParam (required=false) String IDID,
						   ModelMap modelMap    ) {
		
		String warn = null;
		String Exist = null;
		boolean showExist = false;
		boolean showAdmin = false;
		boolean showUser = false;
		boolean delSuccessful = false;
		boolean delFailed = false;
		boolean enter = false;
		String privilege = null;
		
		//Search and notification---------------------------------------------------------
	
		if (search==null || search.isEmpty() || search.equalsIgnoreCase("")) {            //if search is empty and null ; warning message will appear
			warn = "Please Enter Username";												  //no form or table will appear
			showAdmin = false;
			showUser = false;
			delFailed=false;
		
		}else if (adminDAO.existsByUserName(search)) {									  //if search value is exist in administrator database
				
				admin = adminDAO.findByUserName(search).get();							  //retrieve data in database by ID
				int compareID = admin.getId();
				String cRid = admin.getRid();
				if (compareID==1) {														  //if compareID (administrator ID) is equal to 1 
					Exist = "Cannot delete the default System Admin";					  // exist warning will appeared
					showAdmin = false;
					showUser = false;
					showExist = true;
				} else {																// if not equal to 1
				showAdmin = true;														//administrator form will be shown
				showUser = false;
				newID = search;		
				delFailed=false;
				privilege = "Administrator";
				}
				
				if (IDID==null || IDID.isEmpty()|| IDID.equalsIgnoreCase("")) {			//if ID field is empty
					warn = "Please Enter Admin ID";										//delete process will be failed and warning message will appear
					delFailed=false;
				}
				
		} else if (userDAO.existsByUserName(search)) {									//if search value is exist in user database
			
			user = userDAO.findByUserName(search).get();								//retrieve data by ID in user database
			showUser = true;															//user form will be shown
			newID = search;
			delFailed = false;
			privilege = "User-Only";
			
			if (IDID==null || IDID.isEmpty()|| IDID.equalsIgnoreCase("")) {				//if ID field is empty
				warn = "Please Enter Username";											//delete process failed
				delFailed=false;
			}
		
		}else {																			//if all condition is not met shows user account does not exist
			Exist = "User Account Does Not Exist";										//all process failed
			showExist = true;
			showAdmin = false;
			showUser = false;
			delFailed=false;
			
			
		}
		
		//-------------------AdminID Verification-------------------------------------
		
		if (newID == null) {															//if ID in form is empty / null; delete failed and warning message appear
			warn = "Please Enter Username";
			delFailed=false;
		}
		else if (adminDAO.existsByRid(IDID)) {											//if ID is exist in Administrator database
				if (adminDAO.existsByUserName(newID)) {									//if search value is equal to the user name in Administrator database
					admin = adminDAO.findByUserName(newID).get();						//retrieve data by ID from database to POJO
					delSuccessful = true;
					adminDAO.delete(admin);												//delete POJO from database
				} else if (userDAO.existsByUserName(newID)) {							//if ID is exist in User Database
					user = userDAO.findByUserName(newID).get();							//retrieve data by ID from database to POJO
					userDAO.delete(user);												//delete POJO from database
					delSuccessful = true;
				} else {																///if it is not exist in any database
					delFailed = true;													//delete failed!
				}
		} else if (!adminDAO.existsByRid(IDID) || IDID==null){							//if ID field is empty; message enter will appear
			enter = true;
		} else {
			delFailed = true;
		}
		
		
		
		
		String name = search;
		modelMap.put("name", name);
		
		modelMap.put("warn", warn);
		modelMap.put("exist", Exist);
		modelMap.put("showAdmin", showAdmin);
		modelMap.put("showUser", showUser);
		modelMap.put("admin", admin);
		modelMap.put("user", user);
		modelMap.put("delSuccess", delSuccessful);
		modelMap.put("delFail", delFailed);
		modelMap.put("privilege", privilege);
		modelMap.put("showExist", showExist);
		modelMap.put("enter", enter);
		
		
		
		
		
		
		
		
		
		
		return "delUser";
	}
	
	//User logs page-----------------------------------------------------------------------------------------
	
	@RequestMapping (value="/logs")
	public String Logs(ModelMap modelMap) {
		// initialization for function----------------------------------------------------------------------
		double totalCommission = 0;
		double reCommission = 0;
		double commission = 0;
		String action = "Added";
		String privilege = null;
		List <CashierStorage> cashier  = (List<CashierStorage>) cashierDAO.findAll();
		List <Product> product  = (List<Product>) productDAO.findAll();
		long cashiercount = cashierDAO.count();                                                   //count data from the database (start with 1 ; if available)
		
		//retrieving each action and data by user-------------------------------------------------------------
		
		if (cashiercount != 0) {															    //if data in database is exist more or equal to 1
			for (int i = 1; i <= cashiercount ; i++) {											//loops used to retrieve each data in database depending on the count
				CashierStorage newCashier = cashierDAO.findById(i).get();					    //retrieve cashier data by i
				String name =  newCashier.getUser();											//retrieve cashier_storage user name
				if (userDAO.existsByUserName(name)) { 											// if name is exist in user database
					User user1  = userDAO.findByUserName(name).get();                           //retrieve data of user by user name
					commission = user1.getCommission();											// retrieve commission
					reCommission = newCashier.getTotalPrice() * (commission/100);				//re calculate total commission for each user
					privilege = "User-Only";
					totalCommission += reCommission;											//calculate total commission
					double newTotalCommission = Math.round(totalCommission*100.0)/100.0;		//round to 2 decimal places for commission
					user1.setTotalCommission(newTotalCommission);								//set commission to POJO
					userDAO.save(user1);													    //save POJO to database
					
				}
			}
		}
		//List all data----------------------------------------------------------
		
		List <User> user = (List<User>) userDAO.findAll();										//find all data in User database (retrieve all data)
		boolean show = false;
		String productWarn = null;
		String cashierWarn = null;
		String userWarn = null;
		
		if (product ==null && cashier==null && user==null) {									//if all of the Database (product,cashier_storage and user) is empty.
			show = false;
			productWarn = "No Logs at The Moment!";												// no logs will appeared in the page
		} else {
			show = true;																		//if in any field is available ; show the all table even some of it is null/empty
		}
		
		
		modelMap.put("show", show);
		modelMap.put("cashierwarn", cashierWarn);
		modelMap.put("productwarn", productWarn);
		modelMap.put("userwarn", userWarn);
		
		modelMap.put("action", action);
		modelMap.put("user", user);
		modelMap.put("product", product);
		modelMap.put("recommission", reCommission);
		modelMap.put("privilege", privilege);
		modelMap.put("cashier", cashier);
		return "logs";
	}


}
