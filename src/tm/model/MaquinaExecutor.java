package tm.model;

import java.util.TimerTask;

import tm.app.AppController;

// todavia no se utiliza
public class MaquinaExecutor {

	private Maquina maquina;
	private MaquinaTimer maquinaTimer;
	private AppController controller;

	public MaquinaExecutor() {
		this.maquina = new Maquina();
		this.maquinaTimer = new MaquinaTimer();
	}

	public void runWithTimer(String entrada, int velocidad) {
		if (maquinaTimer.estaActivo())
			return;

		maquina.setIndice(maquina.getEstadoInicial());
		maquina.getTape().inicializar(entrada);

		maquinaTimer.iniciar(new TimerTask() {
			@Override
			public void run() {
				try {
					if (maquina.getEstadoActual() == maquina.getEstadoFinal()) {
						detener();
						return;
					}
					int nuevoIndice = realizarTransicion(maquina.getIndice());
					if (nuevoIndice == -1) {
						detener();
						throw new RuntimeException("Error: Cabeza salió de la cinta.");
					}
					maquina.setIndice(nuevoIndice);
				} catch (RuntimeException e) {
					detener();
					controller.showError(e.getMessage());
				}
			}
		}, velocidad, velocidad + 1);
	}

	private int realizarTransicion(int index) {
		char actual = maquina.getTape().leer(index);
		Estado st = maquina.getEstados().get(maquina.getEstadoActual());
		for (Transicion tr : st.getTransiciones()) {
			if (tr.getLee() == actual) {
				maquina.getTape().escribir(index, tr.getEscribe());
				escribirCinta(tr.getEscribe(), index);
				maquina.setEstadoActual(tr.getSiguiente());
				switch (tr.getCambio()) {
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
		return -1; // Placeholder
	}

	public void detener() {
		maquinaTimer.detener();
	}

	public void escribirCinta(char escribe, int index) {
		if (escribe == maquina.getEspacio())
			escribe = '\u25B2';
		controller.escribirCinta(escribe, index - maquina.getInitIndex() + 1);
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}
}
