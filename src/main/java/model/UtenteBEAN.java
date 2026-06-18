package model;

import java.io.Serializable;
import java.time.LocalDateTime;
public class UtenteBEAN implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int idUtente;
	private String nome;
	private String cognome;
	private String email;
	private String pass;
	private LocalDateTime dataCreazione;
	private LocalDateTime dataAnonimizzazione;
	public enum Ruolo {
		amministratore,
		cliente
	}
	private Ruolo ruolo;
	private boolean attivo;

	public UtenteBEAN() {
		super();
	}
	
	public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDateTime getDataAnonimizzazione() {
        return dataAnonimizzazione;
    }

    public void setDataAnonimizzazione(LocalDateTime dataAnonimizzazione) {
        this.dataAnonimizzazione = dataAnonimizzazione;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }
	
}
