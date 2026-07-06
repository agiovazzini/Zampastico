package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import dao.CategoriaDAOImp;
import dao.ProdottoDAOImp;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CategoriaBEAN;
import model.ProdottoBEAN;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private CategoriaDAOImp categoriaDAO;
    private ProdottoDAOImp prodottoDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }
        categoriaDAO = new CategoriaDAOImp(ds);
        prodottoDAO = new ProdottoDAOImp(ds);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {   
        HttpSession session = request.getSession();
        String info = (String) session.getAttribute("info");
        if (info != null) {
        	request.setAttribute("statoAccesso", info);
        	session.removeAttribute("info");
        }
        
        try {
        	List<CategoriaBEAN> tutteLeCategorie = categoriaDAO.doRetrieveAll();
        	for (CategoriaBEAN cat : tutteLeCategorie) {
        	    Integer idProdImg = categoriaDAO.doRetrievePrimoProdottoId(cat.getIdCategoria());
        	    cat.setIdProdottoImmagine(idProdImg);
        	}
        	request.setAttribute("categorieHome", tutteLeCategorie);
            List<ProdottoBEAN> tuttiProdotti = prodottoDAO.doRetrieveAll("");
            List<ProdottoBEAN> prodottiInEvidenza = tuttiProdotti.size() > 4 
                    ? tuttiProdotti.subList(0, 4) 
                    : tuttiProdotti;
            request.setAttribute("prodottiHome", prodottiInEvidenza);
            
        } catch (SQLException e) {
           return;
        }

        request.getRequestDispatcher("/WEB-INF/view/common/home.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}