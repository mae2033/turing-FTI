package tm.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import tm.app.AppController;
import tm.utils.Bufer;

public class ExecutionFrame extends JFrame {

	private AppController controller;
	private JPanel buttonsPanel;
	private JPanel speedPanel;
	private JPanel cintaPanel;
	private JButton btnExecute;
	private JButton btnReturn;
	private JButton btnStop;
	private JButton btnBack; // ultima
	private JButton btnClean; // frena y limpia
	private JSlider sliderVelocidad;
	private JTextField texto;
	private JLabel lblInterval;
	private JLabel[] cinta;
	private Stack<Bufer> bufferStack = new Stack<>();
	private int indice_ant = -1;
	private int espacioAdicional = 0;
	private int TAPE_SIZE;
	private int velocidad = 500; // Velocidad inicial
	private String vacio = "\u25B2";

	private int index; // indice logico

	public ExecutionFrame() {
		super();
		frameConfig();
		// inicializar los componentes
		initSpeedPanel();
		initPanelButtons();
		initTextField();
		initTapePanel();
	}

	/**
	 * Configura las propiedades básicas de la ventana
	 */
	private void frameConfig() {
		setSize(600, 233);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);
	}

	private void initSpeedPanel() {
		lblInterval = new JLabel(String.format("delay: " + velocidad + " ms"));
		lblInterval.setBounds(244, 5, 105, 25);
		lblInterval.setFont(new Font("Tahoma", Font.BOLD, 13));
		// regulador de velocidad
		sliderVelocidad = new JSlider(0, 1000, velocidad);
		sliderVelocidad.setBounds(0, 5, 238, 32);
		sliderVelocidad.setMajorTickSpacing(250);
		sliderVelocidad.setSnapToTicks(true);
		sliderVelocidad.setPaintLabels(true);
		sliderVelocidad.addChangeListener(e -> {
			velocidad = sliderVelocidad.getValue();
			lblInterval.setText("delay: " + velocidad + " ms");
			actualizarTimer();
		});
		sliderVelocidad.setAutoscrolls(true);
		speedPanel = new JPanel();
		speedPanel.setBounds(12, 139, 361, 43);
		getContentPane().add(speedPanel);
		speedPanel.setLayout(null);
		speedPanel.add(sliderVelocidad);
		speedPanel.add(lblInterval);
	}

	private void initPanelButtons() {
		// boton de ejecutar
		btnExecute = new JButton(new ImageIcon(ExecutionFrame.class.getResource("/images/Play22.png")));
		btnExecute.addActionListener(e -> ejecutar());
		btnExecute.setToolTipText("Ejecutar");
		// boton de pausa
		btnStop = new JButton(new ImageIcon(ExecutionFrame.class.getResource("/images/Pause22.png")));
		btnStop.addActionListener(e -> detener());
		btnStop.setToolTipText("detener ejecucion");
		btnStop.setEnabled(false);
		// boton mostrar la cinta anterior,
		btnBack = new JButton(new ImageIcon(ExecutionFrame.class.getResource("/images/Reset22.png")));
		btnBack.addActionListener(e -> estadoAnterior());
		btnBack.setToolTipText("cinta anterior");
		// boton frenar y limpiar, cinta, entrpila
		btnClean = new JButton(new ImageIcon(ExecutionFrame.class.getResource("/images/Trash22.png")));
		btnClean.addActionListener(e -> limpiar());
		btnClean.setToolTipText("limpiar y borrar");
		// boton retorno al inicio
		btnReturn = new JButton(new ImageIcon(ExecutionFrame.class.getResource("/images/Undo22.png")));
		btnReturn.addActionListener(e -> volver());

		buttonsPanel = new JPanel();
		buttonsPanel.setBounds(383, 100, 189, 82);
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.add(btnExecute);
		buttonsPanel.add(btnStop);
		buttonsPanel.add(btnBack);
		buttonsPanel.add(btnClean);
		buttonsPanel.add(btnReturn);
		add(buttonsPanel);
	}

	private void initTextField() {
		texto = new JTextField();
		texto.setToolTipText("input");
		texto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		texto.setLocation(12, 100);
		texto.setSize(361, 27);
		add(texto);
	}

	private void initTapePanel() {
		TAPE_SIZE = 5; // valor inicial
		cintaPanel = new JPanel();
		cintaPanel.setLayout(new GridLayout(1, TAPE_SIZE));

		JScrollPane scrollPane = new JScrollPane(cintaPanel);
		scrollPane.setToolTipText(vacio + " blank symbol");
		scrollPane.setBounds(11, 11, 562, 83);
		getContentPane().add(scrollPane);

		cinta = new JLabel[TAPE_SIZE];

		for (int i = 0; i < TAPE_SIZE; i++) {
			cinta[i] = crearCeldaCinta(vacio);
			cintaPanel.add(cinta[i]);
		}
		cintaPanel.revalidate();
	}

	/**
	 * Crea una celda para la cinta con el valor especificado, configurando su borde
	 * y estilo de fuente.
	 * 
	 * @param valor El valor que se mostrará en la celda de la cinta.
	 * @return Un objeto `JLabel` configurado para representar una celda en la
	 *         cinta.
	 */
	private JLabel crearCeldaCinta(String valor) {
		JLabel celda = new JLabel(valor, SwingConstants.CENTER);
		celda.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		celda.setFont(new Font("Arial", Font.PLAIN, 28));
		return celda;
	}

	/**
	 * Actualiza la velocidad de ejecución de la máquina según el valor actual.
	 */
	private void actualizarTimer() {
		controller.setVelocidad(velocidad);
	}

	/**
	 * Establece el controlador de la aplicación.
	 * 
	 * @param controller El controlador que se asignará para gestionar la lógica de
	 *                   la máquina.
	 */
	public void setController(AppController controller) {
		this.controller = controller;
	}

	/**
	 * Inicia la ejecución de la máquina, guardando el estado actual de la cinta y
	 * actualizando los botones para reflejar que la ejecución ha comenzado.
	 */
	private void ejecutar() {
		saveBufferState(); // apila la cinta pre-ejecucion
		actualizarBotones(true); // cambia a en ejecucion
		controller.ejecutar(texto.getText());
	}

	/**
	 * Detiene la ejecución de la máquina y actualiza los botones para reflejar que
	 * la ejecución ha sido interrumpida.
	 */
	private void detener() {
		actualizarBotones(false);
		controller.interrumpir();
	}

	/**
	 * Restaura el estado de la máquina al inicio y resetea la interfaz a su estado
	 * inicial.
	 */
	private void volver() {
		controller.volverInicio();
		reset();
	}

	public void setText(String resultado) {
		texto.setText(resultado);
	}

	/**
	 * Realiza acciones después de la ejecución, como actualizar los botones de
	 * control, mostrar el resultado y ajustar la cinta.
	 * 
	 * @param resultado Resultado de la ejecución que se mostrará en el campo de
	 *                  entrada
	 */
	public void postEjecucion(String resultado) {
		actualizarBotones(false);
		setText(resultado);
//		ajustarCinta(); // usar si quiere una visual mas limpia
	}

	/**
	 * Limpia el estado actual, deteniendo la ejecución y reseteando los parámetros.
	 */
	private void limpiar() {
		detener();
		reset();
	}

	/**
	 * Resetea la interfaz y los parámetros a su estado inicial, incluyendo la
	 * velocidad, la pila de buffers y la cinta.
	 */
	public void reset() {
		actualizarBotones(false);// cambio no esta ejecutando
		texto.setText("");
		velocidad = 500;
		sliderVelocidad.setValue(velocidad);
		bufferStack = new Stack<>();
		reinicioCinta();
	}

	/**
	 * Guarda el estado actual de la cinta y otros parámetros en la pila de buffers,
	 * permitiendo restaurar el estado más tarde.
	 */
	public void saveBufferState() {
		bufferStack.push(new Bufer(cinta, indice_ant, espacioAdicional, TAPE_SIZE, texto.getText()));
	}

	/**
	 * Escribe un carácter en la cinta en el índice especificado, redimensionando la
	 * cinta si es necesario y actualizando el color del texto.
	 * 
	 * @param c      Caracter que se desea escribir en la cinta.
	 * @param indice Indice en la cinta donde se debe escribir el carácter.
	 */
	public void escribirCinta(char c, int indice) {
		indice += espacioAdicional;
		index = indice;
		redimensionarCinta(indice);
		if (indice_ant != -1)
			cinta[indice_ant].setForeground(Color.BLACK);
		cinta[index].setText(String.valueOf(c));
		cinta[index].setForeground(Color.RED);
		indice_ant = index;
	}

	/**
	 * Redimensiona la cinta si el índice está fuera de los límites actuales,
	 * agregando espacio adicional en la izquierda o derecha según sea necesario.
	 * 
	 * @param indice Indice que se debe verificar para determinar si la cinta
	 *               necesita ser redimensionada.
	 */
	public void redimensionarCinta(int indice) {
		if (indice <= 0) {
			int espacioAdicional = Math.abs(indice) + 2; // Espacio necesario hacia la izquierda
			int nuevoTamano = TAPE_SIZE + espacioAdicional;
			JLabel[] nuevaCinta = new JLabel[nuevoTamano];
			System.arraycopy(cinta, 0, nuevaCinta, espacioAdicional, TAPE_SIZE);// agrega espacio por la izquierda
			for (int i = 0; i < espacioAdicional; i++) { // Inicializar las nuevas posiciones vacías al inicio
				nuevaCinta[i] = crearCeldaCinta(vacio);
				cintaPanel.add(nuevaCinta[i], i);
			}
			cinta = nuevaCinta;
			TAPE_SIZE = nuevoTamano;
			this.espacioAdicional += espacioAdicional;
			index = indice + espacioAdicional;
			refrescarCintaPanel();
			// Ajustar la posición inicial lógica para que coincida con la expansión
		}
		if (indice >= TAPE_SIZE - 1) {
			int nuevoTamano = indice + 2; // Añade espacio adicional
			JLabel[] nuevaCinta = new JLabel[nuevoTamano];
			System.arraycopy(cinta, 0, nuevaCinta, 0, TAPE_SIZE); // agrega espacio por la derecha

			for (int i = TAPE_SIZE; i < nuevoTamano; i++) {
				nuevaCinta[i] = crearCeldaCinta(vacio);
				cintaPanel.add(nuevaCinta[i]);
			}
			cinta = nuevaCinta;
			TAPE_SIZE = nuevoTamano;
			refrescarCintaPanel();
		}
	}

	// se creo porque re-inicializar no funciona
	/**
	 * Reinicia la cinta a su estado inicial, estableciendo un tamaño predeterminado
	 * y limpiando el panel de la cinta.
	 */
	private void reinicioCinta() {
		TAPE_SIZE = 5; // Tamaño inicial definido
		espacioAdicional = 0; // indicadores para el manejo de la cinta
		indice_ant = -1;
		cinta = new JLabel[TAPE_SIZE];
		cintaPanel.removeAll();
		for (int i = 0; i < TAPE_SIZE; i++) {
			cinta[i] = crearCeldaCinta(vacio);
			cintaPanel.add(cinta[i]);
		}
		refrescarCintaPanel();
	}

	/**
	 * Restaura el último estado de la cinta y otros parámetros desde la pila de
	 * buffers. Este método se utiliza para recuperar el estado previo de la
	 * ejecución.
	 */
	private void estadoAnterior() {
		if (!bufferStack.isEmpty()) {
			Bufer lastBuffer = bufferStack.pop();
			this.cinta = lastBuffer.getCinta();
			this.indice_ant = lastBuffer.getIndiceAnt();
			this.espacioAdicional = lastBuffer.getEspacioAdicional();
			TAPE_SIZE = lastBuffer.getTapeSize();
			texto.setText(lastBuffer.getResultado());
			cintaPanel.removeAll();
			for (JLabel cell : cinta) {
				cintaPanel.add(cell);
			}
			refrescarCintaPanel();
		}
	}

	/**
	 * Ajusta la cinta eliminando el espacio adicional y actualiza el panel de la
	 * cinta. Se usa cuando la ejecución ha terminado y hay espacio adicional en la
	 * cinta.
	 */
	public void ajustarCinta() {
		if (espacioAdicional > 0) {
			JLabel[] nuevaCinta = new JLabel[TAPE_SIZE - espacioAdicional];
			System.arraycopy(cinta, espacioAdicional, nuevaCinta, 0, nuevaCinta.length);
			cinta = nuevaCinta;
			TAPE_SIZE = nuevaCinta.length;
			espacioAdicional = 0;
			cintaPanel.removeAll();
			for (int i = 0; i < TAPE_SIZE; i++) {
				cintaPanel.add(cinta[i]);
			}
			refrescarCintaPanel();
		}
	}

	/**
	 * Actualiza la habilitación de los botones de control según el estado de
	 * ejecución.
	 * 
	 * @param valor `true` si la máquina está en ejecución, `false` si no lo está.
	 */
	public void actualizarBotones(boolean valor) {
		btnExecute.setEnabled(!valor); // Habilita cuando no esta en ejecucion
		btnStop.setEnabled(valor); // Habilita cuando esta en ejecucion
		btnBack.setEnabled(!valor); // Habilita cuando no esta en ejecucion
		sliderVelocidad.setEnabled(!valor);
	}

	/**
	 * Refresca el panel de la cinta para reflejar los cambios visuales.
	 */
	private void refrescarCintaPanel() {
		cintaPanel.revalidate();
		cintaPanel.repaint();
	}

}