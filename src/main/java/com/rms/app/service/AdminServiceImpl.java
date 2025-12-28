package com.rms.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms.app.dao.MenuRepo;
import com.rms.app.dao.StaffRepo;
import com.rms.app.dao.UserRepo;
import com.rms.app.model.Menu;
import com.rms.app.model.Staff;
import com.rms.app.model.User;


@Service
public class AdminServiceImpl implements AdminService{

	
	@Autowired
	private MenuRepo menuRepo;
	
	@Autowired
	private StaffRepo staffRepo;

	@Override
	public List<Menu> getAllMenu() {
		// TODO Auto-generated method stub
		return menuRepo.findAll();
	}

	@Override
	public int saveMenu(Menu menu) {
		// TODO Auto-generated method stub
		Menu men = menuRepo.save(menu);
		if(men != null) {
			return 1;
		}
		else {
			return 0;
		}
		
		
	}

	@Override
	public int deleteMenu(Long id) {
		// TODO Auto-generated method stub
		menuRepo.deleteById(id);
		return 1;
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public Menu getMenuById(Long id) {
		// TODO Auto-generated method stub
		return menuRepo.getById(id);
	}

	@Override
	public int updateMenu(Menu menu) {
		// TODO Auto-generated method stub
		Menu men = menuRepo.save(menu);
		
		if(men != null) {
			return 1;
		}
		else {
			return 0;
		}
		
	}

	@Override
	public List<Staff> getAllStaff() {
		// TODO Auto-generated method stub
		return staffRepo.findAll();
	}

	@Override
	public int saveStaff(Staff staff) {
		// TODO Auto-generated method stub
		Staff staf = staffRepo.save(staff);
		if(staf != null) {
			return 1;
		}
		else {
			return 0;
		}
		
	}

	@Override
	public void deleteStaff(Long id) {
		// TODO Auto-generated method stub
		staffRepo.deleteById(id);
		
	}
	
	

	
	
	
	

}
