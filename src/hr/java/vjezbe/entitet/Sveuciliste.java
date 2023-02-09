package hr.java.vjezbe.entitet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sveuciliste <T extends ObrazovnaUstanova> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3783276028092122316L;
	private List<T> listaSveuciliste = new ArrayList<T>();
	
	
	public void dodajObrazovnuUstanovu (T vrijednst) {
		listaSveuciliste.add(vrijednst);
	}
	
	public T dohvatiObrazovnuUstanovu (Integer index) {		
		return listaSveuciliste.get(index);	
	}

	public List<T> getListaSveuciliste() {
		return listaSveuciliste;
	}
}
