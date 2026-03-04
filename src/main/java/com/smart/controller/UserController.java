package com.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal){
		String username=principal.getName();
		System.out.println("USERNAME:"+username);
		User user=userRepository.getUserByEmail(username);
		System.out.println("USER"+user.toString());
		return "normal/user_dashboard";
		
	}
}
