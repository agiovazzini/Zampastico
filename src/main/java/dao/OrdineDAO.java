package dao;

import java.sql.SQLException;
import java.util.List;

import model.IndirizzoBEAN;
import model.OrdineBEAN;
import model.OrdineBEAN.StatoOrdine;

public interface OrdineDAO {
    void doSave(OrdineBEAN order) throws SQLException;
    boolean doUpdateStato(int idOrder, StatoOrdine newStatus) throws SQLException;
    OrdineBEAN doRetrieveByKey(int idOrder) throws SQLException;
    List<OrdineBEAN> doRetrieveByUser(int idUser) throws SQLException;
    List<OrdineBEAN> doRetrieveAll() throws SQLException;
    void doCheckout(int idUser, Integer idSavedAddress, IndirizzoBEAN manualAddress, boolean saveCheck, Integer idCoupon, String paymentMethod) throws SQLException;

}