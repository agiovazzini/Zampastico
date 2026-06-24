package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;
import model.IndirizzoBEAN;

public class IndirizzoDAOImp implements IndirizzoDAO {
	private static final String TABLE_NAME = "Indirizzo";
    private DataSource ds;
    public static final int ADDRESS_LIMIT = 5;
    
    public IndirizzoDAOImp(DataSource ds) {
        this.ds = ds;
    }
    
    @Override
    public synchronized void doSave(IndirizzoBEAN address) throws SQLException {
        String countSQL = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE id_utente = ?";
        String resetSQL = "UPDATE " + TABLE_NAME + " SET predefinito = false WHERE id_utente = ?";
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (id_utente, citta, provincia, via, cap, predefinito) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ds.getConnection()) {
            try (PreparedStatement preparedStatementCount = connection.prepareStatement(countSQL)) {
                preparedStatementCount.setInt(1, address.getIdUtente());
                try (ResultSet resultSet = preparedStatementCount.executeQuery()){
                    if (resultSet.next() ) {
                        int count = resultSet.getInt(1);
                        if (count >= ADDRESS_LIMIT) {
                            throw new IllegalStateException("Hai raggiunto il limite massimo di indirizzi");
                        }
                    }
                }
            }
            connection.setAutoCommit(false);
            try {
                if (address.isPredefinito()) {
                    try (PreparedStatement preparedStatementReset = connection.prepareStatement(resetSQL)) {
                    	preparedStatementReset.setInt(1, address.getIdUtente());
                    	preparedStatementReset.executeUpdate();
                    }
                }
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                    preparedStatement.setInt(1, address.getIdUtente());
                    preparedStatement.setString(2, address.getCitta());
                    preparedStatement.setString(3, address.getProvincia());
                    preparedStatement.setString(4, address.getVia());
                    preparedStatement.setString(5, address.getCap());
                    preparedStatement.setBoolean(6, address.isPredefinito());
                    preparedStatement.executeUpdate();
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    @Override
    public synchronized boolean doUpdate(IndirizzoBEAN address) throws SQLException {
        String resetSQL = "UPDATE " + TABLE_NAME + " SET predefinito = false WHERE id_utente = ?";
        String updateSQL = "UPDATE " + TABLE_NAME + " SET citta = ?, provincia = ?, via = ?, cap = ?, predefinito = ? WHERE id_indirizzo = ? AND id_utente = ?";
        try (Connection connection = ds.getConnection()) {
            connection.setAutoCommit(false);
            try {
                if (address.isPredefinito()) {
                    try (PreparedStatement psReset = connection.prepareStatement(resetSQL)) {
                        psReset.setInt(1, address.getIdUtente());
                        psReset.executeUpdate();
                    }
                }
                int rowsAffected;
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                    preparedStatement.setString(1, address.getCitta());
                    preparedStatement.setString(2, address.getProvincia());
                    preparedStatement.setString(3, address.getVia());
                    preparedStatement.setString(4, address.getCap());
                    preparedStatement.setBoolean(5, address.isPredefinito());
                    preparedStatement.setInt(6, address.getIdIndirizzo());
                    preparedStatement.setInt(7, address.getIdUtente());
                    rowsAffected = preparedStatement.executeUpdate();
                }
                connection.commit();
                return rowsAffected != 0;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    @Override
    public synchronized boolean doDelete(int idAddress, int idUser) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id_indirizzo = ? AND id_utente = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, idAddress);
            preparedStatement.setInt(2, idUser);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected != 0;
        }
    }
    
    @Override
    public IndirizzoBEAN doRetrieveByKey(int idAddress) throws SQLException {
        IndirizzoBEAN bean = null;
        String selectSQL = "SELECT id_indirizzo, id_utente, citta, provincia, via, cap, predefinito FROM " + TABLE_NAME + " WHERE id_indirizzo = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, idAddress);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                	bean = new IndirizzoBEAN();
                    bean.setIdIndirizzo(resultSet.getInt("id_indirizzo"));
                    bean.setIdUtente(resultSet.getInt("id_utente"));
                    bean.setCitta(resultSet.getString("citta"));
                    bean.setProvincia(resultSet.getString("provincia"));
                    bean.setVia(resultSet.getString("via"));
                    bean.setCap(resultSet.getString("cap"));
                    bean.setPredefinito(resultSet.getBoolean("predefinito"));
                }
            }
        }
        return bean;
    }
    
    @Override
    public List<IndirizzoBEAN> doRetrieveByUser(int idUser) throws SQLException {
        List<IndirizzoBEAN> indirizzi = new LinkedList<>();
        String selectSQL = "SELECT id_indirizzo, id_utente, citta, provincia, via, cap, predefinito FROM " + TABLE_NAME + " WHERE id_utente = ? ORDER BY predefinito DESC, id_indirizzo ASC";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, idUser);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                	IndirizzoBEAN bean = new IndirizzoBEAN();
                    bean.setIdIndirizzo(resultSet.getInt("id_indirizzo"));
                    bean.setIdUtente(resultSet.getInt("id_utente"));
                    bean.setCitta(resultSet.getString("citta"));
                    bean.setProvincia(resultSet.getString("provincia"));
                    bean.setVia(resultSet.getString("via"));
                    bean.setCap(resultSet.getString("cap"));
                    bean.setPredefinito(resultSet.getBoolean("predefinito"));
                	indirizzi.add(bean);
                }
            }
        }
        return indirizzi;
    }
    
    @Override
    public synchronized boolean doSetPredefinito(int idAddress, int idUser) throws SQLException {
        String resetSQL = "UPDATE " + TABLE_NAME + " SET predefinito = false WHERE id_utente = ?";
        String setSQL = "UPDATE " + TABLE_NAME + " SET predefinito = true WHERE id_indirizzo = ? AND id_utente = ?";
        try (Connection connection = ds.getConnection()) {
        	connection.setAutoCommit(false);
        	try (PreparedStatement psReset = connection.prepareStatement(resetSQL);
                 PreparedStatement psSet = connection.prepareStatement(setSQL)) {
                psReset.setInt(1, idUser);
                psReset.executeUpdate();
                psSet.setInt(1, idAddress);
                psSet.setInt(2, idUser);
                int rowsAffected = psSet.executeUpdate();
                connection.commit();
                return rowsAffected != 0;
            } catch (SQLException e) {
                connection.rollback();
                throw e; 
            } finally {
                connection.setAutoCommit(true);
            }
        } 
    }
}