package john.project.controllers;

import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.*;
import john.project.dao.PostgreSQLDAO;
import john.project.models.Order;

@Controller
@RequestMapping("/user")
public class ControllerUser {
	private final PostgreSQLDAO DAO;

    public ControllerUser(PostgreSQLDAO DAO) {
        this.DAO = DAO;
    }
    
    @GetMapping("/")
    public String helloPage() {
        return "user/hello";
    }
    
    @GetMapping("/about")
    public String aboutPage() {
        return "user/about";
    }
    
    @GetMapping("/contacts")
    public String contactsPage() {
        return "user/contacts";
    }
    
    @GetMapping("/orders")
    public String ordersPage(Model model) {
    	model.addAttribute("ordersList", DAO.getAllOrders());
        return "user/orders";
    }
    
    @GetMapping("/main")
    public String mainPage() {
        return "user/main";
    }
    
    @GetMapping("/exit")
    public String exitPage(HttpServletResponse response, @CookieValue(name = "auth_cookie", required = false) Cookie userCookie) {
    	if (userCookie != null) {
    		userCookie.setMaxAge(0);
    		response.addCookie(userCookie);
    	}

    	return "redirect:/hello";
    }
    
    @GetMapping("/setcookie")
    public String setCookie(HttpServletResponse response) {
    	Cookie cookie = new Cookie("auth_cookie", UUID.randomUUID().toString());
    	cookie.setMaxAge(3600*24*7);
    	response.addCookie(cookie);
        return "redirect:/user/";
    }
    
    @GetMapping("/new")
    public String newOrder(@ModelAttribute("order") Order order) {
        return "user/new";
    }

    @PostMapping()
    public String addOrder(@ModelAttribute("order") Order order, BindingResult res) {
    	if (res.hasErrors())
            return "user/new";
        DAO.addOrderByUser(order);
        return "redirect:/user/orders";
    }
}
