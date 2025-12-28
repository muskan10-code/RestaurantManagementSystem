package com.rms.app.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.rms.app.model.Menu;
import com.rms.app.model.Staff;
import com.rms.app.model.User;
import com.rms.app.service.AdminService;
import com.rms.app.service.UserService;


@Controller
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	

	@GetMapping("/admin")
	public String getAdminWelcomePage(@ModelAttribute("user") User user, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);

		return "admin/welcomeadmin";
	}
	
	@GetMapping("/menu")
	public String getMenuPage(@ModelAttribute("menu") Menu menu, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		List<Menu> menuList = adminService.getAllMenu();
		
        model.addAttribute("sessionMessages", messages);
        model.addAttribute("menus", menuList);

		return "admin/menu";
	}
	
	@PostMapping("/saveMenu")
	public String saveMenu(@ModelAttribute("menu") Menu menu, Model model, HttpSession session, @RequestParam("image") MultipartFile itemImage)
	{
			try {
				menu.setPhoto(Base64.getEncoder().encodeToString(itemImage.getBytes()));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			adminService.saveMenu(menu);
		
			return "redirect:/menu";
		
	}
	
	@GetMapping("/editMenu/{id}")
	public String editMenu(Model model, HttpSession session, @PathVariable(name="id") Long id) {
		
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		Menu menu = adminService.getMenuById(id);
		model.addAttribute("menu", menu);
		
        model.addAttribute("sessionMessages", messages);
		
		return "admin/updatemenu";
	}
	
	@PostMapping("/updateMenu")
	public String updateMenu(@ModelAttribute("menu") Menu menu, Model model, HttpSession session, @RequestParam("image") MultipartFile itemImage)
	{
		System.out.println("menu updated");
		
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
        model.addAttribute("sessionMessages", messages);
       
        
        if(!itemImage.getOriginalFilename().isEmpty()) {
        	try {
        		
				menu.setPhoto(Base64.getEncoder().encodeToString(itemImage.getBytes()));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
        }
        else {
        	Menu m = adminService.getMenuById(menu.getId());
        	menu.setPhoto(m.getPhoto());
        }
		
		adminService.updateMenu(menu);
		
			return "redirect:/menu";
		
	}
	
	@PostMapping("/deleteMenu/{id}")
	public String deleteMMenu(@PathVariable(name="id") Long id)
	{
		adminService.deleteMenu(id);
		
		return "redirect:/menu";
	}
	
	@GetMapping("/staffs")
	public String getStaffPage(@ModelAttribute("staff") Staff staff, Model model, HttpSession session)
	{
		@SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

		if(messages == null) {
			model.addAttribute("errormsg", "Session Expired. Please Login Again");
			return "home/error";
		}
		List<Staff> staffList = adminService.getAllStaff();
		
        model.addAttribute("sessionMessages", messages);
        model.addAttribute("staffs", staffList);

		return "admin/staff";
	}
	
	@PostMapping("/saveStaff")
	public String saveStaff(@ModelAttribute("staff") Staff staff, Model model, HttpSession session)
	{
			adminService.saveStaff(staff);
		
			return "redirect:/staffs";
		
	}
	
	
	
	@PostMapping("/deleteStaff/{id}")
	public String deleteStaff(@PathVariable(name="id") Long id)
	{
		adminService.deleteStaff(id);
		
		return "redirect:/staffs";
	}
}
