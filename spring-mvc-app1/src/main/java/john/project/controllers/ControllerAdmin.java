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
import john.project.models.ClientForm;
import john.project.models.Order;
import john.project.models.OrderForm;

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
    
    /*Orders page
     *CRUD-system */
    //Read-all
    @GetMapping("/orders")
    public String ordersPage(Model model) {
    	model.addAttribute("ordersList", DAO.getAllOrderLists());
        return "admin/orders";
    }
    //Create form
    @GetMapping("/new")
    public String newOrder(@ModelAttribute("order") OrderForm order) {
        return "admin/new";
    }
    //Create
    @PostMapping("/order")
    public String addOrder(@ModelAttribute("order") OrderForm order, BindingResult res) {
    	if (res.hasErrors())
            return "admin/new";
        DAO.save("order",order);
        return "redirect:/admin/addClient";
    }
    @GetMapping("/addClient")
    public String addClient(Model model) {
    	model.addAttribute("clients", DAO.getAllClients());
        return "admin/addCli";
    }
    @PostMapping("/orderlist")
    public String addOrderList(@RequestParam("clientId") int clientId) {
    	OrderForm form = DAO.get("order");
    	DAO.deleteOrder("order");
    	Order order = new Order
    			.OrderBuilder()
    			.id(form.getId())
    			.date(form.getDate())
    			.status(form.getStatus())
    			.build();
        DAO.addOrderList(order,clientId);
        return "redirect:/admin/orders";
    }
    
    //Update form
    @GetMapping("/{orderID}/{clientID}/editOrder")
    public String editOrder(Model model, @PathVariable("orderID") int id, @PathVariable("clientID") int idCli) {
        model.addAttribute("order", DAO.getOrder(id));
        Client client = DAO.getClient(idCli);
        ClientForm form = new ClientForm();
        form.setId(client.getId());
        form.setName(client.getName());
        form.setSurname(client.getSurname());
        form.setPatronymic(client.getPatronymic());
        form.setPhoneNumber(client.getPhoneNumber());
        form.setEmail(client.getEmail());
        DAO.save("lastCli", form);
        return "admin/update";
    }
    //Update
    @PostMapping("/orderUPD/{id}")
    public String updateOrder(@ModelAttribute("order") @Valid OrderForm order, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/update";

        DAO.save("updOrder",order);
        return "redirect:/admin/editOrderCli";
    }
    @GetMapping("/editOrderCli")
    public String editOrder(Model model) {
    	model.addAttribute("clients", DAO.getAllClients());
    	model.addAttribute("last", DAO.getLast("lastCli"));
    	DAO.deleteClient("lastCli");
        return "admin/updateOrderCli";
    }
    @PostMapping("/orderUPDCli")
    public String updateOrder(@RequestParam("clientId") int clientId) {
    	OrderForm form = DAO.get("updOrder");
    	DAO.deleteOrder("updOrder");
    	Order order = new Order
    			.OrderBuilder()
    			.id(form.getId())
    			.date(form.getDate())
    			.status(form.getStatus())
    			.build();
        DAO.updateOrderList(order, clientId);
        return "redirect:/admin/orders";
    }
    //Delete
    @PostMapping("/orderDEL/{orderID}/{clientID}")
    public String deleteOrder(@PathVariable("orderID") int orderID, @PathVariable("clientID") int clientID) {
    	DAO.deleteOrderList(orderID, clientID);
        return "redirect:/admin/orders";
    }
    
    /*Clients page
     *CRUD-system */
    //Read-all
    @GetMapping("/clients")
    public String clientsPage(Model model) {
    	model.addAttribute("clientsList", DAO.getAllClients());
        return "admin/clients";
    }
    //Create form
    @GetMapping("/newClient")
    public String newClient(@ModelAttribute("client") ClientForm form) {
        return "admin/newClient";
    }
    //Create
    @PostMapping("/client")
    public String addClient(@ModelAttribute("client") ClientForm form, BindingResult res) {
    	if (res.hasErrors())
            return "admin/newClient";
    	
    	Client client = new Client
    			.ClientBuilder()
    			.id(form.getId())
    			.name(form.getName())
    			.surname(form.getSurname())
    			.patronymic(form.getPatronymic())
    			.phoneNumber(form.getPhoneNumber())
    			.email(form.getEmail())
    			.build();
    	
        DAO.addClient(client);
        return "redirect:/admin/clients";
    }
    //Update form
    @GetMapping("/{id}/editClient")
    public String editClient(Model model, @PathVariable("id") int id) {
        model.addAttribute("client", DAO.getClient(id));
        return "admin/updateCli";
    }
    //Update
    @PostMapping("/clientUPD/{id}")
    public String updateClient(@ModelAttribute("client") @Valid ClientForm form, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "admin/updateCli";
        
    	Client client = new Client
    			.ClientBuilder()
    			.id(form.getId())
    			.name(form.getName())
    			.surname(form.getSurname())
    			.patronymic(form.getPatronymic())
    			.phoneNumber(form.getPhoneNumber())
    			.email(form.getEmail())
    			.build();
        
        DAO.updateClient(id, client);
        return "redirect:/admin/clients";
    }
    //Delete
    @PostMapping("/clientDEL/{id}")
    public String deleteClient(@PathVariable("id") int id) {
    	DAO.deleteClient(id);
        return "redirect:/admin/clients";
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
        return "redirect:/admin/";
    }
}
