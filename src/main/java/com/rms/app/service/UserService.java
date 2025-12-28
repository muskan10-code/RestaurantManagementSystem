package com.rms.app.service;

import java.util.List;

import com.rms.app.model.Cart;
import com.rms.app.model.Menu;
import com.rms.app.model.Notification;
import com.rms.app.model.Order;
import com.rms.app.model.Review;
import com.rms.app.model.Tables;
import com.rms.app.model.Ticket;
import com.rms.app.model.User;



public interface UserService {

	User findUser(String email);

	User findUserByUsername(String username);

	int saveUser(User user);

	User authenticateUser(User user);

	int validatePassword(User user, String securityQuestion, String securityAnswer, String role);

	void saveNewPassword(User user, String role);

	void deleteUser(Long id);
	
	List<User> getAllUsers();

	List<Menu> getAllMenu();

	User getUserByEmail(String email);

	void addToCart(Cart cart);

	List<Cart> getUserCart(String email);

	void deleteFromCart(Long id);

	int saveTable(Tables table);

	void saveOrder(Order order);

	List<Order> getCustomerOrders(String email);

	void cancelOrder(Long id);

	void saveReview(Review review);

	int saveTicket(Ticket ticket);

	List<Menu> filterMenu(String category, String type, String vegOrNonVeg);

	void saveNotify(Notification notification);

	Order getOrder(Long id);

	List<Notification> getAllNotifications(String email);

	List<Menu> getSeasonalMenu();

	List<Menu> filterSeasonMenu(String season);

	void increaseCart(Long id);

	void reduceQuantity(Long id);

	String checkIsFirstOrder(String string);

	



}
