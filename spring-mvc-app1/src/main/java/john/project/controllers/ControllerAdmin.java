package john.project.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import john.project.dao.ClientDAO;
import john.project.dao.OrderDAO;
import john.project.dao.OrderListDAO;
import john.project.models.Client;
import john.project.models.ClientForm;
import john.project.models.Order;
import john.project.models.OrderForm;

@Controller
@RequestMapping("/admin")
public class ControllerAdmin {
    private final ClientDAO clientDAO;
    private final OrderDAO orderDAO;
    private final OrderListDAO orderListDAO;
    private final CacheManager cache = new CacheManager();
	
    public ControllerAdmin(@Qualifier("realClientDAO")ClientDAO clientDAO,
    		OrderDAO orderDAO,@Qualifier("realOrderListDAO") OrderListDAO orderListDAO) {
        this.clientDAO = clientDAO;
        this.orderDAO = orderDAO;
        this.orderListDAO = orderListDAO;
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
    	model.addAttribute("ordersList", orderListDAO.getAllOrderLists());
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
    	cache.save("order",order);
        return "redirect:/admin/addClient";
    }
    @GetMapping("/addClient")
    public String addClient(Model model) {
    	model.addAttribute("clients", clientDAO.getAllClients());
        return "admin/addCli";
    }
    @PostMapping("/orderlist")
    public String addOrderList(@RequestParam("clientId") int clientId) {
    	OrderForm form = cache.get("order");
    	cache.deleteOrder("order");
    	Order order = new Order
    			.OrderBuilder()
    			.id(form.getId())
    			.date(form.getDate())
    			.status(form.getStatus())
    			.build();
    	orderListDAO.addOrderList(order,clientId);
        return "redirect:/admin/orders";
    }
    
    //Update form
    @GetMapping("/{orderID}/{clientID}/editOrder")
    public String editOrder(Model model, @PathVariable("orderID") int id, @PathVariable("clientID") int idCli) {
        model.addAttribute("order", orderDAO.getOrder(id));
        Client client = clientDAO.getClient(idCli);
        ClientForm form = new ClientForm();
        form.setId(client.getId());
        form.setName(client.getName());
        form.setSurname(client.getSurname());
        form.setPatronymic(client.getPatronymic());
        form.setPhoneNumber(client.getPhoneNumber());
        form.setEmail(client.getEmail());
        form.setPass(client.getPass());
        cache.save("lastCli", form);
        return "admin/update";
    }
    //Update
    @PostMapping("/orderUPD/{id}")
    public String updateOrder(@ModelAttribute("order") @Valid OrderForm order, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/update";

        cache.save("updOrder",order);
        return "redirect:/admin/editOrderCli";
    }
    @GetMapping("/editOrderCli")
    public String editOrder(Model model) {
    	model.addAttribute("clients", clientDAO.getAllClients());
    	model.addAttribute("last", cache.getLast("lastCli"));
    	cache.deleteClient("lastCli");
        return "admin/updateOrderCli";
    }
    @PostMapping("/orderUPDCli")
    public String updateOrder(@RequestParam("clientId") int clientId) {
    	OrderForm form = cache.get("updOrder");
    	cache.deleteOrder("updOrder");
    	Order order = new Order
    			.OrderBuilder()
    			.id(form.getId())
    			.date(form.getDate())
    			.status(form.getStatus())
    			.build();
    	orderListDAO.updateOrderList(order, clientId);
        return "redirect:/admin/orders";
    }
    //Delete
    @PostMapping("/orderDEL/{orderID}/{clientID}")
    public String deleteOrder(@PathVariable("orderID") int orderID, @PathVariable("clientID") int clientID) {
    	orderListDAO.deleteOrderList(orderID, clientID);
        return "redirect:/admin/orders";
    }
    
    /*Clients page
     *CRUD-system */
    //Read-all
    @GetMapping("/clients")
    public String clientsPage(Model model) {
    	model.addAttribute("clientsList", clientDAO.getAllClients());
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
    			.pass(form.getPass())
    			.build();
    	
    	clientDAO.addClient(client);
        return "redirect:/admin/clients";
    }
    //Update form
    @GetMapping("/{id}/editClient")
    public String editClient(Model model, @PathVariable("id") int id) {
        model.addAttribute("client", clientDAO.getClient(id));
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
    			.pass(form.getPass())
    			.build();
        
    	clientDAO.updateClient(id, client);
        return "redirect:/admin/clients";
    }
    //Delete
    @PostMapping("/clientDEL/{id}")
    public String deleteClient(@PathVariable("id") int id) {
    	clientDAO.deleteClient(id);
        return "redirect:/admin/clients";
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
