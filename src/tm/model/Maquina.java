package tm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import tm.app.AppController;

public class Maquina {

	private final static Logger logger = Logger.getLogger(AppController.class);

	private AppController controller;
	private Timer timer; 
	private boolean running = false; // control del estado de la maquina
	static int VELOCIDAD = 500; // delay de las tareas
	final int INIT_INDEX = 50; // indice inicial
	private int index = INIT_INDEX; // Campo de instancia para el índice
	private static final char LIMITE_MARCA = '~';
	private Scanner fs;
	// elementos de la maquina
	String maquinaNombre;
	Set<Character> alpha;
	int cantidadEstados;
	int estadoInicial;
	int estadoActual;
	int estadoFinal;
	char espacioSym;
	StringBuffer Tape; // Cinta
	List<Estado> states; // estados

	private String resultado;

	public void carga(Scanner f) {
		alpha = null;
		Tape = new StringBuffer();
		states = new ArrayList<>();
		MaquinaBuilder builder = new MaquinaBuilder(f);
		builder.buildMachine(this);
	}

	public void runTuringWithTimer(String inputstr) {
		if (running)
			return;
		reset();
		setTape(inputstr);
		running = true;
		logger.info("ejecuto maquina");
		if (timer == null) {
			timer = new Timer();
		}
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
//				displayTape(index);
				printTape(index);
			}
		};
		timer.scheduleAtFixedRate(task, VELOCIDAD, VELOCIDAD + 1);
	}

	/** ejecutar la maquina simulada */
	public void runTuring(String inputstr) throws InterruptedException {
		setTape(inputstr);
		int index = INIT_INDEX;
		estadoActual = estadoInicial;
		while (estadoActual != estadoFinal) {
			index = makeTrans(index);
			if (index == -1)
				throw new InterruptedException("ERROR: Cabeza salio de la Tape. Maquina detenida.");
			printTape(index);
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
				escribirCinta(tr.write, index);
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

	private void escribirCinta(char write, int index) {
		if (write == espacioSym)
			write = '\u25B2';
		controller.escribirCinta(write, index - INIT_INDEX + 1);
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
		output.append(String.valueOf(' ').repeat(index));
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

	public boolean validarInput(String inputstr) {
		char[] array = inputstr.toCharArray();
		for (char c : array) {
			if (!alpha.contains(c))
				return false;
		}
		return true;
	}

	public String getName() {
		return maquinaNombre;
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
		resultado = Tape.toString();
		resultado = resultado.replaceAll("^[\\" + LIMITE_MARCA + "]+|[\\" + LIMITE_MARCA + "]+~", "");
		resultado = resultado.replaceAll("^[" + espacioSym + "]+|[" + espacioSym + "]+~", "");
		controller.updateTextField(resultado);
		// System.out.println("resultadoado de la cinta: " + resultado);
	}

	public void setVelocidad(int nuevaVelocidad) {
		VELOCIDAD = nuevaVelocidad;
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}

	public int getInitIndex() {
		return INIT_INDEX;
	}

	public String getTape() {
		return Tape.toString();
	}

	public String getResultado() {
		return resultado;
	}

	public boolean isRunning() {
		return running;
	}

}