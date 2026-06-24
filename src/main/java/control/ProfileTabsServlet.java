package control;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.IndirizzoBEAN;
import model.OrdineBEAN;
import model.UtenteBEAN;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import dao.IndirizzoDAOImp;
import dao.OrdineDAOImp;

@WebServlet("/profile/*")
public class ProfileTabsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private IndirizzoDAOImp indirizzoDAO;
    private OrdineDAOImp ordineDAO;
	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }
        indirizzoDAO = new IndirizzoDAOImp(ds);
        ordineDAO = new OrdineDAOImp(ds);
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String feedback = null;
        String pathInfo = request.getPathInfo();
        String contentPage = "/WEB-INF/view/common/profile-tabs/my-data.jsp";
        String activeTab = "personal-data";
        try {
        	if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/personal-data")) {
        		contentPage = "/WEB-INF/view/common/profile-tabs/my-data.jsp";
        		activeTab = "personal-data";
        	} else if (pathInfo.equals("/orders-history")) {
        		contentPage = "/WEB-INF/view/common/profile-tabs/my-orders.jsp";
        		activeTab = "orders-history";
        		UtenteBEAN userLogged = (UtenteBEAN) session.getAttribute("utenteLoggato");
        		if (userLogged != null) {
        			try {
        				List<OrdineBEAN> ordini = ordineDAO.doRetrieveByUser(userLogged.getIdUtente());
                        request.setAttribute("orders", ordini);
        			} catch (SQLException e) {
        				request.setAttribute("feedback", "Errore nel caricamento degli ordini.");
        			}
        		}
        	} else if (pathInfo.equals("/addresses")) {
        		contentPage = "/WEB-INF/view/common/profile-tabs/my-address.jsp";
        		activeTab = "addresses";
        		UtenteBEAN userLogged = (UtenteBEAN) session.getAttribute("utenteLoggato");
        		if (userLogged != null) {
        			try {
        				List<IndirizzoBEAN> list = indirizzoDAO.doRetrieveByUser(userLogged.getIdUtente());
        				request.setAttribute("addresses", list);
        			} catch (SQLException e) {
        				request.setAttribute("feedback", "Errore nel caricamento degli indirizzi.");
        			}
        		}
        	} else if (pathInfo.equals("/reviews")) {
        		contentPage = "/WEB-INF/view/common/profile-tabs/my-reviews.jsp";
        		activeTab = "reviews";
        	} else {
        		feedback = "La sezione richiesta non esiste. Ti abbiamo reindirizzato ai dati personali.";
                contentPage = "/WEB-INF/view/common/profile-tabs/my-data.jsp";
                activeTab = "personal-data";
        	}
        } catch (Exception e) {
        	feedback = "Si è verificato un errore durante il caricamento della pagina profilo.";
            contentPage = "/WEB-INF/view/common/profile-tabs/my-data.jsp";
            activeTab = "personal-data";
        }
        request.setAttribute("contentPage", contentPage);
        request.setAttribute("activeTab", activeTab);
        if (feedback != null) {
            request.setAttribute("feedback", feedback);
        }
        request.getRequestDispatcher("/WEB-INF/view/common/profile.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
