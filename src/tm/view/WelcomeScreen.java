package tm.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class WelcomeScreen extends JFrame {
	FileSelectedListener listener;

	public WelcomeScreen() {
		setTitle("Bienvenida");
		setSize(300, 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		JButton startButton = new JButton("Seleccionar Archivo");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFileChooser();
			}
		});

		setLayout(new FlowLayout());
		add(new JLabel("Bienvenido a la Maquina de Turing"));
		add(startButton);

		setVisible(true);
	}

	private void openFileChooser() {
		JFileChooser fileChooser = new JFileChooser("./src/resources");
		FileFilter filter = new FileNameExtensionFilter("Text Files", "txt");
		fileChooser.setFileFilter(filter);
		int returnValue = fileChooser.showOpenDialog(this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			if (listener != null) {
				listener.onFileSelected(selectedFile); // Notifica al controlador
			}
		}
	}

	public void setFileSelectedListener(FileSelectedListener listener) {
		this.listener = listener; // Asigna el listener
	}

	// Interfaz para la notificación de selección de archivo
	public interface FileSelectedListener {
		void onFileSelected(File file);
	}

}
