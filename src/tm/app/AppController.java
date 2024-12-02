package tm.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import org.apache.log4j.Logger;

import tm.model.Maquina;
import tm.utils.FileScanner;
import tm.utils.FileValidator;
import tm.view.ErrorFrame;
import tm.view.ExecutionFrame;
import tm.view.WelcomeScreen;

public class AppController {

	final static Logger logger = Logger.getLogger(AppController.class);

	private FileScanner fs;
	private FileValidator fv;
	private Maquina maquina;
	private WelcomeScreen wsGUI;
	private ExecutionFrame efGUI;
	private ErrorFrame errGUI;

	public void setFs(FileScanner fs) {
		this.fs = fs;
	}

	public void setFv(FileValidator fv) {
		this.fv = fv;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}

	public void setWsGUI(WelcomeScreen wsGUI) {
		this.wsGUI = wsGUI;
	}

	public void setEfGUI(ExecutionFrame efGUI) {
		this.efGUI = efGUI;
	}

	public void setErrGUI(ErrorFrame errGUI) {
		this.errGUI = errGUI;
	}

	public String getNombre() {
		return maquina.getName();
	}

	public void ejecutar(String inputStr) throws InterruptedException {
		maquina.runTuringWithTimer(inputStr);
	}

	public String getResultado() {
		return maquina.getResultado();
	}

	public void setCinta(String s) {
		maquina.setTape(s);
	}

	public String getCinta() {
		return maquina.getTape();
	}

	public void escribirCinta(char c, int i) {
		efGUI.updateTape(c, i);
	}

	public void iniciarMaquina(File file) {
		try {
			fs = new FileScanner(file);
			maquina.carga(fs.getFileScan());
			seleccion(maquina.getName());
		} catch (FileNotFoundException e) {
			logger.error("archivo no encontrado");
//			showError(e);
		}
	}

	private void seleccion(String s) {
		logger.info("inicio maquina");
		wsGUI.dispose();
		efGUI.setTitle(s);
		efGUI.setVisible(true);
	}

	public void volverInicio() {
		efGUI.cleanText();
		efGUI.dispose();
		wsGUI.setVisible(true);
		maquina.stop();
		maquina.reset();
	}

	public void interrumpir() {
		maquina.stop();
	}

	public void setVelocidad(int v) {
		maquina.setVelocidad(v);
	}

	public boolean formatoValido(File archivoSeleccionado) {
		return fv.validarArchivo(archivoSeleccionado);
	}

	public List<String> getError() {
		return fv.getErrores();
	}

	public void updateTextField(String string) {
		efGUI.setText(string);
	}

	public void showError(Object error) {
		errGUI.showError(error);

	}

}
