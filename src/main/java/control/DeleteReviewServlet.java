package control;

import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.UtenteBEAN;
import dao.RecensioneDAO;
import dao.RecensioneDAOImp;

@WebServlet("/DeleteReview")
public class DeleteReviewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RecensioneDAO recensioneDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }
        this.recensioneDAO = new RecensioneDAOImp(ds);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        UtenteBEAN utenteLoggato = (UtenteBEAN) session.getAttribute("utenteLoggato");
        if (utenteLoggato == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String idVarianteStr = request.getParameter("idVarianteProdotto");
        if (idVarianteStr == null || idVarianteStr.trim().isEmpty()) {
            session.setAttribute("feedback", "Impossibile identificare il prodotto associato alla recensione.");
            response.sendRedirect(request.getContextPath() + "/profile/reviews");
            return;
        }

        try {
            int idVariante = Integer.parseInt(idVarianteStr);
            boolean eliminato = recensioneDAO.doDelete(utenteLoggato.getIdUtente(), idVariante);
            
            if (eliminato) {
                session.setAttribute("feedback", "Recensione eliminata con successo.");
            } else {
                session.setAttribute("feedback", "Impossibile eliminare la recensione. Potrebbe essere già stata rimossa.");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("feedback", "Identificativo prodotto non valido.");
        } catch (SQLException e) {
            session.setAttribute("feedback", "Errore interno del database durante l'eliminazione.");
        }
        
        response.sendRedirect(request.getContextPath() + "/profile/reviews");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/profile/reviews");
    }
}