package hr.java.vjezbe.entitet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;
import hr.java.vjezbe.util.Datoteke;

/**
 * Vraæa objekt veleuciliste jave, nasljeðuje svojstva i metode klase obrazovna ustanova te implementira suèelje diplomski
 * @author Karlo
 *
 */

public class VeleucilisteJave extends ObrazovnaUstanova implements Visokoskolska, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7470721964233553245L;
	public static final Logger logger = LoggerFactory.getLogger(Datoteke.class);

	/**
	 * Inicilaizira podatke o veleucilistu jave
	 * @param predmet polje predmeta
	 * @param profesor polje profesora
	 * @param student polje studenata
	 * @param ispit polje ispita
	 */
	
	public VeleucilisteJave(Long id, List<Predmet> predmet, List<Profesor> profesor, List<Student> student,
			List<Ispit> ispit) {
		super(id, predmet, profesor, student, ispit);
	}
	
	/**
	 * @param ispit sadrži objekte ispita
	 * @param ocjenaPismeniZavrsniRad ocjena pisemong djela završnog rada
	 * @param ocjenaObranaZavrsniRad ocjena obrane pisemong djela završnog rada
	 * @return konacnaOcjena vraæa konaènu ocjenu tipa BigDecimal
	 */

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> ispit, Integer ocjenaPismeniZavrsniRad,
			Integer ocjenaObranaZavrsniRad) {
		BigDecimal konacnaOcjena = new BigDecimal("1");
		try {
			konacnaOcjena = (odrediProsjekOcjenaNaIspitima(ispit).multiply(new BigDecimal("2"))
					.add(new BigDecimal(ocjenaPismeniZavrsniRad)).add(new BigDecimal(ocjenaObranaZavrsniRad)))
							.divide(new BigDecimal("4"));
		} catch (NemoguceOdreditiProsjekStudentaException e) {
			logger.info(e.getMessage(), e);
			System.out.println(e.getMessage());
		}
		return konacnaOcjena;
	}
	
	/**
	 * @parm godina predstavlja trenutnu godinu
	 * @return najboljiStudent vraæa najboljeg studenta tipa student
	 */

	@Override
	public Student odrediNajuspjesnijegStudentaNaGodini(int godina) {
		List<Ispit> listaIspita = getIspit();
		List<Student> listaStudenta = new  ArrayList<>();
		for (Ispit ispiti : listaIspita) {
			if (ispiti.getDatumIVrijeme().getYear() == godina) {
				listaStudenta.add(ispiti.getStudent());
			}
		}
		Student najboljiStudent = listaStudenta.get(0);
		BigDecimal najboljiProsjek = new BigDecimal("0");

		for (Student student : listaStudenta) {
			try {
				if (odrediProsjekOcjenaNaIspitima(filtrirajIspitePoStudentu(listaIspita, student))
						.compareTo(najboljiProsjek) >= 0) {
					najboljiProsjek = odrediProsjekOcjenaNaIspitima(filtrirajIspitePoStudentu(listaIspita, student));
					najboljiStudent = student;
				}
			} catch (NemoguceOdreditiProsjekStudentaException e) {
				logger.info("Nije moguce odrediti prosjek studenta "+student.getIme()+" "+student.getPrezime()+" za oredivanje najuspjesnijeg studenta na godini zbog negativne ocjene na jednom od ispita");
			}
		}
		return najboljiStudent;
	}
}
