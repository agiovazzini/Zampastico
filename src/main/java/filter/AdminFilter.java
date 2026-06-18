package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UtenteBEAN;

import java.io.IOException;

@WebFilter(urlPatterns = {
    "/admin-home", "/admin-catalog", "/admin-orders", 
    "/admin-product", "/coupons"
})
public class AdminFilter extends HttpFilter{

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

    	HttpSession session = request.getSession(false);
    	
        if (session == null || session.getAttribute("utenteLoggato") == null) {
            response.sendRedirect(request.getContextPath() + "/login?errore=not_allowed");
            return; 
        }
        
        UtenteBEAN utente = (UtenteBEAN) session.getAttribute("utenteLoggato");
        if (utente.getRuolo() != UtenteBEAN.Ruolo.amministratore) {
            response.sendRedirect(request.getContextPath() + "/login?errore=not_allowed");
            return; 
        }

        chain.doFilter(request, response);
    }
}