package tm.app;

import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;

import tm.utils.FileScanner;
import tm.utils.FileValidator;
import tm.model.Maquina;
import tm.view.WelcomeScreen;
import tm.view.ExecutionFrame;

public class App {

	private Maquina turingMachine;

	public static void main(String[] args) {
		new App().start();
	}

	private void start() {
		WelcomeScreen welcomeScreen = new WelcomeScreen();
		welcomeScreen.setVisible(true);
		welcomeScreen.setFileSelectedListener(file -> {
			if (isValidFile(file)) {
				welcomeScreen.dispose();
				runMachine(file);
			} else {
				JOptionPane.showMessageDialog(null, "Format error", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	private boolean isValidFile(File file) {
		return new FileValidator().validarArchivo(file);
	}

	private void runMachine(File file) {
		try {
			FileScanner fileScanner = new FileScanner(file);
			turingMachine = new Maquina(fileScanner.getFileScan());
			new ExecutionFrame(turingMachine);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not found: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
