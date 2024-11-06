package tm.model;

import java.util.List;

public class Estado {

	String nombre;
	List<Transicion> transiciones;

	@Deprecated
	public Estado(List<Transicion> transiciones) {
		this.transiciones = transiciones;
	}

	public Estado(int i, List<Transicion> transiciones) {
		this.nombre = "q" + i;
		this.transiciones = transiciones;
	}

	@Override
	public String toString() {
		return nombre;
	}
}