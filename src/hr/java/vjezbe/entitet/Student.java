package hr.java.vjezbe.entitet;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Vraæa objekt klase student predstavljen jbmga-om i datumom roðenja te nasljeðuje klasu osoba i svojstva imena i prezimena
 * @author Karlo
 *
 */

public class Student extends Osoba implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2805651126996616210L;
	private String jmbag;
	private LocalDate datmRodjenja;
	
	/**
	 * Inicijalizira podatke o studentu
	 * @param ime ime studenta
	 * @param prezime prezime studenta
	 * @param jmbag jmbag studenta
	 * @param datmRodjenja datum roðenja studenta
	 */
	
	public Student(Long id, String ime, String prezime, String jmbag, LocalDate datmRodjenja) {
		super(id, ime, prezime);
		this.jmbag = jmbag;
		this.datmRodjenja = datmRodjenja;		
	}

	public String getJmbag() {
		return jmbag;
	}

	public void setJmbag(String jmbag) {
		this.jmbag = jmbag;
	}

	public LocalDate getDatmRodjenja() {
		return datmRodjenja;
	}

	public void setDatmRodjenja(LocalDate datmRodjenja) {
		this.datmRodjenja = datmRodjenja;
	}

	@Override
    public String toString() {
        return this.ime+" "+this.prezime;
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datmRodjenja == null) ? 0 : datmRodjenja.hashCode());
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (datmRodjenja == null) {
			if (other.datmRodjenja != null)
				return false;
		} else if (!datmRodjenja.equals(other.datmRodjenja))
			return false;
		if (jmbag == null) {
			if (other.jmbag != null)
				return false;
		} else if (!jmbag.equals(other.jmbag))
			return false;
		return true;
	}
}
