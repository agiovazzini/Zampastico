package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProdottoBEAN implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String nome;
    private String descrizione;
    private double prezzo;
    private String marca;
    private String immagine;
    private String categoria;
    private int idCategoria;
    private boolean attivo;
    private List<VarianteProdottoBEAN> varianti = new ArrayList<>();
    private String path;
    private String mimeType;

    public ProdottoBEAN() {
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

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    
    public List<VarianteProdottoBEAN> getVarianti() { return varianti; }
    public void setVarianti(List<VarianteProdottoBEAN> varianti) { this.varianti = varianti; }

    // Aggiungi questi metodi in fondo alla classe
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    

}
