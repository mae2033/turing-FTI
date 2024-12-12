package tm.model;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase responsable de gestionar el temporizador para la ejecuci�n de la
 * m�quina de Turing. Controla el estado de ejecuci�n de la m�quina, permitiendo
 * iniciar y detener la ejecuci�n a intervalos espec�ficos.
 */
public class Temporizador {

	private Timer timer;
	private boolean running = false;

	public void iniciar(TimerTask tarea, int delay, int period) {
		if (running)
			return;
		running = true;
		timer = new Timer();
		timer.scheduleAtFixedRate(tarea, delay, period);
	}

	public void detener() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		running = false;
	}

	public boolean estaActivo() {
		return running;
	}
}
