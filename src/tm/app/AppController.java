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

/**
 * Controlador principal que coordina la interacci�n entre los componentes de la
 * aplicaci�n, gestionando la ejecuci�n de la m�quina de Turing y la
 * actualizaci�n de la interfaz visual.
 */
public class AppController {

	private FileScanner fileScanner;
	private FileValidator fileValidator;
	private Ejecutor ejecutor;
	private WelcomeScreen welcomeScreen;
	private ExecutionFrame executionFrame;
	private ErrorFrame errorFrame;

	public AppController(FileScanner fileScanner, FileValidator fileValidator, Ejecutor ejecutor,
			WelcomeScreen welcomeScreen, ExecutionFrame executionFrame, ErrorFrame errorFrame) {
		this.fileScanner = fileScanner;
		this.fileValidator = fileValidator;
		this.ejecutor = ejecutor;
		this.welcomeScreen = welcomeScreen;
		this.executionFrame = executionFrame;
		this.errorFrame = errorFrame;
	}

	/**
	 * Inicia la ejecuci�n de la m�quina de Turing con la cadena de entrada
	 * proporcionada.
	 * 
	 * @param inputStr La cadena de entrada para la ejecuci�n de la m�quina.
	 */
	public void ejecutar(String inputStr) {
		ejecutor.runWithTimer(inputStr);
	}

	/**
	 * Actualiza la visualizaci�n de la cinta en la interfaz de usuario.
	 * 
	 * @param c El s�mbolo a escribir en la cinta.
	 * @param i El �ndice donde se escribir� el s�mbolo en la cinta.
	 */
	public void escribirCinta(char c, int i) {
		executionFrame.escribirCinta(c, i);
	}

	/**
	 * Configura e inicia la m�quina de Turing utilizando un archivo de entrada.
	 *
	 * @param file el archivo con la configuraci�n de la m�quina de Turing.
	 */
	public void iniciarMaquina(File file) {
		try {
			if (!formatoValido(file)) {
				throw new IllegalArgumentException("El formato del archivo no es v�lido.");
			}
			fileScanner = new FileScanner(file);
			ejecutor.carga(fileScanner.getFileScan());
			seleccion(ejecutor.getNombre());
		} catch (FileNotFoundException e) {
			showError("Archivo no encontrado: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			showError(fileValidator.getErrores());
		} catch (Exception e) {
			showError("Ocurri� un error inesperado: " + e.getMessage());
		}
	}

	/**
	 * Actualiza la interfaz para mostrar la pantalla de ejecuci�n, con el nombre de
	 * la m�quina de Turing cargada.
	 * 
	 * @param s El nombre de la m�quina de Turing.
	 */
	private void seleccion(String s) {
		welcomeScreen.dispose();
		executionFrame.setTitle(s);
		executionFrame.setVisible(true);
	}

	/**
	 * Vuelve a la pantalla de inicio y detiene la ejecuci�n de la m�quina.
	 */
	public void volverInicio() {
		ejecutor.detener();
		ejecutor.reset();
		executionFrame.setVisible(false);
		welcomeScreen.setVisible(true);
	}

	/**
	 * Detiene la ejecuci�n de la m�quina de Turing en caso de interrupci�n.
	 */
	public void interrumpir() {
		ejecutor.detener();
	}

	/**
	 * Establece la velocidad de ejecuci�n de la m�quina de Turing seg�n la
	 * preferencia del usuario.
	 * 
	 * @param v La velocidad deseada en milisegundos.
	 */
	public void setVelocidad(int v) {
		ejecutor.setVelocidad(v);
	}

	/**
	 * Valida el formato del archivo seleccionado para asegurarse de que sea
	 * compatible con la m�quina de Turing.
	 * 
	 * @param archivoSeleccionado El archivo a validar.
	 * @return {@code true} si el archivo es v�lido, {@code false} en caso
	 *         contrario.
	 * @throws IOException Si ocurre un error al leer el archivo.
	 */
	public boolean formatoValido(File archivoSeleccionado) throws IOException {
		return fileValidator.validarArchivo(archivoSeleccionado);
	}

	/**
	 * Retorna los errores encontrados durante la validaci�n del archivo.
	 * 
	 * @return Una lista de con los errores
	 */
	public List<String> getError() {
		return fileValidator.getErrores();
	}

	/**
	 * Muestra el resultado final de la ejecuci�n en la pantalla de ejecuci�n.
	 * 
	 * @param string El resultado que se mostrar� en la pantalla.
	 */
	public void setResultadoPantalla(String string) {
		executionFrame.postEjecucion(string);
	}

	/**
	 * Muestra un mensaje de error gen�rico en la interfaz de usuario.
	 * 
	 * @param error El error que ocurri�, puede ser de cualquier tipo.
	 */
	public void showError(Object error) {
		errorFrame.showError(error);
	}
}
