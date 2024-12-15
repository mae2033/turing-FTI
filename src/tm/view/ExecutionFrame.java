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

@SuppressWarnings("serial")
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
	private boolean running = false;

	public ExecutionFrame() {
		super();
		// Frame config
		frameConfig();
		// inicializar componentes
		initSpeedPanel();
		initPanelButtons();
		initTextField();
		initTapePanel();
	}

	private void initSpeedPanel() {
		lblInterval = new JLabel(String.format("delay: " + velocidad + " ms"));
		lblInterval.setBounds(244, 5, 105, 25);
		lblInterval.setFont(new Font("Tahoma", Font.BOLD, 13));

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

	private void frameConfig() {
		setSize(600, 233);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);
	}

	private void initPanelButtons() {
		// ejecutar
		btnExecute = new JButton(new ImageIcon(ExecutionFrame.class.getResource("/images/Play22.png")));
		btnExecute.addActionListener(e -> run());
		btnExecute.setToolTipText("Ejecutar");
		// pausar
		btnStop = new JButton(new ImageIcon(ExecutionFrame.class.getResource("/images/Pause22.png")));
		btnStop.addActionListener(e -> stop());
		btnStop.setToolTipText("detener ejecucion");
		btnStop.setEnabled(running);
		// anterior
		btnBack = new JButton(new ImageIcon(ExecutionFrame.class.getResource("/images/StepBack22.png")));
		btnBack.addActionListener(e -> restoreBufferState());
		btnBack.setToolTipText("cinta anterior");
		// frena y limpia
		btnClean = new JButton(new ImageIcon(ExecutionFrame.class.getResource("/images/Trash22.png")));
		btnClean.addActionListener(e -> clean());
		btnClean.setToolTipText("limpiar y borrar");
		// retornar
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
		getContentPane().add(buttonsPanel);
	}

	private void initTextField() {
		texto = new JTextField();
		texto.setToolTipText("input");
		texto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		texto.setLocation(12, 100);
		texto.setSize(361, 27);
		getContentPane().add(texto);
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
			cinta[i] = new JLabel(String.valueOf(vacio), SwingConstants.CENTER);
			cinta[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			cinta[i].setFont(new Font("Arial", Font.PLAIN, 28));
			cintaPanel.add(cinta[i]);
		}
		cintaPanel.revalidate();
	}

	private void actualizarTimer() {
		controller.setVelocidad(velocidad);
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}

	private void run() {
		if (!running)
			saveBufferState();
		running = true;
		btnStop.setEnabled(running);
		controller.ejecutar(texto.getText());
	}

	private void stop() {
		running = false;
		btnStop.setEnabled(running);
		controller.interrumpir();
	}

	private void volver() {
		controller.volverInicio();
		reset();
	}

	public void setText(String resultado) {
		texto.setText(resultado);
	}

	public void title(String s) {
		setTitle(s);
	}

	public void afterRun(String resultado) {
		running = false;
		btnStop.setEnabled(running);
		setText(resultado);
	}

	private void clean() {
		stop();
		reset();
	}

	public void reset() {
		running = false;
		btnStop.setEnabled(running);
		texto.setText("");
		velocidad = 500;
		sliderVelocidad.setValue(velocidad);
		bufferStack = new Stack<>();
		resetTape();
	}

	// la idea es tener un pila para ir hacia atras
	public void saveBufferState() {
		bufferStack.push(new Bufer(cinta, indice_ant, espacioAdicional, TAPE_SIZE, texto.getText()));
	}

	public void updateTape(char c, int indice) {
		indice += espacioAdicional;
		redimensionarCinta(indice);
		if (indice_ant != -1) {
			cinta[indice_ant].setText(cinta[indice_ant].getText().replace("[", "").replace("]", ""));
			cinta[indice_ant].setForeground(Color.BLACK);
		}
		cinta[indice].setText(String.valueOf(c));
		cinta[indice].setForeground(Color.RED);
		indice_ant = indice;
	}

	public void redimensionarCinta(int indice) {
		if (indice <= 0) {
			int espacioAdicional = Math.abs(indice) + 2; // Espacio necesario hacia la izquierda
			int nuevoTamano = TAPE_SIZE + espacioAdicional;
			JLabel[] nuevaCinta = new JLabel[nuevoTamano];
			System.arraycopy(cinta, 0, nuevaCinta, espacioAdicional, TAPE_SIZE);// Desplazar los elementos existentes
																				// hacia la derecha
			for (int i = 0; i < espacioAdicional; i++) { // Inicializar las nuevas posiciones vacías al inicio
				nuevaCinta[i] = new JLabel("\u25B2", SwingConstants.CENTER);
				nuevaCinta[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				nuevaCinta[i].setFont(new Font("Arial", Font.PLAIN, 28));
				cintaPanel.add(nuevaCinta[i], i);
			}
			cinta = nuevaCinta;
			TAPE_SIZE = nuevoTamano;
			cintaPanel.revalidate();
			cintaPanel.repaint();
			// Ajustar la posición inicial lógica para que coincida con la expansión
			this.espacioAdicional += espacioAdicional;
		}

		if (indice >= TAPE_SIZE) {
			int nuevoTamano = indice + 2; // Añade espacio adicional
			JLabel[] nuevaCinta = new JLabel[nuevoTamano];
			System.arraycopy(cinta, 0, nuevaCinta, 0, TAPE_SIZE);

			for (int i = TAPE_SIZE; i < nuevoTamano; i++) {
				nuevaCinta[i] = new JLabel("\u25B2", SwingConstants.CENTER);
				nuevaCinta[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				nuevaCinta[i].setFont(new Font("Arial", Font.PLAIN, 28));
				cintaPanel.add(nuevaCinta[i]);
			}

			cinta = nuevaCinta;
			TAPE_SIZE = nuevoTamano;

			cintaPanel.revalidate();
			cintaPanel.repaint();
		}
	}

	// se creo porque re-inicializar no funciona
	private void resetTape() {
		TAPE_SIZE = 5; // Tamaño inicial definido
		espacioAdicional = 0; // indicadores para el manejo de la cinta
		indice_ant = -1;
		cinta = new JLabel[TAPE_SIZE];
		cintaPanel.removeAll();
		for (int i = 0; i < TAPE_SIZE; i++) {
			cinta[i] = new JLabel(String.valueOf(vacio), SwingConstants.CENTER);
			cinta[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			cinta[i].setFont(new Font("Arial", Font.PLAIN, 28));
			cintaPanel.add(cinta[i]);
		}
		cintaPanel.revalidate();
		cintaPanel.repaint();
	}

	// Restaurar el último estado desde la pila
	private void restoreBufferState() {
		if (!bufferStack.isEmpty() && !running) {
			Bufer lastBuffer = bufferStack.pop();
			cinta = lastBuffer.getCinta();
			indice_ant = lastBuffer.getIndiceAnt();
			espacioAdicional = lastBuffer.getEspacioAdicional();
			TAPE_SIZE = lastBuffer.getTapeSize();
			texto.setText(lastBuffer.getResultado());

			cintaPanel.removeAll();
			for (JLabel cell : cinta) {
				cintaPanel.add(cell);
			}
			cintaPanel.revalidate();
			cintaPanel.repaint();
		}
	}

	// funciona, todavia no se aplica
	public void ajustarCinta() { // la idea es usarlo cuando termina de ejecutar
		if (espacioAdicional > 0) {
			JLabel[] nuevaCinta = new JLabel[TAPE_SIZE - espacioAdicional];
			System.arraycopy(cinta, espacioAdicional, nuevaCinta, 0, nuevaCinta.length);
			cinta = nuevaCinta;
			TAPE_SIZE = nuevaCinta.length;
			espacioAdicional = 0;

			// Actualizar el panel de la cinta
			cintaPanel.removeAll();
			for (int i = 0; i < TAPE_SIZE; i++) {
				cintaPanel.add(cinta[i]);
			}
			cintaPanel.revalidate();
			cintaPanel.repaint();
		}
	}

	public void updateCompleteTape(String input) {
		for (int i = 0; i < TAPE_SIZE; i++) {
			if (i < espacioAdicional && i > espacioAdicional + input.length())
				cinta[i].setText(input.charAt(i - espacioAdicional) + "");
			else
				cinta[i].setText(String.valueOf(vacio));
		}
	}

}