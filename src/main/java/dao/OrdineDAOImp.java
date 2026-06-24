package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;

import model.IndirizzoBEAN;
import model.OrdineBEAN;
import model.OrdineBEAN.StatoOrdine;

public class OrdineDAOImp implements OrdineDAO {

    private static final String TABLE_NAME = "Ordine";
    private DataSource ds;

    public OrdineDAOImp(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(OrdineBEAN order) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (id_utente, id_coupon, totale, spedizione_citta, spedizione_provincia, spedizione_via, spedizione_cap, stato, data_ordine) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
        	preparedStatement.setInt(1, order.getIdUtente());
            if (order.getIdCoupon() != null) {
            	preparedStatement.setInt(2, order.getIdCoupon());
            } else {
            	preparedStatement.setNull(2, Types.INTEGER);
            }
            preparedStatement.setBigDecimal(3, order.getTotale());
            preparedStatement.setString(4, order.getSpedizioneCitta());
            preparedStatement.setString(5, order.getSpedizioneProvincia());
            preparedStatement.setString(6, order.getSpedizioneVia());
            preparedStatement.setString(7, order.getSpedizioneCap());
            preparedStatement.setString(8, order.getStato().name());
            preparedStatement.setObject(9, order.getDataOrdine());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public synchronized boolean doUpdateStato(int idOrder, StatoOrdine newStatus) throws SQLException {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET stato = ? WHERE id_ordine = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
        	preparedStatement.setString(1, newStatus.name());
        	preparedStatement.setInt(2, idOrder);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected != 0;
        }
    }

    @Override
    public OrdineBEAN doRetrieveByKey(int idOrder) throws SQLException {
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_ordine = ?";
        OrdineBEAN order = null;
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, idOrder);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    order = extractBean(resultSet);
                }
            }
        }
        return order;
    }

    @Override
    public List<OrdineBEAN> doRetrieveByUser(int idUser) throws SQLException {
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_utente = ? ORDER BY data_ordine DESC";
        List<OrdineBEAN> ordini = new LinkedList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
        	preparedStatement.setInt(1, idUser);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ordini.add(extractBean(resultSet));
                }
            }
        }
        return ordini;
    }

    @Override
    public List<OrdineBEAN> doRetrieveAll() throws SQLException {
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " ORDER BY data_ordine DESC";
        List<OrdineBEAN> ordini = new LinkedList<>();
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                ordini.add(extractBean(resultSet));
            }
        }
        return ordini;
    }

    @Override
    public synchronized void doCheckout(int idUser, Integer idSavedAddress, IndirizzoBEAN manualAddress, boolean saveCheck, Integer idCoupon, String paymentMethod) throws SQLException {
        try (Connection connection = ds.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int idCarrello = -1;
                String checkCarrello = "SELECT id_carrello FROM Carrello WHERE id_utente = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(checkCarrello)) {
                	preparedStatement.setInt(1, idUser);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            idCarrello = resultSet.getInt("id_carrello");
                        } else {
                            throw new SQLException("Carrello non trovato per l'utente.");
                        }
                    }
                }

                String checkElementi = "SELECT COUNT(*) FROM VoceCarrello WHERE id_carrello = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(checkElementi)) {
                	preparedStatement.setInt(1, idCarrello);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next() && resultSet.getInt(1) == 0) {
                            throw new SQLException("Il carrello è vuoto. Impossibile procedere.");
                        }
                    }
                }
                String citta, provincia, via, cap;
                if (idSavedAddress != null) {
                    String getIndirizzo = "SELECT citta, provincia, via, cap FROM Indirizzo WHERE id_indirizzo = ? AND id_utente = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(getIndirizzo)) {
                    	preparedStatement.setInt(1, idSavedAddress);
                    	preparedStatement.setInt(2, idUser);
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                citta = resultSet.getString("citta");
                                provincia = resultSet.getString("provincia");
                                via = resultSet.getString("via");
                                cap = resultSet.getString("cap");
                            } else {
                                throw new SQLException("Indirizzo salvato non valido o non appartenente all'utente.");
                            }
                        }
                    }
                } else {
                    citta = manualAddress.getCitta();
                    provincia = manualAddress.getProvincia();
                    via = manualAddress.getVia();
                    cap = manualAddress.getCap();

                    if (saveCheck) {
                        String insertIndirizzoSQL = "INSERT INTO Indirizzo (id_utente, citta, provincia, via, cap, predefinito) VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertIndirizzoSQL)) {
                        	preparedStatement.setInt(1, idUser);
                        	preparedStatement.setString(2, citta);
                        	preparedStatement.setString(3, provincia);
                        	preparedStatement.setString(4, via);
                        	preparedStatement.setString(5, cap);
                        	preparedStatement.setBoolean(6, false);
                        	preparedStatement.executeUpdate();
                        }
                    }
                }
                double scontoCoupon = 0.0;
                if (idCoupon != null) {
                    String getCoupon = "SELECT percentuale_sconto FROM Coupon WHERE id_coupon = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(getCoupon)) {
                    	preparedStatement.setInt(1, idCoupon);
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                scontoCoupon = resultSet.getDouble("percentuale_sconto");
                            }
                        }
                    }
                }
                int idOrdine = -1;
                String insertOrdineSQL = "INSERT INTO Ordine (id_utente, id_coupon, totale, spedizione_citta, spedizione_provincia, spedizione_via, spedizione_cap) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertOrdineSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setInt(1, idUser);
                    if (idCoupon != null) {
                    	preparedStatement.setInt(2, idCoupon);
                    } else {
                    	preparedStatement.setNull(2, java.sql.Types.INTEGER);
                    }
                    preparedStatement.setDouble(3, 0.0);
                    preparedStatement.setString(4, citta);
                    preparedStatement.setString(5, provincia);
                    preparedStatement.setString(6, via);
                    preparedStatement.setString(7, cap);
                    preparedStatement.executeUpdate();

                    try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            idOrdine = resultSet.getInt(1);
                        } else {
                            throw new SQLException("Creazione ordine fallita, ID non generato.");
                        }
                    }
                }

                double totaleOrdineTemp = 0.0;
                
                String getVociCarrelloSQL = 
                    "SELECT vc.id_varianteProdotto, vc.quantita, vp.prezzo_listino, s.percentuale_sconto " +
                    "FROM VoceCarrello vc " +
                    "JOIN VarianteProdotto vp ON vc.id_varianteProdotto = vp.id_varianteProdotto " +
                    "LEFT JOIN Sconto s ON vp.id_sconto = s.id_sconto " +
                    "WHERE vc.id_carrello = ?";

                String insertVoceOrdineSQL = "INSERT INTO VoceOrdine (id_ordine, id_varianteProdotto, quantita, prezzo_acquisto) VALUES (?, ?, ?, ?)";
                String updateStockSQL = "UPDATE InventarioStock SET quantita_disponibile = quantita_disponibile - ?, quantita_prenotata = quantita_prenotata + ? WHERE id_varianteProdotto = ?";

                try (PreparedStatement preparedStatementGetVoci = connection.prepareStatement(getVociCarrelloSQL);
                     PreparedStatement preparedStatementInsertVoce = connection.prepareStatement(insertVoceOrdineSQL);
                     PreparedStatement preparedStatementUpdateStock = connection.prepareStatement(updateStockSQL)) {

                	preparedStatementGetVoci.setInt(1, idCarrello);
                    try (ResultSet resultSet = preparedStatementGetVoci.executeQuery()) {
                        while (resultSet.next()) {
                            int idVariante = resultSet.getInt("id_varianteProdotto");
                            int quantita = resultSet.getInt("quantita");
                            double prezzoListino = resultSet.getDouble("prezzo_listino");
                            double scontoProdotto = resultSet.getDouble("percentuale_sconto");
                            if (resultSet.wasNull()) {
                                scontoProdotto = 0.0;
                            }
                            double prezzoAcquisto = prezzoListino * (1.0 - scontoProdotto);
                            prezzoAcquisto = Math.round(prezzoAcquisto * 100.0) / 100.0;
                            totaleOrdineTemp += (prezzoAcquisto * quantita);
                            preparedStatementInsertVoce.setInt(1, idOrdine);
                            preparedStatementInsertVoce.setInt(2, idVariante);
                            preparedStatementInsertVoce.setInt(3, quantita);
                            preparedStatementInsertVoce.setDouble(4, prezzoAcquisto);
                            preparedStatementInsertVoce.executeUpdate();
                            preparedStatementUpdateStock.setInt(1, quantita);
                            preparedStatementUpdateStock.setInt(2, quantita);
                            preparedStatementUpdateStock.setInt(3, idVariante);
                            preparedStatementUpdateStock.executeUpdate();
                        }
                    }
                }

                double totaleFinale = totaleOrdineTemp * (1.0 - scontoCoupon);
                totaleFinale = Math.round(totaleFinale * 100.0) / 100.0;
                String updateOrdineTotalSQL = "UPDATE Ordine SET totale = ? WHERE id_ordine = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateOrdineTotalSQL)) {
                	preparedStatement.setDouble(1, totaleFinale);
                	preparedStatement.setInt(2, idOrdine);
                	preparedStatement.executeUpdate();
                }
                String deleteCarrelloSQL = "DELETE FROM VoceCarrello WHERE id_carrello = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteCarrelloSQL)) {
                	preparedStatement.setInt(1, idCarrello);
                	preparedStatement.executeUpdate();
                }
                String insertPagamentoSQL = "INSERT INTO Pagamento (id_ordine, totale, metodo, stato, fattura) VALUES (?, ?, ?, 'in_elaborazione', ?)";
                try (PreparedStatement preparedStatement= connection.prepareStatement(insertPagamentoSQL)) {
                	preparedStatement.setInt(1, idOrdine);
                	preparedStatement.setDouble(2, totaleFinale);
                	preparedStatement.setString(3, paymentMethod);
                	preparedStatement.setString(4, "FATT-ZMP-" + idOrdine); 
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

    private OrdineBEAN extractBean(ResultSet rs) throws SQLException {
        OrdineBEAN bean = new OrdineBEAN();
        bean.setIdOrdine(rs.getInt("id_ordine"));
        bean.setIdUtente(rs.getInt("id_utente"));
        int couponTemp = rs.getInt("id_coupon");
        if (rs.wasNull()) {
            bean.setIdCoupon(null);
        } else {
            bean.setIdCoupon(couponTemp);
        }
        bean.setTotale(rs.getBigDecimal("totale"));
        bean.setSpedizioneCitta(rs.getString("spedizione_citta"));
        bean.setSpedizioneProvincia(rs.getString("spedizione_provincia"));
        bean.setSpedizioneVia(rs.getString("spedizione_via"));
        bean.setSpedizioneCap(rs.getString("spedizione_cap"));
        String statoStr = rs.getString("stato");
        if (statoStr != null) {
            bean.setStato(StatoOrdine.valueOf(statoStr));
        }
        LocalDateTime data = rs.getObject("data_ordine", LocalDateTime.class);
        bean.setDataOrdine(data);
        return bean;
    }
}