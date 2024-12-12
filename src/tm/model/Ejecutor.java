package tm.model;

import java.util.Scanner;
import java.util.TimerTask;

import javax.swing.JOptionPane;

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

	public Ejecutor() {
		timer = new Temporizador();
		maquina = new Maquina();
	}

	public void carga(Scanner f) {
		maquina.carga(f);
	}

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
					JOptionPane.showMessageDialog(null, e); // arreglar esta parte
				}
			}
		}, VELOCIDAD, VELOCIDAD + 1);
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

	private void actualiza() {
		String resultado = maquina.getTape().obtenerResultado();
		controller.setResultadoPantalla(resultado);
	}

	private void escribirCinta(char escribe, int index) {
		if (escribe == maquina.getEspacio())
			escribe = '\u25B2';
		controller.escribirCinta(escribe, index - maquina.getInitIndex() + 1);
	}

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
