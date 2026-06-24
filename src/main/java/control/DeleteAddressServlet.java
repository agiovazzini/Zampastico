package control;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.IndirizzoBEAN;
import model.UtenteBEAN;
import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;
import dao.IndirizzoDAOImp;

@WebServlet("/deleteAddress")
public class DeleteAddressServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IndirizzoDAOImp indirizzoDAO;

	public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }
        indirizzoDAO = new IndirizzoDAOImp(ds);
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UtenteBEAN userLogged = (UtenteBEAN) session.getAttribute("utenteLoggato");
		if (userLogged == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		try {
			int idAddress = Integer.parseInt(request.getParameter("idAddress"));
			boolean statusDeletion = indirizzoDAO.doDelete(idAddress,userLogged.getIdUtente()); 
			if (statusDeletion) {
				session.setAttribute("feedback", "Indirizzo eliminato con successo.");
			} else {
				session.setAttribute("feedback", "Impossibile eliminare l'indirizzo.");
			}
	    } catch (SQLException | NumberFormatException e) {
	    	session.setAttribute("feedback", "Errore durante la cancellazione dell'indirizzo all'interno del database.");
	    }
		response.sendRedirect(request.getContextPath() + "/profile/addresses");
	}
}
