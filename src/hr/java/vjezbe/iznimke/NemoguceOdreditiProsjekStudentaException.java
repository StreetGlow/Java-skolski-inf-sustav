package hr.java.vjezbe.iznimke;

/**
 * Vra�a objekt iznimke nemogu�e odrediti prosjek studenta koja naslje�uje svojstva i metode ozna�ene iznimke Exception.
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
