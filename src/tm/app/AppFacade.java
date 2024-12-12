package tm.app;

import tm.model.Ejecutor;
import tm.utils.FileScanner;
import tm.utils.FileValidator;
import tm.view.ErrorFrame;
import tm.view.ExecutionFrame;
import tm.view.WelcomeScreen;

public class AppFacade {
	private AppController controller;
	private FileScanner fs;
	private FileValidator fv;
	private Ejecutor ejecutor;
	private WelcomeScreen wsGUI;
	private ExecutionFrame efGUI;
	private ErrorFrame errGUI;

	public AppFacade() {
		fv = new FileValidator();
		fs = new FileScanner();
		ejecutor = new Ejecutor();
		wsGUI = new WelcomeScreen();
		efGUI = new ExecutionFrame();
		errGUI = new ErrorFrame();
		controller = new AppController();
	}

	void carga() {
		ejecutor.setController(controller);
		efGUI.setController(controller);
		wsGUI.setController(controller);
	}

	void cargaControlador() {
		controller.setFs(fs);
		controller.setFv(fv);
		controller.setEjecutor(ejecutor);
		controller.setWsGUI(wsGUI);
		controller.setEfGUI(efGUI);
		controller.setErrGUI(errGUI);

	}
}
