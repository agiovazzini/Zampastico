package it.unisa.zampastico.dao;

import java.sql.SQLException;
import java.util.List;

import it.unisa.zampastico.model.UtenteBEAN;

public interface UtenteDAO {
	 public void doSave(UtenteBEAN utente) throws SQLException;
	 public UtenteBEAN doRetrieveById(int idUtente) throws SQLException;
	 public UtenteBEAN doRetrieveByEmail(String email) throws SQLException;
	 public boolean doUpdate(UtenteBEAN utente) throws SQLException;
	 public boolean doDelete(int code) throws SQLException;
	 public List<UtenteBEAN> doRetrieveAll(String order) throws SQLException;
}
