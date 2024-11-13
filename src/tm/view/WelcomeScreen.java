package tm.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.util.List;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import tm.app.AppController;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Font;

@SuppressWarnings("serial")
public class WelcomeScreen extends JFrame {
	private AppController controller;

	public WelcomeScreen() {
		setTitle("FTI");
		setSize(364, 111);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		// Configurar layout antes de agregar componentes
		getContentPane().setLayout(new BorderLayout());

		// Crear y agregar etiqueta de bienvenida
		JLabel label = new JLabel("Bienvenido a la Maquina de Turing", SwingConstants.CENTER);
		label.setFont(new Font("Rockwell", Font.BOLD, 18));
		getContentPane().add(label, BorderLayout.NORTH);

		// Crear panel para los botones
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		// Botón para seleccionar archivo
		JButton startButton = new JButton("Seleccionar Maquina");
		startButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFileChooser();
			}
		});
		buttonPanel.add(startButton);

		// Botón para salir
		JButton exitButton = new JButton("Salir");
		exitButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		buttonPanel.add(exitButton);

		// Agregar panel de botones al centro del layout
		getContentPane().add(buttonPanel, BorderLayout.CENTER);

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
				error(controller.getError());
		}
	}

	@SuppressWarnings("unused")
	public void error(List<String> s) {
		String err = s.toString();
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}
}
