package tm.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import tm.model.Ejecutor;
import tm.utils.FileScanner;
import tm.utils.FileValidator;
import tm.view.ErrorFrame;
import tm.view.ExecutionFrame;
import tm.view.WelcomeScreen;

public class AppController {

	private FileScanner fileScanner;
	private FileValidator fileValidator;
	private Ejecutor ejecutor;
	private WelcomeScreen welcomeScreen;
	private ExecutionFrame executionFrame;
	private ErrorFrame errorFrame;

	public AppController(FileScanner fileScanner, FileValidator fileValidator, Ejecutor ejecutor, WelcomeScreen welcomeScreen,
			ExecutionFrame executionFrame, ErrorFrame errorFrame) {
		this.fileScanner = fileScanner;
		this.fileValidator = fileValidator;
		this.ejecutor = ejecutor;
		this.welcomeScreen = welcomeScreen;
		this.executionFrame = executionFrame;
		this.errorFrame = errorFrame;
	}

	public void setFs(FileScanner fileScanner) {
		this.fileScanner = fileScanner;
	}

	public void setFv(FileValidator fileValidator) {
		this.fileValidator = fileValidator;
	}

	public void setEjecutor(Ejecutor ejecutor) {
		this.ejecutor = ejecutor;
	}

	public void setWsGUI(WelcomeScreen welcomeScreen) {
		this.welcomeScreen = welcomeScreen;
	}

	public void setEfGUI(ExecutionFrame executionFrame) {
		this.executionFrame = executionFrame;
	}

	public void setErrGUI(ErrorFrame errorFrame) {
		this.errorFrame = errorFrame;
	}

	public void ejecutar(String inputStr) {
		ejecutor.runWithTimer(inputStr);
	}

	public void escribirCinta(char c, int i) {
		executionFrame.updateTape(c, i);
	}

	/**
	 * Configura e inicia la máquina de Turing utilizando un archivo de entrada.
	 *
	 * @param file el archivo con la configuración de la máquina de Turing.
	 * @throws IllegalArgumentException si el archivo tiene un formato inválido.
	 * @throws FileNotFoundException    si el archivo no se encuentra.
	 * @throws Exception                si ocurre un error inesperado.
	 */
	public void iniciarMaquina(File file) {
		try {
			if (!formatoValido(file)) {
				throw new IllegalArgumentException("El formato del archivo no es válido.");
			}
			fileScanner = new FileScanner(file);
			ejecutor.carga(fileScanner.getFileScan());
			seleccion(ejecutor.getNombre());
		} catch (FileNotFoundException e) {
			showError("Archivo no encontrado: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			showError(fileValidator.getErrores());
		} catch (Exception e) {
			showError("Ocurrió un error inesperado: " + e.getMessage());
		}
	}

	private void seleccion(String s) {
		welcomeScreen.dispose();
		executionFrame.setTitle(s);
		executionFrame.setVisible(true);
	}

	public void volverInicio() {
		ejecutor.detener();
		ejecutor.reset();
		executionFrame.setVisible(false);
		welcomeScreen.setVisible(true);
	}

	public void interrumpir() {
		ejecutor.detener();
	}

	public void setVelocidad(int v) {
		ejecutor.setVelocidad(v);
	}

	public boolean formatoValido(File archivoSeleccionado) throws IOException {
		return fileValidator.validarArchivo(archivoSeleccionado);
	}

	/**
	 * Errores encontrados al usar el validador
	 */
	public List<String> getError() {
		return fileValidator.getErrores();
	}

	public void setResultadoPantalla(String string) {
		executionFrame.afterRun(string);
	}

	public void showError(Object error) {
		errorFrame.showError(error);
	}
}
