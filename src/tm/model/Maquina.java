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

	Cinta cinta; // Refactorizaci�n de Cinta como una clase, incompleto

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
	 * Ejecuta la m�quina de Turing hasta que se alcance el estado final. En cada
	 * iteraci�n, realiza una transici�n basada en el estado actual y la entrada de
	 * la Tape, actualizando la posici�n del cabezal.
	 * 
	 * Si el cabezal sale de los l�mites de la Cinta, se lanza una excepci�n
	 * {@link InterruptedException} para indicar un error en la ejecuci�n.
	 * 
	 * Durante la ejecuci�n, se muestra la Cinta despu�s de cada transici�n para
	 * proporcionar una visualizaci�n del estado actual de la m�quina.
	 * 
	 * @param index La posici�n actual del cabezal de lectura/escritura en la Cinta.
	 * @throws InterruptedException si el cabezal sale de la Tape o si se produce
	 *                              una interrupci�n en la ejecuci�n.
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
	 * Restablece el estado actual de la m�quina a su valor inicial y actualiza la
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
	 * Realiza una transici�n en la m�quina de Turing basada en el s�mbolo actual en
	 * la Tape y el estado actual de la m�quina.
	 * 
	 * Este m�todo verifica el s�mbolo en la posici�n actual del cabezal. Si es un
	 * delimitador (`~`), se lanza una excepci�n {@link InterruptedException} para
	 * indicar que el cabezal ha salido de la Tape. Luego, busca en las transiciones
	 * del estado actual para encontrar una coincidencia con el s�mbolo le�do. Si se
	 * encuentra una transici�n, actualiza la Tape y el estado actual de la m�quina,
	 * y determina la nueva posici�n del cabezal en funci�n de la direcci�n
	 * especificada en la transici�n.
	 * 
	 * @param index La posici�n actual del cabezal de lectura/escritura en la Tape.
	 * @return La nueva posici�n del cabezal despu�s de la transici�n.
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
	 * Genera una representaci�n de la Tape de la m�quina con una marca que indica
	 * la posici�n actual del cabezal de lectura/escritura y el estado de la
	 * m�quina.
	 * 
	 * La Tape se muestra en la consola, con un puntero (`^q`) ubicado en el �ndice
	 * especificado para se�alar la posici�n actual del cabezal. Incluye una pausa
	 * para permitir la visualizaci�n de la Tape antes de la siguiente
	 * actualizaci�n.
	 * 
	 * @param index La posici�n actual del cabezal en la cinta.
	 * @return Una cadena que representa el estado actual de la cinta y la posici�n
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
	 * Construye una cadena que representa la Tape de entrada de la m�quina,
	 * agregando caracteres en blanco y delimitadores al principio y al final.
	 * 
	 * La Tape comienza con un delimitador '~', seguido de una serie de caracteres
	 * en blanco especificados por el par�metro {@code blank}, el contenido de la
	 * cadena de entrada, m�s caracteres en blanco para completar el resto de la
	 * Tape, y finaliza con otro delimitador '~'.
	 *
	 * @param str   La cadena de entrada que se agregar� a la Tape.
	 * @param blank El car�cter en blanco utilizado para rellenar la Tape.
	 * @return Una cadena que representa la Tape de entrada, preparada para ser
	 *         procesada por la m�quina.
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
	 * Establece la Tape de entrada de la m�quina utilizando una cadena de entrada,
	 * formateada con el s�mbolo en blanco y delimitadores al principio y al final.
	 */
	public void setTape(String inputstr) {
		this.Tape = new StringBuffer(buildTape(inputstr, espacioSym));

		printTape(INIT_INDEX);
	}

	/**
	 * Obtiene el nombre de la m�quina de Turing.
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