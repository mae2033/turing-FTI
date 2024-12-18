package tm.model;

import java.util.Scanner;
import java.util.TimerTask;

import tm.app.AppController;

/**
 * Clase responsable de manejar la ejecución de la Máquina de Turing, incluyendo
 * el control del flujo y la interacción con el temporizador.
 */
public class Ejecutor {

	private int VELOCIDAD = 500; // delay
	private Maquina maquina;
	private Temporizador timer;
	private AppController controller;

	/**
	 * Constructor que inicializa el temporizador y la máquina.
	 */
	public Ejecutor() {
		timer = new Temporizador();
		maquina = new Maquina();
	}

	/**
	 * Carga la configuración de la máquina desde el archivo proporcionado por el
	 * objeto `Scanner`.
	 * 
	 * @param f Objeto `Scanner` que contiene los datos del archivo de
	 *          configuración.
	 */
	public void carga(Scanner f) {
		maquina.carga(f);
	}

	/**
	 * Ejecuta la máquina de Turing con un temporizador. La ejecución se detiene
	 * cuando se alcanza el estado final o si ocurre un error.
	 * 
	 * @param entrada La cadena de entrada que se procesará en la máquina de Turing.
	 */
	public void runWithTimer(String entrada) {
		if (estaActivo())
			return;
		reset();
		maquina.getTape().inicializar(entrada);
		timer.iniciar(new TimerTask() {
			@Override
			public void run() {
				try {
					if (maquina.getEstadoActual() == maquina.getEstadoFinal()) {
						actualiza();
						detener();
						return;
					}
					int nuevoIndice = realizarTransicion(maquina.getIndice());
					if (nuevoIndice == -1) {
						detener();
						throw new InterruptedException("Error: Cabeza salió de la cinta.");
					}
					maquina.setIndice(nuevoIndice);
				} catch (InterruptedException e) {
					detener();
					controller.showError("Error: Cabeza salió de la cinta.");
				}
			}
		}, VELOCIDAD, VELOCIDAD + 1);
	}

	/**
	 * Realiza una transición en la máquina de Turing basándose en el estado actual
	 * y el carácter leído en la cinta. Actualiza la cinta, el estado de la máquina
	 * y la posición de la cabeza según las reglas de transición.
	 * 
	 * @param index El índice actual de la cabeza en la cinta.
	 * @return El nuevo índice de la cabeza después de la transición, o -1 si hubo
	 *         un error.
	 */
	private int realizarTransicion(int index) {
		char actual = maquina.getTape().leer(index);
		Estado st = maquina.getEstados().get(maquina.getEstadoActual());
		for (Transicion tr : st.getTransiciones()) {
			if (tr.getLee() == actual) {
				maquina.getTape().escribir(index, tr.getEscribe());
				escribirCinta(tr.getEscribe(), index);
				maquina.setEstadoActual(tr.getSiguiente());
				switch (tr.getCambio()) {
				case 'R':// desplaza a la derecha
					return index + 1;
				case 'L':// desplaza a la izquierda
					return index - 1;
				case 'N':// no hay desplazamiento
					return index;
				default:
					return -1;
				}
			}
		}
		return -1;
	}

	/**
	 * Actualiza el resultado procesado en la máquina de Turing y lo muestra en la
	 * interfaz de usuario.
	 */
	private void actualiza() {
		String resultado = maquina.getTape().obtenerResultado();
		controller.setResultadoPantalla(resultado);
	}

	/**
	 * Escribe un carácter en la cinta en la posición indicada y actualiza la vista.
	 * 
	 * @param escribe El carácter a escribir.
	 * @param index   La posición en la cinta donde se escribirá el carácter.
	 */
	private void escribirCinta(char escribe, int index) {
		if (escribe == maquina.getEspacio())
			escribe = '\u25B2';
		controller.escribirCinta(escribe, index - maquina.getInitIndex() + 1);
	}

	/**
	 * Detiene la ejecución del temporizador que controla el ciclo de la máquina de
	 * Turing.
	 */
	public void detener() {
		timer.detener();
	}

	public boolean estaActivo() {
		return timer.estaActivo();
	}

	public void reset() {
		maquina.reset();
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}

	public void setVelocidad(int velocidad) {
		VELOCIDAD = velocidad;
	}

	public String getNombre() {
		return maquina.getNombreMaquina();
	}

}
