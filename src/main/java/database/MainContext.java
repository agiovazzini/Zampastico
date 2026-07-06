package database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import javax.sql.DataSource;

import dao.CategoriaDAOImp;
import model.CategoriaBEAN;

@WebListener
public class MainContext implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        DataSource ds = null;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/Zampastico");
        } catch (NamingException e) {
            System.out.println("Error DataSource: " + e.getMessage());
        }
        
        // 1. Salviamo il DataSource nel contesto globale
        context.setAttribute("DataSource", ds);

        // 2. Carichiamo le categorie di LIVELLO 0 per la navigazione nell'Header
        if (ds != null) {
            CategoriaDAOImp categoriaDAO = new CategoriaDAOImp(ds);
            try {
                List<CategoriaBEAN> tutteLeCategorie = categoriaDAO.doRetrieveAll();
                List<CategoriaBEAN> categorieLivelloZero = new ArrayList<>();
                
                for (CategoriaBEAN cat : tutteLeCategorie) {
                    // Filtriamo solo le categorie principali (livello 0)
                    if (cat.getLivello() == 0) {
                        categorieLivelloZero.add(cat);
                    }
                }
                
                context.setAttribute("categorieHeader", categorieLivelloZero);
                System.out.println("--- Categorie (Livello 0) caricate con successo nell'Header ---");
            } catch (SQLException e) {
                System.out.println("Errore caricamento categorie Header: " + e.getMessage());
            }
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}