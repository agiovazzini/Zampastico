package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import model.ProdottoBEAN;

public class ProdottoDAOImp implements ProdottoDAO {
	private static final String TABLE_NAME = "Prodotto";
    private DataSource ds;

    public ProdottoDAOImp(DataSource ds) {
        this.ds = ds;
    }
    
    @Override
    public synchronized void doSave(ProdottoBEAN prodotto) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (nome, descrizione, brand, id_categoria, attivo) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, prodotto.getNome());
            ps.setString(2, prodotto.getDescrizione());
            ps.setString(3, prodotto.getMarca());
            ps.setInt(4, prodotto.getIdCategoria());
            ps.setBoolean(5, prodotto.isAttivo());         
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    prodotto.setId(rs.getInt(1));
                }
            }
        }
    }
    
    // Aggiornamento
    public synchronized boolean doUpdate(ProdottoBEAN prodotto) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET nome = ?, descrizione = ?, brand = ?, id_categoria = ?, attivo = ? WHERE id_prodotto = ?";
        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, prodotto.getNome());
            ps.setString(2, prodotto.getDescrizione());
            ps.setString(3, prodotto.getMarca());
            ps.setInt(4, prodotto.getIdCategoria());
            ps.setBoolean(5, prodotto.isAttivo());
            ps.setInt(6, prodotto.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean doDelete(int id) throws SQLException {
        String sql = "UPDATE Prodotto SET attivo = TRUE WHERE id_prodotto = ?";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public ProdottoBEAN doRetrieveByKey(int id) throws SQLException {
        return null;
    }

    @Override
    public List<ProdottoBEAN> doRetrieveAll(String orderBy) throws SQLException {
        List<ProdottoBEAN> lista = new ArrayList<>();
        String sql = "SELECT p.id_prodotto, p.nome, p.descrizione, p.brand, " +
                     "vp.prezzo_listino, c.nome AS categoria, iv.url_immagine " +
                     "FROM Prodotto p " +
                     "JOIN VarianteProdotto vp ON p.id_prodotto = vp.id_prodotto " +
                     "JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                     "LEFT JOIN ImmagineVariante iv ON vp.id_varianteProdotto = iv.id_varianteProdotto AND iv.immagine_copertina = TRUE " +
                     "WHERE p.attivo = FALSE";
        
        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ProdottoBEAN p = new ProdottoBEAN();
                p.setId(rs.getInt("id_prodotto"));
                p.setNome(rs.getString("nome"));
                p.setDescrizione(rs.getString("descrizione"));
                p.setPrezzo(rs.getDouble("prezzo_listino"));
                p.setMarca(rs.getString("brand"));
                p.setImmagine(rs.getString("url_immagine"));
                p.setCategoria(rs.getString("categoria"));
                lista.add(p);
            }
        }
        return lista;
    }
    
    public List<ProdottoBEAN> doRetrieveByCategoria(int idCategoria) throws SQLException {
        List<ProdottoBEAN> lista = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id_categoria = ?";
        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProdottoBEAN p = new ProdottoBEAN();
                    p.setId(rs.getInt("id_prodotto"));
                    p.setNome(rs.getString("nome"));
                    p.setDescrizione(rs.getString("descrizione"));
                    p.setMarca(rs.getString("brand"));
                    p.setIdCategoria(rs.getInt("id_categoria"));
                    p.setAttivo(rs.getBoolean("attivo"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}