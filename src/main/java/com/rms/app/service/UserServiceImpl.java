package com.rms.app.service;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms.app.dao.CartRepo;
import com.rms.app.dao.MenuRepo;
import com.rms.app.dao.NotificationRepo;
import com.rms.app.dao.OrderRepo;
import com.rms.app.dao.ReviewRepo;
import com.rms.app.dao.StaffRepo;
import com.rms.app.dao.TablesRepo;
import com.rms.app.dao.TicketRepo;
import com.rms.app.dao.UserRepo;
import com.rms.app.model.Cart;
import com.rms.app.model.Menu;
import com.rms.app.model.Notification;
import com.rms.app.model.Order;
import com.rms.app.model.Review;
import com.rms.app.model.Staff;
import com.rms.app.model.Tables;
import com.rms.app.model.Ticket;
import com.rms.app.model.User;


@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private MenuRepo menuRepo;
	
	@Autowired
	private StaffRepo staffRepo;
	
	@Autowired
	private CartRepo cartRepo;
	
	@Autowired
	private TablesRepo tableRepo;
	
	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private ReviewRepo reviewRepo;
	
	@Autowired
	private TicketRepo ticketRepo;
	
	@Autowired
	private NotificationRepo notifyRepo;
	
	

	
	public int saveUser(User user) {
		user.setUsertype("customer");
		userRepo.save(user);
		
		if(userRepo.save(user)!=null) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public User findUser(String email) {
		List<User> user = userRepo.findAll();
		System.out.println("----"+user.size());
		if(user.size() == 0) {
			return null;
		}
		List<User> veifiedUser = user.stream().filter(n -> n.getEmail().equals(email) || n.getUsername().equals(email)).collect(Collectors.toList());
		if(veifiedUser.size() > 0) {
			return veifiedUser.get(0);
		}
		else {
			return null;
		}
		
	}
	
	public User authenticateUser(User user) {
		
		if(user.getEmail().equals("admin@gmail.com") && user.getPassword().equals("admin")) {
			
			user.setUsertype("admin");
			
			return user;
		}
		
		List<User> users = userRepo.findAll();
		List<User> veifiedUser = users.stream().filter(n -> (n.getEmail().equals(user.getEmail()) || n.getUsername().equals(user.getEmail())) && n.getPassword().equals(user.getPassword())).collect(Collectors.toList());
		
		if(veifiedUser.size() ==1) {
			
			
			return veifiedUser.get(0);
		}
		else {
			List<Staff> staffs = staffRepo.findAll();
			List<Staff> verifiedStaff = staffs.stream().filter(s -> (s.getEmail().equals(user.getEmail()) || s.getUsername().equals(user.getEmail())) && s.getPassword().equals(user.getPassword())).collect(Collectors.toList());
			
			if(verifiedStaff.size() ==1) {
				
				user.setUsertype("staff");
				return user;
			}
			else {
				return null;
			}
		}
			
	}
	
	public User findUserByUsername(String username) {
		
		List<User> users = userRepo.findAll();
		List<User> veifiedUser = users.stream().filter(n -> n.getUsername().equals(username)).collect(Collectors.toList());
		if(veifiedUser.size() > 0) {
			return veifiedUser.get(0);
		}
		else {
			return null;
		}
		
	}
	
	public int validatePassword(User usermodel, String securityQuestion, String securityAnswer, String role) {
		
		if(role.equals("customer")) {
		
		List<User> users = userRepo.findAll();
		List<User> verifiedUser = users.stream().filter(n -> n.getEmail().equals(usermodel.getEmail())).collect(Collectors.toList());
		if(verifiedUser.size() ==1) {
			List<User> userSecurities = userRepo.findAll();
			
			List<User> securedUser = userSecurities.stream().filter(security -> security.getSecurityQuestion().equals(securityQuestion) && security.getSecurityAnswer().equals(securityAnswer)
					
					).collect(Collectors.toList());
			if(securedUser.size() == 1) {
				return 1;
			}
			else {
				return 2;
			}
		}
		else {
			return 0;
		}
		
		}
		else {
			
			List<Staff> staffs = staffRepo.findAll();
			List<Staff> verifiedStaff = staffs.stream().filter(s -> s.getEmail().equals(usermodel.getEmail())).collect(Collectors.toList());
			if(verifiedStaff.size() ==1) {
				List<Staff> staffSecurities = staffRepo.findAll();
				
				List<Staff> securedStaff = staffSecurities.stream().filter(security -> security.getSecurityQuestion().equals(securityQuestion) && security.getSecurityAnswer().equals(securityAnswer)
						
						).collect(Collectors.toList());
				if(securedStaff.size() == 1) {
					return 1;
				}
				else {
					return 2;
				}
			}
			else {
				return 0;
			}
			
		}
	}
	
	public void saveNewPassword(User usermodel, String role) {
		
		if(role.equals("customer")) {
		
			User user = userRepo.findbyEmail(usermodel.getEmail());
			System.out.println("user#########"+user.toString());
			user.setPassword(usermodel.getPassword());
			userRepo.save(user);
			
		}
		else {
			Staff staff = staffRepo.findbyEmail(usermodel.getEmail());
			System.out.println("user#########"+staff.toString());
			staff.setPassword(usermodel.getPassword());
			staffRepo.save(staff);
		}
	}
	
	public void deleteUser(Long id) {
			
			userRepo.deleteById(id);
			
	}

	

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepo.findAll();
	}

	@Override
	public List<Menu> getAllMenu() {
		// TODO Auto-generated method stub
		System.out.print("hellooooo");
		return menuRepo.findAll().stream().filter(m -> m.getSeason().equals("normal")).collect(Collectors.toList());
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepo.findbyEmail(email);
	}

	@Override
	public void addToCart(Cart cart) {
		
		List<Cart> extCartList = cartRepo.findAll().stream().filter(c -> c.getCustomerEmail().equals(cart.getCustomerEmail()) && c.getName().equals(cart.getName())).collect(Collectors.toList());
		if(extCartList.size() > 0) {
			Cart extCart = extCartList.get(0);
			int q = Integer.parseInt(cart.getQuantity()) + Integer.parseInt(extCart.getQuantity()); 
			int fp = q * Integer.parseInt(cart.getPrice());
			extCart.setQuantity(String.valueOf(q));
			extCart.setTotalPrice(String.valueOf(fp));
			cartRepo.save(extCart);
		}
		else {
		
		cartRepo.save(cart);
		}
		
	}

	@Override
	public List<Cart> getUserCart(String email) {
		// TODO Auto-generated method stub
		return cartRepo.findAll().stream().filter(c -> c.getCustomerEmail().equals(email)).collect(Collectors.toList());
	}

	@Override
	public void deleteFromCart(Long id) {
		cartRepo.deleteById(id);
		
	}

	@Override
	public int saveTable(Tables table) {
		
		tableRepo.save(table);
		
		if(tableRepo.save(table)!=null) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public void saveOrder(Order order) {
		// TODO Auto-generated method stub
		orderRepo.save(order);
		cartRepo.deleteCart(order.getEmail());
		
	}

	@Override
	public List<Order> getCustomerOrders(String email) {
		// TODO Auto-generated method stub
		return orderRepo.findAll().stream().filter(o -> o.getEmail().equals(email)).collect(Collectors.toList());
	}

	@Override
	public void cancelOrder(Long id) {
		// TODO Auto-generated method stub
		orderRepo.deleteById(id);
		
	}

	@Override
	public void saveReview(Review review) {
		// TODO Auto-generated method stub
		reviewRepo.save(review);
		List<Order> order = orderRepo.findAll().stream().filter(o -> o.getId().equals(Long.parseLong(review.getOrderId()))).collect(Collectors.toList());
				
			
		
		if(order.size() == 1) {
			Order o = order.get(0);
			o.setStatus("reviewed");
			orderRepo.save(o);
		}
		
	}

	@Override
	public int saveTicket(Ticket ticket) {
		// TODO Auto-generated method stub
			ticketRepo.save(ticket);
			
			if(ticketRepo.save(ticket)!=null) {
				return 1;
			}
			else {
				return 0;
			}
	}

	@Override
	public List<Menu> filterMenu(String category, String type, String vegOrNonVeg) {
		
		if(category.isEmpty() && type.isEmpty() && vegOrNonVeg.isEmpty()) {
			return  menuRepo.findAll();
		}
		
		List<Menu> menus = menuRepo.findAll();
		List<Menu> filteredMenus = new ArrayList<Menu>();
		
		if(!category.isEmpty() && type.isEmpty() && vegOrNonVeg.isEmpty()) {
			filteredMenus = menus.stream().filter(menu -> menu.getCategory().equals(category)).collect(Collectors.toList());	
		}
		
		else if(category.isEmpty() && !type.isEmpty() && vegOrNonVeg.isEmpty()) {
			filteredMenus = menus.stream().filter(menu -> menu.getType().equals(type)).collect(Collectors.toList());	
		}
		
		else if(category.isEmpty() && type.isEmpty() && !vegOrNonVeg.isEmpty()) {
			filteredMenus = menus.stream().filter(menu -> menu.getVegOrNonVeg().equals(vegOrNonVeg)).collect(Collectors.toList());	
		}
		
		else if(!category.isEmpty() && !type.isEmpty() && vegOrNonVeg.isEmpty()) {
			filteredMenus = menus.stream().filter(menu -> menu.getCategory().equals(category) && menu.getType().equals(type)).collect(Collectors.toList());	
		}
		
		else if(category.isEmpty() && !type.isEmpty() && !vegOrNonVeg.isEmpty()) {
			filteredMenus = menus.stream().filter(menu -> menu.getVegOrNonVeg().equals(vegOrNonVeg) && menu.getType().equals(type)).collect(Collectors.toList());	
		}
		
		else if(!category.isEmpty() && type.isEmpty() && !vegOrNonVeg.isEmpty()) {
			filteredMenus = menus.stream().filter(menu -> menu.getCategory().equals(category) && menu.getVegOrNonVeg().equals(vegOrNonVeg)).collect(Collectors.toList());	
		}
		else {
			filteredMenus = menus.stream().filter(menu -> menu.getCategory().equals(category) && menu.getType().equals(type) && menu.getVegOrNonVeg().equals(vegOrNonVeg)).collect(Collectors.toList());	
		}
		
		 
		
		
		
		return filteredMenus.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(Menu::getId))),
                ArrayList::new));
	}

	@Override
	public void saveNotify(Notification notification) {
		// TODO Auto-generated method stub
		notifyRepo.save(notification);
	}

	@Override
	public Order getOrder(Long id) {
		// TODO Auto-generated method stub
		
		return orderRepo.getById(id);
	}

	@Override
	public List<Notification> getAllNotifications(String email) {
		// TODO Auto-generated method stub
		return notifyRepo.findAll().stream().filter(n -> n.getEmail().equals(email) && n.getUserType().equals("user")).collect(Collectors.toList());
		
	}

	@Override
	public List<Menu> getSeasonalMenu() {
		// TODO Auto-generated method stub
		return menuRepo.findAll().stream().filter(m -> !m.getSeason().equals("normal")).collect(Collectors.toList());
	}
	
	@Override
	public List<Menu> filterSeasonMenu(String season) {
		
		if(season.isEmpty() ) {
			return  menuRepo.findAll().stream().filter(m -> !m.getSeason().equals("normal")).collect(Collectors.toList());
		}
		
		List<Menu> menus = menuRepo.findAll();
		
		List<Menu> filteredMenus = menus.stream().filter(menu -> menu.getSeason().equals(season)).collect(Collectors.toList());	
		
		
		
		return filteredMenus.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(Menu::getId))),
                ArrayList::new));
	}

	@Override
	public void increaseCart(Long id) {
		// TODO Auto-generated method stub
		
		Cart cart = cartRepo.getById(id);
		
		int q = Integer.parseInt(cart.getQuantity()) + 1;
		int t = q * Integer.parseInt(cart.getPrice());
		
		cart.setQuantity(String.valueOf(q));
		cart.setTotalPrice(String.valueOf(t));
		cartRepo.save(cart);
		
		
	}

	@Override
	public void reduceQuantity(Long id) {
		// TODO Auto-generated method stub
	Cart cart = cartRepo.getById(id);
		if(cart.getQuantity().equals("1")) {
			cartRepo.delete(cart);
		}
		else {
		int q = Integer.parseInt(cart.getQuantity()) - 1;
		int t = q * Integer.parseInt(cart.getPrice());
		cart.setQuantity(String.valueOf(q));
		cart.setTotalPrice(String.valueOf(t));
		cartRepo.save(cart);
		}
	}

	@Override
	public String checkIsFirstOrder(String email) {
		// TODO Auto-generated method stub
		List<Order> orders = orderRepo.findAll().stream().filter(o -> o.getEmail().equals(email)).collect(Collectors.toList());
		if(orders.size() > 0) {
			return "false";
		}
		else {
			return "true";
		}
	}

	
	
	

}
