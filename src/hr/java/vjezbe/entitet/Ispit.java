package hr.java.vjezbe.entitet;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Vraæa objekt ispit predstavljenog predmetom, studentom, ocjenom te datumom i vremenom
 * 
 * @author Karlo
 *
 */

public class Ispit extends Entitet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5951245364760095819L;
	private Predmet predmet;
	private Student student;
	private Integer ocjena;
	private LocalDateTime datumIVrijeme;
	
	/**
	 * 
	 * @param predmet podaci o predmetu
	 * @param student podaci o studentu
	 * @param ocjena podatak o ocjeni
	 * @param datumIVrijeme podatak o datumu i vremenu
	 */
	
	public Ispit(Long id, Predmet predmet, Student student, Integer ocjena, LocalDateTime datumIVrijeme) {
		super(id);
		this.predmet = predmet;
		this.student = student;
		this.ocjena = ocjena;
		this.datumIVrijeme = datumIVrijeme;
	}
	public Predmet getPredmet() {
		return predmet;
	}
	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public Integer getOcjena() {
		return ocjena;
	}
	public void setOcjena(Integer ocjena) {
		this.ocjena = ocjena;
	}
	public LocalDateTime getDatumIVrijeme() {
		return datumIVrijeme;
	}
	public void setDatumIVrijeme(LocalDateTime datumIVrijeme) {
		this.datumIVrijeme = datumIVrijeme;
	}
}
