package john.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerOrder {
	
    @GetMapping("/hello")
    public String helloPage() {
        return "start/hello";
    }
    
    @GetMapping("/about")
    public String aboutPage() {
        return "start/about";
    }
    
    @GetMapping("/contacts")
    public String contactsPage() {
        return "start/contacts";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "start/login";
    }
    
}