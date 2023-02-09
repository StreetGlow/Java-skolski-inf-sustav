package hr.java.vjezbe.javafx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class UnosPredmetController {

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
	private TextField imeNositeljPredmet;

	@FXML
	private TextField prezimeNositeljPredmet;

	@FXML
	private ChoiceBox<Profesor> nositeljPredmetBox;

	@FXML
	public void initialize() {
		try {
			listaPredmet = BazaPodataka.dohvatiPredmete();
			listaProfesor = BazaPodataka.dohvatiProfesore();
			}catch(BazaPodatakaException e) {
				System.err.println(e);
			}
		ObservableList<Profesor> listaPredmetObsBox = FXCollections.observableArrayList(listaProfesor);
		nositeljPredmetBox.setItems(listaPredmetObsBox);
	}

	@FXML
	public void unosPredmet() {
		String kriviUnos = "";
		Profesor nositelj = null;
		OptionalLong maksimalniId = listaPredmet.stream().mapToLong(predmet -> predmet.getId()).max();
		String sifra = sifraPredmet.getText().isEmpty() ? kriviUnos += "\nMorate unijeti sifru!"
				: sifraPredmet.getText();
		String naziv = nazivPredmet.getText().isEmpty() ? kriviUnos += "\nMorate unijeti naziv predmeta!"
				: nazivPredmet.getText();
		String brojEctsBodovaString = ectsPredmet.getText().isEmpty()
				? kriviUnos += "\nMorate unijeti broj ECTS bodova!"
				: ectsPredmet.getText();
		Integer brojEctsBodova = null;
		if (!ectsPredmet.getText().isEmpty()) {
			brojEctsBodova = Integer.parseInt(brojEctsBodovaString);
		}
		if (nositeljPredmetBox.getValue() != null) {
			nositelj = nositeljPredmetBox.getValue();
		} else {
			kriviUnos += "\nMorate odabrati nositelja predmeta!";
		}

		if (kriviUnos.isEmpty()) {
			Predmet predmet = new Predmet(maksimalniId.getAsLong() + 1, sifra, naziv, brojEctsBodova, nositelj);

			try {
				BazaPodataka.spremiNoviPredmet(predmet);
				
				Alert alertSuccess = new Alert(AlertType.INFORMATION);
				alertSuccess.setTitle("Uspješno spremanje predmeta!");
				alertSuccess.setHeaderText("Uspješno spremanje predmeta!");
				alertSuccess.setContentText("Uneseni podaci za predmeta su uspješno spremljeni.");
				alertSuccess.showAndWait();
			}catch (BazaPodatakaException e){
				System.err.println(e);
			}

		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Neuspješno spremanje predmeta!");
			alert.setHeaderText("Potrebno je ispraviti sljedeæe pogreške:");
			alert.setContentText(kriviUnos);
			alert.showAndWait();
		}

	}
}
