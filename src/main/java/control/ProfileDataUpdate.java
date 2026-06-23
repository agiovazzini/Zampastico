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

@WebServlet("/updateData")
public class ProfileDataUpdate extends HttpServlet {
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
		String nameForm = request.getParameter("nameForm");
		String surnameForm = request.getParameter("surnameForm");
		if (userLogged == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		if (nameForm.equals(userLogged.getNome()) && surnameForm.equals(userLogged.getCognome())){
			session.setAttribute("feedback", "Nessuna modifica rilevata. Hai inserito gli stessi dati attuali.");
			response.sendRedirect(request.getContextPath() + "/profile/personal-data");
			return;
		}
		
		String oldName = userLogged.getNome();
		String oldSurname = userLogged.getCognome();
		userLogged.setNome(nameForm);
		userLogged.setCognome(surnameForm);
		try {
			boolean updateStatus = utenteDAO.doUpdate(userLogged);
			if (updateStatus) {
				session.setAttribute("utenteLoggato", userLogged);
				session.setAttribute("feedback", "Profilo aggiornato con successo.");
			} else {
				userLogged.setNome(oldName);
				userLogged.setCognome(oldSurname);
				session.setAttribute("feedback", "Impossibile aggiornare il profilo.");
			}
		} catch (SQLException e) {
			userLogged.setNome(oldName);
            userLogged.setCognome(oldSurname);
            session.setAttribute("feedback", "Errore interno del database.");
		}
		response.sendRedirect(request.getContextPath() + "/profile/personal-data");
	}
}
