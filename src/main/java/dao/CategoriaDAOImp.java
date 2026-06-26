package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import model.CategoriaBEAN;

public class CategoriaDAOImp implements CategoriaDAO {

    private static final String TABLE_NAME = "Categoria";
    private DataSource ds;

    public CategoriaDAOImp(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(CategoriaBEAN categoria) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (nome, id_supercategoria) VALUES (?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, categoria.getNome());
            if (categoria.getIdSuper() == 0) {
                preparedStatement.setNull(2, Types.INTEGER);
            } else {
                preparedStatement.setInt(2, categoria.getIdSuper());
            }
            
            preparedStatement.executeUpdate();
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setIdCategoria(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public synchronized boolean doUpdate(CategoriaBEAN categoria) throws SQLException {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET nome = ?, id_supercategoria = ? WHERE id_categoria = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            
            preparedStatement.setString(1, categoria.getNome());
            if (categoria.getIdSuper() == 0) {
                preparedStatement.setNull(2, Types.INTEGER);
            } else {
                preparedStatement.setInt(2, categoria.getIdSuper());
            }
            preparedStatement.setInt(3, categoria.getIdCategoria());
            return preparedStatement.executeUpdate() != 0;
        }
    }

    @Override
    public synchronized boolean doDelete(int idCategoria) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id_categoria = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, idCategoria);
            return preparedStatement.executeUpdate() != 0;
        }
    }

    @Override
    public CategoriaBEAN doRetrieveById(int idCategoria) throws SQLException {
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_categoria = ?";
        CategoriaBEAN bean = null;
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, idCategoria);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    bean = extractBean(rs);
                }
            }
        }
        return bean;
    }

    @Override
    public List<CategoriaBEAN> doRetrieveAll() throws SQLException {
        List<CategoriaBEAN> categorie = new LinkedList<>();
        String selectSQL = "SELECT c1.id_categoria, c1.nome, c1.id_supercategoria, c2.nome AS nome_super " +
                           "FROM " + TABLE_NAME + " c1 " +
                           "LEFT JOIN " + TABLE_NAME + " c2 ON c1.id_supercategoria = c2.id_categoria " +
                           "ORDER BY c2.nome, c1.nome";
        
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet rs = preparedStatement.executeQuery()) {
            
            while (rs.next()) {
                CategoriaBEAN bean = new CategoriaBEAN();
                bean.setIdCategoria(rs.getInt("id_categoria"));
                bean.setNome(rs.getString("nome"));
                
                int idSuper = rs.getInt("id_supercategoria");
                if (rs.wasNull()) {
                    bean.setidSuper(0); 
                } else {
                    bean.setidSuper(idSuper);
                }
                bean.setNomeSuper(rs.getString("nome_super"));
                categorie.add(bean);
            }
        }
        return categorie;
    }

    private CategoriaBEAN extractBean(ResultSet rs) throws SQLException {
        CategoriaBEAN bean = new CategoriaBEAN();
        bean.setIdCategoria(rs.getInt("id_categoria"));
        bean.setNome(rs.getString("nome"));
        try {
            int idSuper = rs.getInt("id_supercategoria");
            if (rs.wasNull()) {
                bean.setidSuper(0); 
            } else {
                bean.setidSuper(idSuper);
            }
        } catch (SQLException e) {}
        return bean;
    }
    public CategoriaBEAN doRetrieveRootByName(String nomeRoot) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE LOWER(nome) = LOWER(?) AND (id_supercategoria IS NULL OR id_supercategoria = 0)";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nomeRoot.trim()); 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractBean(rs);
                }
            }
        }
        return null; 
    }

    public CategoriaBEAN doRetrieveByNameAndIdSuper(String nomeSub, int idSuper) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE LOWER(nome) = LOWER(?) AND id_supercategoria = ?";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nomeSub.trim()); 
            ps.setInt(2, idSuper); 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractBean(rs);
                }
            }
        }
        return null; 
    }
}