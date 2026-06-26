package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import dao.ProdottoDAOImp;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ProdottoBEAN;

@WebServlet("/catalog")
public class CatalogServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAOImp prodottoDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Collega il database all'avvio della Servlet
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }
        prodottoDAO = new ProdottoDAOImp(ds);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //Chiedi al DAO la lista di tutti i prodotti attivi
        	List<ProdottoBEAN> prodotti = prodottoDAO.doRetrieveAll(""); 
            //Impacchetta i prodotti e chiamali "prodottiCatalogo"
            request.setAttribute("prodottiCatalogo", prodotti);
            
            //Fai il forward alla tua pagina JSP esatta
            request.getRequestDispatcher("/WEB-INF/view/common/catalog.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel recupero del catalogo");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}