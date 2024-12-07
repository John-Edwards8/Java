package john.project.config;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean adminAuthorized = request.getSession().getAttribute("adminLoggedIn") != null;

        if (!adminAuthorized) {
            response.sendRedirect("/spring-mvc-app1/login");
            return false;
        }
		
        return true;
    }
}
