package john.project.frontend_service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ClientServiceClient clientServiceClient;
    private final OrderServiceClient orderServiceClient;
    private final OrderHistory orderHistory;

    public AdminController(ClientServiceClient clientServiceClient, OrderServiceClient orderServiceClient,
    		OrderHistory orderHistory) {
        this.clientServiceClient = clientServiceClient;
        this.orderServiceClient = orderServiceClient;
        this.orderHistory = orderHistory;
    }
    
    @GetMapping
    public String helloPage() {
        return "admin/hello";
    }
    
    /*Orders page
     *CRUD-system */
    //Read-all
    @GetMapping("/orders")
    public String ordersPage(Model model) {
        List<OrderResponseDto> orders = orderServiceClient.getAllOrders();
        
        List<ClientOrderResponseDto> result = new ArrayList<>();
        for (OrderResponseDto order : orders) {
            ClientResponseDto client = clientServiceClient.getClientById(order.getClientId());
            ClientOrderResponseDto orderWithClient = new ClientOrderResponseDto(order, client);
            result.add(orderWithClient);
        }
    	
    	model.addAttribute("orders", result);
        return "admin/orders";
    }
    //Create form
    @GetMapping("/new-order")
    public String newOrder(@ModelAttribute("order") OrderResponseDto order) {
    	OrderResponseDto previousOrder = orderHistory.restoreState();

        if (previousOrder != null) {
        	order.setOrderDate(previousOrder.getOrderDate());
            order.setStatus(previousOrder.getStatus());
        }
        
        return "admin/new";
    }
    //Create
    @PostMapping("/order")
    public String addOrder(@ModelAttribute("order") OrderResponseDto order, BindingResult res) {
    	if (res.hasErrors()) return "admin/new";
    	
    	orderHistory.saveState(order);
    	
        return "redirect:/admin/addClient";
    }
    
    @GetMapping("/addClient")
    public String addClient(Model model) {
    	List<ClientResponseDto> clients = clientServiceClient.getAllClients();
    	List<ClientResponseDto> filteredClients = clients.stream()
                .filter(client -> client.getRole() != 1)
                .collect(Collectors.toList());

        model.addAttribute("clients", filteredClients);
        return "admin/addCli";
    }
    
    @PostMapping("/new-orderlist")
    public String addOrderList(@RequestParam("clientId") Long clientId,
    		@RequestParam(name = "action", required = false) String action) {
    	OrderResponseDto form = orderHistory.restoreState();
    	
    	if ("cancel".equals(action)) {
    		orderHistory.saveState(form);
            return "redirect:/admin/new-order";
        }
    	
    	orderServiceClient.addOrderByClient(form, clientId);
        return "redirect:/admin/orders";
    }
    
    //Update form
    @GetMapping("/edit-order/{orderID}/{clientID}")
    public String editOrder(Model model, @PathVariable("orderID") Long id, @PathVariable("clientID") Long idCli) {
        OrderResponseDto order = orderServiceClient.getOrderById(id);
        ClientResponseDto last = clientServiceClient.getClientById(idCli);
        List<ClientResponseDto> clients = clientServiceClient.getAllClients();
        List<ClientResponseDto> filteredClients = clients.stream()
                .filter(client -> client.getRole() != 1)
                .collect(Collectors.toList());
        
        model.addAttribute("order", order);
        model.addAttribute("last", last);
        model.addAttribute("clients", filteredClients);
        return "admin/update";
    	
    }
    //Update
    @PostMapping("/update-order/{id}")
    public String updateOrder(@PathVariable("id") Long orderId,
				            @RequestParam("clientId") Long clientId,
				            @ModelAttribute OrderResponseDto order) {

    	
    	orderServiceClient.updateOrder(clientId, order);
        return "redirect:/admin/orders";
    }

    //Delete
    @PostMapping("/delete-order/{orderID}/{clientID}")
    public String deleteOrder(@PathVariable("orderID") Long orderID, @PathVariable("clientID") Long clientID) {
    	orderServiceClient.deleteOrder(orderID, clientID);
        return "redirect:/admin/orders";
    }
    
        

    /*Clients page
     *CRUD-system */
    //Read-all
    @GetMapping("/clients")
    public String clientsPage(Model model, @RequestParam(value = "message", required = false) String message) {
    	if (message != null && !message.isEmpty()) {
            model.addAttribute("message", message);
        }
    	List<ClientResponseDto> clients = clientServiceClient.getAllClients();
    	List<ClientResponseDto> filteredClients = clients.stream()
                .filter(client -> client.getRole() != 1)
                .collect(Collectors.toList());

        model.addAttribute("clients", filteredClients);

        return "admin/clients";
    }
    
    //Create form
    @GetMapping("/new-client")
    public String newClient(@ModelAttribute("client") ClientResponseDto form) {
        return "admin/newClient";
    }
    //Create
    @PostMapping("/client")
    public String addClient(@ModelAttribute("client") ClientResponseDto form, BindingResult res,
    		RedirectAttributes redirectAttributes) {
    	if (res.hasErrors())
            return "admin/newClient";
    	
    	clientServiceClient.createClient(form);
    	redirectAttributes.addAttribute("message", "Client successfully added: " + form.getName());
        return "redirect:/admin/clients";
    }
    
    //Update form
    @GetMapping("/edit-client/{id}")
    public String editClient(Model model, @PathVariable("id") Long id) {
    	ClientResponseDto client = clientServiceClient.getClientById(id);
        model.addAttribute("client", client);
        return "admin/updateCli";
    }
    
    //Update
    @PostMapping("/update-client/{id}")
    public String updateClient(@ModelAttribute("client") @Valid ClientResponseDto form, BindingResult bindingResult,
                         @PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors())
            return "admin/updateCli";
        
        clientServiceClient.updateClient(id, form);
    	redirectAttributes.addAttribute("message", "Client successfully updated: " + form.getName());
        return "redirect:/admin/clients";
    }
    
    //Delete
    @PostMapping("/delete-client/{id}")
    public String deleteClient(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    	clientServiceClient.deleteClient(id);
    	redirectAttributes.addAttribute("message", "Client successfully deleted with ID: " + id);
        return "redirect:/admin/clients";
    }

    @GetMapping("/client/{id}")
    public String showClientDetails(@PathVariable Long id, Model model) {
        ClientResponseDto client = clientServiceClient.getClientById(id);

        List<OrderResponseDto> orders = orderServiceClient.getOrdersByClientId(id);

        model.addAttribute("client", client);
        model.addAttribute("orders", orders);

        return "client-details";
    }
    
//  @GetMapping("/exit")
//  public String exitPage(HttpServletResponse response, @CookieValue(name = "auth_cookie", required = false) Cookie cookie) {
//      if (cookie != null) {
//          cookie.setMaxAge(0);
//          cookie.setPath("/");
//          cookie.setDomain("localhost");
//          response.addCookie(cookie);
//      }
//      return "redirect:/";
//  }
  
}
