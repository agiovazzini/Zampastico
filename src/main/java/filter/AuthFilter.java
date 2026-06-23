package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {
		"/checkout", 
	    "/wishlist",
	    "/profile/*",
	    "/updateData",
	    "/updatePassword",
	    "/deleteAccount"
})
public class AuthFilter extends HttpFilter {
	private static final long serialVersionUID = 1L;

	@Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

    	HttpSession session = request.getSession(false);
    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // Per HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // Per HTTP 1.0
        response.setDateHeader("Expires", 0);
        if (session == null || session.getAttribute("utenteLoggato") == null) {
            response.sendRedirect(request.getContextPath() + "/login?errore=login_required");
            return;
        }

        chain.doFilter(request, response);
    }
}