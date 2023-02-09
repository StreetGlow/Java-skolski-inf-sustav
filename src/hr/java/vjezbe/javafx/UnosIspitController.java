package hr.java.vjezbe.javafx;

import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalLong;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import tornadofx.control.DateTimePicker;

public class UnosIspitController {

	private List<Predmet> listaPredmet;
	private List<Student> listaStudent;
	private List<Ispit> listaIspit;

	@FXML
	private ChoiceBox<Predmet> predmetIspitBox;
	
	@FXML
	private ChoiceBox<Student> studentIspitBox;
	
	@FXML
	private ChoiceBox<Integer> ocjenaIspitBox;
	
	@FXML
	private DateTimePicker datumIVrijemeIspit;
	
	@FXML
	public void initialize() {
		try {
			listaStudent = BazaPodataka.dohvatiStudenta();
			listaPredmet = BazaPodataka.dohvatiPredmete();
			listaIspit = BazaPodataka.dohvatiIspite();
			}catch(BazaPodatakaException e) {
				System.err.println(e);
			}
		
		ObservableList<Predmet>listaPredmetObsBox = FXCollections.observableArrayList(listaPredmet);
		predmetIspitBox.setItems(listaPredmetObsBox);
		
		ObservableList<Student>listaStudentObsBox = FXCollections.observableArrayList(listaStudent);
		studentIspitBox.setItems(listaStudentObsBox);
		
		ObservableList<Integer>listaOcjenaObsBox = FXCollections.observableArrayList(1, 2, 3, 4, 5);
		ocjenaIspitBox.setItems(listaOcjenaObsBox);
	}

	@FXML
	public void unosIspit() {
		String kriviUnos = "";
		Predmet predmet = null;
		Student student = null;
		Integer ocjena = null;
		LocalDateTime datumIVrijeme = null;
		OptionalLong maksimalniId = listaIspit.stream().mapToLong(ispit -> ispit.getId()).max();
		if (predmetIspitBox.getValue() != null) {
			predmet = predmetIspitBox.getValue();
		} else {
			kriviUnos += "\nMorate odabrati predmet za ispit!";
		}
		if (studentIspitBox.getValue() != null) {
			student = studentIspitBox.getValue();
		} else {
			kriviUnos += "\nMorate odabrati studenta za ispit!";
		}
		if (ocjenaIspitBox.getValue() != null) {
			ocjena = ocjenaIspitBox.getValue();
		} else {
			kriviUnos += "\nMorate odabrati ocjenu za ispit!";
		}
		if (datumIVrijemeIspit.getValue() != null) {
			datumIVrijeme = datumIVrijemeIspit.getDateTimeValue();
		} else {
			kriviUnos += "\nMorate odabrati datum roðenja!";
		}

		if (kriviUnos.isEmpty()) {
					
			Ispit ispit= new Ispit(maksimalniId.getAsLong() + 1, predmet, student, ocjena, datumIVrijeme);

			try {
				BazaPodataka.spremiNoviIspit(ispit);

				Alert alertSuccess = new Alert(AlertType.INFORMATION);
				alertSuccess.setTitle("Uspješno spremanje ispita!");
				alertSuccess.setHeaderText("Uspješno spremanje ispita!");
				alertSuccess.setContentText("Uneseni podaci za ispita su uspješno spremljeni.");
				alertSuccess.showAndWait();
			} catch (BazaPodatakaException e) {
				System.err.println(e);
			}

			IspitController.dodajNoviIspit(ispit);
			
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Neuspješno spremanje ispita!");
			alert.setHeaderText("Potrebno je ispraviti sljedeæe pogreške:");
			alert.setContentText(kriviUnos);
			alert.showAndWait();
		}

	}
}
