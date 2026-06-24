package model;

import java.io.Serializable;

public class VoceOrdineBEAN implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idOrdine;
    private int idVarianteProdotto;
    private int quantita;
    private double prezzoAcquisto;

    public VoceOrdineBEAN() {
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
}