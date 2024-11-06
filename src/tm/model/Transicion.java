package tm.model;

public class Transicion {

	char read;
	char write;
	char shift;
	int nextState;

	/**
	 * Divide string en lectura, escritura, desplazamiento y el numero del siguiente
	 * estado, en ese orden.
	 */
	public Transicion(String s) {
		read = s.charAt(0);
		write = s.charAt(2);
		shift = s.charAt(4);

		int l = s.length();
		String substr = s.substring(6, l).trim();
		nextState = Integer.parseInt(substr);
	}

	@Override
	public String toString() {
		return "[read=" + read + ", write=" + write + ", shift=" + shift + ", nextState=" + nextState + "]";
	}

}