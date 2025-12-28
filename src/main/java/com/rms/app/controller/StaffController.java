package com.rms.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rms.app.model.Bill;
import com.rms.app.model.Menu;
import com.rms.app.model.Notification;
import com.rms.app.model.Order;
import com.rms.app.model.Review;
import com.rms.app.model.Staff;
import com.rms.app.model.Stock;
import com.rms.app.model.Tables;
import com.rms.app.model.User;
import com.rms.app.service.AdminService;
import com.rms.app.service.StaffService;
import com.rms.app.service.UserService;


@Controller
public class StaffController {
	
	@Autowired
	private StaffService staffService;
	

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private UserService userService;
	
	

	@GetMapping("/staff")
	public String getStaffWelcomePage(@ModelAttribute("staff") Staff staff, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        Staff staffModel = staffService.getStaffByEmail(messages.get(0));
        model.addAttribute("role", staffModel.getStaffType());

		return "staff/welcomestaff";
	}
	
	@GetMapping("/staffprofile")
	public String getStaffProfile(@ModelAttribute("staff") Staff staff, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
		Staff staffModel = staffService.getStaffByEmail(messages.get(0));
		model.addAttribute("role", staffModel.getStaffType());
	        
	        model.addAttribute("staff", staffModel);

		return "staff/profile";
	}
	
	@PostMapping("/updateStaffProfile")
	public String updateStaffProfile(@ModelAttribute("staff") Staff staff, Model model)
	{
		System.out.println("save===user");
		int output =staffService.saveStaff(staff);
		if(output>0) {
			return "redirect:/staffprofile";
		}
		
		else {
			model.addAttribute("errormsg", "Operation failed. Please try again");
			return "home/error";
		}
		
	}
	
	@PostMapping("/deleteStaffProfile/{id}")
	public String deleteStaffProfile(@PathVariable(name="id") Long id,HttpServletRequest request, Model model)
	{
		//staffService.deleteStaff(id);
		 request.getSession().invalidate();
		 model.addAttribute("errormsg", "Your Account Deleted Successfully");
			return "home/error2";
	}
	
	@GetMapping("/viewreservations")
	public String viewreservations(@ModelAttribute("table") Tables table, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
        Staff staffModel = staffService.getStaffByEmail(messages.get(0));
        model.addAttribute("role", staffModel.getStaffType());
		 
		List<Tables> reservs = staffService.getCustomerReservations();
		int rSize = reservs.size();
		if(rSize > 0) {
			model.addAttribute("flag", 1);
		}
		else {
			model.addAttribute("flag", 0);
		}
	        
	    model.addAttribute("reservations", reservs);

		return "staff/viewreservations";
	}
	
	@PostMapping("/deleteReservation/{id}")
	public String deleteReservation(@PathVariable(name="id") Long id)
	{
		Notification notification = new Notification();
		Tables table = staffService.getTableById(id); 
		
		notification.setEmail(table.getCustomerEmail());
		notification.setName(table.getDatetime());
		notification.setDescription(table.getCustomerEmail()+" cancelled "+table.getName());
		notification.setStatus("cancelled");
		notification.setUserType("user");
		
		userService.saveNotify(notification);
		staffService.deleteReservation(id);
		
		
		
		return "redirect:/viewreservations";
	}
	
	@GetMapping("/confirmorders")
	public String confirmorders(@ModelAttribute("order") Order order, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
        Staff staffModel = staffService.getStaffByEmail(messages.get(0));
        model.addAttribute("role", staffModel.getStaffType());
		 
		List<Order> orders = staffService.getAllOrders();
		int oSize = orders.size();
		if(oSize > 0) {
			model.addAttribute("flag", 1);
		}
		else {
			model.addAttribute("flag", 0);
		}
	        
	    model.addAttribute("orders", orders);

		return "staff/confirmorders";
	}
	
	@PostMapping("/confirmOrder/{id}")
	public String confirmOrder(@PathVariable(name="id") Long id)
	{
		staffService.confirmOrder(id);
		
		Notification notification = new Notification();
		Order order = staffService.getOrderById(id); 
		
		notification.setEmail(order.getEmail());
		notification.setName(order.getName());
		notification.setDescription("Your Order "+order.getName()+" is confirmed ");
		notification.setStatus("confirmed");
		notification.setUserType("user");
		
		userService.saveNotify(notification);
		
		return "redirect:/confirmorders";
	}
	
	@PostMapping("/cancellOrder/{id}")
	public String cancellOrder(@PathVariable(name="id") Long id)
	{
		Notification notification = new Notification();
		Order order = staffService.getOrderById(id); 
		
		notification.setEmail(order.getEmail());
		notification.setName(order.getName());
		notification.setDescription("Your Order "+order.getName()+" is cancelled ");
		notification.setStatus("cancelled");
		notification.setUserType("user");
		
		userService.saveNotify(notification);
		staffService.cancellOrder(id);
		
		return "redirect:/confirmorders";
	}
	
	@PostMapping("/updateOrderStatus/{id}")
	public String updateOrderStatus(@PathVariable(name="id") Long id, @RequestParam("status") String status)
	{
		Notification notification = new Notification();
		Order order = staffService.getOrderById(id); 
		
		notification.setEmail(order.getEmail());
		notification.setName(order.getName());
		notification.setDescription("Your Order "+order.getName()+" is "+status);
		notification.setStatus(status);
		notification.setUserType("user");
		
		userService.saveNotify(notification);
		
		staffService.updateOrderStatus(id, status);
		
		return "redirect:/updatestatus";
	}
	
	@GetMapping("/updatestatus")
	public String updatestatus(@ModelAttribute("order") Order order, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
        Staff staffModel = staffService.getStaffByEmail(messages.get(0));
        model.addAttribute("role", staffModel.getStaffType());
		 
		List<Order> orders = staffService.getConfirmedOrders();
		int oSize = orders.size();
		if(oSize > 0) {
			model.addAttribute("flag", 1);
		}
		else {
			model.addAttribute("flag", 0);
		}
	        
	    model.addAttribute("orders", orders);

		return "staff/updatestatus";
	}
	
	@GetMapping("/restock")
	public String restock(@ModelAttribute("stock") Stock stock, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
        Stock stocks = new Stock();
        List<Stock> stockss = staffService.getAllStock();

        Staff staffModel = staffService.getStaffByEmail(messages.get(0));
        model.addAttribute("role", staffModel.getStaffType());

	    model.addAttribute("stock", stocks);
	    model.addAttribute("stocks", stockss);

		return "staff/restock";
	}
	
	
	@PostMapping("/saveStock")
	public String saveStock(@ModelAttribute("stock") Stock stock, Model model, HttpSession session)
	{
			staffService.saveStock(stock);
		
			return "redirect:/staff";
		
	}
	
	@GetMapping("/bills")
	public String bills(@ModelAttribute("bill") Bill bill, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
        Staff staffModel = staffService.getStaffByEmail(messages.get(0));
        model.addAttribute("role", staffModel.getStaffType());
		 
		List<Bill> bills = staffService.getStartedBills();
		int oSize = bills.size();
		if(oSize > 0) {
			model.addAttribute("flag", 1);
		}
		else {
			model.addAttribute("flag", 0);
		}
	        
	    model.addAttribute("bills", bills);

		return "staff/bills";
	}
	
	@PostMapping("/getBill")
	public String getBill(Model model, HttpSession session, @RequestParam("tableName") String tableName) {
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        Staff staffModel = staffService.getStaffByEmail(messages.get(0));
        model.addAttribute("sessionMessages", messages);
        List<Bill> bills = staffService.getBill(tableName);
        model.addAttribute("bills", bills);
		return "staff/bills";
	}
	
	@GetMapping("/assign")
	public String assign(Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        Staff staffModel = staffService.getStaffByEmail(messages.get(0));
        model.addAttribute("role", staffModel.getStaffType());
        List<Menu> menus = adminService.getAllMenu();
        Bill bill = new Bill();

	    model.addAttribute("bill", bill);
	    model.addAttribute("menus", menus);

		return "staff/assign";
	}
	
	
	@PostMapping("/assignOrder")
	public String assignOrder(@ModelAttribute("bill") Bill bill, Model model, HttpSession session, @RequestParam("menuItem") String menuItem)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        Staff staffModel = staffService.getStaffByEmail(messages.get(0));
        model.addAttribute("role", staffModel.getStaffType());
        System.out.println("----"+messages);
        
		Menu menu = staffService.getMenuById(menuItem);
		bill.setName(menu.getName());
		bill.setEmail(staffModel.getEmail());
		bill.setPrice(menu.getPrice());
		int total = Integer.parseInt(menu.getPrice()) * Integer.parseInt(bill.getQuantity());
		bill.setFinalBill(String.valueOf(total));
		bill.setStatus("started");
		
			staffService.saveAssign(bill);
		
			return "redirect:/staff";
		
	}
	

	@PostMapping("/billPaid/{tableName}")
	public String billPaid(@PathVariable(name="tableName") String tableName)
	{
		staffService.completePayment(tableName);
		
		return "redirect:/staff";
	}
	
	@GetMapping("/notifications")
	public String notifications(Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
        Staff staffModel = staffService.getStaffByEmail(messages.get(0));
        model.addAttribute("role", staffModel.getStaffType());
		 
		List<Notification> notifies = staffService.getAllNotifications();
		int oSize = notifies.size();
		if(oSize > 0) {
			model.addAttribute("flag", 1);
		}
		else {
			model.addAttribute("flag", 0);
		}
	        
	    model.addAttribute("notifications", notifies);

		return "staff/notifications";
	}
	
	@GetMapping("/reviews")
	public String viewreviews(Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
        Staff staffModel = staffService.getStaffByEmail(messages.get(0));
        model.addAttribute("role", staffModel.getStaffType());
		 
		List<Review> reviews = staffService.getCustomerReviews();
		int rSize = reviews.size();
		if(rSize > 0) {
			model.addAttribute("flag", 1);
		}
		else {
			model.addAttribute("flag", 0);
		}
	        
	    model.addAttribute("reviews", reviews);

		return "staff/reviews";
	}
}
