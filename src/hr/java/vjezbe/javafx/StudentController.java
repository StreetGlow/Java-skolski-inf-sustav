package hr.java.vjezbe.javafx;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class StudentController {
	
	private static ObservableList<Student> listaStudentObs;

	private List<Student> listaStudent;

	@FXML
	private TextField jmbagStudent;

	@FXML
	private TextField prezimeStudent;

	@FXML
	private TextField imeStudent;

	@FXML
	private DatePicker datumRodenjaStudent;

	@FXML
	private TableView<Student> studentTable;

	@FXML
	private TableColumn<Student, String> jmbagStudentColumn;

	@FXML
	private TableColumn<Student, String> prezimeStudentColumn;

	@FXML
	private TableColumn<Student, String> imeStudentColumn;

	@FXML
	private TableColumn<Student, String> datumRodenjaStudentColumn;

	@FXML
	public void initialize() {
		try {
			listaStudent = BazaPodataka.dohvatiStudenta();
			}catch(BazaPodatakaException e) {
				System.err.println(e);
			}

		jmbagStudentColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("jmbag"));
		prezimeStudentColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("prezime"));
		imeStudentColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("ime"));
		datumRodenjaStudentColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Student, String> student) {
						SimpleStringProperty property = new SimpleStringProperty();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
						property.setValue(student.getValue().getDatmRodjenja().format(formatter));
						return property;
					}
				});

		listaStudentObs = FXCollections.observableArrayList(listaStudent);
		studentTable.setItems(listaStudentObs);
		
		studentTable.setEditable(true);
		imeStudentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		prezimeStudentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		jmbagStudentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		datumRodenjaStudentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		
	}

	@FXML
	public void pretragaStudent(){	
	
		List<Student> filtriraniStudenti = new ArrayList<>();

		if (!jmbagStudent.getText().isEmpty()){
			filtriraniStudenti = listaStudent.stream().filter(p -> p.getJmbag().contains(jmbagStudent.getText())).collect(Collectors.toList());
		}else if (!prezimeStudent.getText().isEmpty()){
			filtriraniStudenti = listaStudent.stream().filter(p -> p.getPrezime().contains(prezimeStudent.getText())).collect(Collectors.toList());
		}else if (!imeStudent.getText().isEmpty()){
			filtriraniStudenti = listaStudent.stream().filter(p -> p.getIme().contains(imeStudent.getText())).collect(Collectors.toList());
		}else if (datumRodenjaStudent.getValue() != null){
			filtriraniStudenti = listaStudent.stream().filter(p -> p.getDatmRodjenja().equals(datumRodenjaStudent.getValue())).collect(Collectors.toList());
		}else {
			filtriraniStudenti = listaStudent;
		}
		ObservableList<Student> listaStudentObs = FXCollections.observableArrayList(filtriraniStudenti);
		studentTable.setItems(listaStudentObs);
	}
	
	public void promjenaImena(CellEditEvent cellEdit) {
		Student odabraniStudent = studentTable.getSelectionModel().getSelectedItem();
		odabraniStudent.setIme(cellEdit.getNewValue().toString());
		try {
			BazaPodataka.urediStudenta(odabraniStudent);
		} catch (BazaPodatakaException e) {
			System.err.println(e);
		}
	}
	public void promjenaPrezimena(CellEditEvent cellEdit) {
		Student odabraniStudent = studentTable.getSelectionModel().getSelectedItem();
		odabraniStudent.setPrezime(cellEdit.getNewValue().toString());
		try {
			BazaPodataka.urediStudenta(odabraniStudent);
		} catch (BazaPodatakaException e) {
			System.err.println(e);
		}
	}
	
	public void promjenaJmbaga(CellEditEvent cellEdit) {
		Student odabraniStudent = studentTable.getSelectionModel().getSelectedItem();
		odabraniStudent.setJmbag(cellEdit.getNewValue().toString());
		try {
			BazaPodataka.urediStudenta(odabraniStudent);
		} catch (BazaPodatakaException e) {
			System.err.println(e);
		}
	}
	
	public void obrisiZapisStudenta() {
		Student odabraniStudent = studentTable.getSelectionModel().getSelectedItem();
		try {
			List<Ispit> listaispita = BazaPodataka.dohvatiIspite();		
			for(Ispit ispiti : listaispita) {
				if(odabraniStudent.getId() == ispiti.getStudent().getId()) {
					Alert alertSuccess = new Alert(AlertType.INFORMATION);
					alertSuccess.setTitle("Nije moguæe brisanje studenta!");
					alertSuccess.setHeaderText("Nije moguæe brisanje studenta koji ima ispite!");
					alertSuccess.setContentText("Studenta ima upisane ispite.");
					alertSuccess.showAndWait();
					return;
				}				
			}		
		} catch (BazaPodatakaException e) {	
			System.err.println(e);
		}
		try {
			BazaPodataka.obrisiStudenta(odabraniStudent);
			
			Alert alertSuccess = new Alert(AlertType.INFORMATION);
			alertSuccess.setTitle("Uspješno brisanje studenta!");
			alertSuccess.setHeaderText("Uspješno brisanje studenta!");
			alertSuccess.setContentText("Podaci o studentu su uspješno obrisani.");
			alertSuccess.showAndWait();
			
		} catch (BazaPodatakaException e) {
			System.err.println(e);
		}		
		initialize();
	}
}
