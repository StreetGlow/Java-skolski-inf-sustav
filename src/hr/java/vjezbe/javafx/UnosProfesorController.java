package hr.java.vjezbe.javafx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.OptionalLong;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class UnosProfesorController {

	private List<Profesor> listaProfesor;

	@FXML
	private TextField sifraProfesor;

	@FXML
	private TextField prezimeProfesor;

	@FXML
	private TextField imeProfesor;

	@FXML
	private TextField titulaProfesor;

	@FXML
	public void initialize(){
		try {
			listaProfesor = BazaPodataka.dohvatiProfesore();
		}catch (BazaPodatakaException e){
			System.err.println(e);
		}		
	}

	@FXML
	public void unosProfesor() {
		String kriviUnos = "";
		OptionalLong maksimalniId = listaProfesor.stream().mapToLong(profesor -> profesor.getId()).max();
		String sifra = sifraProfesor.getText().isEmpty() ? kriviUnos += "\nMorate unijeti sifru!"
				: sifraProfesor.getText();
		String prezime = prezimeProfesor.getText().isEmpty() ? kriviUnos += "\nMorate unijeti prezime!"
				: prezimeProfesor.getText();
		String ime = imeProfesor.getText().isEmpty() ? kriviUnos += "\nMorate unijeti ime!" : imeProfesor.getText();
		String titula = titulaProfesor.getText().isEmpty() ? kriviUnos += "\nMorate unijeti titulu!"
				: titulaProfesor.getText();

		if (kriviUnos.isEmpty()) {
			Profesor profesor = new Profesor(maksimalniId.getAsLong() + 1, ime, prezime, sifra, titula);
			
			try {
				BazaPodataka.spremiNovogProfesora(profesor);
				
				Alert alertSuccess = new Alert(AlertType.INFORMATION);
				alertSuccess.setTitle("Uspješno spremanje profesora!");
				alertSuccess.setHeaderText("Uspješno spremanje profesora!");
				alertSuccess.setContentText("Uneseni podaci za profesora su uspješno spremljeni.");
				alertSuccess.showAndWait();
			}catch (BazaPodatakaException e){
				System.err.println(e);
			}

		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Neuspješno spremanje profesora!");
			alert.setHeaderText("Potrebno je ispraviti sljedeæe pogreške:");
			alert.setContentText(kriviUnos);
			alert.showAndWait();
		}

	}
	
}
