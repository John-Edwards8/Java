package john.project.config;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean userAuthorized = request.getSession().getAttribute("userLoggedIn") != null;
        boolean adminAuthorized = request.getSession().getAttribute("adminLoggedIn") != null;

        if (!userAuthorized && !adminAuthorized) {
            response.sendRedirect("/login");
            return false;
        }
        
        
		
		 if (userAuthorized && request.getRequestURI().startsWith("/admin")) {
			 String referringPage = request.getHeader("Referer");
			 if (referringPage != null && !referringPage.isEmpty()) {
				 response.sendRedirect(referringPage);
				 return false;
			 }
		 }
		 
		 //FIX: Cookie problem
		 
		 if (adminAuthorized && request.getRequestURI().startsWith("/user")) {
			 String referringPage = request.getHeader("Referer");
			 if (referringPage != null && !referringPage.isEmpty()) {
				 response.sendRedirect(referringPage);
				 return false;
			 }
		 }
		 
        
        return true;
    }
}
