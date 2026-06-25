package dao;

import java.sql.SQLException;
import java.util.List;

import model.VarianteProdottoBEAN;

public interface VarianteProdottoDAO {
    void doSave(VarianteProdottoBEAN variante) throws SQLException;
    boolean doUpdate(VarianteProdottoBEAN variante) throws SQLException;
    boolean doDelete(int idVarianteProdotto) throws SQLException;
    VarianteProdottoBEAN doRetrieveByKey(int idVarianteProdotto) throws SQLException;
    List<VarianteProdottoBEAN> doRetrieveAll() throws SQLException;
}