package hr.java.vjezbe.entitet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Vraæa objekt predmet definiran siform, nazivom, brojem ects bodova, profesorom nositeljem i poljem studenata
 * @author Karlo
 *
 */

public class Predmet extends Entitet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1746765087683694660L;
	private String sifra;
	private String naziv;
	private Integer brojEctsBodova;
	private Profesor nositelj;
	private Set<Student> student = new HashSet<>();
	
	/**
	 * Inicijalizira podatake o predmetu
	 * @param sifra sifra predmeta
	 * @param naziv naziv predmeta
	 * @param brojEctsBodova broj ects bodova predmeta 
	 * @param nositelj nositelj predmeta
	 * @param student polje studenata na predmetu
	 */
	
	public Predmet(Long id, String sifra, String naziv, Integer brojEctsBodova, Profesor nositelj) {
		super(id);
		this.sifra = sifra;
		this.naziv = naziv;
		this.brojEctsBodova = brojEctsBodova;
		this.nositelj = nositelj;
	}
	
	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Integer getBrojEctsBodova() {
		return brojEctsBodova;
	}

	public void setBrojEctsBodova(Integer brojEctsBodova) {
		this.brojEctsBodova = brojEctsBodova;
	}

	public Profesor getNositelj() {
		return nositelj;
	}

	public void setNositelj(Profesor nositelj) {
		this.nositelj = nositelj;
	}

	public Set<Student> getStudent() {
		return student;
	}

	public void setStudent(Set<Student> student) {
		this.student = student;
	}
	
	@Override
    public String toString() {
        return this.naziv;
    }
}
