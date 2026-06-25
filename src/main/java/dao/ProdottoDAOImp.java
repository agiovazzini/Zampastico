package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import model.ProdottoBEAN;

public class ProdottoDAOImp implements ProdottoDAO {
	private DataSource ds;

    public ProdottoDAOImp(DataSource ds) {
        this.ds = ds;
    }
    
    @Override
    public void doSave(ProdottoBEAN prodotto) throws SQLException {
        String sql = "INSERT INTO prodotto (nome, descrizione, prezzo, marca, immagine, categoria, attivo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, prodotto.getNome());
            ps.setString(2, prodotto.getDescrizione());
            ps.setDouble(3, prodotto.getPrezzo());
            ps.setString(4, prodotto.getMarca());
            ps.setString(5, prodotto.getImmagine());
            ps.setString(6, prodotto.getCategoria());
            ps.setBoolean(7, prodotto.isAttivo());
            ps.executeUpdate();
        }
    }
    
    @Override
    public boolean doDelete(int id) throws SQLException {
        // Impostiamo attivo = false (Soft Delete)
        String sql = "UPDATE prodotto SET attivo = false WHERE id = ?";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    @Override
    public ProdottoBEAN doRetrieveByKey(int id) throws SQLException {
        String sql = "SELECT * FROM prodotto WHERE id = ?";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ProdottoBEAN p = new ProdottoBEAN();
                    p.setId(rs.getInt("id"));
                    p.setNome(rs.getString("nome"));
                    p.setDescrizione(rs.getString("descrizione"));
                    p.setPrezzo(rs.getDouble("prezzo"));
                    
                    // CORREZIONE 2: Era p.setString, ora è p.setMarca
                    p.setMarca(rs.getString("marca"));
                    
                    p.setImmagine(rs.getString("immagine"));
                    p.setCategoria(rs.getString("categoria"));
                    p.setAttivo(rs.getBoolean("attivo"));
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public List<ProdottoBEAN> doRetrieveAll(String orderBy) throws SQLException {
        List<ProdottoBEAN> lista = new ArrayList<>();
        String order = (orderBy != null && !orderBy.isEmpty()) ? orderBy : "id";
        // Mostriamo solo i prodotti attivi nel catalogo pubblico!
        String sql = "SELECT * FROM prodotto WHERE attivo = true ORDER BY " + order;
        
        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ProdottoBEAN p = new ProdottoBEAN();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setDescrizione(rs.getString("descrizione"));
                p.setPrezzo(rs.getDouble("prezzo"));
                p.setMarca(rs.getString("marca"));
                p.setImmagine(rs.getString("immagine"));
                p.setCategoria(rs.getString("categoria"));
                p.setAttivo(rs.getBoolean("attivo"));
                lista.add(p);
            }
        }
        return lista;
    }
}