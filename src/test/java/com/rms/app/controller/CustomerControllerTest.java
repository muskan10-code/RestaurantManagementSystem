package com.rms.app.controller;

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

import com.rms.app.dao.CartRepo;
import com.rms.app.dao.MenuRepo;
import com.rms.app.dao.StaffRepo;
import com.rms.app.dao.UserRepo;
import com.rms.app.model.Cart;
import com.rms.app.model.Menu;
import com.rms.app.model.User;
import com.rms.app.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CartRepo cartRepo;

    
    @Test
    public void cart() {

        Cart cart = new Cart();
        
        cart.setCategory("Starters");
        cart.setCuisine("indian");
        cart.setDescription("nicely chopped cabbage with deeply fried manchurian");
        cart.setName("Veg Manchurian");
        cart.setPrice("200");
        cart.setType("type");
        cart.setVegOrNonVeg("Veg");
        cart.setCustomerEmail("customer@gmail.com");
        
        
        List<Cart> carts = new ArrayList<>();
        carts.add(cart);
        
        when(cartRepo.findAll()).thenReturn(carts);

        assertEquals(carts, userService.getUserCart("customer@gmail.com"));

    }
    
    @Test
    public void cartNotFound() {

        Cart cart = new Cart();
        
        cart.setCategory("Starters");
        cart.setCuisine("indian");
        cart.setDescription("nicely chopped cabbage with deeply fried manchurian");
        cart.setName("Veg Manchurian");
        cart.setPrice("200");
        cart.setType("type");
        cart.setVegOrNonVeg("Veg");
        cart.setCustomerEmail("customer@gmail.com");
        
        
        List<Cart> carts = new ArrayList<>();
        carts.add(cart);
        
        when(cartRepo.findAll()).thenReturn(carts);

        assertEquals(0, userService.getUserCart("custmer@gmail.com").size());

    }
    
    
    
   
    
    

}