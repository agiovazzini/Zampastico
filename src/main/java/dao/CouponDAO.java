package dao;

import java.sql.SQLException;
import java.util.List;
import model.CouponBEAN;

public interface CouponDAO {
    void doSave(CouponBEAN coupon) throws SQLException;
    boolean doDelete(int idCoupon) throws SQLException;
    List<CouponBEAN> doRetrieveAll() throws SQLException;
    List<CouponBEAN> doRetrieveAllPaginated(int offset, int limit) throws SQLException;
    int countAllCoupons() throws SQLException;
    public CouponBEAN doRetrieveByCodice(String codice) throws SQLException;
}