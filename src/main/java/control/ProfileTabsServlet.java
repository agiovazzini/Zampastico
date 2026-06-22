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
import java.nio.file.Path;
import java.sql.SQLException;

import javax.sql.DataSource;

@WebServlet("/profile/*")
public class ProfileTabsServlet extends HttpServlet {
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
        UtenteBEAN userLogged = (UtenteBEAN) session.getAttribute("utenteLoggato");
        String pathInfo = request.getPathInfo();
        String contentPage = "/WEB-INF/view/common/profile-tabs/my-data.jsp";
        try {
        	if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/personal-data")) {
        		contentPage = "/WEB-INF/view/common/profile-tabs/my-data.jsp";
        	} else if (pathInfo.equals("/orders-history")) {
        		contentPage = "/WEB-INF/view/common/profile-tabs/my-orders.jsp";
        	} else if (pathInfo.equals("/addresses")) {
        		contentPage = "/WEB-INF/view/common/profile-tabs/my-address.jsp";
        	} else if (pathInfo.equals("/reviews")) {
        		contentPage = "/WEB-INF/view/common/profile-tabs/my-reviews.jsp";
        	} else {
        		response.sendError(404);
        		return;
        	}
        } catch (Exception e) {
        	response.sendError(500);
        	return;
        }
        request.setAttribute("contentPage", contentPage);
        request.getRequestDispatcher("/WEB-INF/view/common/profile.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
