package john.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/hello-world")
    public String sayHello() {
        return "hello_world";
    }
}