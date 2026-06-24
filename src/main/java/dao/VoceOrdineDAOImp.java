package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import model.VoceOrdineBEAN;

public class VoceOrdineDAOImp implements VoceOrdineDAO {

    private static final String TABLE_NAME = "VoceOrdine";
    private DataSource ds;

    public VoceOrdineDAOImp(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(VoceOrdineBEAN voceOrdine) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (id_ordine, id_varianteProdotto, quantita, prezzo_acquisto) VALUES (?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, voceOrdine.getIdOrdine());
            preparedStatement.setInt(2, voceOrdine.getIdVarianteProdotto());
            preparedStatement.setInt(3, voceOrdine.getQuantita());
            preparedStatement.setDouble(4, voceOrdine.getPrezzoAcquisto());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<VoceOrdineBEAN> doRetrieveByOrdine(int idOrdine) throws SQLException {
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_ordine = ?";
        List<VoceOrdineBEAN> vociOrdine = new LinkedList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, idOrdine);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    vociOrdine.add(extractBean(rs));
                }
            }
        }

        return vociOrdine;
    }

    @Override
    public synchronized boolean doDelete(int idOrdine, int idVarianteProdotto) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id_ordine = ? AND id_varianteProdotto = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, idOrdine);
            preparedStatement.setInt(2, idVarianteProdotto);
            return preparedStatement.executeUpdate() != 0;
        }
    }

    private VoceOrdineBEAN extractBean(ResultSet rs) throws SQLException {
        VoceOrdineBEAN bean = new VoceOrdineBEAN();
        bean.setIdOrdine(rs.getInt("id_ordine"));
        bean.setIdVarianteProdotto(rs.getInt("id_varianteProdotto"));
        bean.setQuantita(rs.getInt("quantita"));
        bean.setPrezzoAcquisto(rs.getDouble("prezzo_acquisto"));
        return bean;
    }
}