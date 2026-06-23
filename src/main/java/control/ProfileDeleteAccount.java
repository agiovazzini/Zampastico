package control;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UtenteBEAN;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import dao.UtenteDAOImp;

/**
 * Servlet implementation class ProfileDeleteAccount
 */
@WebServlet("/deleteAccount")
public class ProfileDeleteAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UtenteDAOImp utenteDAO;
	
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
		UtenteBEAN userLogged = (UtenteBEAN) session.getAttribute("utenteLoggato");
		if (userLogged == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		try {
			boolean deleteStatus = utenteDAO.doDelete(userLogged.getIdUtente()); 
			if (deleteStatus) {
				session.invalidate();
				response.sendRedirect(request.getContextPath() + "/");
				return;
			} else {
				session.setAttribute("feedback", "Impossibile cancellare l'account in questo momento.");
				response.sendRedirect(request.getContextPath() + "/profile/personal-data");
			}
		} catch (SQLException e) {
			session.setAttribute("feedback", "Errore interno del database durante la cancellazione.");
			response.sendRedirect(request.getContextPath() + "/profile/personal-data");
		}
	}

}
