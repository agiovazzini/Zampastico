package control;

import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;

import dao.UtenteDAOImp;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UtenteBEAN;

@WebServlet("/registrazione")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAOImp utenteDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        utenteDAO = new UtenteDAOImp(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/common/profile-tabs/registrazione.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            if (utenteDAO.doRetrieveByEmail(email) != null) {
                request.setAttribute("erroreEmail", "Questa email è già registrata!");
                request.getRequestDispatcher("/WEB-INF/view/common/profile-tabs/registrazione.jsp").forward(request, response);
                return;
            }

            UtenteBEAN nuovoUtente = new UtenteBEAN();
            nuovoUtente.setNome(nome);
            nuovoUtente.setCognome(cognome);
            nuovoUtente.setEmail(email);
            nuovoUtente.setPass(password); 
            nuovoUtente.setRuolo(UtenteBEAN.Ruolo.cliente); 
            nuovoUtente.setAttivo(true);

            utenteDAO.doSave(nuovoUtente);

            response.sendRedirect(request.getContextPath() + "/login?registrazione=success");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante la registrazione");
        }
    }
}