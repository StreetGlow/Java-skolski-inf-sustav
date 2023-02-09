package hr.java.vjezbe.entitet;

import java.io.Serializable;
import java.util.List;

/**
 * PRedstavlja apstraktnu klasu definiranu nazivom, poljem predmeta, poljem
 * profesora, poljem studenta i poljem ispita
 * 
 * @author Karlo
 *
 */

public abstract class ObrazovnaUstanova extends Entitet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5035307112080571083L;
	private String naziv;
	private List<Predmet> predmet;
	private List<Profesor> profesor;
	private List<Student> student;
	private List<Ispit> ispit;

	/**
	 * Inicijalizira podatke o obrazovnoj ustanovi
	 * 
	 * @param predmet  objekti predmeta
	 * @param profesor objekti profesora
	 * @param student  objekti studenata
	 * @param ispit    objekti ispita
	 */

	public ObrazovnaUstanova(Long id, List<Predmet> predmet, List<Profesor> profesor, List<Student> student,
			List<Ispit> ispit) {
		super(id);
		this.predmet = predmet;
		this.profesor = profesor;
		this.student = student;
		this.ispit = ispit;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public List<Predmet> getPredmet() {
		return predmet;
	}

	public void setPredmet(List<Predmet> predmet) {
		this.predmet = predmet;
	}

	public List<Profesor> getProfesor() {
		return profesor;
	}

	public void setProfesor(List<Profesor> profesor) {
		this.profesor = profesor;
	}

	public List<Student> getStudent() {
		return student;
	}

	public void setStudent(List<Student> student) {
		this.student = student;
	}

	public List<Ispit> getIspit() {
		return ispit;
	}

	public void setIspit(List<Ispit> ispit) {
		this.ispit = ispit;
	}

	/**
	 * 
	 * @param godina prima trenutnu godinu
	 * @return objekt sutdent
	 */

	public abstract Student odrediNajuspjesnijegStudentaNaGodini(int godina);

}
