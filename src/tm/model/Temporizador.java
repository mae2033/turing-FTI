package tm.model;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase responsable de gestionar el temporizador para la ejecuciï¿½n de la
 * mï¿½quina de Turing. Controla el estado de ejecuciï¿½n de la mï¿½quina,
 * permitiendo iniciar y detener la ejecuciï¿½n a intervalos especï¿½ficos.
 */
public class Temporizador {

	private Timer timer;
	private boolean running = false;

	/**
	 * Inicia el temporizador con una tarea que se ejecuta a intervalos regulares.
	 * 
	 * @param tarea  La tarea que se ejecutará repetidamente.
	 * @param delay  El tiempo de espera inicial en milisegundos antes de que se
	 *               ejecute la tarea.
	 * @param period El período en milisegundos entre ejecuciones consecutivas de la
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
	 * Detiene el temporizador y cancela cualquier tarea en ejecución.
	 */
	public void detener() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		running = false;
	}

	/**
	 * Verifica si el temporizador está actualmente activo.
	 * 
	 * @return `true` si el temporizador está activo, `false` si no lo está.
	 */
	public boolean estaActivo() {
		return running;
	}
}
