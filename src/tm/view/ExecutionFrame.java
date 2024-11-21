package tm.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
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

public class ExecutionFrame extends JFrame {

	private AppController controller;

	private JButton btnExecute;
	private JButton btnBack;
	private JSlider sliderVelocidad;
	private JTextField texto;
	private JPanel cintaPanel;
	private JLabel[] cinta;
	private JLabel lblInterval;
	private int indice_ant = -1;
	private int TAPE_SIZE;
	private int velocidad = 500; // Velocidad inicial

	public ExecutionFrame() {
		super();

		// Frame config
		frameConfig();
		// inicializar componentes
		initLabels();
		initSlider();
		initButtons();
		initTextField();
		initTapePanel();
	}

	private void frameConfig() {
		setSize(530, 217);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);
	}

	private void initLabels() {
		lblInterval = new JLabel(String.format("intervalo: " + velocidad + " ms"));
		lblInterval.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblInterval.setBounds(244, 98, 125, 27);
		getContentPane().add(lblInterval);
	}

	private void initSlider() {
		sliderVelocidad = new JSlider(0, 1000, 500);
		sliderVelocidad.setBounds(10, 98, 224, 32);
		sliderVelocidad.setMajorTickSpacing(250);
		sliderVelocidad.setSnapToTicks(true);
		sliderVelocidad.setPaintLabels(true);
		sliderVelocidad.addChangeListener(e -> {
			velocidad = sliderVelocidad.getValue();
			lblInterval.setText("intervalo: " + velocidad + " ms");
			actualizarTimer();
		});
		sliderVelocidad.setAutoscrolls(true);
		getContentPane().add(sliderVelocidad);
	}

	private void initButtons() {
		btnExecute = new JButton("ejecutar");
		btnExecute.setToolTipText("run machine");
		btnExecute.setBounds(244, 140, 125, 27);
		btnExecute.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnExecute.addActionListener(e -> run(texto.getText()));
		getContentPane().add(btnExecute);

		btnBack = new JButton("volver");
		btnBack.setToolTipText("return to main");
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnBack.setBounds(379, 140, 125, 27);
		btnBack.addActionListener(e -> controller.volverInicio());
		getContentPane().add(btnBack);
	}

	private void initTextField() {
		texto = new JTextField();
		texto.setToolTipText("input");
		texto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		texto.setLocation(10, 140);
		texto.setSize(224, 27);
		getContentPane().add(texto);
	}

	private void initTapePanel() {
		TAPE_SIZE = 20; // valor inicial
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
		btnExecute.setEnabled(false);
		try {
			controller.ejecutar(input);

		} catch (InterruptedException ex) {
			btnExecute.setEnabled(true);
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
		}
		btnExecute.setEnabled(true);
	}

	public void actualizarCinta() {
		for (int i = 0; i < 10; i++) {
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

	public void setController(AppController controller) {
		this.controller = controller;
	}
}