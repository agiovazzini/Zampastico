package control;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import javax.sql.DataSource;

import dao.CouponDAOImp;
import model.CouponBEAN;
import model.UtenteBEAN;

// Ho inserito la rotta sotto /admin/ per proteggerla con eventuali filtri di sicurezza
@WebServlet("/admin/manageCoupons")
public class AdminCouponServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CouponDAOImp couponDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }
        couponDAO = new CouponDAOImp(ds);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UtenteBEAN utente = (session != null) ? (UtenteBEAN) session.getAttribute("utenteLoggato") : null;
        if (utente == null || utente.getRuolo() != UtenteBEAN.Ruolo.amministratore) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                String codice = request.getParameter("codice");
                double sconto = Double.parseDouble(request.getParameter("sconto"));
                String dataStr = request.getParameter("dataScadenza"); 
                CouponBEAN nuovoCoupon = new CouponBEAN();
                nuovoCoupon.setCodice(codice);
                nuovoCoupon.setPercentualeSconto(sconto);
                if (dataStr != null && !dataStr.trim().isEmpty()) {
                    try {
                        nuovoCoupon.setDataScadenza(LocalDateTime.parse(dataStr));
                    } catch (DateTimeParseException e) {
                        session.setAttribute("feedback", "Errore: Formato data non valido.");
                        response.sendRedirect(request.getContextPath() + "/admin/coupons");
                        return;
                    }
                }
                couponDAO.doSave(nuovoCoupon);
                session.setAttribute("feedback", "Coupon creato con successo!");

            } else if ("delete".equals(action)) {
                int idCoupon = Integer.parseInt(request.getParameter("idCoupon"));
                boolean deleted = couponDAO.doDelete(idCoupon);
                
                if (deleted) {
                    session.setAttribute("feedback", "Coupon eliminato con successo.");
                } else {
                    session.setAttribute("feedback", "Errore: Il coupon non è stato trovato.");
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                session.setAttribute("feedback", "Errore: Il codice coupon inserito esiste già nel sistema.");
            } else {
                session.setAttribute("feedback", "Errore di comunicazione col database durante l'operazione.");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            session.setAttribute("feedback", "Errore: Dati inseriti non validi. Controlla il formato della percentuale di sconto.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/coupons");
    }
}