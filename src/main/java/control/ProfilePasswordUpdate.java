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

@WebServlet("/updatePassword")
public class ProfilePasswordUpdate extends HttpServlet {
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
		String oldPasswordForm = request.getParameter("passwordForm");
		String newPasswordForm = request.getParameter("newPasswordForm");
		String confirmPasswordForm = request.getParameter("confirmPasswordForm");
		if (userLogged == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		if (!oldPasswordForm.equals(userLogged.getPass())) {
			session.setAttribute("feedback", "La password attuale è errata.");
			response.sendRedirect(request.getContextPath() + "/profile/personal-data");
			return;
		}
		if (newPasswordForm.equals(userLogged.getPass())) {
			session.setAttribute("feedback", "Non puoi utilizzare la stessa password.");
			response.sendRedirect(request.getContextPath() + "/profile/personal-data");
			return;
		}
		
		String userOldPassword = userLogged.getPass();
		userLogged.setPass(newPasswordForm);
		try {
			boolean updateStatus = utenteDAO.doUpdate(userLogged);
			if (updateStatus) {
				session.setAttribute("utenteLoggato", userLogged);
				session.setAttribute("feedback", "Password aggiornata con successo.");
			} else {
				userLogged.setPass(userOldPassword);
				session.setAttribute("feedback", "Impossibile aggiornare la password.");
			}
		} catch (SQLException e) {
			userLogged.setPass(userOldPassword);
            session.setAttribute("feedback", "Errore interno del database.");
		}
		response.sendRedirect(request.getContextPath() + "/profile/personal-data");
	}
}
