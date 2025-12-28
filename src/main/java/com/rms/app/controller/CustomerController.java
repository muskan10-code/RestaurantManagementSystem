package com.rms.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

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

import com.rms.app.model.Cart;
import com.rms.app.model.Menu;
import com.rms.app.model.Notification;
import com.rms.app.model.Order;
import com.rms.app.model.Review;
import com.rms.app.model.Tables;
import com.rms.app.model.Ticket;
import com.rms.app.model.User;
import com.rms.app.service.AdminService;
import com.rms.app.service.UserService;


@Controller
public class CustomerController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AdminService adminService;
	
	

	@GetMapping("/customer")
	public String getCustomerWelcomePage(@ModelAttribute("user") User user, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		
        model.addAttribute("sessionMessages", messages);
        
	List<Menu> menuList = userService.getAllMenu();
		
        
        model.addAttribute("menus", menuList);

		return "customer/welcomecustomer";
	}
	
	@GetMapping("/menus")
	public String menus(@ModelAttribute("user") User user, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		
        model.addAttribute("sessionMessages", messages);
        
	List<Menu> menuList = userService.getAllMenu();
		
        System.out.print("hello");
        model.addAttribute("menus", menuList);

		return "customer/menu";
	}
	
	@GetMapping("/profile")
	public String getCustomerProfile(@ModelAttribute("user") User user, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
		User userModel = userService.getUserByEmail(messages.get(0));
		
		if(userModel == null) {
			model.addAttribute("errormsg", "Guest cannot use Profile");
			return "home/error";
		}
			
	        
	        model.addAttribute("user", userModel);

		return "customer/profile";
	}
	
	@GetMapping("/guest")
	public String guest(@ModelAttribute("user") User user, Model model, HttpSession session)
	{  

		return "customer/guest";
	}
	
	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute("user") User user, Model model)
	{
		System.out.println("save===user");
		int output =userService.saveUser(user);
		if(output>0) {
			return "redirect:/profile";
		}
		
		else {
			model.addAttribute("errormsg", "Operation failed. Please try again");
			return "home/error";
		}
		
	}
	
	@PostMapping("/deleteProfile/{id}")
	public String deleteProfile(@PathVariable(name="id") Long id,HttpServletRequest request, Model model)
	{
		userService.deleteUser(id);
		 request.getSession().invalidate();
		 model.addAttribute("errormsg", "Your Account Deleted Successfully");
			return "home/error2";
	}
	
	@PostMapping("/guestCheckIn")
	public String guestCheckIn(HttpServletRequest request, Model model, @RequestParam("guest") String guestEmail)
	{
		@SuppressWarnings("unchecked")
		List<String> messages = (List<String>) request.getSession().getAttribute("MY_SESSION_MESSAGES");
		if (messages == null) {
			messages = new ArrayList<>();
			request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);
		}
		
			messages.add(guestEmail);
			request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);
			
		 
			return "redirect:/customer";
	}
	
	@PostMapping("/addToCart/{id}")
	public String addToCart(Model model, HttpSession session, @PathVariable(name="id") Long id, @RequestParam("quantity") String quantity) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		String guest = "";
		if(messages == null) {
			return "redirect:/guest";
		}
		else {
			guest = messages.get(0);
		}
		

		Menu menu = adminService.getMenuById(id);
		Cart cart = new Cart();
		
		cart.setCategory(menu.getCategory());
		cart.setCuisine(menu.getCuisine());
		cart.setCustomerEmail(guest);
		cart.setDescription(menu.getDescription());
		cart.setName(menu.getName());
		cart.setType(menu.getType());
		cart.setVegOrNonVeg(menu.getVegOrNonVeg());
		cart.setQuantity(quantity);
		cart.setPrice(menu.getPrice());
		int totalPrice = Integer.parseInt(menu.getPrice())* Integer.parseInt(quantity);
		cart.setTotalPrice(String.valueOf(totalPrice));
		
		userService.addToCart(cart);
		
        model.addAttribute("sessionMessages", messages);
		
		return "redirect:/cart";
	}
	
	@GetMapping("/cart")
	public String cart(Model model, HttpSession session) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		String guest = "";
		if(messages == null) {
			return "redirect:/guest";
		}
		else {
			guest = messages.get(0);
		}
		List<Cart> carts = userService.getUserCart(guest);
		model.addAttribute("carts", carts);
		
		int finalPrice = 0;
		for(int i=0;i<carts.size();i++) {
			finalPrice = finalPrice + Integer.parseInt(carts.get(i).getTotalPrice());
		}
		
		if(finalPrice > 0) {
			model.addAttribute("finalprice", finalPrice);
		}
		else {
			model.addAttribute("finalprice", "0");
		}
		
		
		int cartSize = carts.size();
		if(cartSize > 0) {
			model.addAttribute("flag", 1);
		}
		else {
			model.addAttribute("flag", 0);
		}
		
		
		model.addAttribute("cartsize", cartSize);
		System.out.println(carts.size()+"------");
		
		return "customer/cart";
		
	}
	

	@PostMapping("/removeFromCart/{id}")
	public String removeFromCart(@PathVariable(name="id") Long id)
	{
		userService.deleteFromCart(id);
		
		return "redirect:/cart";
	}
	
	@GetMapping("/reservetable")
	public String reservetable(@ModelAttribute("table") Tables table, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
		User userModel = userService.getUserByEmail(messages.get(0));
		
		if(userModel == null) {
			model.addAttribute("errormsg", "Guest cannot Book Table");
			return "home/error";
		}
		 
		Tables tableModel = new Tables();
			
	        
	    model.addAttribute("table", tableModel);

		return "customer/reservetable";
	}
	
	@PostMapping("/bookTable")
	public String bookTable(@ModelAttribute("table") Tables table, Model model,HttpSession session)
	{
		System.out.println("save===table");
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
		User userModel = userService.getUserByEmail(messages.get(0));
		table.setCustomerEmail(userModel.getEmail());
		int output =userService.saveTable(table);
		
		Notification notification = new Notification();
		
		notification.setEmail(userModel.getEmail());
		notification.setName(userModel.getUsername());
		notification.setDescription(userModel.getEmail()+" "+"booked"+" "+table.getName()+" at "+table.getDatetime());
		notification.setStatus("booked");
		notification.setUserType("staff");
		
		userService.saveNotify(notification);
		
		if(output>0) {
			return "redirect:/customer";
		}
		
		else {
			model.addAttribute("errormsg", "Operation failed. Please try again");
			return "home/error";
		}
		
	}
	
	@GetMapping("/placeOrder")
	public String placeOrder(Model model, HttpSession session) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		
		
		Order order = new Order();
		List<Cart> cart =userService.getUserCart(messages.get(0));
		
		int finalCost = 0;
		for(int i=0;i<cart.size(); i++) {
			
			
			
			finalCost = finalCost + Integer.parseInt(cart.get(i).getTotalPrice());
		}
		
		
		model.addAttribute("isFirst", userService.checkIsFirstOrder(messages.get(0)));
		model.addAttribute("cost", finalCost);
		model.addAttribute("order", order);
		
		return "customer/payment";
		
	}
	
	@PostMapping("makePayment")
	public String makePayment(@ModelAttribute("order") Order order,HttpSession session, Model model, @RequestParam("isFirst") String isFirst, @RequestParam("coupon") String coupon)
	{
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		String guest = "";
		if(messages == null) {
			return "redirect:/guest";
		}
		else {
			guest = messages.get(0);
		}
		
		List<Cart> cart =userService.getUserCart(guest);
		
		StringJoiner name = new StringJoiner(",");
		StringJoiner price = new StringJoiner(",");
		StringJoiner quantity = new StringJoiner(",");
		StringJoiner totalCost = new StringJoiner(",");
		
		 
		double finalCost = 0;
		
		for(int i=0;i<cart.size(); i++) {
			
			name.add(cart.get(i).getName());
			
			price.add(cart.get(i).getPrice());
			
			quantity.add(cart.get(i).getQuantity());
			
			
			
			totalCost.add(cart.get(i).getTotalPrice());
			
			finalCost = finalCost + Integer.parseInt(cart.get(i).getTotalPrice());
		}
		
		if(isFirst.equals("true") && coupon.equals("FIRSTORDER")) {
			finalCost = finalCost - finalCost*0.15;
		}
		
		order.setName(name.toString());
		order.setPrice(price.toString());
		order.setQuantity(quantity.toString());
		order.setTotalCost(totalCost.toString());
		order.setEmail(guest);
		order.setFinalBill(String.valueOf(finalCost));
		order.setStatus("ordered");
		
		if(order.getType().equals("cash")) {
			order.setCardName("cash");
			order.setCardNumber("cash");
			order.setCvv("cash");
		}
		
		
		
		Notification notification = new Notification();
		
		notification.setEmail(guest);
		notification.setName(name.toString());
		notification.setDescription(guest+" "+order.getStatus()+" "+name.toString());
		notification.setStatus("ordered");
		notification.setUserType("staff");
		
		userService.saveNotify(notification);
		
		userService.saveOrder(order);
		
		
		
		
		return "redirect:/customer";
	}
	
	@GetMapping("/orders")
	public String orders(Model model, HttpSession session) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		User userdata = userService.findUser(messages.get(0));
		
		if(userdata == null) {
			model.addAttribute("errormsg", "Guest cannot access orders");
			return "home/error";
		}
		
		List<Order> orders = userService.getCustomerOrders(messages.get(0));
		
		if(orders.size() > 0 ) {
			model.addAttribute("flag", 1);
		}
		else {
			model.addAttribute("flag", 0);
		}

		model.addAttribute("orders", orders);
		
		return "customer/orders";
		
	}
	
	@PostMapping("/cancelOrder/{id}")
	public String cancelOrder(@PathVariable(name="id") Long id)
	{
		Order order = userService.getOrder(id);
		
		Notification notification = new Notification();
		
		notification.setEmail(order.getEmail());
		notification.setName(order.getName());
		notification.setDescription(order.getEmail()+" "+order.getStatus()+" "+order.getName());
		notification.setStatus("cancelled");
		notification.setUserType("staff");
		
		userService.saveNotify(notification);
		userService.cancelOrder(id);
		
		return "redirect:/orders";
	}
	
	@PostMapping("/addReview/{id}")
	public String addReview(@PathVariable(name="id") Long id,Model model, HttpSession session, @RequestParam("rating") String rating, @RequestParam("feedback") String feedback)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error2";
		}
		User userdata = userService.findUser(messages.get(0));
		
		Review review = new Review();
		review.setCustomerEmail(userdata.getEmail());
		review.setOrderId(String.valueOf(id));
		review.setRating(rating);
		review.setFeedback(feedback);
		userService.saveReview(review);
		
		return "redirect:/orders";
	}
	
	@GetMapping("/help")
	public String help(@ModelAttribute("ticket") Ticket ticket, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
		User userModel = userService.getUserByEmail(messages.get(0));
		if(userModel == null) {
			model.addAttribute("errormsg", "Guest cannot request help");
			return "home/error";
		}
		 
		Ticket tickett = new Ticket();
		
		
			
	        
	    model.addAttribute("ticket", tickett);

		return "customer/help";
	}
	
	@PostMapping("/raiseTicket")
	public String raiseTicket(@ModelAttribute("ticket") Ticket ticket, Model model,HttpSession session)
	{
		System.out.println("save===ticket");
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
		User userModel = userService.getUserByEmail(messages.get(0));
		
		if(userModel == null) {
			model.addAttribute("errormsg", "Guest cannot raise Tickets");
			return "home/error";
		}
		
		ticket.setCustomerEmail(userModel.getEmail());
		int output =userService.saveTicket(ticket);
		Notification notification = new Notification();
		
		notification.setEmail(userModel.getEmail());
		notification.setName(userModel.getUsername());
		notification.setDescription(ticket.getPriority()+"-"+ticket.getDescription());
		notification.setStatus("help");
		notification.setUserType("staff");
		
		userService.saveNotify(notification);
		if(output>0) {
			return "redirect:/customer";
		}
		
		else {
			model.addAttribute("errormsg", "Operation failed. Please try again");
			return "home/error";
		}
		
	}
	
	@PostMapping("/applyFilters")
	public String applyFilters(Model model, HttpSession session, @RequestParam("category") String category,
			 @RequestParam("type") String type, @RequestParam("vegOrNonVeg") String vegOrNonVeg) {
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		User userdata = userService.findUser(messages.get(0));
        model.addAttribute("sessionMessages", messages);
        List<Menu> menus = userService.filterMenu(category,type, vegOrNonVeg);
        model.addAttribute("menus", menus);
		return "customer/welcomecustomer";
	}
	
	@GetMapping("/notification")
	public String notification(@ModelAttribute("notification") Notification notification, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
        
		User userModel = userService.getUserByEmail(messages.get(0));
		
		if(userModel == null) {
			model.addAttribute("errormsg", "Guest cannot use Notifications");
			return "home/error";
		}
		 
		List<Notification> notifications = userService.getAllNotifications(userModel.getEmail());
		System.out.print(notifications.size());
	        
	    model.addAttribute("notifications", notifications);

		return "customer/notification";
	}
	
	@GetMapping("/seasonal")
	public String seasonal(@ModelAttribute("user") User user, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		
        model.addAttribute("sessionMessages", messages);
        
        List<Menu> menuList = userService.getSeasonalMenu();
		
        
        model.addAttribute("menus", menuList);

		return "customer/seasonal";
	}
	
	@PostMapping("/applySeasonalFilter")
	public String applyFilters(Model model, HttpSession session, @RequestParam("season") String season) {
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		User userdata = userService.findUser(messages.get(0));
        model.addAttribute("sessionMessages", messages);
        List<Menu> menus = userService.filterSeasonMenu(season);
        model.addAttribute("menus", menus);
		return "customer/seasonal";
	}
	
	@PostMapping("/increaseQuantity/{id}")
	public String increaseQuantity(@PathVariable(name="id") Long id,Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error2";
		}
		User userdata = userService.findUser(messages.get(0));
		
		userService.increaseCart(id);
		
		return "redirect:/cart";
	}
	
	
	@PostMapping("/reduceQuantity/{id}")
	public String reduceQuantity(@PathVariable(name="id") Long id,Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error2";
		}
		User userdata = userService.findUser(messages.get(0));
		
		userService.reduceQuantity(id);
		
		return "redirect:/cart";
	}
}
