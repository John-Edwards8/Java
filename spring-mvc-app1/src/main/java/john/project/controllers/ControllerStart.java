package john.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class ControllerStart {
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
}