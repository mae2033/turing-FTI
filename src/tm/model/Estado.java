package tm.model;

import java.util.List;

public class Estado {

	private String nombre;
	private List<Transicion> transiciones;

	public Estado(List<Transicion> transiciones) {
		this.setTransiciones(transiciones);
	}

	public Estado(int i, List<Transicion> transiciones) {
		this.setNombre(String.valueOf(i));
		this.setTransiciones(transiciones);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Transicion> getTransiciones() {
		return transiciones;
	}

	public void setTransiciones(List<Transicion> transiciones) {
		this.transiciones = transiciones;
	}
}