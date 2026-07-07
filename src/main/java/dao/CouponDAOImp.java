package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import model.CouponBEAN;

public class CouponDAOImp implements CouponDAO {
    private DataSource ds;

    public CouponDAOImp(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public void doSave(CouponBEAN coupon) throws SQLException {
        String query = "INSERT INTO Coupon (codice, percentuale_sconto, data_scadenza) VALUES (?, ?, IFNULL(?, CURRENT_TIMESTAMP + INTERVAL 24 HOUR))";
        
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, coupon.getCodice());
            ps.setDouble(2, coupon.getPercentualeSconto());
            
            if (coupon.getDataScadenza() != null) {
                ps.setObject(3, coupon.getDataScadenza());
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }
            
            ps.executeUpdate();
        }
    }
    
    @Override
    public CouponBEAN doRetrieveByCodice(String codice) throws SQLException {
        String query = "SELECT * FROM Coupon WHERE codice = ?";
        
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, codice.toUpperCase());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CouponBEAN coupon = new CouponBEAN();
                    coupon.setIdCoupon(rs.getInt("id_coupon"));
                    coupon.setCodice(rs.getString("codice"));
                    coupon.setPercentualeSconto(rs.getDouble("percentuale_sconto"));
                    
                    Timestamp ts = rs.getTimestamp("data_scadenza");
                    if (ts != null) {
                        coupon.setDataScadenza(ts.toLocalDateTime());
                    }
                    return coupon;
                }
            }
        }
        return null;
    }

    @Override
    public boolean doDelete(int idCoupon) throws SQLException {
        String query = "DELETE FROM Coupon WHERE id_coupon = ?";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setInt(1, idCoupon);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<CouponBEAN> doRetrieveAll() throws SQLException {
        List<CouponBEAN> couponList = new ArrayList<>();
        String query = "SELECT * FROM Coupon ORDER BY id_coupon DESC";
        
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                CouponBEAN coupon = new CouponBEAN();
                coupon.setIdCoupon(rs.getInt("id_coupon"));
                coupon.setCodice(rs.getString("codice"));
                coupon.setPercentualeSconto(rs.getDouble("percentuale_sconto"));
                Timestamp ts = rs.getTimestamp("data_scadenza");
                if (ts != null) {
                    coupon.setDataScadenza(ts.toLocalDateTime());
                }
                
                couponList.add(coupon);
            }
        }
        return couponList;
    }
    
    @Override
    public List<CouponBEAN> doRetrieveAllPaginated(int offset, int limit) throws SQLException {
        List<CouponBEAN> couponList = new ArrayList<>();
        // Usa LIMIT e OFFSET per la paginazione
        String query = "SELECT * FROM Coupon ORDER BY id_coupon DESC LIMIT ? OFFSET ?";
        
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CouponBEAN coupon = new CouponBEAN();
                    coupon.setIdCoupon(rs.getInt("id_coupon"));
                    coupon.setCodice(rs.getString("codice"));
                    coupon.setPercentualeSconto(rs.getDouble("percentuale_sconto"));
                    Timestamp ts = rs.getTimestamp("data_scadenza");
                    if (ts != null) {
                        coupon.setDataScadenza(ts.toLocalDateTime());
                    }
                    couponList.add(coupon);
                }
            }
        }
        return couponList;
    }

    @Override
    public int countAllCoupons() throws SQLException {
        String query = "SELECT COUNT(*) FROM Coupon";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}