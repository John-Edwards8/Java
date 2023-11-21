package john.project.controllers;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import john.project.dao.PostgreSQLDAO;
import john.project.models.Client;
import john.project.models.Order;

@Controller
@RequestMapping("/admin")
public class ControllerAdmin {
	private final PostgreSQLDAO DAO;

    public ControllerAdmin(PostgreSQLDAO DAO) {
        this.DAO = DAO;
    }
    
    @GetMapping("/")
    public String helloPage() {
        return "admin/hello";
    }
    
    @GetMapping("/clients")
    public String clientsPage(Model model) {
    	model.addAttribute("clientsList", DAO.getAllClients());
        return "admin/clients";
    }
    
    @GetMapping("/orders")
    public String ordersPage(Model model) {
    	model.addAttribute("ordersList", DAO.getAllOrderLists());
        return "admin/orders";
    }
    
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
        return "redirect:/admin/";
    }
    
    @GetMapping("/new")
    public String newOrder(@ModelAttribute("order") Order order) {
        return "admin/new";
    }
    
    @GetMapping("/newClient")
    public String newClient(@ModelAttribute("client") Client client) {
        return "admin/newClient";
    }

    @PostMapping("/order")
    public String addOrder(@ModelAttribute("order") Order order, BindingResult res) {
    	if (res.hasErrors())
            return "admin/new";
        DAO.addOrderByAdmin(order);
        return "redirect:/admin/orders";
    }
    
    @PostMapping("/client")
    public String addClient(@ModelAttribute("client") Client client, BindingResult res) {
    	if (res.hasErrors())
            return "admin/newClient";
        DAO.addClient(client);
        return "redirect:/admin/clients";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("order", DAO.getOrder(id));
        return "admin/update";
    }
    
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("order") @Valid Order order, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "admin/update";

        DAO.updateOrder(id, order);
        return "redirect:/admin/orders";
    }
    
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
    	DAO.deleteOrder(id);
        return "redirect:/admin/orders";
    }
}
