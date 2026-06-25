package dao;

import model.RecensioneBEAN;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class RecensioneDAOImp implements RecensioneDAO {

    private static final String TABLE_NAME = "Recensione";
    private final DataSource ds;

    public RecensioneDAOImp(DataSource ds) {
        this.ds = ds;
    }
    
    @Override
    public synchronized void doSave(RecensioneBEAN recensione) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (id_utente, id_varianteProdotto, votazione, commento) VALUES (?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, recensione.getIdUtente());
            preparedStatement.setInt(2, recensione.getIdVarianteProdotto());
            preparedStatement.setInt(3, recensione.getVotazione());
            preparedStatement.setString(4, recensione.getCommento());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public synchronized boolean doDelete(int idUtente, int idVarianteProdotto) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id_utente = ? AND id_varianteProdotto = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, idUtente);
            preparedStatement.setInt(2, idVarianteProdotto);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected != 0;
        }
    }

    @Override
    public synchronized RecensioneBEAN doRetrieveByKey(int idUtente, int idVarianteProdotto) throws SQLException {
    	String selectSQL = "SELECT r.*, p.nome AS nome_prodotto, p.brand AS brand_prodotto, vp.formato AS formato_prodotto " +
                "FROM " + TABLE_NAME + " r " +
                "JOIN VarianteProdotto vp ON r.id_varianteProdotto = vp.id_varianteProdotto " +
                "JOIN Prodotto p ON vp.id_prodotto = p.id_prodotto " +
                "WHERE r.id_utente = ? ORDER BY r.data_creazione DESC";
                           
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, idUtente);
            preparedStatement.setInt(2, idVarianteProdotto);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    RecensioneBEAN bean = new RecensioneBEAN();
                    bean.setIdUtente(resultSet.getInt("id_utente"));
                    bean.setIdVarianteProdotto(resultSet.getInt("id_varianteProdotto"));
                    bean.setVotazione(resultSet.getInt("votazione"));
                    bean.setCommento(resultSet.getString("commento"));
                    Timestamp varData = resultSet.getTimestamp("data_creazione");
                    if (varData != null) {
                        bean.setDataCreazione(varData.toLocalDateTime());
                    }
                    bean.setNomeProdotto(resultSet.getString("nome_prodotto"));
                    bean.setBrandProdotto(resultSet.getString("brand_prodotto"));
                    bean.setFormatoProdotto(resultSet.getString("formato_prodotto"));
                    
                    return bean;
                }
            }
        }
        return null;
    }

    @Override
    public synchronized List<RecensioneBEAN> doRetrieveByUtente(int idUtente) throws SQLException {
        List<RecensioneBEAN> recensioni = new LinkedList<>();
        String selectSQL = "SELECT r.*, p.nome AS nome_prodotto, p.brand AS brand_prodotto, vp.formato AS formato_prodotto " +
                           "FROM " + TABLE_NAME + " r " +
                           "JOIN VarianteProdotto vp ON r.id_varianteProdotto = vp.id_varianteProdotto " +
                           "JOIN Prodotto p ON vp.id_prodotto = p.id_prodotto " +
                           "WHERE r.id_utente = ? ORDER BY r.data_creazione DESC";
                           
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, idUtente);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    RecensioneBEAN bean = new RecensioneBEAN();
                    bean.setIdUtente(resultSet.getInt("id_utente"));
                    bean.setIdVarianteProdotto(resultSet.getInt("id_varianteProdotto"));
                    bean.setVotazione(resultSet.getInt("votazione"));
                    bean.setCommento(resultSet.getString("commento"));
                    Timestamp varData = resultSet.getTimestamp("data_creazione");
                    if (varData != null) {
                        bean.setDataCreazione(varData.toLocalDateTime());
                    }
                    bean.setNomeProdotto(resultSet.getString("nome_prodotto"));
                    bean.setBrandProdotto(resultSet.getString("brand_prodotto"));
                    bean.setFormatoProdotto(resultSet.getString("formato_prodotto"));
                    
                    recensioni.add(bean);
                }
            }
        }
        return recensioni;
    }

    @Override
    public synchronized List<RecensioneBEAN> doRetrieveByVariante(int idVarianteProdotto) throws SQLException {
        List<RecensioneBEAN> recensioni = new LinkedList<>();
        String selectSQL = "SELECT r.*, p.nome AS nome_prodotto, p.brand AS brand_prodotto, vp.formato AS formato_prodotto " +
                "FROM " + TABLE_NAME + " r " +
                "JOIN VarianteProdotto vp ON r.id_varianteProdotto = vp.id_varianteProdotto " +
                "JOIN Prodotto p ON vp.id_prodotto = p.id_prodotto " +
                "WHERE r.id_utente = ? ORDER BY r.data_creazione DESC";
                           
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, idVarianteProdotto);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    RecensioneBEAN bean = new RecensioneBEAN();
                    bean.setIdUtente(resultSet.getInt("id_utente"));
                    bean.setIdVarianteProdotto(resultSet.getInt("id_varianteProdotto"));
                    bean.setVotazione(resultSet.getInt("votazione"));
                    bean.setCommento(resultSet.getString("commento"));
                    Timestamp varData = resultSet.getTimestamp("data_creazione");
                    if (varData != null) {
                        bean.setDataCreazione(varData.toLocalDateTime());
                    }
                    bean.setNomeProdotto(resultSet.getString("nome_prodotto"));
                    bean.setBrandProdotto(resultSet.getString("brand_prodotto"));
                    bean.setFormatoProdotto(resultSet.getString("formato_prodotto"));
                    
                    recensioni.add(bean);
                }
            }
        }
        return recensioni;
    }

    @Override
    public synchronized List<RecensioneBEAN> doRetrieveAll(String order) throws SQLException {
        List<RecensioneBEAN> recensioni = new LinkedList<>();
        String selectSQL = "SELECT r.*, p.nome AS nome_prodotto, p.brand AS brand_prodotto, vp.formato AS formato_prodotto " +
                "FROM " + TABLE_NAME + " r " +
                "JOIN VarianteProdotto vp ON r.id_varianteProdotto = vp.id_varianteProdotto " +
                "JOIN Prodotto p ON vp.id_prodotto = p.id_prodotto " +
                "WHERE r.id_utente = ? ORDER BY r.data_creazione DESC";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
             
            while (resultSet.next()) {
                RecensioneBEAN bean = new RecensioneBEAN();
                bean.setIdUtente(resultSet.getInt("id_utente"));
                bean.setIdVarianteProdotto(resultSet.getInt("id_varianteProdotto"));
                bean.setVotazione(resultSet.getInt("votazione"));
                bean.setCommento(resultSet.getString("commento"));
                Timestamp varData = resultSet.getTimestamp("data_creazione");
                if (varData != null) {
                    bean.setDataCreazione(varData.toLocalDateTime());
                }
                bean.setNomeProdotto(resultSet.getString("nome_prodotto"));
                bean.setBrandProdotto(resultSet.getString("brand_prodotto"));
                bean.setFormatoProdotto(resultSet.getString("formato_prodotto"));
                
                recensioni.add(bean);
            }
        }
        return recensioni;
    }
}