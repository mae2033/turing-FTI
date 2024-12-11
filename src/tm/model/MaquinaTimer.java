package tm.model;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Clase responsable de gestionar el temporizador para la ejecución de la
 * máquina de Turing. Controla el estado de ejecución de la máquina, permitiendo
 * iniciar y detener la ejecución a intervalos específicos.
 */
public class MaquinaTimer {

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
