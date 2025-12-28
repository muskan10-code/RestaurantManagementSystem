package com.rms.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.rms.app.dao.MenuRepo;
import com.rms.app.dao.OrderRepo;
import com.rms.app.dao.StaffRepo;
import com.rms.app.dao.UserRepo;
import com.rms.app.model.Menu;
import com.rms.app.model.Order;
import com.rms.app.model.User;

import java.util.ArrayList;
import java.util.List;

//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private OrderRepo orderRepo;

    @Test
    public void getAllOrders() {

        Order order = new Order();
        
       order.setCardName("cardname");
       order.setCardNumber("1234");
       order.setCvv("123");
       order.setEmail("customer@gmail.com");
       order.setFinalBill("1234");
        
        
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        
        when(orderRepo.findAll()).thenReturn(orders);

        assertEquals(orders, userService.getCustomerOrders("customer@gmail.com"));

    }
    
    @Test
    public void getAllUsers() {

    	 User user = new User();
    	 
    	 user.setEmail("user@gmail.com");
    	 user.setLastname("lastname");
    	 user.setFirstname("firstname");
    	 user.setMobileNumber("1234567890");
    	 user.setCity("city");
    	 user.setDoorNo("12");
    	 user.setUsername("username");
         
         
         List<User> users = new ArrayList<>();
         users.add(user);
         
         when(userRepo.findAll()).thenReturn(users);

         assertEquals(users, userService.getAllUsers());

    }
    
    
    
   
    
    

}