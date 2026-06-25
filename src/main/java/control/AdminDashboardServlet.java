package control;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CouponBEAN;
import model.OrdineBEAN;
import model.UtenteBEAN;
import model.UtenteBEAN.Ruolo;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import dao.CouponDAOImp;
import dao.OrdineDAOImp;
import dao.UtenteDAOImp;

@WebServlet("/admin/*")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAOImp ordineDAO;
    private CouponDAOImp couponDAO;
    private UtenteDAOImp utenteDAO;
   
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto");
        }
        ordineDAO = new OrdineDAOImp(ds);
        couponDAO = new CouponDAOImp(ds);
        utenteDAO = new UtenteDAOImp(ds);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBEAN utente = (UtenteBEAN) session.getAttribute("utenteLoggato");
        if (utente == null || utente.getRuolo() != Ruolo.amministratore) {
            session.setAttribute("errore", "Accesso negato. Area riservata agli amministratori.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String feedback = null;
        String pathInfo = request.getPathInfo();
        String contentPage = "/WEB-INF/view/admin/admin-users.jsp";
        String activeTab = "users";
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/users")) {
            contentPage = "/WEB-INF/view/admin/admin-users.jsp";
            activeTab = "users";
        }
        try {
            if (pathInfo != null) {
                switch (pathInfo) {
                case "/users":
                    activeTab = "users";
                    contentPage = "/WEB-INF/view/admin/admin-users.jsp";
                    
                    int pageAdmin = 1;
                    int pageClient = 1;
                    int recordsPerPageUsers = 10;
                    
                    // Lettura parametri pagina separati
                    if (request.getParameter("pageAdmin") != null) {
                        try {
                            pageAdmin = Integer.parseInt(request.getParameter("pageAdmin"));
                            if (pageAdmin < 1) pageAdmin = 1;
                        } catch (NumberFormatException e) { pageAdmin = 1; }
                    }
                    if (request.getParameter("pageClient") != null) {
                        try {
                            pageClient = Integer.parseInt(request.getParameter("pageClient"));
                            if (pageClient < 1) pageClient = 1;
                        } catch (NumberFormatException e) { pageClient = 1; }
                    }
                    
                    // Calcolo Offset indipendenti
                    int offsetAdmin = (pageAdmin - 1) * recordsPerPageUsers;
                    int offsetClient = (pageClient - 1) * recordsPerPageUsers;
                    
                    // Recupero Liste
                    List<UtenteBEAN> adminUsers = utenteDAO.doRetrieveByRuoloPaginated(Ruolo.amministratore, offsetAdmin, recordsPerPageUsers);
                    List<UtenteBEAN> clientUsers = utenteDAO.doRetrieveByRuoloPaginated(Ruolo.cliente, offsetClient, recordsPerPageUsers);
                    
                    // Calcolo Pagine Separate
                    int totalAdmins = utenteDAO.countByRuolo(Ruolo.amministratore);
                    int totalClients = utenteDAO.countByRuolo(Ruolo.cliente);
                    
                    int noOfPagesAdmin = (int) Math.ceil((double) totalAdmins / recordsPerPageUsers);
                    int noOfPagesClient = (int) Math.ceil((double) totalClients / recordsPerPageUsers);
                    
                    // Redirect di sicurezza se l'utente manomette l'URL
                    if (pageAdmin > noOfPagesAdmin && pageAdmin > 1) {
                        response.sendRedirect(request.getContextPath() + "/admin/users?pageAdmin=1&pageClient=" + pageClient);
                        return;
                    }
                    if (pageClient > noOfPagesClient && pageClient > 1) {
                        response.sendRedirect(request.getContextPath() + "/admin/users?pageAdmin=" + pageAdmin + "&pageClient=1");
                        return;
                    }
                    
                    // Salvataggio attributi Admin
                    request.setAttribute("adminUsers", adminUsers);
                    request.setAttribute("noOfPagesAdmin", noOfPagesAdmin);
                    request.setAttribute("currentPageAdmin", pageAdmin);
                    
                    // Salvataggio attributi Clienti
                    request.setAttribute("clientUsers", clientUsers);
                    request.setAttribute("noOfPagesClient", noOfPagesClient);
                    request.setAttribute("currentPageClient", pageClient);
                    break;
                    case "/orders":
                        activeTab = "orders";
                        contentPage = "/WEB-INF/view/admin/admin-orders.jsp";
                        int page = 1;
                        int recordsPerPage = 10;
                        if (request.getParameter("page") != null) {
                            try {
                                page = Integer.parseInt(request.getParameter("page"));
                            } catch (NumberFormatException e) {
                                page = 1;
                            }
                        }
                        String emailFilter = request.getParameter("emailFilter");
                        String dateFilter = request.getParameter("dateFilter");
                        String dateFrom = request.getParameter("dateFrom");
                        String dateTo = request.getParameter("dateTo");
                        int offset = (page - 1) * recordsPerPage;
                        List<OrdineBEAN> adminOrders = ordineDAO.doRetrieveAllWithFiltersAndPagination(offset, recordsPerPage, emailFilter, dateFilter, dateFrom, dateTo);
                        int totalRecords = ordineDAO.countAllOrdersWithFilters(emailFilter, dateFilter, dateFrom, dateTo);
                        int noOfPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
                        request.setAttribute("adminOrders", adminOrders);
                        request.setAttribute("noOfPages", noOfPages);
                        request.setAttribute("currentPage", page);
                        request.setAttribute("emailFilter", emailFilter);
                        request.setAttribute("dateFilter", dateFilter != null ? dateFilter : "all");
                        request.setAttribute("dateFrom", dateFrom);
                        request.setAttribute("dateTo", dateTo);
                        break;
                    case "/coupons":
                        activeTab = "coupons";
                        contentPage = "/WEB-INF/view/admin/admin-coupons.jsp";
                        
                        int pageCoupons = 1;
                        int recordsPerPageCoupons = 10; // Quanti coupon mostrare per pagina
                        
                        if (request.getParameter("page") != null) {
                            try {
                                pageCoupons = Integer.parseInt(request.getParameter("page"));
                                if (pageCoupons < 1) pageCoupons = 1;
                            } catch (NumberFormatException e) {
                                pageCoupons = 1;
                            }
                        }
                        
                        int offsetCoupons = (pageCoupons - 1) * recordsPerPageCoupons;
                        
                        // Recupera i coupon paginati e il conteggio totale
                        List<CouponBEAN> coupons = couponDAO.doRetrieveAllPaginated(offsetCoupons, recordsPerPageCoupons);
                        int totalCoupons = couponDAO.countAllCoupons();
                        int noOfPagesCoupons = (int) Math.ceil((double) totalCoupons / recordsPerPageCoupons);
                        
                        // Controlla se l'utente prova ad accedere a una pagina vuota e oltre il limite
                        if (pageCoupons > noOfPagesCoupons && pageCoupons > 1) {
                            response.sendRedirect(request.getContextPath() + "/admin/coupons?page=1");
                            return;
                        }
                        
                        request.setAttribute("coupons", coupons);
                        request.setAttribute("noOfPages", noOfPagesCoupons);
                        request.setAttribute("currentPage", pageCoupons);
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
                        if (!pathInfo.equals("/") && !pathInfo.equals("/users")) {
                            response.sendRedirect(request.getContextPath() + "/admin/users");
                            return;
                        }
                }
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
        }
        
        request.getRequestDispatcher("/WEB-INF/view/admin/admin-dashboard.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}