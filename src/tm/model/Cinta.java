package tm.model;

/**
 * Representa la cinta de la máquina de Turing, que es un arreglo de celdas que
 * puede contener símbolos del alfabeto, y sobre la cual se mueve la cabeza para
 * realizar las transiciones de la máquina.
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

	/**
	 * Inicializa la cinta de la maquina
	 * 
	 * @param entrada La cadena de entrada que usa la maquina
	 */
	public void inicializar(String entrada) {
		cinta = new StringBuffer(armarCinta(entrada, espacio));
	}

	/**
	 * Lectura de un caracter en el índice especificado en la cinta
	 * 
	 * @param index en la cinta correspondiente a un caracter que se lee
	 * @return El caracter que se lee de la cinta
	 */
	public char leer(int index) {
		if (cinta.charAt(index) == LIMITE_MARCA) {
			throw new IllegalStateException("Cabeza salió de la cinta.");
		}
		return cinta.charAt(index);
	}

	/**
	 * Escribir un caracter sobre la cinta en el índice especificado
	 * 
	 * @param index      Índice en la cinta donde se debe escribir el carácter.
	 * @param nuevoValor Carácter que se desea escribir en la cinta.
	 */
	public void escribir(int index, char nuevoValor) {
		cinta.replace(index, index + 1, String.valueOf(nuevoValor));
	}

	/**
	 * Su uso es para debugging, cuando no este disponible la interfaz grafica
	 * 
	 * @return Una descripcion de la cinta para uso
	 */
	public String imprimir(int index, int estadoActual) {
		StringBuilder output = new StringBuilder();
		output.append(cinta).append("\n");
		output.append(" ".repeat(index));
		output.append(" ^q").append(estadoActual).append("\n");
		return output.toString();
	}

	/**
	 * Devuelve el resultado final de la cinta. Se llama despues de terminar la
	 * ejecucion
	 * 
	 * @return El resultado de la operacion
	 */
	public String obtenerResultado() {
		String resultado = cinta.toString();
		resultado = resultado.replaceAll("^[\\" + LIMITE_MARCA + "]+|[\\" + LIMITE_MARCA + "]+~", "");
		resultado = resultado.replaceAll("^[" + espacio + "]+|[" + espacio + "]+~", "");
		return resultado;
	}

	/**
	 * Construye la representacion de la cinta.
	 * 
	 * @param entrada La cadena que se coloca en la cinta
	 * @param blank   El simbolo espacio para rellenar la cinta
	 * @return La cadena que representa la cinta, con sus limites y espacios
	 */
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
