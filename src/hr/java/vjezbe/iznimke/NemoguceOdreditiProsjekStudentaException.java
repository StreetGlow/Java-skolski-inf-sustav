package hr.java.vjezbe.iznimke;

/**
 * Vraæa objekt iznimke nemoguæe odrediti prosjek studenta koja nasljeðuje svojstva i metode oznaèene iznimke Exception.
 * @author Karlo
 *
 */

public class NemoguceOdreditiProsjekStudentaException extends Exception {

	private static final long serialVersionUID = 2711724378809456882L;
	
	/**
	 * Zadani konstruktor bez ulaznih parametara.
	 */

	public NemoguceOdreditiProsjekStudentaException() {
		super();
	}
	
	/**
	 * Inicijalizira poruku.
	 * @param poruka
	 */
	
	public NemoguceOdreditiProsjekStudentaException(String poruka) {
		super(poruka);
	}
	
	/**
	 * Inicijalizira uzrok.
	 * @param uzrok
	 */
	
	public NemoguceOdreditiProsjekStudentaException(Throwable uzrok) {
		super(uzrok);
	}
	
	/**
	 * Inicijalizira poruku i uzrok.
	 * @param poruka
	 * @param uzrok
	 */
	
	public NemoguceOdreditiProsjekStudentaException(String poruka, Throwable uzrok) {
		super(poruka, uzrok);
	}

}
