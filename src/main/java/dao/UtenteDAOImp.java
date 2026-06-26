import javax.sql.DataSource;
import model.UtenteBEAN;
import model.UtenteBEAN.Ruolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class UtenteDAOImp {

    private static final String TABLE_NAME = "Utente";
    private DataSource ds = null;

    public UtenteDAOImp(DataSource ds) {
        this.ds = ds;
    }
    
    //INSERIMENTO UTENTE (Registrazione)
    public synchronized void doSave(UtenteBEAN utente) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (nome, cognome, email, pass, data_anonimizzazione, ruolo, attivo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
             
            preparedStatement.setString(1, utente.getNome());
            preparedStatement.setString(2, utente.getCognome());
            preparedStatement.setString(3, utente.getEmail());
            preparedStatement.setString(4, utente.getPass());
            
            if (utente.getDataAnonimizzazione() != null) {
                preparedStatement.setTimestamp(5, Timestamp.valueOf(utente.getDataAnonimizzazione()));
            } else {
                preparedStatement.setNull(5, java.sql.Types.TIMESTAMP);
            }
            
            // Gestione sicura dell'Enum Ruolo (se nullo, default 'cliente')
            String ruolo = utente.getRuolo() != null ? utente.getRuolo().name() : "cliente";
            preparedStatement.setString(6, ruolo);
            preparedStatement.setBoolean(7, utente.isAttivo());
            
            preparedStatement.executeUpdate();
        }
    }
    
    //RESTITUZIONE UTENTE TRAMITE ID
    public UtenteBEAN doRetrieveById(int idUtente) throws SQLException {
        UtenteBEAN bean = null;
        String selectSQL = "SELECT id_utente, nome, cognome, email, pass, data_creazione, data_anonimizzazione, ruolo, attivo " +
                           "FROM " + TABLE_NAME + " WHERE id_utente = ?";
                           
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
             
            preparedStatement.setInt(1, idUtente);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    bean = new UtenteBEAN();
                    bean.setIdUtente(resultSet.getInt("id_utente"));
                    bean.setNome(resultSet.getString("nome"));
                    bean.setCognome(resultSet.getString("cognome"));
                    bean.setEmail(resultSet.getString("email"));
                    bean.setPass(resultSet.getString("pass"));
                    
                    Timestamp varCreazione = resultSet.getTimestamp("data_creazione");
                    if (varCreazione != null) {
                        bean.setDataCreazione(varCreazione.toLocalDateTime());
                    }
                    
                    Timestamp varAnonym = resultSet.getTimestamp("data_anonimizzazione");
                    if (varAnonym != null) {
                        bean.setDataAnonimizzazione(varAnonym.toLocalDateTime());
                    }
                    
                    String varRuolo = resultSet.getString("ruolo");
                    if (varRuolo != null) {
                        bean.setRuolo(Ruolo.valueOf(varRuolo));
                    }
                    bean.setAttivo(resultSet.getBoolean("attivo"));   
                }
            }
        }
        return bean;
    }

    //AGGIORNAMENTO PROFILO UTENTE
    public synchronized boolean doUpdate(UtenteBEAN utente) throws SQLException {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET nome = ?, cognome = ?, pass = ?, data_anonimizzazione = ?, attivo = ? WHERE id_utente = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
             
            preparedStatement.setString(1, utente.getNome());
            preparedStatement.setString(2, utente.getCognome());
            preparedStatement.setString(3, utente.getPass()); 
            
            if (utente.getDataAnonimizzazione() != null) {
                preparedStatement.setTimestamp(4, Timestamp.valueOf(utente.getDataAnonimizzazione()));
            } else {
                preparedStatement.setNull(4, java.sql.Types.TIMESTAMP);
            }
            
            preparedStatement.setBoolean(5, utente.isAttivo());
            preparedStatement.setInt(6, utente.getIdUtente());
            
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected != 0;
        }
    }
    
    //ANONIMIZZAZIONE UTENTE
    public synchronized boolean doDelete(int idUtente) throws SQLException {
        String anonymSQL = "UPDATE " + TABLE_NAME + " SET attivo = false, data_anonimizzazione = CURRENT_TIMESTAMP WHERE id_utente = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(anonymSQL)) {
             
            preparedStatement.setInt(1, idUtente);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected != 0;
        }
    }
    
    //RESTITUZIONE UTENTE TRAMITE EMAIL (Usato nel Login)
    public UtenteBEAN doRetrieveByEmail(String email) throws SQLException {
        UtenteBEAN bean = null;
        String selectSQL = "SELECT id_utente, nome, cognome, email, pass, data_creazione, data_anonimizzazione, ruolo, attivo " +
                           "FROM " + TABLE_NAME + " WHERE email = ?";
                           
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
             
            preparedStatement.setString(1, email);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) { 
                    bean = new UtenteBEAN();
                    bean.setIdUtente(resultSet.getInt("id_utente"));
                    bean.setNome(resultSet.getString("nome"));
                    bean.setCognome(resultSet.getString("cognome"));
                    bean.setEmail(resultSet.getString("email"));
                    bean.setPass(resultSet.getString("pass"));
                    
                    Timestamp varCreazione = resultSet.getTimestamp("data_creazione");
                    if (varCreazione != null) {
                        bean.setDataCreazione(varCreazione.toLocalDateTime());
                    }
                    
                    Timestamp varAnonym = resultSet.getTimestamp("data_anonimizzazione");
                    if (varAnonym != null) {
                        bean.setDataAnonimizzazione(varAnonym.toLocalDateTime());
                    }
                    
                    String varRuolo = resultSet.getString("ruolo");
                    if (varRuolo != null) {
                        bean.setRuolo(Ruolo.valueOf(varRuolo));
                    }  
                    bean.setAttivo(resultSet.getBoolean("attivo"));   
                }
            }
        }
        return bean;
    }
    
    //RESTITUZIONE DI TUTTI GLI UTENTI (Pannello Admin)
    public synchronized List<UtenteBEAN> doRetrieveAll(String order) throws SQLException {
        List<UtenteBEAN> utenti = new LinkedList<>();
        String selectSQL = "SELECT id_utente, nome, cognome, email, pass, data_creazione, data_anonimizzazione, ruolo, attivo " +
                           "FROM " + TABLE_NAME;
                           
        if (order != null && !order.isEmpty()) {
            switch (order) {
                case "id_utente":
                case "nome":
                case "cognome":
                case "email":
                case "ruolo":
                case "data_creazione":
                case "attivo":
                    selectSQL += " ORDER BY " + order;
                    break;
                default:
                    selectSQL += " ORDER BY id_utente";
                    break;
            }
        } else {
            selectSQL += " ORDER BY id_utente";
        }
                           
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
             
            while (resultSet.next()) {
                UtenteBEAN bean = new UtenteBEAN();
                bean.setIdUtente(resultSet.getInt("id_utente"));
                bean.setNome(resultSet.getString("nome"));
                bean.setCognome(resultSet.getString("cognome"));
                bean.setEmail(resultSet.getString("email"));
                bean.setPass(resultSet.getString("pass"));
                
                Timestamp varCreazione = resultSet.getTimestamp("data_creazione");
                if (varCreazione != null) {
                    bean.setDataCreazione(varCreazione.toLocalDateTime());
                }
                
                Timestamp varAnonym = resultSet.getTimestamp("data_anonimizzazione");
                if (varAnonym != null) {
                    bean.setDataAnonimizzazione(varAnonym.toLocalDateTime());
                }
                
                String varRuolo = resultSet.getString("ruolo");
                if (varRuolo != null) {
                    bean.setRuolo(Ruolo.valueOf(varRuolo));
                }
                bean.setAttivo(resultSet.getBoolean("attivo"));   
                utenti.add(bean); 
            }
        }
        return utenti;
    }
    
    //RESTITUZIONE UTENTI PER RUOLO PAGINATI
    public synchronized List<UtenteBEAN> doRetrieveByRuoloPaginated(Ruolo ruolo, int offset, int limit) throws SQLException {
        List<UtenteBEAN> utenti = new LinkedList<>();
        String selectSQL = "SELECT id_utente, nome, cognome, email, pass, data_creazione, data_anonimizzazione, ruolo, attivo " +
                           "FROM " + TABLE_NAME + " WHERE ruolo = ? ORDER BY id_utente LIMIT ? OFFSET ?";
                           
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSQL)) {
             
            ps.setString(1, ruolo.name());
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    UtenteBEAN bean = new UtenteBEAN();
                    bean.setIdUtente(resultSet.getInt("id_utente"));
                    bean.setNome(resultSet.getString("nome"));
                    bean.setCognome(resultSet.getString("cognome"));
                    bean.setEmail(resultSet.getString("email"));
                    bean.setPass(resultSet.getString("pass"));
                    
                    Timestamp varCreazione = resultSet.getTimestamp("data_creazione");
                    if (varCreazione != null) {
                        bean.setDataCreazione(varCreazione.toLocalDateTime());
                    }
                    
                    Timestamp varAnonym = resultSet.getTimestamp("data_anonimizzazione");
                    if (varAnonym != null) {
                        bean.setDataAnonimizzazione(varAnonym.toLocalDateTime());
                    }
                    
                    String varRuolo = resultSet.getString("ruolo");
                    if (varRuolo != null) {
                        bean.setRuolo(Ruolo.valueOf(varRuolo));
                    }
                    bean.setAttivo(resultSet.getBoolean("attivo"));   
                    utenti.add(bean); 
                }
            }
        }
        return utenti;
    }

    //CONTEGGIO UTENTI PER RUOLO (Per la paginazione)
    public int countByRuolo(Ruolo ruolo) throws SQLException {
        String countSQL = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE ruolo = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(countSQL)) {
             
            ps.setString(1, ruolo.name());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}