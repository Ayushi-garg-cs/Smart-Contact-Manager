package com.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @ModelAttribute
    public void addCommonData(Model m,Principal principal) {
    	if(principal!=null){
            String username = principal.getName();
            User user = userRepository.findByEmail(username);
            m.addAttribute("user", user);
        }
    }
    
    @RequestMapping("/index")
    public String dashboard(Model model){
    	model.addAttribute("title","User Dashboard");  
        return "normal/user_dashboard";
    }
    
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {
    	model.addAttribute("title","Add Contact");
    	model.addAttribute("contact",new Contact());
    	return "normal/add_contact_form";
    }
    
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact,Principal principal) {
    	String name=principal.getName();
    	User user=this.userRepository.findByEmail(name);
    	contact.setUser(user);//bidirectional mapping
    	user.getContacts().add(contact);
    	this.userRepository.save(user);
    	return "normal/add_contact_form";
    }
}

   
