package hr.java.vjezbe.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.FakultetRacunarstva;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.ObrazovnaUstanova;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.entitet.Sveuciliste;
import hr.java.vjezbe.entitet.VeleucilisteJave;

public class Datoteke {
	
	public static final Logger logger = LoggerFactory.getLogger(Datoteke.class);
	
	public static final String FILE_PROFESORI = "dat\\profesori.txt";
	public static final String FILE_STUDENTI = "dat\\studenti.txt";
	public static final String FILE_PREDMETI = "dat\\predmeti.txt";
	public static final String FILE_ISPITI = "dat\\ispiti.txt";
	public static final String FILE_OBRAZOVNE_USTANOVE = "dat\\obrazovneUstanove.txt";
	public final static String FORMAT_DATE = "dd.MM.yyyy.";
	public final static String FORMAT_DATE_TIME = "dd.MM.yyyy.HH:mm";

	public static List<Profesor> dohvatiProfesore() {

		List<Profesor> listaProfesor = new ArrayList<>();
		List<String> podaciProfesor = new ArrayList<>();
		int j = 1;
		try (BufferedReader in = new BufferedReader(new FileReader(FILE_PROFESORI))) {
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(j + ") " + line);
				podaciProfesor.add(line);
				j++;
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		for (int i = 0; i < podaciProfesor.size(); i += 5) {
			String idString = podaciProfesor.get(i);
			Long id = Long.parseLong(idString);
			String sifra = podaciProfesor.get(i + 1);
			String ime = podaciProfesor.get(i + 2);
			String prezime = podaciProfesor.get(i + 3);
			String titula = podaciProfesor.get(i + 4);

			Profesor profesor = new Profesor(id, ime, prezime, sifra, titula);
			listaProfesor.add(profesor);
		}
		return listaProfesor;
	}

	public static List<Student> dohvatiStudente() {

		List<Student> listaStudent = new ArrayList<>();
		List<String> podaciStudent = new ArrayList<>();
		DateTimeFormatter format = DateTimeFormatter.ofPattern(FORMAT_DATE);

		try (BufferedReader in = new BufferedReader(new FileReader(FILE_STUDENTI))) {
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				podaciStudent.add(line);
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		for (int i = 0; i < podaciStudent.size(); i += 5) {
			String idString = podaciStudent.get(i);
			Long id = Long.parseLong(idString);
			String ime = podaciStudent.get(i + 1);
			String prezime = podaciStudent.get(i + 2);
			String jmbag = podaciStudent.get(i + 3);
			String stringDatumRodjenja = podaciStudent.get(i + 4);
			LocalDate datumRodjenja = LocalDate.parse(stringDatumRodjenja, format);

			Student student = new Student(id, ime, prezime, jmbag, datumRodjenja);
			listaStudent.add(student);
		}
		return listaStudent;
	}

	public static List<Predmet> dohvatiPredmete(List<Profesor> listaProfesor, List<Student> listaStudent) {

		List<Predmet> listaPredmet = new ArrayList<>();
		List<String> podaciPredmet = new ArrayList<>();

		try (BufferedReader in = new BufferedReader(new FileReader(FILE_PREDMETI))) {
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				podaciPredmet.add(line);
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		Profesor nositelj = null;
		int j = 0;
		for (int i = 0; i < podaciPredmet.size(); i += 6) {
			Set<Student> setStudent = new HashSet<>();

			String idString = podaciPredmet.get(i);
			Long id = Long.parseLong(idString);
			String sifra = podaciPredmet.get(i + 1);
			String naziv = podaciPredmet.get(i + 2);
			String StringBrojEctsBodova = podaciPredmet.get(i + 3);
			Integer brojEctsBodova = Integer.parseInt(StringBrojEctsBodova);
			String stringIdNositelj = podaciPredmet.get(i + 4);
			Long idNositelj = Long.parseLong(stringIdNositelj);
			for (Profesor prof : listaProfesor) {
				if (prof.getId().equals(idNositelj)) {
					nositelj = prof;
				}
			}
			String idStringStudenti = podaciPredmet.get(i + 5);
			String[] parts = idStringStudenti.split(" ");

			for (String part : parts) {
				Long idStudent = Long.parseLong(part);
				for (Student stud : listaStudent) {
					if (stud.getId().equals(idStudent)) {
						setStudent.add(stud);
					}
				}
			}

			Predmet predmet = new Predmet(id, sifra, naziv, brojEctsBodova, nositelj);
			listaPredmet.add(predmet);
			listaPredmet.get(j).setStudent(setStudent);
			j++;
		}
		return listaPredmet;
	}

	public static List<Ispit> dohvatiIspite(List<Predmet> listaPredmet, List<Student> listaStudent) {

		List<Ispit> listaIspit = new ArrayList<>();
		List<String> podaciIspit = new ArrayList<>();
		DateTimeFormatter format = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME);

		try (BufferedReader in = new BufferedReader(new FileReader(FILE_ISPITI))) {
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				podaciIspit.add(line);
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		Predmet predmet = null;
		Student student = null;
		for (int i = 0; i < podaciIspit.size(); i += 5) {
			String idString = podaciIspit.get(i);
			Long id = Long.parseLong(idString);
			String stringIdPredmet = podaciIspit.get(i + 1);
			Long idPredmet = Long.parseLong(stringIdPredmet);
			for (Predmet pred : listaPredmet) {
				if (pred.getId().equals(idPredmet)) {
					predmet = pred;
				}
			}

			String StringIdStudent = podaciIspit.get(i + 2);
			Long idStudent = Long.parseLong(StringIdStudent);
			for (Student stud : listaStudent) {
				if (stud.getId().equals(idStudent)) {
					student = stud;
				}
			}
			String stringOcjena = podaciIspit.get(i + 3);
			Integer ocjena = Integer.parseInt(stringOcjena);
			String stringDatumIVrijeme = podaciIspit.get(i + 4);
			LocalDateTime datumIVrijeme = LocalDateTime.parse(stringDatumIVrijeme, format);

			Ispit ispit = new Ispit(id, predmet, student, ocjena, datumIVrijeme);
			listaIspit.add(ispit);
		}
		return listaIspit;
	}

	public static Sveuciliste<ObrazovnaUstanova> dohvatiObrazovneUstanove(List<Predmet> listaPredmet,
			List<Profesor> listaProfesor, List<Student> listaStudent, List<Ispit> listaIspit) {

		Sveuciliste<ObrazovnaUstanova> listaObrazovnaUstanova = new Sveuciliste<>();
		List<String> podaciObrazovnaUstanova = new ArrayList<>();

		try (BufferedReader in = new BufferedReader(new FileReader(FILE_OBRAZOVNE_USTANOVE))) {
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				podaciObrazovnaUstanova.add(line);
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		for (int i = 0; i < podaciObrazovnaUstanova.size(); i += 6) {
			List<Predmet> novaListaPredmet = new ArrayList<>();
			List<Profesor> novaListaProfesor = new ArrayList<>();
			List<Student> novaListaStudent = new ArrayList<>();
			List<Ispit> novaListaIspit = new ArrayList<>();

			String idString = podaciObrazovnaUstanova.get(i);
			Long id = Long.parseLong(idString);
			String naziv = podaciObrazovnaUstanova.get(i + 1);
			String stringIdPredmet = podaciObrazovnaUstanova.get(i + 2);
			String[] partsPredmet = stringIdPredmet.split(" ");
			for (String part : partsPredmet) {
				Long idPredmet = Long.parseLong(part);
				for (Predmet pred : listaPredmet) {
					if (pred.getId().equals(idPredmet)) {
						novaListaPredmet.add(pred);
					}
				}
			}

			String stringIdProfesor = podaciObrazovnaUstanova.get(i + 3);
			String[] partsProfesor = stringIdProfesor.split(" ");
			for (String part : partsProfesor) {
				Long idProfesor = Long.parseLong(part);
				for (Profesor prof : listaProfesor) {
					if (prof.getId().equals(idProfesor)) {
						novaListaProfesor.add(prof);
					}
				}
			}

			String stringIdStudent = podaciObrazovnaUstanova.get(i + 4);
			String[] partsStudent = stringIdStudent.split(" ");
			for (String part : partsStudent) {
				Long idStudent = Long.parseLong(part);
				for (Student stud : listaStudent) {
					if (stud.getId().equals(idStudent)) {
						novaListaStudent.add(stud);
					}
				}
			}

			String stringIdIspit = podaciObrazovnaUstanova.get(i + 5);
			String[] partsIspit = stringIdIspit.split(" ");
			for (String part : partsIspit) {
				Long idIspit = Long.parseLong(part);
				for (Ispit isp : listaIspit) {
					if (isp.getId().equals(idIspit)) {
						novaListaIspit.add(isp);
					}
				}
			}
			if (id == 1) {
				ObrazovnaUstanova obrazovnaUstanova = new VeleucilisteJave(id, novaListaPredmet, novaListaProfesor,
						novaListaStudent, novaListaIspit);
				obrazovnaUstanova.setNaziv(naziv);
				listaObrazovnaUstanova.dodajObrazovnuUstanovu(obrazovnaUstanova);
			} else {
				ObrazovnaUstanova obrazovnaUstanova = new FakultetRacunarstva(id, novaListaPredmet, novaListaProfesor,
						novaListaStudent, novaListaIspit);
				obrazovnaUstanova.setNaziv(naziv);
				listaObrazovnaUstanova.dodajObrazovnuUstanovu(obrazovnaUstanova);
			}
		}
		return listaObrazovnaUstanova;
	}

}
