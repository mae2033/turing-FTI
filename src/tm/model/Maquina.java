package tm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TimerTask;

import javax.swing.JOptionPane; // revisar y quitar

import tm.app.AppController;

public class Maquina {

	private AppController controller;
	private MaquinaTimer maquinaTimer;
	static int VELOCIDAD = 500; // delay
	private final int INDICE_INICIAL = 75; // inicio
	private int indice;
	// elementos de la maquina
	private String nombreMaquina;
	private Set<Character> alfabeto;
	private int cantidadEstados;
	private int estadoInicial;
	private int estadoActual;
	private int estadoFinal; // estado aceptador
	private char espacio; // simbolo espacio
	private List<Estado> estados;
	private Cinta tape;

	public void carga(Scanner f) {
		setIndice(INDICE_INICIAL);
		maquinaTimer = new MaquinaTimer();
		setAlfabeto(null);
		setEstados(new ArrayList<>());
		MaquinaBuilder builder = new MaquinaBuilder(f);
		builder.buildMachine(this);
		setTape(new Cinta(getIndice(), espacio));
	}

	public void runTuringWithTimer(String entrada) {
		if (maquinaTimer.estaActivo())
			return;
		reset();
		getTape().inicializar(entrada);
		maquinaTimer.iniciar(new TimerTask() {
			@Override
			public void run() {
				try {
					if (estadoActual == estadoFinal) {
						actualiza();
						detener();
						return;
					}
					setIndice(realizarTransicion(getIndice()));
					if (getIndice() == -1) {
						detener();
						throw new InterruptedException("ERROR: Cabeza salió de la cinta. Máquina detenida.");
					}
				} catch (InterruptedException e) {
					detener();
					JOptionPane.showMessageDialog(null, e); // arreglar esta parte
				}
			}
		}, VELOCIDAD, VELOCIDAD + 1);
	}

	public void runTuring(String inputstr) throws InterruptedException {
		getTape().inicializar(inputstr);
		int index = INDICE_INICIAL;
		setEstadoActual(estadoInicial);
		while (getEstadoActual() != getEstadoFinal()) {
			index = realizarTransicion(index);
			if (index == -1)
				throw new InterruptedException("ERROR: Cabeza salio de la cinta. Maquina detenida.");
		}
		actualiza();
	}

	public int realizarTransicion(int index) throws InterruptedException {
		char actual = getTape().leer(index);
		Estado st = estados.get(estadoActual);
		for (Transicion tr : st.getTransiciones()) {
			if (tr.getLee() == actual) {
				getTape().escribir(index, tr.getEscribe());
				escribirCinta(tr.getEscribe(), index);
				setEstadoActual(tr.getSiguiente());
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
		return -1;
	}

	private void escribirCinta(char escribe, int index) {
		if (escribe == getEspacio())
			escribe = '\u25B2';
		controller.escribirCinta(escribe, index - INDICE_INICIAL + 1);
	}

	public void detener() {
		maquinaTimer.detener();
	}

	public void reset() {
		setEstadoActual(estadoInicial);
		setIndice(INDICE_INICIAL);
	}

	private void actualiza() {
		String resultado = getTape().obtenerResultado();
		controller.setResultadoPantalla(resultado);
	}

	public void setVelocidad(int nuevaVelocidad) {
		VELOCIDAD = nuevaVelocidad;
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}

	public int getInitIndex() {
		return INDICE_INICIAL;
	}

	public int getCantidadEstados() {
		return cantidadEstados;
	}

	public void setCantidadEstados(int cantidadEstados) {
		this.cantidadEstados = cantidadEstados;
	}

	public Set<Character> getAlfabeto() {
		return alfabeto;
	}

	public void setAlfabeto(Set<Character> alfabeto) {
		this.alfabeto = alfabeto;
	}

	public String getNombreMaquina() {
		return nombreMaquina;
	}

	public void setNombreMaquina(String nombreMaquina) {
		this.nombreMaquina = nombreMaquina;
	}

	public int getEstadoInicial() {
		return estadoInicial;
	}

	public void setEstadoInicial(int estadoInicial) {
		this.estadoInicial = estadoInicial;
	}

	public int getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(int estadoActual) {
		this.estadoActual = estadoActual;
	}

	public int getEstadoFinal() {
		return estadoFinal;
	}

	public void setEstadoFinal(int estadoFinal) {
		this.estadoFinal = estadoFinal;
	}

	public char getEspacio() {
		return espacio;
	}

	public void setEspacio(char espacio) {
		this.espacio = espacio;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public Cinta getTape() {
		return tape;
	}

	public void setTape(Cinta tape) {
		this.tape = tape;
	}

}