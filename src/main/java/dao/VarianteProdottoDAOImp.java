package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import model.VarianteProdottoBEAN;

public class VarianteProdottoDAOImp implements VarianteProdottoDAO {

    private static final String TABLE_NAME = "VarianteProdotto";
    private DataSource ds;

    public VarianteProdottoDAOImp(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(VarianteProdottoBEAN variante) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (id_prodotto, formato, prezzo_listino, id_sconto, disponibile) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            
            preparedStatement.setInt(1, variante.getIdProdotto());
            preparedStatement.setString(2, variante.getFormato());
            preparedStatement.setDouble(3, variante.getPrezzoListino());
            if (variante.getIdSconto() == null || variante.getIdSconto() == 0) {
                preparedStatement.setNull(4, Types.INTEGER);
            } else {
                preparedStatement.setInt(4, variante.getIdSconto());
            }
            
            preparedStatement.setBoolean(5, variante.isDisponibile());
            
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public synchronized boolean doUpdate(VarianteProdottoBEAN variante) throws SQLException {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET id_prodotto = ?, formato = ?, prezzo_listino = ?, id_sconto = ?, disponibile = ? WHERE id_varianteProdotto = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            
            preparedStatement.setInt(1, variante.getIdProdotto());
            preparedStatement.setString(2, variante.getFormato());
            preparedStatement.setDouble(3, variante.getPrezzoListino());
            
            if (variante.getIdSconto() == null || variante.getIdSconto() == 0) {
                preparedStatement.setNull(4, Types.INTEGER);
            } else {
                preparedStatement.setInt(4, variante.getIdSconto());
            }
            
            preparedStatement.setBoolean(5, variante.isDisponibile());
            preparedStatement.setInt(6, variante.getIdVarianteProdotto());
            
            return preparedStatement.executeUpdate() != 0;
        }
    }

    @Override
    public synchronized boolean doDelete(int idVarianteProdotto) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id_varianteProdotto = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            
            preparedStatement.setInt(1, idVarianteProdotto);
            
            return preparedStatement.executeUpdate() != 0;
        }
    }

    @Override
    public VarianteProdottoBEAN doRetrieveByKey(int idVarianteProdotto) throws SQLException {
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_varianteProdotto = ?";
        VarianteProdottoBEAN bean = null;
        
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            
            preparedStatement.setInt(1, idVarianteProdotto);
            
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    bean = extractBean(rs);
                }
            }
        }
        return bean;
    }

    @Override
    public List<VarianteProdottoBEAN> doRetrieveAll() throws SQLException {
        List<VarianteProdottoBEAN> varianti = new LinkedList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME;
        
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet rs = preparedStatement.executeQuery()) {
            
            while (rs.next()) {
                varianti.add(extractBean(rs));
            }
        }
        return varianti;
    }

    private VarianteProdottoBEAN extractBean(ResultSet rs) throws SQLException {
        VarianteProdottoBEAN bean = new VarianteProdottoBEAN();
        
        bean.setIdVarianteProdotto(rs.getInt("id_varianteProdotto"));
        bean.setIdProdotto(rs.getInt("id_prodotto"));
        bean.setFormato(rs.getString("formato"));
        bean.setPrezzoListino(rs.getDouble("prezzo_listino"));
        bean.setDisponibile(rs.getBoolean("disponibile"));
        try {
            int idSconto = rs.getInt("id_sconto");
            if (rs.wasNull()) {
                bean.setIdSconto(null);
            } else {
                bean.setIdSconto(idSconto);
            }
        } catch (SQLException e) {
        }

        return bean;
    }
    
    public List<VarianteProdottoBEAN> doRetrieveByProdotto(int idProdotto) throws SQLException {
        List<VarianteProdottoBEAN> varianti = new java.util.ArrayList<>();
        String sql = "SELECT * FROM VarianteProdotto WHERE id_prodotto = ?";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProdotto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    varianti.add(extractBean(rs));
                }
            }
        }
        return varianti;
    }
}