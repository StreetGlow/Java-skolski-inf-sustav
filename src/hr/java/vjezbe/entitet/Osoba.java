package hr.java.vjezbe.entitet;

/**
 * Predstavlja apstraktnu klasu definiranu imenom i prezimenom.
 * @author Karlo
 *
 */

public abstract class Osoba extends Entitet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6700775030123806533L;
	protected String ime;
	protected String prezime;
	
	/**
	 * Inicijalizira podatak o imenu i prezimenu
	 * @param ime ime osobe
	 * @param prezime prezime osobe
	 */
	
	public Osoba(Long id, String ime, String prezime) {
		super(id);
		this.ime = ime;
		this.prezime = prezime;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
}
