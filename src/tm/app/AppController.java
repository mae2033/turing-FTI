package tm.app;

import tm.model.Maquina;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.JOptionPane;

import tm.utils.FileScanner;
import tm.utils.FileValidator;
import tm.view.ExecutionFrame;
import tm.view.WelcomeScreen;

public class AppController {

	private FileScanner fs;
	private FileValidator fv;
	private Maquina maquina;
	private WelcomeScreen wsGUI;
	private ExecutionFrame efGUI;

	public FileScanner getFs() {
		return fs;
	}

	public void setFs(FileScanner fs) {
		this.fs = fs;
	}

	public FileValidator getFv() {
		return fv;
	}

	public void setFv(FileValidator fv) {
		this.fv = fv;
	}

	public Maquina getMaquina() {
		return maquina;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}

	public WelcomeScreen getWsGUI() {
		return wsGUI;
	}

	public void setWsGUI(WelcomeScreen wsGUI) {
		this.wsGUI = wsGUI;
	}

	public ExecutionFrame getEfGUI() {
		return efGUI;
	}

	public void setEfGUI(ExecutionFrame efGUI) {
		this.efGUI = efGUI;
	}

	public String getNombre() {
		return maquina.getName();
	}

	public void ejecutar(String inputStr) throws InterruptedException {
		setCinta(inputStr);
		maquina.runTuring(maquina.getInitIndex());
	}

	public String getResultado() {
		return maquina.getResult();
	}

	public void setCinta(String s) {
		maquina.setTape(s);
	}

	public String getCinta() {
		return maquina.getTape();
	}

	public void guiCinta(String s) {
		efGUI.setTape(s);
	}

	public int getIndice() {
		return maquina.getIndice();
	}

	public void iniciarMaquina(File file) {
		try {
			System.out.println(fv.validarArchivo(file));
			fs = new FileScanner(file);
			maquina.carga(fs.getFileScan());
			seleccion(maquina.getName());
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not found: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void seleccion(String s) {
		wsGUI.dispose();
		efGUI.setTitle(s);
		efGUI.setVisible(true);
	}

	public void volverInicio() {
		efGUI.cleanText();
		efGUI.dispose();
		wsGUI.setVisible(true);
		maquina.setTape("");
	}

	public void interrumpir() {
		maquina.interrupt();
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
}
