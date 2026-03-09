package com.smart.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

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
    public String processContact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file, Principal principal,RedirectAttributes redirectAttributes) throws IOException  {
    	
    	try {
	    	String name=principal.getName();
	    	User user=this.userRepository.findByEmail(name);
	    	
	    	if(file.isEmpty()) {
	    		//if file is empty then try our message
	    		System.out.println("File is empty");
	    	}else {
	    		//file the file to folder and update the name to contact
	    		contact.setImage(file.getOriginalFilename());
	    		File saveFile=new ClassPathResource("static/image").getFile();
	    		Path path=Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
	    		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	    		
	    		System.out.println("Image is uploaded");
	    	}
	    	contact.setUser(user);//bidirectional mapping
	    	user.getContacts().add(contact);
	    	this.userRepository.save(user);
	    	//message success
	    	 redirectAttributes.addFlashAttribute(
		        "message",
		        new Message("Contact added successfully","success")
		     );
    	}catch(Exception e) {
    		System.out.println("ERROR:"+e.getMessage());
    		e.printStackTrace();
    		//message error
    		 redirectAttributes.addFlashAttribute(
		        "message",
		        new Message("Something went worng!!Try Again","danger")
		     );
    	}
    	return "redirect:/user/add-contact";//redirect lgate time url return krte h naa ki html (view page) page
    }
}

   
