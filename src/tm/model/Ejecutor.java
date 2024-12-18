package tm.model;

import java.util.Scanner;
import java.util.TimerTask;

import tm.app.AppController;

/**
 * Clase responsable de manejar la ejecuci�n de la M�quina de Turing, incluyendo
 * el control del flujo y la interacci�n con el temporizador.
 */
public class Ejecutor {

	private int VELOCIDAD = 500; // delay
	private Maquina maquina;
	private Temporizador timer;
	private AppController controller;

	/**
	 * Constructor que inicializa el temporizador y la m�quina.
	 */
	public Ejecutor() {
		timer = new Temporizador();
		maquina = new Maquina();
	}

	/**
	 * Carga la configuraci�n de la m�quina desde el archivo proporcionado por el
	 * objeto `Scanner`.
	 * 
	 * @param f Objeto `Scanner` que contiene los datos del archivo de
	 *          configuraci�n.
	 */
	public void carga(Scanner f) {
		maquina.carga(f);
	}

	/**
	 * Ejecuta la m�quina de Turing con un temporizador. La ejecuci�n se detiene
	 * cuando se alcanza el estado final o si ocurre un error.
	 * 
	 * @param entrada La cadena de entrada que se procesar� en la m�quina de Turing.
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
						throw new InterruptedException("Error: Cabeza sali� de la cinta.");
					}
					maquina.setIndice(nuevoIndice);
				} catch (InterruptedException e) {
					detener();
					controller.showError("Error: Cabeza sali� de la cinta.");
				}
			}
		}, VELOCIDAD, VELOCIDAD + 1);
	}

	/**
	 * Realiza una transici�n en la m�quina de Turing bas�ndose en el estado actual
	 * y el car�cter le�do en la cinta. Actualiza la cinta, el estado de la m�quina
	 * y la posici�n de la cabeza seg�n las reglas de transici�n.
	 * 
	 * @param index El �ndice actual de la cabeza en la cinta.
	 * @return El nuevo �ndice de la cabeza despu�s de la transici�n, o -1 si hubo
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
	 * Actualiza el resultado procesado en la m�quina de Turing y lo muestra en la
	 * interfaz de usuario.
	 */
	private void actualiza() {
		String resultado = maquina.getTape().obtenerResultado();
		controller.setResultadoPantalla(resultado);
	}

	/**
	 * Escribe un car�cter en la cinta en la posici�n indicada y actualiza la vista.
	 * 
	 * @param escribe El car�cter a escribir.
	 * @param index   La posici�n en la cinta donde se escribir� el car�cter.
	 */
	private void escribirCinta(char escribe, int index) {
		if (escribe == maquina.getEspacio())
			escribe = '\u25B2';
		controller.escribirCinta(escribe, index - maquina.getInitIndex() + 1);
	}

	/**
	 * Detiene la ejecuci�n del temporizador que controla el ciclo de la m�quina de
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
