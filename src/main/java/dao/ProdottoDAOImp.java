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
        // Lo implementeremo quando faremo il lato Amministratore!
    }
    
    @Override
    public boolean doDelete(int id) throws SQLException {
        // Nel tuo DB la colonna si chiama "eliminato" e non "attivo"
        String sql = "UPDATE Prodotto SET eliminato = TRUE WHERE id_prodotto = ?";
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public ProdottoBEAN doRetrieveByKey(int id) throws SQLException {
        return null; // Lo implementeremo per la pagina del singolo prodotto
    }

    @Override
    public List<ProdottoBEAN> doRetrieveAll(String orderBy) throws SQLException {
        List<ProdottoBEAN> lista = new ArrayList<>();
        
        // LA MAGIA: Uniamo (JOIN) le tue tabelle per recuperare tutti i pezzi del prodotto!
        String sql = "SELECT p.id_prodotto, p.nome, p.descrizione, p.brand, " +
                     "vp.prezzo_listino, c.nome AS categoria, iv.url_immagine " +
                     "FROM Prodotto p " +
                     "JOIN VarianteProdotto vp ON p.id_prodotto = vp.id_prodotto " +
                     "JOIN Categoria c ON p.id_categoria = c.id_categoria " +
                     "LEFT JOIN ImmagineVariante iv ON vp.id_varianteProdotto = iv.id_varianteProdotto AND iv.immagine_copertina = TRUE " +
                     "WHERE p.eliminato = FALSE";
        
        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ProdottoBEAN p = new ProdottoBEAN();
                p.setId(rs.getInt("id_prodotto"));
                p.setNome(rs.getString("nome"));
                p.setDescrizione(rs.getString("descrizione"));
                p.setPrezzo(rs.getDouble("prezzo_listino")); // Preso dalla Variante!
                p.setMarca(rs.getString("brand"));
                p.setImmagine(rs.getString("url_immagine")); // Preso dalle Immagini!
                p.setCategoria(rs.getString("categoria"));
                lista.add(p);
            }
        }
        return lista;
    }
}