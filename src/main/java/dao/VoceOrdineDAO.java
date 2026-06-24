package dao;

import java.sql.SQLException;
import java.util.List;

import model.VoceOrdineBEAN;

public interface VoceOrdineDAO {
    void doSave(VoceOrdineBEAN voceOrdine) throws SQLException;
    List<VoceOrdineBEAN> doRetrieveByOrdine(int idOrdine) throws SQLException;
    boolean doDelete(int idOrdine, int idVarianteProdotto) throws SQLException;
}