package tm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Maquina {

	private final int INDICE_INICIAL = 75; // inicio en la cinta
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

	/**
	 * Inicializa la configuración de la máquina de Turing a partir de un archivo de
	 * entrada
	 * 
	 * @param f un objeto {@code Scanner} que representa el archivo de entrada con
	 *          la configuración de la máquina.
	 */
	public void carga(Scanner f) {
		setIndice(INDICE_INICIAL);
		setAlfabeto(null);
		setEstados(new ArrayList<>());
		MaquinaBuilder builder = new MaquinaBuilder(f);
		builder.buildMachine(this);
		setTape(new Cinta(INDICE_INICIAL, espacio));
	}

	public void reset() {
		setEstadoActual(estadoInicial);
		setIndice(INDICE_INICIAL);
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