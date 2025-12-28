package com.rms.app.service;

import java.util.List;

import com.rms.app.model.Menu;
import com.rms.app.model.Staff;
import com.rms.app.model.User;



public interface AdminService {

	List<Menu> getAllMenu();

	int saveMenu(Menu menu);

	int deleteMenu(Long id);

	Menu getMenuById(Long id);

	int updateMenu(Menu menu);

	List<Staff> getAllStaff();

	int saveStaff(Staff staff);

	void deleteStaff(Long id);

	



}
