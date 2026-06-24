package model;

import java.io.Serializable;

public class IndirizzoBEAN implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idIndirizzo;
    private int idUtente;
    private String citta;
    private String provincia;
    private String via;
    private String cap;
    private boolean predefinito;

    public IndirizzoBEAN() {
    	super();
    }

    public int getIdIndirizzo() { 
    	return idIndirizzo; 
    }
    
    public void setIdIndirizzo(int idIndirizzo) { 
    	this.idIndirizzo = idIndirizzo; 
    }

    public int getIdUtente() { 
		return idUtente; 
	}
    
    public void setIdUtente(int idUtente) { 
		this.idUtente = idUtente; 
	}

    public String getCitta() { 
		return citta; 
	}
    
    public void setCitta(String citta) { 
		this.citta = citta; 
	}

    public String getProvincia() { 
		return provincia; 
	}
    
    public void setProvincia(String provincia) { 
		this.provincia = provincia; 
	}

    public String getVia() { 
		return via; 
	}
    
    public void setVia(String via) { 
		this.via = via; 
	}	

    public String getCap() { 
		return cap; 
	}
    
    public void setCap(String cap) { 
		this.cap = cap; 
	}

    public boolean isPredefinito() { 
		return predefinito; 
	}
    
    public void setPredefinito(boolean predefinito) { 
		this.predefinito = predefinito; 
	}
}