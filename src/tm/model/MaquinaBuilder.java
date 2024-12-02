package tm.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase herramienta para la construccion de una {@link Maquina} turing simulada
 *
 */
public class MaquinaBuilder {

	private Scanner fs;

	/**
	 * @param f Un objeto {@link Scanner} que se utiliza para leer la configuración
	 *          de la máquina desde una fuente de entrada.
	 */
	public MaquinaBuilder(Scanner f) {
		this.fs = f;
	}

	/**
	 * Construye la máquina de Turing a partir de la entrada proporcionada a través
	 * de un objeto {@link Maquina}.
	 * 
	 * Este método lee el título de la máquina, los símbolos de entrada, el símbolo
	 * de espacio, la cantidad de estados definidos y el estado inicial. A partir de
	 * los símbolos de entrada, se crea un conjunto único de caracteres. Luego, se
	 * establece el estado actual en el estado inicial y se agregan los estados a la
	 * máquina mediante el método {@link #addEstado(int)}.
	 * 
	 * @param m Un objeto {@link Maquina} que se utiliza para leer la configuración
	 *          de la máquina desde una fuente de entrada.
	 */
	public void buildMachine(Maquina m) {
		m.maquinaNombre = readString();
		m.alpha = readAlphabet();
		m.espacioSym = readChar();
		m.cantidadEstados = readInt();
		m.estadoInicial = readInt();
		m.estadoActual = m.estadoInicial;
		for (int i = 0; i < m.cantidadEstados; i++)
			addState(i, m);
	}

	/**
	 * Agrega un estado a la lista de estados de la máquina de Turing. Si el estado
	 * tiene cero transiciones, se considera un estado aceptador.
	 * 
	 * Este método lee la cantidad de transiciones para el estado desde la entrada,
	 * y si el número de transiciones es cero, actualiza el estado final de la
	 * máquina al índice del estado actual. Luego, lee las transiciones y las agrega
	 * al estado antes de añadirlo a la lista de estados.
	 * 
	 * @param ind El índice del estado que se está agregando.
	 * @param m   La maquina a la que se le agrega un estado
	 */
	public void addState(int ind, Maquina m) {
		int trCount = readInt();

		ArrayList<Transicion> trs = new ArrayList<>();
		if (trCount == 0)
			m.estadoFinal = ind;

		for (int i = 0; i < trCount; i++) {
			String s = readString();
			Transicion tr = new Transicion(s);
			trs.add(tr);
		}
		Estado st = new Estado(ind, trs);
		m.states.add(st);
	}

	/**
	 * Lee la siguiente entrada del archivo `fs` y la devuelve como una cadena.
	 * Omite cualquier línea que sea un comentario (que comience con "//") o esté
	 * vacía.
	 *
	 * @return La siguiente entrada válida como String.
	 */
	private String readString() {
		return read();
	}

	private Set<Character> readAlphabet() {
		String s = readString();
		return Arrays.stream(s.split(" ")).flatMap(symbol -> symbol.chars().mapToObj(c -> (char) c))
				.collect(Collectors.toSet());
	}

	/**
	 * Lee la siguiente entrada del archivo `fs` y devuelve el primer carácter.
	 * Omite cualquier línea que sea un comentario (que comience con "//") o esté
	 * vacía.
	 *
	 * @return El primer carácter de la siguiente entrada válida.
	 */
	private char readChar() {
		return read().charAt(0);
	}

	/**
	 * Lee la siguiente entrada del archivo `fs`, elimina cualquier espacio en
	 * blanco y la convierte a un entero. Omite cualquier línea que sea un
	 * comentario (que comience con "//") o esté vacía.
	 *
	 * @return La siguiente entrada válida como entero.
	 * @throws NumberFormatException si la entrada no puede convertirse en un
	 *                               entero.
	 */
	private int readInt() {
		return Integer.parseInt(read().trim());
	}

	/**
	 * Lee la siguiente entrada válida del archivo `fs`. Ignora líneas que comienzan
	 * con "//" o están vacías.
	 *
	 * @return La siguiente entrada válida como String.
	 */
	private String read() {
		String s = fs.next();
		while (s.startsWith("//") || s.isEmpty())
			s = fs.next();
		return s;
	}
}
