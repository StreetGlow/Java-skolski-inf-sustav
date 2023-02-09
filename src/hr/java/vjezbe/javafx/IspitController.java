package hr.java.vjezbe.javafx;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import tornadofx.control.DateTimePicker;

public class IspitController {
	
	private static ObservableList<Ispit> listaIspitObs;
	
	private List<Predmet> listaPredmet;
	private List<Student> listaStudent;
	private List<Ispit> listaIspit;

	@FXML
	private ChoiceBox<Predmet> predmetIspitBox;
	
	@FXML
	private ChoiceBox<Student> studentIspitBox;
	
	@FXML
	private ChoiceBox<String> ocjenaIspitBox;
	
	@FXML
	private DateTimePicker datumIVrijemeIspit;
	
	@FXML
	private TableView<Ispit> ispitTable;

	@FXML
	private TableColumn<Ispit, String> predmetIspitColumn;
	
	@FXML
	private TableColumn<Ispit, String> studentIspitColumn;
	
	@FXML
	private TableColumn<Ispit, Integer> ocjenaIspitColumn;
	
	@FXML
	private TableColumn<Ispit, String> datumIVrijemeIspitColumn;
	
	
	@FXML
	public void initialize() {
		try {
			listaStudent = BazaPodataka.dohvatiStudenta();
			listaPredmet = BazaPodataka.dohvatiPredmete();
			listaIspit = BazaPodataka.dohvatiIspite();
			}catch(BazaPodatakaException e) {
				System.err.println(e);
			}

		predmetIspitColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Ispit, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Ispit, String> ispit) {
						SimpleStringProperty property = new SimpleStringProperty();
						property.setValue(ispit.getValue().getPredmet().getNaziv());
						return property;
					}
				});	
		studentIspitColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Ispit, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Ispit, String> ispit) {
						SimpleStringProperty property = new SimpleStringProperty();
						property.setValue(ispit.getValue().getStudent().getPrezime()+" "+ispit.getValue().getStudent().getIme());
						return property;
					}
				});		
		ocjenaIspitColumn.setCellValueFactory(new PropertyValueFactory<Ispit, Integer>("ocjena"));
		datumIVrijemeIspitColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Ispit, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Ispit, String> ispit) {
						SimpleStringProperty property = new SimpleStringProperty();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm");
						property.setValue(ispit.getValue().getDatumIVrijeme().format(formatter));
						return property;
					}
				});
		
		listaIspitObs = FXCollections.observableArrayList(listaIspit);
		ispitTable.setItems(listaIspitObs);
		
		ObservableList<Predmet>listaPredmetObsBox = FXCollections.observableArrayList(listaPredmet);
		predmetIspitBox.setItems(listaPredmetObsBox);
		
		ObservableList<Student>listaStudentObsBox = FXCollections.observableArrayList(listaStudent);
		studentIspitBox.setItems(listaStudentObsBox);
		
		ObservableList<String>listaOcjenaObsBox = FXCollections.observableArrayList("1", "2", "3", "4", "5");
		ocjenaIspitBox.setItems(listaOcjenaObsBox);
	}
	
	public static void dodajNoviIspit(Ispit noviIspit){
		listaIspitObs.add(noviIspit);
	}

	@FXML
	public void pretragaIspit(){	
		List<Ispit> filtriraniIspiti = new ArrayList<>();
		
		if (predmetIspitBox.getValue() != null){
			filtriraniIspiti = listaIspit.stream().filter(p -> p.getPredmet().getId().equals(predmetIspitBox.getValue().getId())).collect(Collectors.toList());
		}else if (studentIspitBox.getValue() != null){
			filtriraniIspiti = listaIspit.stream().filter(p -> p.getStudent().getId().equals(studentIspitBox.getValue().getId())).collect(Collectors.toList());
		}else if (ocjenaIspitBox.getValue() != null){
			Integer ocjenaIspitInteger = Integer.parseInt(ocjenaIspitBox.getValue());
			filtriraniIspiti = listaIspit.stream().filter(p -> p.getOcjena().equals(ocjenaIspitInteger)).collect(Collectors.toList());
		}else if (datumIVrijemeIspit.getValue() != null){	
			filtriraniIspiti = listaIspit.stream().filter(p -> p.getDatumIVrijeme().equals(datumIVrijemeIspit.getDateTimeValue())).collect(Collectors.toList());
		}else {
			filtriraniIspiti = listaIspit;
		}
			ObservableList<Ispit> listaIspitObs = FXCollections.observableArrayList(filtriraniIspiti);
			ispitTable.setItems(listaIspitObs);
	}
	
	public void obrisiZapisIspita() {
		Ispit odabraniIspit = ispitTable.getSelectionModel().getSelectedItem();
		try {
			BazaPodataka.obrisiIspit(odabraniIspit);
			
			Alert alertSuccess = new Alert(AlertType.INFORMATION);
			alertSuccess.setTitle("Uspješno brisanje ispita!");
			alertSuccess.setHeaderText("Uspješno brisanje ispita!");
			alertSuccess.setContentText("Podaci o ispitu su uspješno obrisani.");
			alertSuccess.showAndWait();
			
		} catch (BazaPodatakaException e) {
			System.err.println(e);
		}
		initialize();
	}
}
