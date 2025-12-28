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
import com.rms.app.dao.TablesRepo;
import com.rms.app.model.Menu;
import com.rms.app.model.Order;
import com.rms.app.model.Tables;

import java.util.ArrayList;
import java.util.List;

//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class StaffServiceTest {

    @InjectMocks
    private StaffServiceImpl staffService;

    @Mock
    private TablesRepo tableRepo;

    @Mock
    private OrderRepo orderRepo;

    
    @Test
    public void getCustomerReservations() {

        Tables table = new Tables();
        
        table.setCustomerEmail("customer@gmail.com");
        table.setDatetime("20-11-2023");
        table.setName("t1");
        table.setNoOfPersons("3");
        
        
        List<Tables> tables = new ArrayList<>();
        tables.add(table);
        
        when(tableRepo.findAll()).thenReturn(tables);

        assertEquals(tables, staffService.getCustomerReservations());

    }
    
    @Test
    public void getCustomerOrders() {

    	 Order order = new Order();
         
         order.setEmail("customer@gmail.com");
         order.setName("Veg Fired Rice");
         order.setPrice("123");
         order.setStatus("confirmed");
         order.setTotalCost("123");
         order.setQuantity("1");
         
         
         
         List<Order> orders = new ArrayList<>();
         
         when(orderRepo.findAll()).thenReturn(orders);

         assertEquals(orders, staffService.getAllOrders());

    }
    
    
    
   
    
    

}