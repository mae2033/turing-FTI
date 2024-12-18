package tm.app;

import tm.model.Ejecutor;
import tm.utils.FileScanner;
import tm.utils.FileValidator;
import tm.view.ErrorFrame;
import tm.view.ExecutionFrame;
import tm.view.WelcomeScreen;

/**
 * Configura e inicializa los componentes necesarios para la ejecuci�n de la
 * aplicaci�n.
 */
public class AppConfigurator {
	private AppController controller;
	private FileScanner scanner;
	private FileValidator validator;
	private Ejecutor ejecutor;
	private WelcomeScreen welcomeFrame;
	private ExecutionFrame executionFrame;
	private ErrorFrame errorFrame;

	public AppConfigurator() {
		scanner = new FileScanner();
		validator = new FileValidator();
		ejecutor = new Ejecutor();
		welcomeFrame = new WelcomeScreen();
		executionFrame = new ExecutionFrame();
		errorFrame = new ErrorFrame();
	}

	/**
	 * Configura el controlador de la aplicaci�n y lo asocia con los componentes.
	 */
	public void configurarComponentes() {
		controller = new AppController(scanner, validator, ejecutor, welcomeFrame, executionFrame, errorFrame);
	}

	/**
	 * Inicia la ejecuci�n de la aplicaci�n mostrando la pantalla de bienvenida.
	 */
	public void iniciarEjecucion() {
		ejecutor.setController(controller);
		executionFrame.setController(controller);
		welcomeFrame.setController(controller);
		welcomeFrame.setVisible(true);
	}

}
