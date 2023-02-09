 package hr.java.vjezbe.javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

public class PredmetController {
	
	private static ObservableList<Predmet> listaPredmetObs;

	private List<Predmet> listaPredmet;
	private List<Student> listaStudent;
	private List<Profesor> listaProfesor;

	@FXML
	private TextField sifraPredmet;
	
	@FXML
	private TextField nazivPredmet;
	
	@FXML
	private TextField ectsPredmet;
	
	@FXML
	private ChoiceBox<Profesor> nositeljPredmetBox;
	
	@FXML
	private TableView<Predmet> predmetTable;

	@FXML
	private TableColumn<Predmet, String> sifraPredmetColumn;
	
	@FXML
	private TableColumn<Predmet, String> nazivPredmetColumn;
	
	@FXML
	private TableColumn<Predmet, Integer> ectsPredmetColumn;

	@FXML
	private TableColumn<Predmet, String> profesorNositeljColumn;
	
	@FXML
	public void initialize() {	
		try {
			listaPredmet = BazaPodataka.dohvatiPredmete();
			listaProfesor = BazaPodataka.dohvatiProfesore();
			}catch(BazaPodatakaException e) {
				System.err.println(e);
			}

		sifraPredmetColumn.setCellValueFactory(new PropertyValueFactory<Predmet, String>("sifra"));
		nazivPredmetColumn.setCellValueFactory(new PropertyValueFactory<Predmet, String>("naziv"));
		ectsPredmetColumn.setCellValueFactory(new PropertyValueFactory<Predmet, Integer>("brojEctsBodova"));
		profesorNositeljColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Predmet, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Predmet, String> predmet) {
						SimpleStringProperty property = new SimpleStringProperty();
						property.setValue(predmet.getValue().getNositelj().getPrezime()+" "+predmet.getValue().getNositelj().getIme());
						return property;
					}
				});		
		listaPredmetObs = FXCollections.observableArrayList(listaPredmet);
		predmetTable.setItems(listaPredmetObs);
		
		ObservableList<Profesor>listaProfesorObsBox = FXCollections.observableArrayList(listaProfesor);
		nositeljPredmetBox.setItems(listaProfesorObsBox);
		
		predmetTable.setEditable(true);
		sifraPredmetColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nazivPredmetColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		ectsPredmetColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		profesorNositeljColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	}

	@FXML
	public void pretragaPredmet(){	
	
		List<Predmet> filtriraniPredmeti = new ArrayList<>();
		
		if (!sifraPredmet.getText().isEmpty()){
			filtriraniPredmeti = listaPredmet.stream().filter(p -> p.getSifra().contains(sifraPredmet.getText())).collect(Collectors.toList());
		}else if (!nazivPredmet.getText().isEmpty()){
			filtriraniPredmeti = listaPredmet.stream().filter(p -> p.getNaziv().contains(nazivPredmet.getText())).collect(Collectors.toList());
		}else if (!ectsPredmet.getText().isEmpty()){
			Integer ectsPredmetInteger = Integer.parseInt(ectsPredmet.getText());
			filtriraniPredmeti = listaPredmet.stream().filter(p -> p.getBrojEctsBodova().equals(ectsPredmetInteger)).collect(Collectors.toList());
		}else if (nositeljPredmetBox.getValue() != null){
			filtriraniPredmeti = listaPredmet.stream().filter(p -> p.getNositelj().getId().equals(nositeljPredmetBox.getValue().getId())).collect(Collectors.toList());
		}else {
			filtriraniPredmeti = listaPredmet;
		}
			ObservableList<Predmet> listaPredmetObs = FXCollections.observableArrayList(filtriraniPredmeti);
			predmetTable.setItems(listaPredmetObs);
	}
	
	public void promjenaSifre(CellEditEvent cellEdit) {
		Predmet odabraniPredmet = predmetTable.getSelectionModel().getSelectedItem();
		odabraniPredmet.setSifra(cellEdit.getNewValue().toString());
		try {
			BazaPodataka.urediPredmet(odabraniPredmet);
		} catch (BazaPodatakaException e) {
			System.err.println(e);
		}
	}
	public void promjenaNaziva(CellEditEvent cellEdit) {
		Predmet odabraniPredmet = predmetTable.getSelectionModel().getSelectedItem();
		odabraniPredmet.setNaziv(cellEdit.getNewValue().toString());
		try {
			BazaPodataka.urediPredmet(odabraniPredmet);
		} catch (BazaPodatakaException e) {
			System.err.println(e);
		}
	}

	public void obrisiZapisPredmeta() {
		Predmet odabraniPredmet = predmetTable.getSelectionModel().getSelectedItem();
		try {
			List<Ispit> listaispita = BazaPodataka.dohvatiIspite();		
			for(Ispit ispiti : listaispita) {
				if(odabraniPredmet.getId() == ispiti.getPredmet().getId()) {
					Alert alertSuccess = new Alert(AlertType.INFORMATION);
					alertSuccess.setTitle("Nije moguæe brisanje predmeta!");
					alertSuccess.setHeaderText("Nije moguæe brisanje predmeta! Postoji ispit s odabranim predmetom!");
					alertSuccess.setContentText("Nije moguæe obrisati.");
					alertSuccess.showAndWait();
					return;
				}				
			}		
		} catch (BazaPodatakaException e) {	
			System.err.println(e);
		}
		try {
			BazaPodataka.obrisiPredmet(odabraniPredmet);
			
			Alert alertSuccess = new Alert(AlertType.INFORMATION);
			alertSuccess.setTitle("Uspješno brisanje predmeta!");
			alertSuccess.setHeaderText("Uspješno brisanje predmeta!");
			alertSuccess.setContentText("Podaci o predmetu su uspješno obrisani.");
			alertSuccess.showAndWait();
			
		} catch (BazaPodatakaException e) {
			System.err.println(e);
		}
		initialize();
	}
}
