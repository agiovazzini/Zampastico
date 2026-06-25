package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CouponBEAN implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idCoupon;
    private String codice;
    private double percentualeSconto;
    private LocalDateTime dataScadenza;

    public CouponBEAN() {
    }

    public int getIdCoupon() {
        return idCoupon;
    }

    public void setIdCoupon(int idCoupon) {
        this.idCoupon = idCoupon;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public double getPercentualeSconto() {
        return percentualeSconto;
    }

    public void setPercentualeSconto(double percentualeSconto) {
        this.percentualeSconto = percentualeSconto;
    }

    public LocalDateTime getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDateTime dataScadenza) {
        this.dataScadenza = dataScadenza;
    }
    
    public boolean isScaduto() {
        if (this.dataScadenza == null) {
            return false;
        }
        return this.dataScadenza.isBefore(java.time.LocalDateTime.now());
    }
}