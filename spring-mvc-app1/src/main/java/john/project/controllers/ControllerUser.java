package john.project.controllers;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import john.project.dao.OrderListDAO;
import john.project.models.Client;
import john.project.models.Order;
import john.project.models.OrderForm;

@Controller
@RequestMapping("/user")
public class ControllerUser {
    private final OrderListDAO orderListDAO;
	
    public ControllerUser(OrderListDAO orderListDAO) {
        this.orderListDAO = orderListDAO;
    }
    
    @GetMapping("/")
    public String helloPage() {
        return "user/hello";
    }
    
    /*Orders page
     *CRUD-system */
    //Read-all
    @GetMapping("/orders")
    public String ordersPage(HttpSession session, Model model) {
    	Client client = (Client) session.getAttribute("current_client");
    	model.addAttribute("ordersList", orderListDAO.getOrderList(client.getId()));
        return "user/orders";
    }
    //Create form
    @GetMapping("/new")
    public String newOrder(@ModelAttribute("order") OrderForm form) {
        return "user/new";
    }
    //Create
    @PostMapping("/order")
    public String addOrder(@ModelAttribute("order") OrderForm form, HttpSession session, BindingResult res) {
    	if (res.hasErrors())
            return "user/new";
    	Order order = new Order
    			.OrderBuilder()
    			.id(form.getId())
    			.date(form.getDate())
    			.status("Pending")
    			.build();
    	Client client = (Client) session.getAttribute("current_client");
    	orderListDAO.addOrderList(order,client.getId());
        return "redirect:/user/orders";
    }
    
    //Cookie makers
    @GetMapping("/exit")
    public String exitPage(HttpServletResponse response, @CookieValue(name = "auth_cookie", required = false) Cookie adminCookie) {
    	if (adminCookie != null) {
    		adminCookie.setMaxAge(0);
    		response.addCookie(adminCookie);
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
}
