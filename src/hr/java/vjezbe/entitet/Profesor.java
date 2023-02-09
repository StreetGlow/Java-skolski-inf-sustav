package hr.java.vjezbe.entitet;

import java.io.Serializable;

/**
 * Vraæa objekt profesor predstavljen siform i tiulom te nasljeduje klasu osoba i svojstva imena i prezimena
 * @author Karlo
 *
 */

public class Profesor extends Osoba implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7865709803491574254L;
	private String sifra;
	private String titula;
	
	/**
	 * Inicijalizira podatke o profesoru
	 * @param ime ime profesora
	 * @param prezime prezime profesora
	 * @param sifra sifra profesora 
	 * @param titula titula profesora
	 */
	
	public Profesor(Long id, String ime, String prezime, String sifra, String titula) {
		super(id, ime, prezime);
		this.sifra = sifra;
		this.titula = titula;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getTitula() {
		return titula;
	}

	public void setTitula(String titula) {
		this.titula = titula;
	}
	
	@Override
    public String toString() {
        return this.ime+" "+this.prezime;
    }
}

