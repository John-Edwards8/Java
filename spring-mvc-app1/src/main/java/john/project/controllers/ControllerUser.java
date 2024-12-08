package john.project.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
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
	Client client;
    private final OrderListDAO orderListDAO;
    
    public ControllerUser(@Qualifier("proxyOrderListDAO") OrderListDAO targetDAO) {
        this.orderListDAO = targetDAO;
    }
    
    @GetMapping("/")
    public String helloPage(HttpSession session) {
    	this.client = (Client) session.getAttribute("current_client");
        if (this.client == null) {
            throw new IllegalStateException("No current_client found in session.");
        }
        return "user/hello";
    }
    
    /*Orders page
     *CRUD-system */
    //Read-all
    @GetMapping("/orders")
    public String ordersPage(Model model) {
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
    public String addOrder(@ModelAttribute("order") OrderForm form, BindingResult res) {
    	if (res.hasErrors())
            return "user/new";
    	Order order = new Order
    			.OrderBuilder()
    			.id(form.getId())
    			.date(form.getDate())
    			.status("Pending")
    			.build();
    	orderListDAO.addOrderList(order,client.getId());
        return "redirect:/user/orders";
    }
    
    @GetMapping("/exit")
    public String exitPage(HttpServletResponse response, @CookieValue(name = "auth_cookie", required = false) Cookie cookie) {
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            cookie.setDomain("localhost");
            response.addCookie(cookie);
        }
        return "redirect:/";
    }
}
