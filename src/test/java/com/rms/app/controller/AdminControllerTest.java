package com.rms.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
import com.rms.app.dao.StaffRepo;
import com.rms.app.model.Menu;
import com.rms.app.model.Staff;
import com.rms.app.service.AdminServiceImpl;

import java.util.ArrayList;
import java.util.List;

//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock
    private MenuRepo menuRepo;

    @Mock
    private StaffRepo staffRepo;

    
    @Test
    public void getMenuPage() {

        Menu menu = new Menu();
        
        menu.setCategory("Starters");
        menu.setCuisine("indian");
        menu.setDescription("nicely chopped cabbage with deeply fried manchurian");
        menu.setName("Veg Manchurian");
        menu.setPrice("200");
        menu.setType("type");
        menu.setVegOrNonVeg("Veg");
        
        
        List<Menu> menus = new ArrayList<>();
        menus.add(menu);
        
        when(menuRepo.findAll()).thenReturn(menus);

        assertEquals(menus, adminService.getAllMenu());

    }
    
    @Test
    public void checkMenuList() {

    	 Menu menu = new Menu();
         
         menu.setCategory("Starters");
         menu.setCuisine("indian");
         menu.setDescription("nicely chopped cabbage with deeply fried manchurian");
         menu.setName("Veg Manchurian");
         menu.setPrice("200");
         menu.setType("type");
         menu.setVegOrNonVeg("Veg");
         
         
         List<Menu> menus = new ArrayList<>();
         
         when(menuRepo.findAll()).thenReturn(menus);

         assertEquals(0, adminService.getAllMenu().size());

    }
    
    @Test
    public void saveMenu() {

        Menu menu = new Menu();
        
        menu.setCategory("Starters");
        menu.setCuisine("indian");
        menu.setDescription("nicely chopped cabbage with deeply fried manchurian");
        menu.setName("Veg Manchurian");
        menu.setPrice("200");
        menu.setType("type");
        menu.setVegOrNonVeg("Veg");
        
        
       
        
        when(menuRepo.save(menu)).thenReturn(menu);

        assertEquals(1, adminService.saveMenu(menu));

    }
    
   
    
    @Test
    public void deleteMMenu() {

        Menu menu = new Menu();
        
        menu.setCategory("Starters");
        menu.setCuisine("indian");
        menu.setDescription("nicely chopped cabbage with deeply fried manchurian");
        menu.setName("Veg Manchurian");
        menu.setPrice("200");
        menu.setType("type");
        menu.setVegOrNonVeg("Veg");
        menu.setId(1L);
        
        menuRepo.save(menu);
       
        menuRepo.deleteById(1L);

        assertEquals(0, adminService.getAllMenu().size());

    }
    
    
    
    @Test
    public void editMenu() {

        Menu menu = new Menu();
        
        menu.setCategory("Starters");
        menu.setCuisine("indian");
        menu.setDescription("nicely chopped cabbage with deeply fried manchurian");
        menu.setName("Veg Manchurian");
        menu.setPrice("200");
        menu.setType("type");
        menu.setVegOrNonVeg("Veg");
        menu.setId(1L);
        
        when(menuRepo.save(menu)).thenReturn(menu);

        assertEquals(1, adminService.updateMenu(menu));

    }
    
    @Test
    public void getStaffPage() {

        Staff staff = new Staff();
        
        staff.setEmail("staff@gmail.com");
        staff.setDoorNo("123");
        staff.setFirstname("firstname");
        staff.setLastname("lastname");
        staff.setMobileNumber("12345");
        
        List<Staff> staffs = new ArrayList<>();
        staffs.add(staff);
        
        when(staffRepo.findAll()).thenReturn(staffs);

        assertEquals(staffs, adminService.getAllStaff());

    }
    

    @Test
    public void saveStaff() {

    	Staff staff = new Staff();
        
        staff.setEmail("staff@gmail.com");
        staff.setDoorNo("123");
        staff.setFirstname("firstname");
        staff.setLastname("lastname");
        staff.setMobileNumber("12345");
        
        
       
        
        when(staffRepo.save(staff)).thenReturn(staff);

        assertEquals(1, adminService.saveStaff(staff));

    }
    
    @Test
    public void deleteStaff() {

    	Staff staff = new Staff();
        
        staff.setEmail("staff@gmail.com");
        staff.setDoorNo("123");
        staff.setFirstname("firstname");
        staff.setLastname("lastname");
        staff.setMobileNumber("12345");
        staff.setId(1L);
        
        staffRepo.save(staff);
       
        staffRepo.deleteById(1L);

        assertEquals(0, adminService.getAllStaff().size());

    }
    
   
    
    

}