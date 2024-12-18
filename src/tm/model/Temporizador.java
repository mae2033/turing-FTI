package tm.model;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase responsable de gestionar el temporizador para la ejecuci�n de la
 * m�quina de Turing. Controla el estado de ejecuci�n de la m�quina,
 * permitiendo iniciar y detener la ejecuci�n a intervalos espec�ficos.
 */
public class Temporizador {

	private Timer timer;
	private boolean running = false;

	/**
	 * Inicia el temporizador con una tarea que se ejecuta a intervalos regulares.
	 * 
	 * @param tarea  La tarea que se ejecutar� repetidamente.
	 * @param delay  El tiempo de espera inicial en milisegundos antes de que se
	 *               ejecute la tarea.
	 * @param period El per�odo en milisegundos entre ejecuciones consecutivas de la
	 *               tarea.
	 */
	public void iniciar(TimerTask tarea, int delay, int period) {
		if (running)
			return;
		running = true;
		timer = new Timer();
		timer.scheduleAtFixedRate(tarea, delay, period);
	}

	/**
	 * Detiene el temporizador y cancela cualquier tarea en ejecuci�n.
	 */
	public void detener() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		running = false;
	}

	/**
	 * Verifica si el temporizador est� actualmente activo.
	 * 
	 * @return `true` si el temporizador est� activo, `false` si no lo est�.
	 */
	public boolean estaActivo() {
		return running;
	}
}
