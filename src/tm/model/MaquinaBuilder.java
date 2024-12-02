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
	 * @param f Un objeto {@link Scanner} que se utiliza para leer la configuraci�n
	 *          de la m�quina desde una fuente de entrada.
	 */
	public MaquinaBuilder(Scanner f) {
		this.fs = f;
	}

	/**
	 * Construye la m�quina de Turing a partir de la entrada proporcionada a trav�s
	 * de un objeto {@link Maquina}.
	 * 
	 * Este m�todo lee el t�tulo de la m�quina, los s�mbolos de entrada, el s�mbolo
	 * de espacio, la cantidad de estados definidos y el estado inicial. A partir de
	 * los s�mbolos de entrada, se crea un conjunto �nico de caracteres. Luego, se
	 * establece el estado actual en el estado inicial y se agregan los estados a la
	 * m�quina mediante el m�todo {@link #addEstado(int)}.
	 * 
	 * @param m Un objeto {@link Maquina} que se utiliza para leer la configuraci�n
	 *          de la m�quina desde una fuente de entrada.
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
	 * Agrega un estado a la lista de estados de la m�quina de Turing. Si el estado
	 * tiene cero transiciones, se considera un estado aceptador.
	 * 
	 * Este m�todo lee la cantidad de transiciones para el estado desde la entrada,
	 * y si el n�mero de transiciones es cero, actualiza el estado final de la
	 * m�quina al �ndice del estado actual. Luego, lee las transiciones y las agrega
	 * al estado antes de a�adirlo a la lista de estados.
	 * 
	 * @param ind El �ndice del estado que se est� agregando.
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
	 * Omite cualquier l�nea que sea un comentario (que comience con "//") o est�
	 * vac�a.
	 *
	 * @return La siguiente entrada v�lida como String.
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
	 * Lee la siguiente entrada del archivo `fs` y devuelve el primer car�cter.
	 * Omite cualquier l�nea que sea un comentario (que comience con "//") o est�
	 * vac�a.
	 *
	 * @return El primer car�cter de la siguiente entrada v�lida.
	 */
	private char readChar() {
		return read().charAt(0);
	}

	/**
	 * Lee la siguiente entrada del archivo `fs`, elimina cualquier espacio en
	 * blanco y la convierte a un entero. Omite cualquier l�nea que sea un
	 * comentario (que comience con "//") o est� vac�a.
	 *
	 * @return La siguiente entrada v�lida como entero.
	 * @throws NumberFormatException si la entrada no puede convertirse en un
	 *                               entero.
	 */
	private int readInt() {
		return Integer.parseInt(read().trim());
	}

	/**
	 * Lee la siguiente entrada v�lida del archivo `fs`. Ignora l�neas que comienzan
	 * con "//" o est�n vac�as.
	 *
	 * @return La siguiente entrada v�lida como String.
	 */
	private String read() {
		String s = fs.next();
		while (s.startsWith("//") || s.isEmpty())
			s = fs.next();
		return s;
	}
}
