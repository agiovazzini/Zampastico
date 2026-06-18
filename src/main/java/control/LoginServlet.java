package control;

import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;

import dao.UtenteDAOImp;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UtenteBEAN;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAOImp utenteDAO;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		if (ds == null) {
			throw new ServletException("DataSource non disponibile nel contesto");
		}
		utenteDAO = new UtenteDAOImp(ds);
	}
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession(); 
        if (session.getAttribute("utenteLoggato") != null) {
            session.setAttribute("info", "Accesso già effettuato.");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        String email = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            UtenteBEAN utente = utenteDAO.doRetrieveByEmail(email);
            if (utente != null && utente.getEmail() != null && utente.isAttivo()) {
                if (utente.getPass().equals(password)) {
                    session.setAttribute("utenteLoggato", utente);
                    response.sendRedirect(request.getContextPath() + "/home");
                    return; 
                }
            }
            session.setAttribute("errore", "Email o password errati.");
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (SQLException e) {
            session.setAttribute("errore", "Errore interno del server.");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("utenteLoggato") != null) {
            session.setAttribute("info", "L'utente risulta già loggato.");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        String errore = (String) session.getAttribute("errore");
        if (errore != null) {
            request.setAttribute("errore", errore);
            session.removeAttribute("errore"); 
        }
        request.getRequestDispatcher("/WEB-INF/view/common/login.jsp").forward(request, response);
    }
}