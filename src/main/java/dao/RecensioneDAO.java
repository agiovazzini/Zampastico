package dao;

import java.sql.SQLException;
import java.util.List;
import model.RecensioneBEAN;

public interface RecensioneDAO {
    void doSave(RecensioneBEAN recensione) throws SQLException;
    boolean doDelete(int idUtente, int idVarianteProdotto) throws SQLException;
    RecensioneBEAN doRetrieveByKey(int idUtente, int idVarianteProdotto) throws SQLException;
    List<RecensioneBEAN> doRetrieveByUtente(int idUtente) throws SQLException;
    List<RecensioneBEAN> doRetrieveByVariante(int idVarianteProdotto) throws SQLException;
    List<RecensioneBEAN> doRetrieveAll(String order) throws SQLException;
}