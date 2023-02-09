package hr.java.vjezbe.baza;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;

public class BazaPodataka {

	public static final Logger logger = LoggerFactory.getLogger(BazaPodataka.class);
	private static final String DATABASE_FILE = "bazaPodataka.properties";

	private static Connection spajanjeNaBazuPodataka() throws SQLException, IOException {

		Properties svojstva = new Properties();

		svojstva.load(new FileReader(DATABASE_FILE));

		String urlBazePodataka = svojstva.getProperty("bazaPodatakaUrl");
		String korisnickoIme = svojstva.getProperty("korisnickoIme");
		String lozinka = svojstva.getProperty("lozinka");

		Connection veza = DriverManager.getConnection(urlBazePodataka, korisnickoIme, lozinka);

		return veza;
	}


	public static List<Profesor> dohvatiProfesore() throws BazaPodatakaException {

		List<Profesor> listaProfesor = new ArrayList<>();

		try (Connection veza = spajanjeNaBazuPodataka()) {

			Statement upit = veza.createStatement();

			ResultSet resultSet = upit.executeQuery("SELECT * FROM PROFESOR");

			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String sifra = resultSet.getString("sifra");
				String ime = resultSet.getString("ime");
				String prezime = resultSet.getString("prezime");
				String titula = resultSet.getString("titula");
				Profesor noviProfesor = new Profesor(id, ime, prezime, sifra, titula);
				listaProfesor.add(noviProfesor);
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
		return listaProfesor;
	}

	public static List<Profesor> dohvatiProfesorePremaKriterijima(Profesor profesor) throws BazaPodatakaException {

		List<Profesor> listaProfesor = new ArrayList<>();

		try (Connection veza = spajanjeNaBazuPodataka()) {
			StringBuilder sqlUpit = new StringBuilder("SELECT * FROM PROFESOR WHERE 1 = 1");

			if (Optional.ofNullable(profesor).isEmpty() == false) {

				if (Optional.ofNullable(profesor).map(Profesor::getId).isPresent()) {
					sqlUpit.append(" AND ID = " + profesor.getId());
				}

				if (Optional.ofNullable(profesor.getSifra()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND SIFRA LIKE '%" + profesor.getSifra() + "%'");
				}

				if (Optional.ofNullable(profesor.getIme()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND IME LIKE '%" + profesor.getIme() + "%'");
				}

				if (Optional.ofNullable(profesor.getPrezime()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND PREZIME LIKE '%" + profesor.getPrezime() + "%'");
				}

				if (Optional.ofNullable(profesor.getTitula()).map(String::isBlank).orElse(true) == false) {
					sqlUpit.append(" AND TITULA LIKE '%" + profesor.getTitula() + "%'");
				}
			}

			Statement upit = veza.createStatement();

			ResultSet resultSet = upit.executeQuery(sqlUpit.toString());

			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String sifra = resultSet.getString("sifra");
				String ime = resultSet.getString("ime");
				String prezime = resultSet.getString("prezime");
				String titula = resultSet.getString("titula");
				Profesor noviProfesor = new Profesor(id, ime, prezime, sifra, titula);
				listaProfesor.add(noviProfesor);
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
		return listaProfesor;
	}

	public static void spremiNovogProfesora(Profesor profesor) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazuPodataka()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("INSERT INTO PROFESOR(ime, prezime, sifra, titula) VALUES (?, ?, ?, ?)");
			preparedStatement.setString(1, profesor.getIme());
			preparedStatement.setString(2, profesor.getPrezime());
			preparedStatement.setString(3, profesor.getSifra());
			preparedStatement.setString(4, profesor.getTitula());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static void urediProfesora(Profesor profesor) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazuPodataka()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("UPDATE PROFESOR SET ime = ?, prezime = ?, sifra = ?, titula = ?  WHERE id = ?");
			preparedStatement.setString(1, profesor.getIme());
			preparedStatement.setString(2, profesor.getPrezime());
			preparedStatement.setString(3, profesor.getSifra());
			preparedStatement.setString(4, profesor.getTitula());
			preparedStatement.setLong(5, profesor.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void obrisiProfesora(Profesor profesor) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazuPodataka()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("DELETE FROM PROFESOR WHERE id = ?");
			preparedStatement.setLong(1, profesor.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static List<Predmet> dohvatiPredmete() throws BazaPodatakaException {

		List<Predmet> listaPredmet = new ArrayList<>();

		try (Connection veza = spajanjeNaBazuPodataka()) {

			Statement upit = veza.createStatement();

			ResultSet resultSet = upit.executeQuery("SELECT * FROM PREDMET");

			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String sifra = resultSet.getString("sifra");
				String naziv = resultSet.getString("naziv");
				Integer brojEctsBodova = resultSet.getInt("broj_ects_bodova");
				Long nositeljId = resultSet.getLong("profesor_id");
				
				Statement upitNositelj = veza.createStatement();
				ResultSet resultSetNositeljId = upitNositelj.executeQuery("SELECT * FROM PROFESOR WHERE ID = "+ nositeljId);
				Profesor nositelj = null;
				while (resultSetNositeljId.next()) {
					Long idNositelj = resultSetNositeljId.getLong("id");
					String imeNositelj = resultSetNositeljId.getString("ime");
					String prezimeNositelj = resultSetNositeljId.getString("prezime");
					String sifraNositelj = resultSetNositeljId.getString("sifra");
					String titulaNositelj = resultSetNositeljId.getString("titula");
					nositelj = new Profesor(idNositelj, imeNositelj, prezimeNositelj, sifraNositelj, titulaNositelj);
					}
				
				Predmet noviPredmet = new Predmet(id, sifra, naziv, brojEctsBodova, nositelj);
				listaPredmet.add(noviPredmet);
		}
			
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
		return listaPredmet;
	}

	public static void spremiNoviPredmet(Predmet predmet) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazuPodataka()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("INSERT INTO PREDMET(sifra, naziv, broj_ects_bodova, profesor_id) VALUES (?, ?, ?, ?)");
			preparedStatement.setString(1, predmet.getSifra());
			preparedStatement.setString(2, predmet.getNaziv());
			preparedStatement.setInt(3, predmet.getBrojEctsBodova());
			preparedStatement.setLong(4, predmet.getNositelj().getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static void urediPredmet(Predmet predmet) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazuPodataka()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("UPDATE PREDMET SET sifra = ?, naziv = ?, broj_ects_bodova = ?, profesor_id = ?  WHERE id = ?");
			preparedStatement.setString(1, predmet.getSifra());
			preparedStatement.setString(2, predmet.getNaziv());
			preparedStatement.setInt(3, predmet.getBrojEctsBodova());
			preparedStatement.setLong(4, predmet.getNositelj().getId());
			preparedStatement.setLong(5, predmet.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void obrisiPredmet(Predmet Predmet) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazuPodataka()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("DELETE FROM PREDMET WHERE id = ?");
			preparedStatement.setLong(1, Predmet.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static List<Student> dohvatiStudenta() throws BazaPodatakaException {

		List<Student> listaStudent = new ArrayList<>();

		try (Connection veza = spajanjeNaBazuPodataka()) {

			Statement upit = veza.createStatement();

			ResultSet resultSet = upit.executeQuery("SELECT * FROM STUDENT");

			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String ime = resultSet.getString("ime");
				String prezime = resultSet.getString("prezime");
				String jmbag = resultSet.getString("jmbag");
				LocalDate datmRodjenja = resultSet.getTimestamp("datum_rodjenja").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				Student noviStudent = new Student(id, ime, prezime, jmbag, datmRodjenja);
				listaStudent.add(noviStudent);
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
		return listaStudent;
	}
	
	public static void spremiNovogStudenta(Student student) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazuPodataka()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("INSERT INTO STUDENT(ime, prezime, jmbag, datum_rodjenja) VALUES (?, ?, ?, ?)");
			preparedStatement.setString(1, student.getIme());
			preparedStatement.setString(2, student.getPrezime());
			preparedStatement.setString(3, student.getJmbag());
			preparedStatement.setDate(4, Date.valueOf(student.getDatmRodjenja()));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static void urediStudenta(Student student) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazuPodataka()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("UPDATE STUDENT SET ime = ?, prezime = ?, jmbag = ?, datum_rodjenja = ?  WHERE id = ?");
			preparedStatement.setString(1, student.getIme());
			preparedStatement.setString(2, student.getPrezime());
			preparedStatement.setString(3, student.getJmbag());
			preparedStatement.setDate(4, Date.valueOf(student.getDatmRodjenja()));
			preparedStatement.setLong(5, student.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void obrisiStudenta(Student student) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazuPodataka()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("DELETE FROM STUDENT WHERE id = ?");
			preparedStatement.setLong(1, student.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static List<Ispit> dohvatiIspite() throws BazaPodatakaException {

		List<Ispit> listaIspit = new ArrayList<>();

		try (Connection veza = spajanjeNaBazuPodataka()) {

			Statement upit = veza.createStatement();

			ResultSet resultSet = upit.executeQuery("SELECT * FROM ISPIT");

			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				Long predmetId = resultSet.getLong("predmet_id");
				Long studentId = resultSet.getLong("student_id");
				Integer ocjena = resultSet.getInt("ocjena");
				LocalDateTime datumIVrijeme = resultSet.getTimestamp("datum_i_vrijeme").toLocalDateTime();
				
				Statement upitPredmetId = veza.createStatement();
				ResultSet resultSetPredmetId = upitPredmetId.executeQuery("SELECT * FROM PREDMET WHERE ID = "+ predmetId);
				Predmet predmet = null;
				while (resultSetPredmetId.next()) {
					Long idPredmet = resultSetPredmetId.getLong("id");
					String sifra = resultSetPredmetId.getString("sifra");
					String naziv = resultSetPredmetId.getString("naziv");
					Integer brojEctsBodova = resultSetPredmetId.getInt("broj_ects_bodova");
					Long nositeljId = resultSetPredmetId.getLong("profesor_id");
					
					Statement upitNositelj = veza.createStatement();
					ResultSet resultSetNositeljId = upitNositelj.executeQuery("SELECT * FROM PROFESOR WHERE ID = "+ nositeljId);
					Profesor nositelj = null;
					while (resultSetNositeljId.next()) {
						Long idNositelj = resultSetNositeljId.getLong("id");
						String imeNositelj = resultSetNositeljId.getString("ime");
						String prezimeNositelj = resultSetNositeljId.getString("prezime");
						String sifraNositelj = resultSetNositeljId.getString("sifra");
						String titulaNositelj = resultSetNositeljId.getString("titula");
						nositelj = new Profesor(idNositelj, imeNositelj, prezimeNositelj, sifraNositelj, titulaNositelj);
						}					
					predmet = new Predmet(idPredmet, sifra, naziv, brojEctsBodova, nositelj);	
				}
				
					Statement upitStudentId = veza.createStatement();
					ResultSet resultSetStudentId = upitStudentId.executeQuery("SELECT * FROM STUDENT WHERE ID = "+ studentId);
					Student student = null;
					while (resultSetStudentId.next()) {
						Long idStudent = resultSetStudentId.getLong("id");
						String ime = resultSetStudentId.getString("ime");
						String prezime = resultSetStudentId.getString("prezime");
						String jmbag = resultSetStudentId.getString("jmbag");
						LocalDate datmRodjenja = resultSetStudentId.getTimestamp("datum_rodjenja").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						student = new Student(idStudent, ime, prezime, jmbag, datmRodjenja);
					}
					
					Ispit noviIspit = new Ispit(id, predmet, student, ocjena, datumIVrijeme);
					listaIspit.add(noviIspit);						
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
		return listaIspit;
	}

	public static void spremiNoviIspit(Ispit ispit) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazuPodataka()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("INSERT INTO ISPIT(predmet_id, student_id, ocjena, datum_i_vrijeme) VALUES (?, ?, ?, ?)");
			preparedStatement.setLong(1, ispit.getPredmet().getId());
			preparedStatement.setLong(2, ispit.getStudent().getId());
			preparedStatement.setInt(3, ispit.getOcjena());
			preparedStatement.setTimestamp(4, Timestamp.valueOf(ispit.getDatumIVrijeme()));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	
	public static void obrisiIspit(Ispit ispit) throws BazaPodatakaException {
		try (Connection veza = spajanjeNaBazuPodataka()) {
			PreparedStatement preparedStatement = veza
					.prepareStatement("DELETE FROM ISPIT WHERE id = ?");
			preparedStatement.setLong(1, ispit.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			logger.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
}
