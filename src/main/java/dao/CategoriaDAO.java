package dao;

import java.sql.SQLException;
import java.util.List;

import model.CategoriaBEAN;

public interface CategoriaDAO {
    void doSave(CategoriaBEAN categoria) throws SQLException;
    boolean doUpdate(CategoriaBEAN categoria) throws SQLException;
    boolean doDelete(int idCategoria) throws SQLException;
    CategoriaBEAN doRetrieveById(int idCategoria) throws SQLException;
    List<CategoriaBEAN> doRetrieveAll() throws SQLException;
}