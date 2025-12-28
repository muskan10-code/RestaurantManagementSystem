package com.rms.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms.app.dao.BillRepo;
import com.rms.app.dao.MenuRepo;
import com.rms.app.dao.NotificationRepo;
import com.rms.app.dao.OrderRepo;
import com.rms.app.dao.ReviewRepo;
import com.rms.app.dao.StaffRepo;
import com.rms.app.dao.StockRepo;
import com.rms.app.dao.TablesRepo;
import com.rms.app.dao.UserRepo;
import com.rms.app.model.Bill;
import com.rms.app.model.Menu;
import com.rms.app.model.Notification;
import com.rms.app.model.Order;
import com.rms.app.model.Review;
import com.rms.app.model.Staff;
import com.rms.app.model.Stock;
import com.rms.app.model.Tables;
import com.rms.app.model.User;


@Service
public class StaffServiceImpl implements StaffService{
	
	@Autowired
	private StaffRepo staffRepo;
	
	@Autowired
	private MenuRepo menuRepo;
	
	@Autowired
	private TablesRepo tableRepo;
	
	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private StockRepo stockRepo;
	
	@Autowired
	private BillRepo billRepo;
	
	@Autowired
	private NotificationRepo notifyRepo;

	@Autowired
	private ReviewRepo reviewRepo;
	
	@Override
	public int saveStaff(Staff staff) {
		// TODO Auto-generated method stub
		staffRepo.save(staff);
		return 1;
	}

	@Override
	public Staff getStaffByEmail(String email) {
		// TODO Auto-generated method stub
		return staffRepo.findbyEmail(email);
	}

	@Override
	public void deleteStaff(Long id) {
		// TODO Auto-generated method stub
		staffRepo.deleteById(id);
	}
	
	@Override
	public List<Tables> getCustomerReservations() {
		// TODO Auto-generated method stub
		return tableRepo.findAll();
	}

	@Override
	public void deleteReservation(Long id) {
		// TODO Auto-generated method stub
		tableRepo.deleteById(id);
		
	}

	@Override
	public List<Order> getAllOrders() {
		// TODO Auto-generated method stub
		return orderRepo.findAll().stream().filter(o -> o.getStatus().equals("ordered")).collect(Collectors.toList());
	}

	@Override
	public void confirmOrder(Long id) {
		// TODO Auto-generated method stub
		List<Order> order = orderRepo.findAll().stream().filter(o -> o.getId().equals(id)).collect(Collectors.toList());
				
			
		
		if(order.size() == 1) {
			Order o = order.get(0);
			o.setStatus("confirmed");
			orderRepo.save(o);
		}
		
	}

	@Override
	public void cancellOrder(Long id) {
		// TODO Auto-generated method stub
		
		List<Order> order = orderRepo.findAll().stream().filter(o -> o.getId().equals(id)).collect(Collectors.toList());
				
			
		
		if(order.size() == 1) {
			Order o = order.get(0);
			o.setStatus("cancelled");
			orderRepo.save(o);
		}
		
	}

	@Override
	public void updateOrderStatus(Long id, String status) {
		// TODO Auto-generated method stub
		
	List<Order> order = orderRepo.findAll().stream().filter(o -> o.getId().equals(id)).collect(Collectors.toList());
				
			
		
		if(order.size() == 1) {
			Order o = order.get(0);
			o.setStatus(status);
			orderRepo.save(o);
		}
		
	}

	@Override
	public List<Order> getConfirmedOrders() {
		// TODO Auto-generated method stub
		return orderRepo.findAll().stream().filter(o -> o.getStatus().equals("preparing") || o.getStatus().equals("confirmed") || o.getStatus().equals("ready") || o.getStatus().equals("completed")).collect(Collectors.toList());
	}

	@Override
	public List<Stock> getAllStock() {
		// TODO Auto-generated method stub
		return stockRepo.findAll();
	}

	@Override
	public void saveStock(Stock stock) {
		// TODO Auto-generated method stub
		stockRepo.save(stock);
		
	}

	@Override
	public List<Bill> getStartedBills() {
		// TODO Auto-generated method stub
		return billRepo.findAll().stream().filter(b -> b.getStatus().equals("started")).collect(Collectors.toList());
	}

	@Override
	public List<Bill> getBill(String tableName) {
		// TODO Auto-generated method stub
		return billRepo.findAll().stream().filter(b -> b.getStatus().equals("started") && b.getTableName().equals(tableName)).collect(Collectors.toList());
	}

	@Override
	public Menu getMenuById(String menuItem) {
		// TODO Auto-generated method stub
		List<Menu> menuList = menuRepo.findAll().stream().filter(m -> m.getId().equals(Long.parseLong(menuItem))).collect(Collectors.toList());
		return menuList.get(0);
	}

	@Override
	public void saveAssign(Bill bill) {
		// TODO Auto-generated method stub
		billRepo.save(bill);
		
	}

	@Override
	public void completePayment(String tableName) {
		// TODO Auto-generated method stub
		List<Bill> bills = billRepo.findAll().stream().filter(b -> b.getTableName().equals(tableName)).collect(Collectors.toList());
		bills.forEach(b -> b.setStatus("completed"));
		billRepo.saveAll(bills);
		
	}

	@Override
	public List<Notification> getAllNotifications() {
		// TODO Auto-generated method stub
		return notifyRepo.findAll().stream().filter(n -> n.getUserType().equals("staff")).collect(Collectors.toList());
	}

	@Override
	public Tables getTableById(Long id) {
		// TODO Auto-generated method stub
		return tableRepo.getById(id);
	}

	@Override
	public Order getOrderById(Long id) {
		// TODO Auto-generated method stub
		return orderRepo.getById(id);
	}

	@Override
	public List<Review> getCustomerReviews() {
		// TODO Auto-generated method stub
		return reviewRepo.findAll();
	}


	
	

	
	

}
