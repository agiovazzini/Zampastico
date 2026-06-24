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

@WebServlet("/addAddress")
public class AddAddressServlet extends HttpServlet {
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
		String streetForm = request.getParameter("streetForm");
		String houseNumberForm = request.getParameter("houseNumberForm");
		String cityForm = request.getParameter("cityForm");
		String provinceForm = request.getParameter("provinceForm");
		String postCodeForm = request.getParameter("postCodeForm");
		boolean defaultForm = (request.getParameter("defaultForm") != null);
		IndirizzoBEAN newAddress = new IndirizzoBEAN();
        newAddress.setIdUtente(userLogged.getIdUtente());
        newAddress.setVia(streetForm + ", " + houseNumberForm);
        newAddress.setCitta(cityForm);
        newAddress.setProvincia(provinceForm);
        newAddress.setCap(postCodeForm);
        newAddress.setPredefinito(defaultForm);
		try {
			indirizzoDAO.doSave(newAddress);
			session.setAttribute("feedback", "Indirizzo salvato con successo!");
		} catch (SQLException e) {
			session.setAttribute("feedback", "Errore nel caricamento dell'indirizzo nel database.");
		}
		response.sendRedirect(request.getContextPath() + "/profile/addresses");
	}
}
