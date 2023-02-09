package hr.java.vjezbe.entitet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;
import hr.java.vjezbe.iznimke.PostojiViseNajmladjihStudenataException;
import hr.java.vjezbe.util.Datoteke;

/**
 * Vraæa objekt fakultet raèunarstva, nasljeðuje svojstva i metode klase
 * obrazovna ustanova te implementira suèelje diplomski
 * 
 * 
 * @author Karlo
 *
 */

public class FakultetRacunarstva extends ObrazovnaUstanova implements Diplomski, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8661430103390711071L;
	public static final Logger logger = LoggerFactory.getLogger(Datoteke.class);

	/**
	 * Inicijalizira podatke o fakultetu racunarstva
	 * 
	 * @param predmet  polje predmeta
	 * @param profesor polje profesora
	 * @param student  polje studenata
	 * @param ispit    polje ispita
	 */

	public FakultetRacunarstva(Long id, List<Predmet> predmet, List<Profesor> profesor, List<Student> student,
			List<Ispit> ispit) {
		super(id, predmet, profesor, student, ispit);
	}

	/**
	 * @param ispit                     sadrži objekte ispita
	 * @param ocjenaPismeniDiplomskirad ocjena pisemong djela diplomskog
	 * @param ocjenaObranaDiplomskirad  ocjena obrane pisemong djela diplomskog
	 */

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> ispit, Integer ocjenaPismeniDiplomskirad,
			Integer ocjenaObranaDiplomskirad) {
		BigDecimal konacnaOcjena = new BigDecimal("1");
		try {
			konacnaOcjena = (odrediProsjekOcjenaNaIspitima(ispit).multiply(new BigDecimal("3"))
					.add(new BigDecimal(ocjenaPismeniDiplomskirad)).add(new BigDecimal(ocjenaObranaDiplomskirad))
					.divide(new BigDecimal("5")));
		} catch (NemoguceOdreditiProsjekStudentaException e) {
			logger.info(e.getMessage(), e);
			System.out.println(e.getMessage());
		}
		return konacnaOcjena;
	}

	/**
	 * Odreðuje studenta za rekotrovu nagradu.
	 * 
	 * @return objekt student.
	 */

	@Override
	public Student odrediStudentaZaRektorovuNagradu() throws PostojiViseNajmladjihStudenataException{
		List<Ispit> listaIspita = getIspit();
		List<Student> listaStudenta = getStudent();
		Student najboljiStudent = listaStudenta.get(0);
		BigDecimal najboljiProsjek = new BigDecimal("0");

		for (Student student : listaStudenta) {
			try {
				if (odrediProsjekOcjenaNaIspitima(filtrirajIspitePoStudentu(listaIspita, student))
						.compareTo(najboljiProsjek) > 0) {
					najboljiProsjek = odrediProsjekOcjenaNaIspitima(filtrirajIspitePoStudentu(listaIspita, student));
					najboljiStudent = student;
				} else if (odrediProsjekOcjenaNaIspitima(filtrirajIspitePoStudentu(listaIspita, student))
						.compareTo(najboljiProsjek) == 0) {
					if (najboljiStudent.getDatmRodjenja().compareTo(student.getDatmRodjenja()) < 0) {
						najboljiProsjek = odrediProsjekOcjenaNaIspitima(
								filtrirajIspitePoStudentu(listaIspita, student));
						najboljiStudent = student;
					} else if (najboljiStudent.getDatmRodjenja().compareTo(student.getDatmRodjenja()) == 0) {
						logger.info("Pronaðeno je više najmlaðih studenata s istim datumom roðenja");
						throw new PostojiViseNajmladjihStudenataException(
								"Pronaðeno je više najmlaðih studenata s istim datumom roðenja, a to su: "
										+ student.getIme() + " " + student.getPrezime() + ", "
										+ najboljiStudent.getIme() + " " + najboljiStudent.getPrezime());
					}
				}
			} catch (NemoguceOdreditiProsjekStudentaException e) {
				logger.info("Nije moguce odrediti prosjek studenta " + student.getIme() + " " + student.getPrezime()
						+ " za rektorovu nagradu zbog negativne ocjene na jednom od ispita");
			}
		}
		return najboljiStudent;
	}

	/**
	 * Odreðuje najuspješnijeg studenta na godini
	 * 
	 * @param godina predstavlja trenutnu godinu
	 * @return student objekt
	 */

	@Override
	public Student odrediNajuspjesnijegStudentaNaGodini(int godina) {
		List<Ispit> listaIspita = getIspit();
		List<Student> listaStudenta = new ArrayList<>();
		for (Ispit ispiti : listaIspita) {
			if (ispiti.getDatumIVrijeme().getYear() == godina) {
				listaStudenta.add(ispiti.getStudent());
			}
		}
		List<Integer> listaIzvrsnih = new ArrayList<>();
		int index = 0;
		int brojac = 1;
		for (Student student : listaStudenta) {
			for (Ispit ispit : filtrirajIspitePoStudentu(listaIspita, student)) {
				if (ispit.getOcjena() == 5) {
					listaIzvrsnih.add(index, brojac++);
				}
			}
			index++;
		}
		Integer brojUsporedba = listaIzvrsnih.get(0);
		index = 0;
		brojac = 0;
		for (Integer broj : listaIzvrsnih) {
			if (broj > brojUsporedba) {
				index = brojac;
			}
			brojac++;
		}
		return listaStudenta.get(index);
	}
}
