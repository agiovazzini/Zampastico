package dao;

import java.sql.SQLException;
import java.util.List;

import model.CategoriaBEAN;

public interface CategoriaDAO {
    void insert(CategoriaBEAN categoria) throws SQLException;
    boolean update(CategoriaBEAN categoria) throws SQLException;
    boolean delete(int idCategoria) throws SQLException;
    CategoriaBEAN findById(int idCategoria) throws SQLException;
    List<CategoriaBEAN> findAll() throws SQLException;
}