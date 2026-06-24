package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;

import model.IndirizzoBEAN;
import model.OrdineBEAN;
import model.OrdineBEAN.StatoOrdine;
import model.VoceOrdineBEAN;

public class OrdineDAOImp implements OrdineDAO {

    private static final String TABLE_NAME = "Ordine";
    private static final String TABLE_VOCE = "VoceOrdine";
    private DataSource ds;

    public OrdineDAOImp(DataSource ds) {
        this.ds = ds;
    }

    public synchronized void doSave(OrdineBEAN order) throws SQLException {
        String insertOrdineSQL = "INSERT INTO " + TABLE_NAME + " (id_utente, id_coupon, totale, spedizione_citta, spedizione_provincia, spedizione_via, spedizione_cap, stato, data_ordine) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertVoceSQL = "INSERT INTO " + TABLE_VOCE + " (id_ordine, id_variante_prodotto, quantita, prezzo_acquisto) VALUES (?, ?, ?, ?)";

        try (Connection connection = ds.getConnection()) {
            try {
                connection.setAutoCommit(false);
                int idOrdine = -1;
                try (PreparedStatement psOrdine = connection.prepareStatement(insertOrdineSQL, Statement.RETURN_GENERATED_KEYS)) {
                    psOrdine.setInt(1, order.getIdUtente());
                    if (order.getIdCoupon() != null) {
                        psOrdine.setInt(2, order.getIdCoupon());
                    } else {
                        psOrdine.setNull(2, Types.INTEGER);
                    }
                    psOrdine.setBigDecimal(3, order.getTotale());
                    psOrdine.setString(4, order.getSpedizioneCitta());
                    psOrdine.setString(5, order.getSpedizioneProvincia());
                    psOrdine.setString(6, order.getSpedizioneVia());
                    psOrdine.setString(7, order.getSpedizioneCap());
                    psOrdine.setString(8, order.getStato().name());
                    psOrdine.setObject(9, order.getDataOrdine());
                    psOrdine.executeUpdate();
                    try (ResultSet generatedKeys = psOrdine.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            idOrdine = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Errore: Impossibile recuperare l'ID dell'ordine generato.");
                        }
                    }
                }
                if (order.getVociOrdine() != null && !order.getVociOrdine().isEmpty()) {
                    try (PreparedStatement psVoce = connection.prepareStatement(insertVoceSQL)) {
                        for (VoceOrdineBEAN voce : order.getVociOrdine()) {
                            psVoce.setInt(1, idOrdine);
                            psVoce.setInt(2, voce.getIdVarianteProdotto());
                            psVoce.setInt(3, voce.getQuantita());
                            psVoce.setDouble(4, voce.getPrezzoAcquisto());
                            psVoce.executeUpdate(); 
                        }
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                if (connection != null) {
                    connection.rollback();
                }
                throw e;
            }
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

    public synchronized OrdineBEAN doRetrieveByKey(int idOrdine) throws SQLException {
        String selectOrdineSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_ordine = ?";
        String selectVociSQL = "SELECT * FROM " + TABLE_VOCE + " WHERE id_ordine = ?";
        OrdineBEAN order = null;
        try (Connection connection = ds.getConnection()) {
            try (PreparedStatement psOrdine = connection.prepareStatement(selectOrdineSQL)) {
                psOrdine.setInt(1, idOrdine);
                try (ResultSet rsOrdine = psOrdine.executeQuery()) {
                    if (rsOrdine.next()) {
                        order = new OrdineBEAN();
                        order.setIdOrdine(rsOrdine.getInt("id_ordine")); 
                        order.setIdUtente(rsOrdine.getInt("id_utente"));
                        int idCoupon = rsOrdine.getInt("id_coupon");
                        if (!rsOrdine.wasNull()) {
                            order.setIdCoupon(idCoupon);
                        } else {
                            order.setIdCoupon(null);
                        }
                        order.setTotale(rsOrdine.getBigDecimal("totale"));
                        order.setSpedizioneCitta(rsOrdine.getString("spedizione_citta"));
                        order.setSpedizioneProvincia(rsOrdine.getString("spedizione_provincia"));
                        order.setSpedizioneVia(rsOrdine.getString("spedizione_via"));
                        order.setSpedizioneCap(rsOrdine.getString("spedizione_cap"));
                        order.setStato(StatoOrdine.valueOf(rsOrdine.getString("stato"))); 
                        order.setDataOrdine(rsOrdine.getObject("data_ordine", java.time.LocalDateTime.class));
                    }
                }
            }
            if (order != null) {
                List<VoceOrdineBEAN> voci = new ArrayList<>();
                try (PreparedStatement psVoci = connection.prepareStatement(selectVociSQL)) {
                    psVoci.setInt(1, idOrdine);
                    try (ResultSet rsVoci = psVoci.executeQuery()) {
                        while (rsVoci.next()) {
                            VoceOrdineBEAN voce = new VoceOrdineBEAN();
                            voce.setIdOrdine(rsVoci.getInt("id_ordine"));
                            voce.setIdVarianteProdotto(rsVoci.getInt("id_variante_prodotto"));
                            voce.setQuantita(rsVoci.getInt("quantita"));
                            voce.setPrezzoAcquisto(rsVoci.getDouble("prezzo_acquisto"));
                            voci.add(voce); 
                        }
                    }
                }
                order.setVociOrdine(voci);
            }
        }
        return order;
    }

    public synchronized List<OrdineBEAN> doRetrieveByUser(int idUtente) throws SQLException {
        String selectOrdiniSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_utente = ? ORDER BY data_ordine DESC";
        List<OrdineBEAN> ordini = new ArrayList<>();
        VoceOrdineDAOImp voceDAO = new VoceOrdineDAOImp(this.ds);
        try (Connection connection = ds.getConnection();
             PreparedStatement psOrdini = connection.prepareStatement(selectOrdiniSQL)) {
            psOrdini.setInt(1, idUtente);
            try (ResultSet rsOrdini = psOrdini.executeQuery()) {
                while (rsOrdini.next()) {
                    OrdineBEAN order = extractBean(rsOrdini);
                    ordini.add(order);
                }
            }
            for (OrdineBEAN order : ordini) {
                List<VoceOrdineBEAN> voci = voceDAO.doRetrieveByOrdine(order.getIdOrdine());
                order.setVociOrdine(voci);
            }
        } catch (SQLException e) {
            System.err.println("ERRORE SQL IN doRetrieveByUser: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return ordini;
    }

    @Override
    public synchronized List<OrdineBEAN> doRetrieveAll() throws SQLException {
        String selectOrdiniSQL = "SELECT * FROM " + TABLE_NAME + " ORDER BY data_ordine DESC";
        String selectVociSQL = "SELECT * FROM " + TABLE_VOCE + " WHERE id_ordine = ?";
        List<OrdineBEAN> ordini = new ArrayList<>();
        try (Connection connection = ds.getConnection()) {
            try (PreparedStatement psOrdini = connection.prepareStatement(selectOrdiniSQL);
                 ResultSet rsOrdini = psOrdini.executeQuery()) {
                while (rsOrdini.next()) {
                    OrdineBEAN order = new OrdineBEAN();
                    order.setIdOrdine(rsOrdini.getInt("id_ordine"));
                    order.setIdUtente(rsOrdini.getInt("id_utente"));
                    int idCoupon = rsOrdini.getInt("id_coupon");
                    if (!rsOrdini.wasNull()) {
                        order.setIdCoupon(idCoupon);
                    } else {
                        order.setIdCoupon(null);
                    }
                    order.setTotale(rsOrdini.getBigDecimal("totale"));
                    order.setSpedizioneCitta(rsOrdini.getString("spedizione_citta"));
                    order.setSpedizioneProvincia(rsOrdini.getString("spedizione_provincia"));
                    order.setSpedizioneVia(rsOrdini.getString("spedizione_via"));
                    order.setSpedizioneCap(rsOrdini.getString("spedizione_cap"));
                    order.setStato(StatoOrdine.valueOf(rsOrdini.getString("stato")));
                    order.setDataOrdine(rsOrdini.getObject("data_ordine", java.time.LocalDateTime.class));
                    order.setVociOrdine(new ArrayList<>());
                    ordini.add(order);
                }
            }
            if (!ordini.isEmpty()) {
                try (PreparedStatement psVoci = connection.prepareStatement(selectVociSQL)) {
                    for (OrdineBEAN order : ordini) {
                        psVoci.setInt(1, order.getIdOrdine());
                        try (ResultSet rsVoci = psVoci.executeQuery()) {
                            while (rsVoci.next()) {
                                VoceOrdineBEAN voce = new VoceOrdineBEAN();
                                voce.setIdOrdine(rsVoci.getInt("id_ordine"));
                                voce.setIdVarianteProdotto(rsVoci.getInt("id_variante_prodotto"));
                                voce.setQuantita(rsVoci.getInt("quantita"));
                                voce.setPrezzoAcquisto(rsVoci.getDouble("prezzo_acquisto"));
                                order.getVociOrdine().add(voce);
                            }
                        }
                    }
                }
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
                String insertVoceOrdineSQL = "INSERT INTO " + TABLE_VOCE + " (id_ordine, id_varianteProdotto, quantita, prezzo_acquisto) VALUES (?, ?, ?, ?)";
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