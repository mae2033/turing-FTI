package tm.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import tm.app.AppController;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class ExecutionFrame extends JFrame {

	private AppController controller;

	private JPanel buttonsPanel;
	private JPanel speedPanel;
	private JButton btnExecute;
	private JButton btnBack;
	private JSlider sliderVelocidad;
	private JTextField texto;
	private JPanel cintaPanel;
	private JLabel[] cinta;
	private JLabel lblInterval;
	private int indice_ant = -1;
	private int espacioAdicional = 0;
	private int TAPE_SIZE;

	private int velocidad = 500; // Velocidad inicial

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
		lblInterval.setFont(new Font("Tahoma", Font.BOLD, 13));
//		lblInterval.setBounds(244, 98, 125, 27);

		sliderVelocidad = new JSlider(0, 1000, 500);
//		sliderVelocidad.setBounds(10, 98, 224, 32);
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
		speedPanel.setBounds(10, 124, 318, 42);
		getContentPane().add(speedPanel);
		speedPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		speedPanel.add(sliderVelocidad);
		speedPanel.add(lblInterval);

	}

	private void frameConfig() {
		setSize(530, 217);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);
	}

	private void initPanelButtons() {
		buttonsPanel = new JPanel();
		buttonsPanel.setBounds(390, 140, 114, 27);
		getContentPane().add(buttonsPanel);

		btnExecute = new JButton(new ImageIcon(ExecutionFrame.class.getResource("/images/Play22.png")));
		btnExecute.setToolTipText("run machine");
		btnExecute.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnExecute.addActionListener(e -> run(texto.getText()));

		btnBack = new JButton(new ImageIcon(ExecutionFrame.class.getResource("/images/Undo22.png")));
		btnBack.setToolTipText("return to main");
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnBack.addActionListener(e -> controller.volverInicio());
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

		buttonsPanel.add(btnExecute);
		buttonsPanel.add(btnBack);
	}

	private void initTextField() {
		texto = new JTextField();
		texto.setToolTipText("input");
		texto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		texto.setLocation(154, 99);
		texto.setSize(224, 27);
		getContentPane().add(texto);
	}

	private void initTapePanel() {
		TAPE_SIZE = 5; // valor inicial
		cintaPanel = new JPanel();
		cintaPanel.setLayout(new GridLayout(1, TAPE_SIZE));

		JScrollPane scrollPane = new JScrollPane(cintaPanel);
		scrollPane.setToolTipText("\u25B2 blank symbol");
		scrollPane.setBounds(10, 11, 494, 76);
		getContentPane().add(scrollPane);

		cinta = new JLabel[TAPE_SIZE];

		for (int i = 0; i < TAPE_SIZE; i++) {
			cinta[i] = new JLabel("\u25B2", SwingConstants.CENTER);
			cinta[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			cinta[i].setFont(new Font("Arial", Font.PLAIN, 24));
			cintaPanel.add(cinta[i]);
		}
	}

	private void actualizarTimer() {
		controller.setVelocidad(velocidad);
	}

	private void run(String input) {
		// updateCompleteTape(input);
		try {
			controller.ejecutar(input);
			btnExecute.setEnabled(true);
		} catch (InterruptedException ex) {
			btnExecute.setEnabled(true);
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
		}
	}

	public void updateCompleteTape(String input) {
		for (int i = 0; i < TAPE_SIZE; i++) {
			if (i < espacioAdicional && i > espacioAdicional + input.length())
				cinta[i].setText(input.charAt(i - espacioAdicional) + "");
			else
				cinta[i].setText("\u25B2");
		}
	}

	public void cleanText() {
		for (int i = 0; i < TAPE_SIZE; i++) {
			cinta[i].setText("\u25B2");
		}
		texto.setText("");
	}

	public void setText(String resultado) {
		texto.setText(resultado);
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

	public void title(String s) {
		setTitle(s);
	}

	public void redimensionarCinta(int indice) {
		if (indice <= 0) {
			int espacioAdicional = Math.abs(indice) + 2; // Espacio necesario hacia la izquierda
			int nuevoTamano = TAPE_SIZE + espacioAdicional;

			JLabel[] nuevaCinta = new JLabel[nuevoTamano];

			// Desplazar los elementos existentes hacia la derecha
			System.arraycopy(cinta, 0, nuevaCinta, espacioAdicional, TAPE_SIZE);

			// Inicializar las nuevas posiciones vacías al inicio
			for (int i = 0; i < espacioAdicional; i++) {
				nuevaCinta[i] = new JLabel("\u25B2", SwingConstants.CENTER);
				nuevaCinta[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				nuevaCinta[i].setFont(new Font("Arial", Font.PLAIN, 24));
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
				nuevaCinta[i].setFont(new Font("Arial", Font.PLAIN, 24));
				cintaPanel.add(nuevaCinta[i]);
			}

			cinta = nuevaCinta;
			TAPE_SIZE = nuevoTamano;
			cintaPanel.revalidate();
			cintaPanel.repaint();
		}
	}

	public void reset() {
		espacioAdicional = 0;
		initTapePanel();
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}
}