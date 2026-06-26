package dao;

import java.sql.SQLException;
import java.util.List;
import model.ProdottoBEAN;

public interface ProdottoDAO {
	public void doSave(ProdottoBEAN prodotto) throws SQLException;
    public boolean doDelete(int id) throws SQLException;
    public ProdottoBEAN doRetrieveByKey(int id) throws SQLException;
    public List<ProdottoBEAN> doRetrieveAll(String orderBy) throws SQLException;
    public List<ProdottoBEAN> doRetrieveByCategoria(int idCategoria) throws SQLException;
    public boolean checkEsiste(String nome, String brand) throws SQLException;
    public int contaVarianti(int idProdotto) throws SQLException;
}
