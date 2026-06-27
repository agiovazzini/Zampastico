package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import javax.sql.DataSource;
import dao.ProdottoDAOImp;
import model.ProdottoBEAN;

@WebServlet("/image")
public class ImageControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAOImp prodottoDAO;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile");
        }
        prodottoDAO = new ProdottoDAOImp(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null && action.equalsIgnoreCase("show")) {
            int productCode = Integer.parseInt(request.getParameter("code"));
            try {
                ProdottoBEAN bean = prodottoDAO.doRetrieveByKey(productCode);
                if (bean != null && bean.getPath() != null) {
                    String mimeType = bean.getMimeType();
                    String path = bean.getPath();
                    response.setContentType(mimeType);
                    File file = new File(path);
                    if (file.exists()) {
                        try (InputStream is = new FileInputStream(file)) {
                            OutputStream os = response.getOutputStream();
                            is.transferTo(os);
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Errore nel recupero dell'immagine: " + e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}