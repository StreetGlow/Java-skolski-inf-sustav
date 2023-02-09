package hr.java.vjezbe.javafx;

import java.time.LocalDate;
import java.util.List;
import java.util.OptionalLong;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class UnosStudentController {

	private List<Student> listaStudent;

	@FXML
	private TextField jmbagStudent;

	@FXML
	private TextField prezimeStudent;

	@FXML
	private TextField imeStudent;

	@FXML
	private DatePicker datumRodjenjaStudent;

	@FXML
	public void initialize() {
		try {
			listaStudent = BazaPodataka.dohvatiStudenta();
			}catch(BazaPodatakaException e) {
				System.err.println(e);
			}
	}

	@FXML
	public void unosStudent() {
		String kriviUnos = "";
		LocalDate datumRodjenja = null;
		OptionalLong maksimalniId = listaStudent.stream().mapToLong(student -> student.getId()).max();
		String jmbag = jmbagStudent.getText().isEmpty() ? kriviUnos += "\nMorate unijeti jmbag!"
				: jmbagStudent.getText();
		String prezime = prezimeStudent.getText().isEmpty() ? kriviUnos += "\nMorate unijeti prezime!"
				: prezimeStudent.getText();
		String ime = imeStudent.getText().isEmpty() ? kriviUnos += "\nMorate unijeti ime!" : imeStudent.getText();
		if (datumRodjenjaStudent.getValue() != null) {
			datumRodjenja = datumRodjenjaStudent.getValue();
		} else {
			kriviUnos += "\nMorate odabrati datum roðenja!";
		}

		if (kriviUnos.isEmpty()) {
			Student student = new Student(maksimalniId.getAsLong() + 1, ime, prezime, jmbag, datumRodjenja);

			try {
				BazaPodataka.spremiNovogStudenta(student);

				Alert alertSuccess = new Alert(AlertType.INFORMATION);
				alertSuccess.setTitle("Uspješno spremanje studenta!");
				alertSuccess.setHeaderText("Uspješno spremanje studenta!");
				alertSuccess.setContentText("Uneseni podaci za studenta su uspješno spremljeni.");
				alertSuccess.showAndWait();
			} catch (BazaPodatakaException e) {
				System.err.println(e);
			}

		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Neuspješno spremanje studenta!");
			alert.setHeaderText("Potrebno je ispraviti sljedeæe pogreške:");
			alert.setContentText(kriviUnos);
			alert.showAndWait();
		}

	}
}
