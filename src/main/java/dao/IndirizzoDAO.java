package dao;

import java.sql.SQLException;
import java.util.List;

import model.IndirizzoBEAN;

public interface IndirizzoDAO {
	void doSave(IndirizzoBEAN address) throws SQLException;
    boolean doUpdate(IndirizzoBEAN address) throws SQLException;
    boolean doDelete(int idAddress, int idUser) throws SQLException;
    IndirizzoBEAN doRetrieveByKey(int idAddress) throws SQLException;
    List<IndirizzoBEAN> doRetrieveByUser(int idUser) throws SQLException;
    boolean doSetPredefinito(int idAddress, int idUser) throws SQLException;
}
