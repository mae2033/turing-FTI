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

@SuppressWarnings("serial")
public class WelcomeScreen extends JFrame {
	private AppController controller;

	public WelcomeScreen() {
		config();
		initComponentes();

	}

	private void config() {
		setTitle("FTI");
		setSize(396, 162);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
	}

	private void initComponentes() {
		// Crear panel para las etiquetas
		JPanel lblPanel = new JPanel(new BorderLayout());
		JLabel title = new JLabel("Bienvenido", SwingConstants.CENTER);
		title.setFont(new Font("Rockwell", Font.BOLD, 20));
		JLabel detail = new JLabel("Simulador Maquina de Turing", SwingConstants.CENTER);
		detail.setFont(new Font("Rockwell", Font.PLAIN, 17));
		lblPanel.add(title, BorderLayout.NORTH);
		lblPanel.add(detail, BorderLayout.SOUTH);

		// Crear panel para los botones
		JPanel buttonPanel = new JPanel(new FlowLayout());

		// Boton para seleccionar archivo
		JButton startButton = new JButton("Seleccionar");
		startButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFileChooser();
			}
		});
		buttonPanel.add(startButton, BorderLayout.CENTER);

		// Boton para salir
		JButton exitButton = new JButton("Salir");
		exitButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		buttonPanel.add(exitButton, BorderLayout.LINE_END);

		getContentPane().add(lblPanel, BorderLayout.NORTH);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	// nueva version
	private void openFileChooser() {
		JFileChooser fileChooser = new JFileChooser("./src/resources");
		FileFilter filter = new FileNameExtensionFilter("Text Files", "txt");
		fileChooser.setFileFilter(filter);

		int returnValue = fileChooser.showOpenDialog(this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			controller.iniciarMaquina(selectedFile); 
		}
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}
}
