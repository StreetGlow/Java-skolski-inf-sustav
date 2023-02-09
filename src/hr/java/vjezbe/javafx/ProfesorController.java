package hr.java.vjezbe.javafx;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ch.qos.logback.core.util.Loader;
import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class ProfesorController {
	private static ObservableList<Profesor> listaProfesorObs;

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
	private TableView<Profesor> profesorTable;

	@FXML
	private TableColumn<Profesor, String> sifraProfesorColumn;

	@FXML
	private TableColumn<Profesor, String> prezimeProfesorColumn;

	@FXML
	private TableColumn<Profesor, String> imeProfesorColumn;

	@FXML
	private TableColumn<Profesor, String> titulaProfesorColumn;

	@FXML
	public void initialize() {
		try {
		listaProfesor = BazaPodataka.dohvatiProfesore();
		}catch(BazaPodatakaException e) {
			System.err.println(e);
		}

		sifraProfesorColumn.setCellValueFactory(new PropertyValueFactory<Profesor, String>("sifra"));
		prezimeProfesorColumn.setCellValueFactory(new PropertyValueFactory<Profesor, String>("prezime"));
		imeProfesorColumn.setCellValueFactory(new PropertyValueFactory<Profesor, String>("ime"));
		titulaProfesorColumn.setCellValueFactory(new PropertyValueFactory<Profesor, String>("titula"));

		listaProfesorObs = FXCollections.observableArrayList(listaProfesor);
		profesorTable.setItems(listaProfesorObs);

		profesorTable.setEditable(true);
		imeProfesorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		prezimeProfesorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		sifraProfesorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		titulaProfesorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	}

	@FXML
	public void pretragaProfesor() throws BazaPodatakaException {
		List<Profesor> filtriraniProfesori = new ArrayList<>();

		Long id = null;
		String sifra = sifraProfesor.getText();
		String prezime = prezimeProfesor.getText();
		String ime = imeProfesor.getText();
		String titula = titulaProfesor.getText();

		Profesor profesor = new Profesor(id, ime, prezime, sifra, titula);
		filtriraniProfesori = BazaPodataka.dohvatiProfesorePremaKriterijima(profesor);
		ObservableList<Profesor> listaProfesorObs = FXCollections.observableArrayList(filtriraniProfesori);
		profesorTable.setItems(listaProfesorObs);
	}

	public void promjenaImena(CellEditEvent cellEdit) {
		Profesor odabraniProfesor = profesorTable.getSelectionModel().getSelectedItem();
		odabraniProfesor.setIme(cellEdit.getNewValue().toString());
		try {
			BazaPodataka.urediProfesora(odabraniProfesor);
		} catch (BazaPodatakaException e) {
			System.err.println(e);
		}
	}
	public void promjenaPrezimena(CellEditEvent cellEdit) {
		Profesor odabraniProfesor = profesorTable.getSelectionModel().getSelectedItem();
		odabraniProfesor.setPrezime(cellEdit.getNewValue().toString());
		try {
			BazaPodataka.urediProfesora(odabraniProfesor);
		} catch (BazaPodatakaException e) {
			System.err.println(e);
		}
	}
	public void promjenaSifre(CellEditEvent cellEdit) {
		Profesor odabraniProfesor = profesorTable.getSelectionModel().getSelectedItem();
		odabraniProfesor.setSifra(cellEdit.getNewValue().toString());
		try {
			BazaPodataka.urediProfesora(odabraniProfesor);
		} catch (BazaPodatakaException e) {
			System.err.println(e);
		}
	}
	public void promjenaTitule(CellEditEvent cellEdit) {
		Profesor odabraniProfesor = profesorTable.getSelectionModel().getSelectedItem();
		odabraniProfesor.setTitula(cellEdit.getNewValue().toString());
		try {
			BazaPodataka.urediProfesora(odabraniProfesor);
		} catch (BazaPodatakaException e) {
			System.err.println(e);
		}
	}
	
	public void obrisiZapisProfesora() {
		Profesor odabraniProfesor = profesorTable.getSelectionModel().getSelectedItem();
		try {
			List<Predmet> listapredmeta = BazaPodataka.dohvatiPredmete();		
			for(Predmet predmet : listapredmeta) {
				if(odabraniProfesor.getId() == predmet.getNositelj().getId()) {
					Alert alertSuccess = new Alert(AlertType.INFORMATION);
					alertSuccess.setTitle("Nije moguæe brisanje profesora!");
					alertSuccess.setHeaderText("Nije moguæe brisanje profesora! Odabrani profesor nositelje je predmeta " + predmet.getNaziv() + "!" );
					alertSuccess.setContentText("Nije moguæe obrisati profesora. Potrebno promjeniti nositelja predmeta.");
					alertSuccess.showAndWait();
					return;
				}				
			}		
		} catch (BazaPodatakaException e) {	
			System.err.println(e);
		}
		try {
			BazaPodataka.obrisiProfesora(odabraniProfesor);
			
			Alert alertSuccess = new Alert(AlertType.INFORMATION);
			alertSuccess.setTitle("Uspješno brisanje profesora!");
			alertSuccess.setHeaderText("Uspješno brisanje profesora!");
			alertSuccess.setContentText("Podaci o profesoru su uspješno obrisani.");
			alertSuccess.showAndWait();
			
		} catch (BazaPodatakaException e) {
			System.err.println(e);
		}
		initialize();
	}
}
