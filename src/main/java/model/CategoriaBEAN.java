package model;

import java.io.Serializable;

public class CategoriaBEAN implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idCategoria;
    private String nome;
    private int idSuperCategoria;
    private String nomeSuper; 
    private int livello;
    private Integer idProdottoImmagine;

    public CategoriaBEAN() {
    	super();
    }

    public int getIdCategoria() { 
    	return idCategoria; 
    }
    public void setIdCategoria(int idCategoria) { 
    	this.idCategoria = idCategoria; 
    }

    public String getNome() { 
    	return nome; 
    }
    public void setNome(String nome) { 
    	this.nome = nome; 
    }

    public int getIdSuper() { 
    	return idSuperCategoria; 
    }
    public void setidSuper(int idSuperCategoria) { 
    	this.idSuperCategoria = idSuperCategoria; 
    }

    public String getNomeSuper() {
        return nomeSuper;
    }
    public void setNomeSuper(String nomeSuper) {
        this.nomeSuper = nomeSuper;
    }

    public int getLivello() {
        return livello;
    }

    public void setLivello(int livello) {
        this.livello = livello;
    }
    
    public Integer getIdProdottoImmagine() {
        return idProdottoImmagine;
    }

    public void setIdProdottoImmagine(Integer idProdottoImmagine) {
        this.idProdottoImmagine = idProdottoImmagine;
    }
}