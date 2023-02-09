package hr.java.vjezbe.javafx;

import java.io.FileInputStream;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static final String FILE_PROFESORI = "dat\\profesori.txt";
	public static final String FILE_STUDENTI = "dat\\studenti.txt";
	public static final String FILE_PREDMETI = "dat\\predmeti.txt";
	public static final String FILE_ISPITI = "dat\\ispiti.txt";
	public static DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm");
	public static DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
	
	private static BorderPane root;
	@Override
	public void start(Stage primaryStage) {
		try {
			root = (BorderPane)FXMLLoader.load(getClass().getResource("/hr/java/vjezbe/javafx/PocetnaStranica.fxml"));
			Scene scene = new Scene(root,400,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.getIcons().add(new Image("http://icons.iconarchive.com/icons/giannis-zographos/spanish-football-club/256/FC-Barcelona-icon.png"));
			primaryStage.resizableProperty().setValue(Boolean.FALSE);
			primaryStage.setTitle("Java No.1, kakva kina");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			FileInputStream input = new FileInputStream("C:\\Users\\Karlo\\eclipse-workspace\\Java\\bcg.jpg"); 
			  
            Image image = new Image(input); 
  
            BackgroundImage backgroundimage = new BackgroundImage(image,  
                                             BackgroundRepeat.NO_REPEAT,  
                                             BackgroundRepeat.NO_REPEAT,  
                                             BackgroundPosition.DEFAULT,  
                                                BackgroundSize.DEFAULT); 
  
            Background background = new Background(backgroundimage); 
            root.setBackground(background);            
        	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void setMainPage(BorderPane root1) {
		root.setCenter(root1);
		}
	}
	
