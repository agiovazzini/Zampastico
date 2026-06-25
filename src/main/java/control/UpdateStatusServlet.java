package control;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

import javax.sql.DataSource;

import dao.OrdineDAOImp;
import model.OrdineBEAN.StatoOrdine;
import model.UtenteBEAN;
import org.json.JSONObject;

@WebServlet("/admin/updateStatus")
public class UpdateStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAOImp ordineDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }
        ordineDAO = new OrdineDAOImp(ds);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();
        HttpSession session = request.getSession(false);
        UtenteBEAN utente = (session != null) ? (UtenteBEAN) session.getAttribute("utenteLoggato") : null;
        if (utente == null || utente.getRuolo() != UtenteBEAN.Ruolo.amministratore) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Accesso negato. Operazione non autorizzata.");
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }
        String idOrdineStr = request.getParameter("idOrdine");
        String statoStr = request.getParameter("stato");
        if (idOrdineStr != null && statoStr != null) {
            try {
                int idOrdine = Integer.parseInt(idOrdineStr);
                StatoOrdine nuovoStato = StatoOrdine.valueOf(statoStr);
                ordineDAO.doUpdateStato(idOrdine, nuovoStato);
                jsonResponse.put("success", true);
                jsonResponse.put("nuovoStato", nuovoStato.toString());
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Stato ordine non valido.");
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Errore interno durante l'aggiornamento.");
                e.printStackTrace();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Parametri mancanti.");
        }
        out.print(jsonResponse.toString());
        out.flush();
    }
}