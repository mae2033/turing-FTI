package tm.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tm.app.AppController;

public class ExecutionFrame extends JFrame {

	private AppController controller;

	private JButton btnExecute;
	private JButton btnBack;
	private JSlider sliderVelocidad;
	private JTextField texto;
	private JLabel[] cinta;

	private int TAPE_SIZE = 50;
	private int velocidad = 500; // Velocidad inicial

	public ExecutionFrame() {
		super();

		// Frame config
		setSize(530, 217);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		// Frame Panel
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel(String.format("intervalo: " + velocidad + " ms"));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(244, 98, 125, 27);
		getContentPane().add(lblNewLabel);

		sliderVelocidad = new JSlider(0, 1000, 500);
		sliderVelocidad.setBounds(10, 98, 224, 32);
		sliderVelocidad.setMajorTickSpacing(250);
		sliderVelocidad.setSnapToTicks(true);
		sliderVelocidad.setPaintLabels(true);
		sliderVelocidad.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				velocidad = sliderVelocidad.getValue();
				lblNewLabel.setText("intervalo: " + velocidad + " ms");
				actualizarTimer();
			}
		});
		getContentPane().add(sliderVelocidad);

		btnExecute = new JButton("ejecutar");
		btnExecute.setToolTipText("run machine");
		btnExecute.setBounds(244, 140, 125, 27);
		getContentPane().add(btnExecute);
		btnExecute.setFont(new Font("Tahoma", Font.PLAIN, 15));

		btnBack = new JButton("volver");
		btnBack.setToolTipText("return to main");
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnBack.setBounds(379, 140, 125, 27);
		getContentPane().add(btnBack);
		btnExecute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				run(texto.getText());
			}
		});
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.volverInicio();
			}
		});

		texto = new JTextField();
		texto.setToolTipText("input");
		texto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		texto.setLocation(10, 140);
		texto.setSize(224, 27);
		getContentPane().add(texto);

		JPanel cintaPanel = new JPanel();
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
		sliderVelocidad.setAutoscrolls(true);
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
		// redimensionarCinta(indice)
		if (c == '-')
			c = '\u25B2';
		cinta[indice].setText(String.valueOf(c));
	}

	public void title(String s) {
		setTitle(s);
	}

	public void redimensionarCinta(int indice) {
		if (TAPE_SIZE <= indice)
			TAPE_SIZE = Math.abs(TAPE_SIZE - indice + 2);
		System.out.println(indice);
		// logica si no hay suficiente espacio redimenciona
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}
}