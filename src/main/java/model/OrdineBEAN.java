package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

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
        in_attesa("In attesa di pagamento"), 
        pagato("Pagato"), 
        in_lavorazione("In lavorazione"), 
        spedito("Spedito"), 
        consegnato("Consegnato"),
        annullato("Annullato");

        private final String descrizione;

        StatoOrdine(String descrizione) {
            this.descrizione = descrizione;
        }

        public String getDescrizione() {
            return descrizione;
        }
    }
    
    private StatoOrdine stato = StatoOrdine.in_attesa;
    private LocalDateTime dataOrdine;
    private List<VoceOrdineBEAN> vociOrdine = new ArrayList<>();
    private String metodoPagamento;
    private String emailUtente;

    public OrdineBEAN() {
    	super();
    }

    
    public String getEmailUtente() {
        return emailUtente;
    }

    public void setEmailUtente(String emailUtente) {
        this.emailUtente = emailUtente;
    }
    
	 public String getStatoVisualizzabile() {
	     if (this.stato == null) {
	         return "Non specificato";
	     }
	     
	     switch (this.stato) {
	         case in_attesa:
	             return "In attesa di pagamento";
	         case pagato:
	             return "Pagato";
	         case in_lavorazione:
	             return "In lavorazione";
	         case spedito:
	             return "Spedito";
	         case consegnato:
	             return "Consegnato";
	         case annullato:
	             return "Annullato";
	         default:
	             return this.stato.toString();
	     }
	 }
	 public String getMetodoPagamentoVisualizzabile() {
	     if (this.metodoPagamento == null) {
	         return "Non specificato";
	     }
	     
	     switch (this.metodoPagamento) {
	         case "carta_credito":
	             return "Carta di Credito";
	         case "paypal":
	             return "PayPal";
	         case "bonifico":
	             return "Bonifico Bancario";
	         default:
	             return this.metodoPagamento;
	     }
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
    
    public List<VoceOrdineBEAN> getVociOrdine() {
        return vociOrdine;
    }

    public void setVociOrdine(List<VoceOrdineBEAN> vociOrdine) {
        this.vociOrdine = vociOrdine;
    }
    
    public void addVoce(VoceOrdineBEAN voce) {
        this.vociOrdine.add(voce);
    }
    
    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}