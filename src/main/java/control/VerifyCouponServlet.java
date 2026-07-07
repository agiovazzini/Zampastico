package control;

import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.CouponDAO;
import dao.CouponDAOImp;
import model.CouponBEAN;

@WebServlet("/verificaCoupon")
public class VerifyCouponServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codice = request.getParameter("codice");
        
        // Impostiamo l'header per far sapere a XMLHttpRequest che riceverà un JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();

        if (codice == null || codice.trim().isEmpty()) {
            jsonResponse.put("valido", false);
            jsonResponse.put("messaggio", "Inserisci un codice valido.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        try {
            // ATTENZIONE: Assicurati che "DataSource" sia il nome esatto usato in MainContext.java
            DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
            
            // Se il DataSource è null, il server andrà in crash alla riga successiva. 
            // Meglio intercettarlo in modo pulito!
            if (ds == null) {
                System.err.println("ERRORE CRITICO: DataSource non trovato nel ServletContext!");
                throw new SQLException("DataSource nullo.");
            }
            
            CouponDAO couponDao = new CouponDAOImp(ds);
            CouponBEAN coupon = couponDao.doRetrieveByCodice(codice);

            if (coupon != null && !coupon.isScaduto()) {
                jsonResponse.put("valido", true);
                jsonResponse.put("sconto", coupon.getPercentualeSconto());
                jsonResponse.put("messaggio", "Codice applicato con successo!");
            } else {
                jsonResponse.put("valido", false);
                jsonResponse.put("messaggio", "Codice inesistente o scaduto.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace(); // Leggi la console di Eclipse per vedere il vero errore!
            jsonResponse.put("valido", false);
            jsonResponse.put("messaggio", "Errore interno del server durante la verifica.");
            // Inviamo un codice d'errore HTTP formale (che farà scattare il blocco ELSE del nostro XHR)
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
        }

        // Inviamo fisicamente la stringa JSON al browser dell'utente
        response.getWriter().write(jsonResponse.toString());
    }
}