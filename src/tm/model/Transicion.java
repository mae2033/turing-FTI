package tm.model;

public class Transicion {

	private char lee;
	private char escribe;
	private char cambio;
	private int siguiente;

	/**
	 * Divide string en lectura, escritura, desplazamiento y el numero del siguiente
	 * estado, en ese orden.
	 */
	public Transicion(String s) {
		setLee(s.charAt(0));
		setEscribe(s.charAt(2));
		setCambio(s.charAt(4));

		int l = s.length();
		String substr = s.substring(6, l).trim();
		setSiguiente(Integer.parseInt(substr));
	}

	public char getLee() {
		return lee;
	}

	public void setLee(char lee) {
		this.lee = lee;
	}

	public char getEscribe() {
		return escribe;
	}

	public void setEscribe(char escribe) {
		this.escribe = escribe;
	}

	public char getCambio() {
		return cambio;
	}

	public void setCambio(char cambio) {
		this.cambio = cambio;
	}

	public int getSiguiente() {
		return siguiente;
	}

	public void setSiguiente(int siguiente) {
		this.siguiente = siguiente;
	}
}