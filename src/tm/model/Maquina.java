package tm.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
//import java.util.Arrays;
//import java.util.stream.Collectors;

import tm.app.AppController;

public class Maquina {

	AppController controller;

	static int VELOCIDAD = 300;
	Scanner fs;
	String nameMachine;
	Set<Character> alpha;
	int cantidadEstados;
	int estadoInicial;
	int estadoActual;
	int estadoFinal;
	char espacioSym;

	Cinta cinta; // Refactorización de Cinta como una clase, incompleto

	StringBuffer Tape = new StringBuffer(); // Cinta

	List<Estado> states = new ArrayList<>(); // estados

	final int INIT_INDEX = 10; // posicion de inicio en la cinta
	private int indice;
	private String result;

	public Maquina() {
		indice = INIT_INDEX;
	}

	public void carga(Scanner f) {
		MaquinaBuilder builder = new MaquinaBuilder(f);
		builder.buildMachine(this);
	}

	public Maquina(Scanner f) {
		MaquinaBuilder builder = new MaquinaBuilder(f);
		builder.buildMachine(this);
	}

	/**
	 * Ejecuta la máquina de Turing hasta que se alcance el estado final. En cada
	 * iteración, realiza una transición basada en el estado actual y la entrada de
	 * la Tape, actualizando la posición del cabezal.
	 * 
	 * Si el cabezal sale de los límites de la Cinta, se lanza una excepción
	 * {@link InterruptedException} para indicar un error en la ejecución.
	 * 
	 * Durante la ejecución, se muestra la Cinta después de cada transición para
	 * proporcionar una visualización del estado actual de la máquina.
	 * 
	 * @param index La posición actual del cabezal de lectura/escritura en la Cinta.
	 * @throws InterruptedException si el cabezal sale de la Tape o si se produce
	 *                              una interrupción en la ejecución.
	 */
	public void runTuring(int index) throws InterruptedException {
		while (estadoActual != estadoFinal) {
			index = makeTrans(index);
			if (index == -1)
				throw new InterruptedException("ERROR: Cabeza salio de la Tape. Maquina detenida.");
			displayTape(index);

		}
		update();
	}

	/**
	 * Restablece el estado actual de la máquina a su valor inicial y actualiza la
	 * Tape de entrada con el resultado obtenido.
	 */
	private void update() {
		estadoActual = estadoInicial;
		result = Tape.toString();
		result = result.replaceAll("^[\\~]+|[\\~]+~", "");
		result = result.replaceAll("^[" + espacioSym + "]+|[" + espacioSym + "]+~", "");

		System.out.println("Resultado de la cinta: " + result);
	}

	public String getTape() {
		return Tape.toString();
	}

	public String getResult() {
		return result;
	}

	/**
	 * Realiza una transición en la máquina de Turing basada en el símbolo actual en
	 * la Tape y el estado actual de la máquina.
	 * 
	 * Este método verifica el símbolo en la posición actual del cabezal. Si es un
	 * delimitador (`~`), se lanza una excepción {@link InterruptedException} para
	 * indicar que el cabezal ha salido de la Tape. Luego, busca en las transiciones
	 * del estado actual para encontrar una coincidencia con el símbolo leído. Si se
	 * encuentra una transición, actualiza la Tape y el estado actual de la máquina,
	 * y determina la nueva posición del cabezal en función de la dirección
	 * especificada en la transición.
	 * 
	 * @param index La posición actual del cabezal de lectura/escritura en la Tape.
	 * @return La nueva posición del cabezal después de la transición.
	 * @throws InterruptedException si el cabezal sale de la Tape.
	 */
	public int makeTrans(int index) throws InterruptedException {
		if (Tape.charAt(index) == '~')
			throw new InterruptedException("ERROR: Cabeza salio de la Tape. Maquina detenida.");
		Estado st = states.get(estadoActual);

		for (Transicion tr : st.transiciones) {
			if (tr.read == Tape.charAt(index)) {
				Tape.replace(index, index + 1, String.valueOf(tr.write));
				estadoActual = tr.nextState;

				switch (tr.shift) {
				case 'R':
					return index + 1;
				case 'L':
					return index - 1;
				case 'N': // nuevo, no desplazamientos
					return index;
				default:
					return -1;
				}
			}
		}
		return -1;
	}

	/* metodo para pruebas */
	public String displayTape(int index) {
		String aTape = printTape(index);
		controller.guiCinta(aTape);
		System.out.println(aTape);
		return aTape;
	}

	/**
	 * Genera una representación de la Tape de la máquina con una marca que indica
	 * la posición actual del cabezal de lectura/escritura y el estado de la
	 * máquina.
	 * 
	 * La Tape se muestra en la consola, con un puntero (`^q`) ubicado en el índice
	 * especificado para señalar la posición actual del cabezal. Incluye una pausa
	 * para permitir la visualización de la Tape antes de la siguiente
	 * actualización.
	 * 
	 * @param index La posición actual del cabezal en la cinta.
	 * @return Una cadena que representa el estado actual de la cinta y la posición
	 *         del cabezal.
	 */
	public String printTape(int index) {
		int interval = VELOCIDAD; // ms
		StringBuilder output = new StringBuilder();

		output.append("Cinta: \n").append(Tape).append("\n");
		for (int i = 0; i < index-1; i++) {
			output.append(" ");
		}
		output.append("^q").append(estadoActual).append("\n");
//		output.append("^[indice]:").append(index).append("\n");

		try { // Esperar la siguiente
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			output.append(e.getMessage());
		}

		return output.toString();
	}

	/**
	 * Construye una cadena que representa la Tape de entrada de la máquina,
	 * agregando caracteres en blanco y delimitadores al principio y al final.
	 * 
	 * La Tape comienza con un delimitador '~', seguido de una serie de caracteres
	 * en blanco especificados por el parámetro {@code blank}, el contenido de la
	 * cadena de entrada, más caracteres en blanco para completar el resto de la
	 * Tape, y finaliza con otro delimitador '~'.
	 *
	 * @param str   La cadena de entrada que se agregará a la Tape.
	 * @param blank El carácter en blanco utilizado para rellenar la Tape.
	 * @return Una cadena que representa la Tape de entrada, preparada para ser
	 *         procesada por la máquina.
	 */
	public String buildTape(String str, char blank) {
		String s = "~";
		for (int i = 0; i < INIT_INDEX - 1; i++)
			s += blank;
		s = s.concat(str);
		for (int i = 0; i < 30; i++)
			s += blank;
		s += '~';
		return s;
	}

	/**
	 * Establece la Tape de entrada de la máquina utilizando una cadena de entrada,
	 * formateada con el símbolo en blanco y delimitadores al principio y al final.
	 */
	public void setTape(String inputstr) {
		this.Tape = new StringBuffer(buildTape(inputstr, espacioSym));

		printTape(INIT_INDEX);
	}

	/**
	 * Obtiene el nombre de la máquina de Turing.
	 */
	public String getName() {
		return nameMachine;
	}

	public void interrupt() {
		// interruppir el hilo
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}

	public void setVelocidad(int v) {
		VELOCIDAD = v;
	}

	public int getIndice() {
		return indice;
	}

	public int getInitIndex() {
		return INIT_INDEX;
	}

}