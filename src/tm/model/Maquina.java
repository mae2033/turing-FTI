package tm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import tm.app.AppController;

public class Maquina {

	AppController controller;

	private Timer timer;
	private boolean running = false; // Control del estado
	static int VELOCIDAD = 500;
	final int INIT_INDEX = 10;
	private int index = INIT_INDEX; // Campo de instancia para el índice
	private static final char LIMITE_MARCA = '~';
	Scanner fs;
	String nameMachine;
	Set<Character> alpha;
	int cantidadEstados;
	int estadoInicial;
	int estadoActual;
	int estadoFinal;
	char espacioSym;

	StringBuffer Tape = new StringBuffer(); // Cinta
	List<Estado> states = new ArrayList<>(); // estados

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

	public void runTuringWithTimer() {
		if (running)
			return;
		reset();
		running = true;

		if (timer == null) {
			timer = new Timer();
		}
		// Configuración del TimerTask
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					if (estadoActual == estadoFinal) {
						stop();
						return;
					}
					index = makeTrans(index);
					if (index == -1) {
						stop();
						throw new InterruptedException("ERROR: Cabeza salió de la cinta. Máquina detenida.");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					stop();

				}
				displayTape(index);
			}
		};
		timer.scheduleAtFixedRate(task, VELOCIDAD, VELOCIDAD + 1);
	}

	public void runTuring(int index) throws InterruptedException {
		while (estadoActual != estadoFinal) {
			index = makeTrans(index);
			if (index == -1)
				throw new InterruptedException("ERROR: Cabeza salio de la Tape. Maquina detenida.");
			displayTape(index);
		}
		update();
	}

	public int makeTrans(int index) throws InterruptedException {
		if (Tape.charAt(index) == LIMITE_MARCA)
			throw new InterruptedException("Cabeza salio de la Cinta. Maquina detenida.");
		Estado st = states.get(estadoActual);
		for (Transicion tr : st.transiciones) {
			if (tr.read == Tape.charAt(index)) {
				Tape.replace(index, index + 1, String.valueOf(tr.write));
				controller.guiCinta(tr.write, index);
				estadoActual = tr.nextState;
				switch (tr.shift) {
				case 'R':
					return index + 1;
				case 'L':
					return index - 1;
				case 'N':
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
		System.out.println(aTape);
		return aTape;
	}

	public String printTape(int index) {
		StringBuilder output = new StringBuilder();
		output.append(Tape).append("\n");
		output.append(String.valueOf(' ').repeat(index - 1));
		output.append(" ^q").append(estadoActual).append("\n");
		return output.toString();
	}

	public String buildTape(String str, char blank) {
		String blanks = String.valueOf(blank).repeat(INIT_INDEX - 1);
		String trailingBlanks = String.valueOf(blank).repeat(30);
		return String.format("%s%s%s%s%s", LIMITE_MARCA, blanks, str, trailingBlanks, LIMITE_MARCA);
	}

	public void setTape(String inputstr) {
		this.Tape = new StringBuffer(buildTape(inputstr, espacioSym));
		printTape(INIT_INDEX);
	}

	public String getName() {
		return nameMachine;
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		update();
		running = false;
	}

	public void reset() {
		running = false;
		estadoActual = estadoInicial; // O el valor que defina el estado inicial
		index = INIT_INDEX; // O la posición inicial de la cinta

	}

	private void update() {
		result = Tape.toString();
		result = result.replaceAll("^[\\" + LIMITE_MARCA + "]+|[\\" + LIMITE_MARCA + "]+~", "");
		result = result.replaceAll("^[" + espacioSym + "]+|[" + espacioSym + "]+~", "");
		controller.updateTextField(result);
		System.out.println("Resultado de la cinta: " + result);
	}

	public void setVelocidad(int nuevaVelocidad) {
		VELOCIDAD = nuevaVelocidad;
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}

	public int getIndice() {
		return indice;
	}

	public int getInitIndex() {
		return INIT_INDEX;
	}

	public String getTape() {
		return Tape.toString();
	}

	public String getResult() {
		return result;
	}

}