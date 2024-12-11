package tm.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import tm.model.Maquina;
import tm.utils.FileScanner;
import tm.utils.FileValidator;
import tm.view.ErrorFrame;
import tm.view.ExecutionFrame;
import tm.view.WelcomeScreen;

public class AppController {

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

	public void ejecutar(String inputStr) {
		maquina.runTuringWithTimer(inputStr);
	}

	public void escribirCinta(char c, int i) {
		efGUI.updateTape(c, i);
	}

	public void iniciarMaquina(File file) {
		try {
			if (!formatoValido(file)) {
				throw new IllegalArgumentException("El formato del archivo no es válido.");
			}
			fs = new FileScanner(file);
			maquina.carga(fs.getFileScan());
			seleccion(maquina.getNombreMaquina());
		} catch (FileNotFoundException e) {
			showError("Archivo no encontrado: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			showError(fv.getErrores());
		} catch (Exception e) {
			showError("Ocurrió un error inesperado: " + e.getMessage());
		}
	}

	private void seleccion(String s) {
		wsGUI.dispose();
		efGUI.setTitle(s);
		efGUI.setVisible(true);
	}

	public void volverInicio() {
		efGUI.setVisible(false);
		wsGUI.setVisible(true);
		maquina.detener();
		maquina.reset();
	}

	public void interrumpir() {
		maquina.detener();
	}

	public void setVelocidad(int v) {
		maquina.setVelocidad(v);
	}

	public boolean formatoValido(File archivoSeleccionado) throws IOException {
		return fv.validarArchivo(archivoSeleccionado);
	}

	public List<String> getError() {
		return fv.getErrores();
	}

	public void setResultadoPantalla(String string) {
		efGUI.setText(string);
	}

	public void showError(Object error) {
		errGUI.showError(error);
	}
}
