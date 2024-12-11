package tm.app;

import tm.model.Maquina;
import tm.utils.FileScanner;
import tm.utils.FileValidator;
import tm.view.ErrorFrame;
import tm.view.ExecutionFrame;
import tm.view.WelcomeScreen;

public class AppFacade {
	private AppController controller;
	private FileScanner fs;
	private FileValidator fv;
	private Maquina maquina;
	private WelcomeScreen wsGUI;
	private ExecutionFrame efGUI;
	private ErrorFrame errGUI;

	public AppFacade() {
		fv = new FileValidator();
		fs = new FileScanner();
		maquina = new Maquina();
		wsGUI = new WelcomeScreen();
		efGUI = new ExecutionFrame();
		errGUI = new ErrorFrame();
		controller = new AppController();
	}

	void carga() {
		maquina.setController(controller);
		efGUI.setController(controller);
		wsGUI.setController(controller);
//		errGUI.setController(controller);
	}

	void cargaControlador() {
		controller.setFs(fs);
		controller.setFv(fv);
		controller.setMaquina(maquina);
		controller.setWsGUI(wsGUI);
		controller.setEfGUI(efGUI);
		controller.setErrGUI(errGUI);

	}
}
