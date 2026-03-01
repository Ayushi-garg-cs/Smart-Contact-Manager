package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
//	@GetMapping("/test")
//	@ResponseBody
//	public String test() {
//		User user=new User();
//		user.setName("Ayushi garg");
//		user.setEmail("ayushigarg2346@gmail.com");
//		Contact contact=new Contact();
//		user.getContacts().add(contact);
//		userRepository.save(user);
//		return "Working";
//	}
	@RequestMapping("/")
	public String home(Model m) {
		m.addAttribute("title","Home-Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model m) {
		m.addAttribute("title","About-Smart Contact Manager");
		return "about";
	}
	@RequestMapping("/signin")
	public String signin(Model m) {
		m.addAttribute("user",new User());
		m.addAttribute("title","Register-Smart Contact Manager");
		return "signin";
	}
	//for registering user
	@PostMapping("/do-register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1, @RequestParam(value="agreement",defaultValue="false") boolean agreement,Model model,HttpSession session) {
		try {
			
			if(result1.hasErrors()) {
				System.out.println("ERROR"+ result1.toString());
				model.addAttribute("user",user);
				return "signin";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			
			System.out.println("Agreement"+ agreement);
			if(!agreement) {
				System.out.println("You have not agreed terms and conditions");
				throw new Exception("You have not agreed terms and conditions");
			}
			System.out.println("USER"+user);
			
			User result=this.userRepository.save(user);
			model.addAttribute("user",new User());
			session.setAttribute("message",new Message("Successfully registered!!","alert-success"));
			model.addAttribute("message", session.getAttribute("message"));
			session.removeAttribute("message");
			return "signin";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message",new Message("Something went wrong"+ e.getMessage(),"alert-danger"));
			model.addAttribute("message", session.getAttribute("message"));
			session.removeAttribute("message");
			return "signin";
		}
	}
	
}
