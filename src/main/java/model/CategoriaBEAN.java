package model;

import java.io.Serializable;

public class CategoriaBEAN implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idCategoria;
    private String nome;
    private String descrizione;

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

    public String getDescrizione() { 
    	return descrizione; 
    }
    
    public void setDescrizione(String descrizione) { 
    	this.descrizione = descrizione; 
    }
}