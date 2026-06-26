package dao;

import java.sql.SQLException;

import java.util.List;

import model.UtenteBEAN;
import model.UtenteBEAN.Ruolo;

public interface UtenteDAO {
	 public void doSave(UtenteBEAN utente) throws SQLException;
	 public UtenteBEAN doRetrieveById(int idUtente) throws SQLException;
	 public UtenteBEAN doRetrieveByEmail(String email) throws SQLException;
	 public boolean doUpdate(UtenteBEAN utente) throws SQLException;
	 public boolean doDelete(int code) throws SQLException;
	 public List<UtenteBEAN> doRetrieveAll(String order) throws SQLException;
	 public int countByRuolo(Ruolo ruolo) throws SQLException;
	 public List<UtenteBEAN> doRetrieveByRuoloPaginated(Ruolo ruolo, int offset, int limit) throws SQLException;
}
