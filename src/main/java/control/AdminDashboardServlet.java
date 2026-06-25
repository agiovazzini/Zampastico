package control;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UtenteBEAN;

import java.io.IOException;
import javax.sql.DataSource;

@WebServlet("/admin/*")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
   
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBEAN utente = (UtenteBEAN) session.getAttribute("utenteLoggato");
        if (utente == null || utente.getRuolo() != UtenteBEAN.Ruolo.amministratore) {
            session.setAttribute("errore", "Accesso negato. Area riservata agli amministratori.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String feedback = null;
        String pathInfo = request.getPathInfo();
        String contentPage = "/WEB-INF/view/admin/admin-users.jsp";
        String activeTab = "users";

     // 2. GESTIONE DEI TAB DINAMICI
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/users")) {
        	contentPage = "/WEB-INF/view/admin/admin-users.jsp";
    		activeTab = "users";
        }
        try {
            switch (pathInfo) {
                case "/users":
                    activeTab = "users";
                    contentPage = "/WEB-INF/view/admin/admin-users.jsp";
                    break;
                case "/orders":
                    activeTab = "orders";
                    contentPage = "/WEB-INF/view/admin/admin-orders.jsp";
                    break;
                case "/coupons":
                    activeTab = "coupons";
                    contentPage = "/WEB-INF/view/admin/admin-coupons.jsp";
                    break;
                    
                case "/product":
                    activeTab = "product";
                    contentPage = "/WEB-INF/view/admin/admin-product.jsp";
                    break;
                    
                case "/catalog":
                    activeTab = "catalog";
                    contentPage = "/WEB-INF/view/admin/admin-catalog.jsp";
                    break;
                    
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/users");
                    return;
            }
        } catch (Exception e) {
            feedback = "Si è verificato un errore durante il caricamento del pannello amministrativo.";
            activeTab = "users";
            contentPage = "/WEB-INF/view/admin/admin-users.jsp";
        }
        request.setAttribute("contentPage", contentPage);
        request.setAttribute("activeTab", activeTab);
        if (feedback != null) {
            request.setAttribute("feedback", feedback);
            session.removeAttribute("feedback"); 
        }
        request.getRequestDispatcher("/WEB-INF/view/admin/admin-dashboard.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}