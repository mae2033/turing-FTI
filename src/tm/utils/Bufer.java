package tm.utils;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Representa el estado de la cinta de una m�quina de Turing en un momento dado.
 * Esta clase se utiliza para almacenar una copia del estado actual de la cinta,
 * incluyendo su contenido y la posici�n de la cabeza de lectura, para poder
 * realizar operaciones de "deshacer" y "volver atr�s".
 */
public class Bufer {
	private JLabel[] cinta; // Guarda el estado de la cinta
	private int indiceAnt; // �ndice previo de la cabeza
	private int espacioAdicional; // Espacios adicionales usados en la cinta
	private int tapeSize; // Tama�o de la cinta
	private String resultado;

	public Bufer(JLabel[] cinta, int indiceAnt, int espacioAdicional, int tapeSize, String result) {
		this.cinta = copiarCinta(cinta);
		this.indiceAnt = indiceAnt;
		this.espacioAdicional = espacioAdicional;
		this.tapeSize = tapeSize;
		this.resultado = result;
	}

	private JLabel[] copiarCinta(JLabel[] cintaOriginal) {
		JLabel[] copia = new JLabel[cintaOriginal.length];
		for (int i = 0; i < cintaOriginal.length; i++) {
			copia[i] = new JLabel(cintaOriginal[i].getText(), SwingConstants.CENTER);
			copia[i].setFont(cintaOriginal[i].getFont());
			copia[i].setForeground(cintaOriginal[i].getForeground());
			copia[i].setBorder(cintaOriginal[i].getBorder());
		}
		return copia;
	}

	public JLabel[] getCinta() {
		return copiarCinta(this.cinta);
	}

	public int getIndiceAnt() {
		return indiceAnt;
	}

	public int getEspacioAdicional() {
		return espacioAdicional;
	}

	public int getTapeSize() {
		return tapeSize;
	}

	public String getResultado() {
		return resultado;
	}
}
