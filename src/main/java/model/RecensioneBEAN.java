package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RecensioneBEAN implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idUtente;
    private int idVarianteProdotto;
    private int votazione;
    private String commento;
    private LocalDateTime dataCreazione;
    private String nomeProdotto;
    private String brandProdotto;
    private String formatoProdotto;

    public RecensioneBEAN() {
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public int getIdVarianteProdotto() {
        return idVarianteProdotto;
    }

    public void setIdVarianteProdotto(int idVarianteProdotto) {
        this.idVarianteProdotto = idVarianteProdotto;
    }

    public int getVotazione() {
        return votazione;
    }

    public void setVotazione(int votazione) {
        this.votazione = votazione;
    }

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }
    
    public String getBrandProdotto() { 
    	return brandProdotto; 
    }
    
    public void setBrandProdotto(String brandProdotto) { 
    	this.brandProdotto = brandProdotto; 
    }

    public String getFormatoProdotto() {
        return formatoProdotto;
    }

    public void setFormatoProdotto(String formatoProdotto) {
        this.formatoProdotto = formatoProdotto;
    }
}