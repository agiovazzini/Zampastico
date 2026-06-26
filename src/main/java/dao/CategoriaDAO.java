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
    public CategoriaBEAN doRetrieveRootByName(String nomeRoot) throws SQLException;
    public CategoriaBEAN doRetrieveByNameAndIdSuper(String nomeSub, int idSuper) throws SQLException;
}