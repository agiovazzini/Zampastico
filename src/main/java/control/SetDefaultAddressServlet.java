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

import dao.IndirizzoDAOImp;

@WebServlet("/setDefaultAddress")
public class SetDefaultAddressServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IndirizzoDAOImp indirizzoDAO;

	@Override
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }
        indirizzoDAO = new IndirizzoDAOImp(ds);
    }

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UtenteBEAN userLogged = (UtenteBEAN) session.getAttribute("utenteLoggato");
		if (userLogged == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		try {
			int idAddress = Integer.parseInt(request.getParameter("idAddress"));
			boolean defaultStatus = indirizzoDAO.doSetPredefinito(idAddress, userLogged.getIdUtente()); 
			if (defaultStatus) {
				session.setAttribute("feedback", "Indirizzo impostato come predefinito con successo!");
			} else {
				session.setAttribute("feedback", "Impossibile aggiornare l'indirizzo predefinito. Riprova.");
			}
			
	    } catch (NumberFormatException e) {
	    	session.setAttribute("feedback", "Errore nel formato dei dati inviati.");
	    } catch (SQLException e) {
	    	session.setAttribute("feedback", "Errore interno del database durante l'aggiornamento.");
	    }
		response.sendRedirect(request.getContextPath() + "/profile/addresses");
	}
}