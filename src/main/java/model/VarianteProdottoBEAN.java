package model;

import java.io.Serializable;

public class VarianteProdottoBEAN implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idVarianteProdotto;
    private int idProdotto;
    private String formato;
    private double prezzoListino;
    private Integer idSconto;
    private boolean disponibile = false;
    private String path;
    private String mimeType;

    public VarianteProdottoBEAN() {
    	super();
    }

    public int getIdVarianteProdotto() { 
    	return idVarianteProdotto; 
    }
    
    public void setIdVarianteProdotto(int idVarianteProdotto) { 
    	this.idVarianteProdotto = idVarianteProdotto; 
    }

    public int getIdProdotto() { 
    	return idProdotto; 
    }
    
    public void setIdProdotto(int idProdotto) { 
    	this.idProdotto = idProdotto; 
    }

    public String getFormato() { 
    	return formato; 
    }
    
    public void setFormato(String formato) { 
    	this.formato = formato; 	
    }

    public double getPrezzoListino() { 
    	return prezzoListino; 
    }
    
    public void setPrezzoListino(double prezzoListino) { 
    	this.prezzoListino = prezzoListino; 
    }

    public Integer getIdSconto() { 
    	return idSconto; 
    }
    
    public void setIdSconto(Integer idSconto) { 
    	this.idSconto = idSconto; 
    }
    
    public boolean isDisponibile() { 
    	return disponibile; 
    }
    
    public void setDisponibile(boolean disponibile) { 
    	this.disponibile = disponibile; 	
    }
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
}