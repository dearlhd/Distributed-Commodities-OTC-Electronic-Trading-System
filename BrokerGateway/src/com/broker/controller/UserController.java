package com.broker.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.broker.entity.User;
import com.broker.service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Resource
	private IUserService userService;
	
	@RequestMapping("/showUser")
	public String toIndex(HttpServletRequest request,Model model){
		//int userId = Integer.parseInt(request.getParameter("id"));
		int userId = 1;
		User user = this.userService.getUserById(userId);
		System.out.println("Username: " + user.getUserName());
		System.out.println("age: " + user.getAge());
		
		model.addAttribute("hello", "luo");
		model.addAttribute("user", user.getUserName());
		return "showUser";
	}
}
