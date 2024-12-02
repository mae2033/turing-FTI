package tm.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import tm.app.AppController;

public class WelcomeScreen extends JFrame {
	private AppController controller;

	public WelcomeScreen() {
		setTitle("FTI");
		setSize(369, 131);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);

		// Crear y agregar etiqueta de bienvenida
		JLabel label = new JLabel("Simulador Maquina de Turing", SwingConstants.CENTER);
		label.setBounds(22, 5, 311, 22);
		label.setFont(new Font("Rockwell", Font.BOLD, 20));
		getContentPane().add(label);

		// Crear panel para los botones
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(22, 39, 311, 43);
		buttonPanel.setLayout(new FlowLayout());

		// Boton para seleccionar archivo
		JButton startButton = new JButton("Seleccionar");
		startButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFileChooser();
			}
		});
		buttonPanel.add(startButton);

		// Boton para salir
		JButton exitButton = new JButton("Salir");
		exitButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		buttonPanel.add(exitButton);

		getContentPane().add(buttonPanel);

		setVisible(true);
	}

	private void openFileChooser() {
		JFileChooser fileChooser = new JFileChooser("./src/resources");
		FileFilter filter = new FileNameExtensionFilter("Text Files", "txt");
		fileChooser.setFileFilter(filter);
		int returnValue = fileChooser.showOpenDialog(this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			if (controller.formatoValido(selectedFile))
				controller.iniciarMaquina(selectedFile);
			else
				error();

		}
	}

	public void error() {
		Object error = controller.getError();
//		controller.showError(error);
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}
}
