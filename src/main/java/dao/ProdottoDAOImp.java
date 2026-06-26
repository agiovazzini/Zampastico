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
    
    public synchronized boolean doUpdateStato(int id, boolean stato) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET attivo = ? WHERE id_prodotto = ?";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, stato);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean doDelete(int id) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id_prodotto = ?";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    @Override
    public ProdottoBEAN doRetrieveByKey(int id) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id_prodotto = ?";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ProdottoBEAN p = new ProdottoBEAN();
                    p.setId(rs.getInt("id_prodotto"));
                    p.setNome(rs.getString("nome"));
                    p.setDescrizione(rs.getString("descrizione"));
                    p.setMarca(rs.getString("brand"));
                    p.setIdCategoria(rs.getInt("id_categoria"));
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
        
        String sql = "SELECT p.id_prodotto, p.nome, p.descrizione, p.brand, p.attivo, " +
                     "vp.id_varianteProdotto, vp.formato, vp.prezzo_listino, vp.id_sconto, vp.disponibile, " +
                     "c.nome AS categoria, iv.url_immagine " +
                     "FROM Prodotto p " +
                     "LEFT JOIN VarianteProdotto vp ON p.id_prodotto = vp.id_prodotto " +
                     "LEFT JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                     "LEFT JOIN ImmagineVariante iv ON vp.id_varianteProdotto = iv.id_varianteProdotto AND iv.immagine_copertina = TRUE";
        
        if (orderBy != null && !orderBy.trim().isEmpty()) {
            sql += " ORDER BY " + orderBy;
        }
        
        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                int idProdotto = rs.getInt("id_prodotto");
                ProdottoBEAN p = null;
                
                for (ProdottoBEAN prod : lista) {
                    if (prod.getId() == idProdotto) {
                        p = prod;
                        break; 
                    }
                }
                
                if (p == null) {
                    p = new ProdottoBEAN();
                    p.setId(idProdotto);
                    p.setNome(rs.getString("nome"));
                    p.setDescrizione(rs.getString("descrizione"));
                    p.setMarca(rs.getString("brand"));
                    p.setCategoria(rs.getString("categoria"));
                    p.setAttivo(rs.getBoolean("attivo"));
                    p.setPrezzo(rs.getDouble("prezzo_listino"));
                    p.setImmagine(rs.getString("url_immagine"));
                    
                    lista.add(p);
                }
                
                int idVariante = rs.getInt("id_varianteProdotto");
                
                if (!rs.wasNull()) {
                    model.VarianteProdottoBEAN variante = new model.VarianteProdottoBEAN();
                    variante.setIdVarianteProdotto(idVariante);
                    variante.setIdProdotto(idProdotto);
                    variante.setFormato(rs.getString("formato"));
                    variante.setPrezzoListino(rs.getDouble("prezzo_listino"));
                    variante.setDisponibile(rs.getBoolean("disponibile"));
                    
                    int idSconto = rs.getInt("id_sconto");
                    if (rs.wasNull()) {
                        variante.setIdSconto(null);
                    } else {
                        variante.setIdSconto(idSconto);
                    }
                    
                    p.getVarianti().add(variante);
                }
            }
        }
        
        return lista;
    }
    
    public List<ProdottoBEAN> doRetrieveByCategoria(int idCategoria) throws SQLException {
        List<ProdottoBEAN> lista = new ArrayList<>();
        String sql = "WITH RECURSIVE CategoriaTree AS ( " +
                     "    SELECT id_categoria FROM Categoria WHERE id_categoria = ? " +
                     "    UNION ALL " +
                     "    SELECT c.id_categoria FROM Categoria c " +
                     "    INNER JOIN CategoriaTree ct ON c.id_supercategoria = ct.id_categoria " +
                     ") " +
                     "SELECT * FROM " + TABLE_NAME + " WHERE id_categoria IN (SELECT id_categoria FROM CategoriaTree)";
                     
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
    
    public boolean checkEsiste(String nome, String brand) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE LOWER(nome) = LOWER(?) AND LOWER(brand) = LOWER(?)";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, brand);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public int contaVarianti(int idProdotto) throws SQLException {
        String sql = "SELECT COUNT(*) FROM VarianteProdotto WHERE id_prodotto = ?";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProdotto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public synchronized boolean doUpdateImage(int idProdotto, String path, String mimeType) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET path = ?, mime_type = ? WHERE id_prodotto = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, path);
            ps.setString(2, mimeType);
            ps.setInt(3, idProdotto);
            return ps.executeUpdate() != 0;
        }
    }
}