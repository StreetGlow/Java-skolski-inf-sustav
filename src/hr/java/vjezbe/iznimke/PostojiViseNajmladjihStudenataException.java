package hr.java.vjezbe.iznimke;

/**
 * Vra�a objekt iznimke postoji vi�e najmla�ih studenata koja naslje�uje svojstva i metode neozna�ene iznimke RuntimeException.
 * @author Karlo
 *
 */

public class PostojiViseNajmladjihStudenataException extends RuntimeException {

	private static final long serialVersionUID = 1324053465120359404L;
	
	/**
	 * Zadani konstruktor bez ulaznih parametara.
	 */	

	public PostojiViseNajmladjihStudenataException() {
		super();
	}
	
	/**
	 * Inicijalizira poruku.
	 * @param poruka
	 */
	
	public PostojiViseNajmladjihStudenataException(String poruka) {
		super(poruka);
	}
	
	/**
	 * Inicijalizira uzrok.
	 * @param uzrok
	 */
	
	public PostojiViseNajmladjihStudenataException(Throwable uzrok) {
		super(uzrok);
	}
	
	/**
	 * Inicijalizira poruku i uzrok.
	 * @param poruka
	 * @param uzrok
	 */
	
	public PostojiViseNajmladjihStudenataException(String poruka, Throwable uzrok) {
		super(poruka, uzrok);
	}

}
