package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrdineBEAN implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idOrdine;
    private int idUtente;
    private Integer idCoupon; 
    private BigDecimal totale;    
    private String spedizioneCitta;
    private String spedizioneProvincia;
    private String spedizioneVia;
    private String spedizioneCap;
    public enum StatoOrdine {
    	in_attesa, 
    	pagato, 
    	in_lavorazione, 
    	spedito, 
    	consegnato,
    	annullato
	}
    private StatoOrdine stato = StatoOrdine.in_attesa;
    private LocalDateTime dataOrdine;

    public OrdineBEAN() {
    	super();
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public Integer getIdCoupon() {
        return idCoupon;
    }

    public void setIdCoupon(Integer idCoupon) {
        this.idCoupon = idCoupon;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public String getSpedizioneCitta() {
        return spedizioneCitta;
    }

    public void setSpedizioneCitta(String spedizioneCitta) {
        this.spedizioneCitta = spedizioneCitta;
    }

    public String getSpedizioneProvincia() {
        return spedizioneProvincia;
    }

    public void setSpedizioneProvincia(String spedizioneProvincia) {
        this.spedizioneProvincia = spedizioneProvincia;
    }

    public String getSpedizioneVia() {
        return spedizioneVia;
    }

    public void setSpedizioneVia(String spedizioneVia) {
        this.spedizioneVia = spedizioneVia;
    }

    public String getSpedizioneCap() {
        return spedizioneCap;
    }

    public void setSpedizioneCap(String spedizioneCap) {
        this.spedizioneCap = spedizioneCap;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(LocalDateTime dataOrdine) {
        this.dataOrdine = dataOrdine;
    }
}