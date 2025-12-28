package com.rms.app.service;

import java.util.List;

import com.rms.app.model.Bill;
import com.rms.app.model.Menu;
import com.rms.app.model.Notification;
import com.rms.app.model.Order;
import com.rms.app.model.Review;
import com.rms.app.model.Staff;
import com.rms.app.model.Stock;
import com.rms.app.model.Tables;
import com.rms.app.model.User;



public interface StaffService {

	int saveStaff(Staff staff);

	Staff getStaffByEmail(String string);

	void deleteStaff(Long id);

	List<Tables> getCustomerReservations();

	void deleteReservation(Long id);

	List<Order> getAllOrders();

	void confirmOrder(Long id);

	void cancellOrder(Long id);

	void updateOrderStatus(Long id, String status);

	List<Order> getConfirmedOrders();

	List<Stock> getAllStock();

	void saveStock(Stock stock);

	List<Bill> getStartedBills();

	List<Bill> getBill(String tableName);

	Menu getMenuById(String menuItem);

	void saveAssign(Bill bill);

	void completePayment(String tableName);

	List<Notification> getAllNotifications();

	Tables getTableById(Long id);

	Order getOrderById(Long id);

	List<Review> getCustomerReviews();




}
