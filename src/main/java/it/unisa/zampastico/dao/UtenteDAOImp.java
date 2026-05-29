package it.unisa.zampastico.dao;

import javax.sql.DataSource;

public class UtenteDAOImp {

	private static final String TABLE_NAME = "Utente";
    private DataSource ds = null;
	
    public UtenteDAOImp(DataSource ds) {
        this.ds = ds;
    }
    
}
