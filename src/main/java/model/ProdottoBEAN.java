package model;

import java.io.Serializable;

public class ProdottoBEAN implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String nome;
    private String descrizione;
    private double prezzo;
    private String marca;
    private String immagine;
    private String categoria;
    private boolean attivo; //Non elimina prodotti dagli ordini passati
    
    public ProdottoBEAN() {
        this.attivo = true;
    }
    
    //Metodi Getter e Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getImmagine() { return immagine; }
    public void setImmagine(String immagine) { this.immagine = immagine; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean isAttivo() { return attivo; }
    public void setAttivo(boolean attivo) { this.attivo = attivo; }
}
