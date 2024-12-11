package tm.model;

/**
 * Representa la cinta de la máquina de Turing, que es un arreglo de celdas
 * que puede contener símbolos del alfabeto, y sobre la cual se mueve la cabeza
 * para realizar las transiciones de la máquina.
 */
public class Cinta {
	private static final char LIMITE_MARCA = '~';
	private StringBuffer cinta;
	private final int indiceInicial;
	private char espacio;
	
	public Cinta(int indiceInicial, char espacio) {
		this.indiceInicial = indiceInicial;
		this.espacio = espacio;
	}

	public void inicializar(String entrada) {
		cinta = new StringBuffer(armarCinta(entrada, espacio));
	}

	public char leer(int index) {
		if (cinta.charAt(index) == LIMITE_MARCA) {
			throw new IllegalStateException("Cabeza salió de la cinta.");
		}
		return cinta.charAt(index);
	}

	public void escribir(int index, char nuevoValor) {
		cinta.replace(index, index + 1, String.valueOf(nuevoValor));
	}

	public String imprimir(int index, int estadoActual) {
		StringBuilder output = new StringBuilder();
		output.append(cinta).append("\n");
		output.append(" ".repeat(index));
		output.append(" ^q").append(estadoActual).append("\n");
		return output.toString();
	}

	public String obtenerResultado() {
		String resultado = cinta.toString();
		resultado = resultado.replaceAll("^[\\" + LIMITE_MARCA + "]+|[\\" + LIMITE_MARCA + "]+~", "");
		resultado = resultado.replaceAll("^[" + espacio + "]+|[" + espacio + "]+~", "");
		return resultado;
	}

	private String armarCinta(String entrada, char blank) {
		String blanks = String.valueOf(blank).repeat(indiceInicial - 1);
		String trailingBlanks = String.valueOf(blank).repeat(30);
		return String.format("%s%s%s%s%s", LIMITE_MARCA, blanks, entrada, trailingBlanks, LIMITE_MARCA);
	}

	public String getCinta() {
		return cinta.toString();
	}

	public char getEspacio() {
		return espacio;
	}

	public void setEspacio(char espacio) {
		this.espacio = espacio;
	}
}
