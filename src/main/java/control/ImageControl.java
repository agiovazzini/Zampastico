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
import dao.VarianteProdottoDAOImp;
import model.ProdottoBEAN;
import model.VarianteProdottoBEAN;

@WebServlet("/image")
public class ImageControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAOImp prodottoDAO;
    private VarianteProdottoDAOImp varianteDAO;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile");
        }
        prodottoDAO = new ProdottoDAOImp(ds);
        varianteDAO = new VarianteProdottoDAOImp(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            try {
                String path = null;
                String mimeType = null;
                if (action.equalsIgnoreCase("show")) {
                    int productCode = Integer.parseInt(request.getParameter("code"));
                    ProdottoBEAN pBean = prodottoDAO.doRetrieveByKey(productCode);
                    if(pBean != null) {
                        path = pBean.getPath();
                        mimeType = pBean.getMimeType();
                    }
                } else if (action.equalsIgnoreCase("showVar")) {
                    int varCode = Integer.parseInt(request.getParameter("code"));
                    VarianteProdottoBEAN vBean = varianteDAO.doRetrieveByKey(varCode);
                    if(vBean != null) {
                        path = vBean.getPath();
                        mimeType = vBean.getMimeType();
                    }
                }
                if (path != null) {
                    File file = new File(path);
                    if (file.exists()) {
                        response.setContentType(mimeType);
                        try (InputStream is = new FileInputStream(file)) {
                            OutputStream os = response.getOutputStream();
                            is.transferTo(os);
                        }
                    } else {
                        System.err.println("ERRORE IMAGE-CONTROL: Il file non esiste fisicamente in -> " + path);
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Immagine non trovata sul server");
                    }
                } else {
                     response.sendError(HttpServletResponse.SC_NOT_FOUND, "Nessun percorso immagine presente nel DB");
                }
            } catch (SQLException e) {
                System.err.println("Errore nel recupero dell'immagine dal DB: " + e.getMessage());
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}