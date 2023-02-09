package hr.java.vjezbe.iznimke;

public class BazaPodatakaException extends Exception {

	private static final long serialVersionUID = 6186263838409046177L;

	/**
	 * Zadani konstruktor bez ulaznih parametara.
	 */
	
	public BazaPodatakaException() {
		super();
	}
	
	/**
	 * Inicijalizira poruku.
	 * @param poruka
	 */
	
	public BazaPodatakaException(String poruka) {
		super(poruka);
	}
	
	/**
	 * Inicijalizira uzrok.
	 * @param uzrok
	 */
	
	public BazaPodatakaException(Throwable uzrok) {
		super(uzrok);
	}
	
	/**
	 * Inicijalizira poruku i uzrok.
	 * @param poruka
	 * @param uzrok
	 */
	
	public BazaPodatakaException(String poruka, Throwable uzrok) {
		super(poruka, uzrok);
	}
}
