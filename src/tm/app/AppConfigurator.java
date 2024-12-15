package tm.app;

import tm.model.Ejecutor;
import tm.utils.FileScanner;
import tm.utils.FileValidator;
import tm.view.ErrorFrame;
import tm.view.ExecutionFrame;
import tm.view.WelcomeScreen;

/**
 * Clase encargada de la configuración y la inicialización de los componentes
 * necesarios para la ejecución de la aplicación.
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

	public void configurarComponentes() {
		controller = new AppController(scanner, validator, ejecutor, welcomeFrame, executionFrame, errorFrame);
	}

	public void iniciarEjecucion() {
		ejecutor.setController(controller);
		executionFrame.setController(controller);
		welcomeFrame.setController(controller);
		welcomeFrame.setVisible(true);
	}

}
