package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

/**
 * Predstavlja suèelje sa metodama
 * @author Karlo
 *
 */

public interface Visokoskolska {

	/**
	 * 
	 * @param ispit lista ispita
	 * @param ocjenaPismeniZavrsniRad ocjena zavrsnog rada
	 * @param ocjenaObranaZavrsniRad ocjena obrane zavrsnog rada
	 * @return ocjenu tipa BigDecimal
	 */
	
	BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> ispit, Integer ocjenaPismeniZavrsniRad,
			Integer ocjenaObranaZavrsniRad);

	/**
	 * 
	 * @param ispit lista ispita
	 * @return vraæa prosjek ocjena na ispitima
	 * @throws NemoguceOdreditiProsjekStudentaException iznimka koju baca
	 */
	
	default BigDecimal odrediProsjekOcjenaNaIspitima(List<Ispit> ispit) throws NemoguceOdreditiProsjekStudentaException {
		BigDecimal brojac = new BigDecimal("0");
		BigDecimal ocjena = new BigDecimal("0");
		for (Ispit ispiti : ispit) {
			if (ispiti.getOcjena() == 1) {
				throw new NemoguceOdreditiProsjekStudentaException("Nije moguæe odrediti prosjek ocjena studenta "
						+ ispiti.getStudent().getIme() + " " + ispiti.getStudent().getPrezime()
						+ " zbog ocjene nedovoljan iz predmeta " + ispiti.getPredmet().getNaziv() + ".");
			} else {
				ocjena = ocjena.add(new BigDecimal(ispiti.getOcjena()));
				brojac = brojac.add(new BigDecimal("1"));
			}
		}
		return ocjena.divide(brojac);
	}

	/**
	 * 
	 * @param ispit lista ispita
	 * @return ispitiFiltrirani vraæa filtrirane polozene ispite
	 */
	
	private List<Ispit> filtrirajPolozeneIspite(List<Ispit> ispit) {
		List<Ispit> ispitiFiltrirani = new ArrayList<>();
		for (Ispit ispiti : ispit) {
			if (ispiti.getOcjena() > 1) {
				ispitiFiltrirani.add(ispiti);
			}
		}
		return ispitiFiltrirani;
	}

	/**
	 * 
	 * @param ispit lista ispita
	 * @param student objekt student
	 * @return ispitPoStudentu vraæa listu ispita filtriranih po studentu
	 */
	

	default List<Ispit> filtrirajIspitePoStudentu(List<Ispit> ispit, Student student) {
		List<Ispit> ispitPoStudentu = new ArrayList<>();
		
		ispit.stream().filter(fStudent -> fStudent.getStudent().getJmbag().equals(student.getJmbag())).forEach(i -> ispitPoStudentu.add(i));
		return ispitPoStudentu;
	}
}