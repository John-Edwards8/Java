package john.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import john.project.dao.ClientDAO;

@Controller
public class ControllerStart {
	private final ClientDAO clientDAO;

	public ControllerStart(ClientDAO clientDAO) {
		this.clientDAO = clientDAO;
	}

	@GetMapping("/")
	public String startPage() {
		return "start/hello";
	}

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

	@PostMapping("/login")
	public String authenticate(@RequestParam String username, @RequestParam String password,
			@RequestParam(value = "check", required = false) boolean check, HttpSession session, Model model) {
		if (username.equals("ADMIN") && password.equals("ADMIN")) {
			session.setAttribute("adminLoggedIn", true);
			return check == true ? "redirect:/admin/setcookie" : "redirect:/admin";
		} else if (clientDAO.authenticate(username, password)) {
			 session.setAttribute("userLoggedIn", true);
			 session.setAttribute("current_client", clientDAO.getClient(username, password));
			 return check == true? "redirect:/user/setcookie" : "redirect:/user";
		} else {
			model.addAttribute("error", "Invalid username or password");
			return "start/login";
		}
	}

}