package john.project.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import john.project.dao.ClientDAO;
import john.project.models.Client;

@Controller
public class AuthenticationController {

    @Autowired
    @Qualifier("proxyClientDAO")
    private ClientDAO clientDAO;
   
    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("auth_cookie")) {
                    String cookieValue = cookie.getValue();
                    if (cookieValue.startsWith("admin")) {
                        return "redirect:/admin/";
                    } else if (cookieValue.startsWith("user")) {
                        return "redirect:/user/";
                    }
                }
            }
        }
        return "start/login";
    }
    
    @PostMapping("/login")
    public String authenticate(@RequestParam String username, @RequestParam String password,
    		@RequestParam(value = "check", required = false) boolean check,
    		HttpSession session, Model model) {
    	
        if (username.equals("ADMIN") && password.equals("ADMIN")) {
            session.setAttribute("adminLoggedIn", true);
			return check? "redirect:/setadmincookie" : "redirect:/admin";
        } else if (clientDAO.authenticate(username, password)) {
            session.setAttribute("userLoggedIn", true);
            session.setAttribute("current_client", clientDAO.getClient(username, password));
			return check? "redirect:/setusercookie" : "redirect:/user";
        } else {
			model.addAttribute("error", "Invalid username or password");
			return "start/login";
		}
        
    }
    
    @GetMapping("/setadmincookie")
    public String setAdminCookie(HttpServletResponse response) {
    	Cookie cookie = new Cookie("auth_cookie", "admin_" +  UUID.randomUUID().toString());
    	cookie.setMaxAge(3600*24*7);
    	response.addCookie(cookie);
        return "redirect:/admin/";
    }
    
    @GetMapping("/setusercookie")
    public String setUserCookie(HttpServletResponse response) {
    	Cookie cookie = new Cookie("auth_cookie", "user_" +  UUID.randomUUID().toString());
    	cookie.setMaxAge(3600*24*7);
    	response.addCookie(cookie);
        return "redirect:/user/";
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "start/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password,
    		@RequestParam(value = "check", required = false) boolean check,
    		HttpSession session, Model model) {
        if (clientDAO.getClient(username, password) != null) {
            model.addAttribute("error", "Username already exists");
            return "start/register";
        }
        
        Client client = new Client.ClientBuilder().email(username).pass(password).build();
        clientDAO.registerClient(client);
        session.setAttribute("userLoggedIn", true);
        session.setAttribute("current_client", clientDAO.getClient(username, password));
        return check? "redirect:/setusercookie" : "redirect:/user/";
    }
}
