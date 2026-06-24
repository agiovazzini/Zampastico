package model;

import java.io.Serializable;

public class VoceOrdineBEAN implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idOrdine;
    private int idVarianteProdotto;
    private int quantita;
    private double prezzoAcquisto;
    private String nomeProdotto;
    private String brand;
    private String formato;

    public VoceOrdineBEAN() {
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }
    
    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdVarianteProdotto() {
        return idVarianteProdotto;
    }

    public void setIdVarianteProdotto(int idVarianteProdotto) {
        this.idVarianteProdotto = idVarianteProdotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public double getPrezzoAcquisto() {
        return prezzoAcquisto;
    }

    public void setPrezzoAcquisto(double prezzoAcquisto) {
        this.prezzoAcquisto = prezzoAcquisto;
    }
    
    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }
}