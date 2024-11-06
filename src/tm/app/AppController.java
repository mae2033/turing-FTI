package tm.app;

import tm.model.Maquina;

import tm.model.Builder;
import tm.utils.FileScanner;
import tm.utils.FileValidator;
import tm.view.ExecutionFrame;
import tm.view.WelcomeScreen;

public class AppController {

	private FileScanner fs;
	private FileValidator fv;
	private Builder builder;
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

	public Builder getBuilder() {
		return builder;
	}

	public void setBuilder(Builder builder) {
		this.builder = builder;
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
		maquina.runTuring(6);
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

	public void volverInicio() {
		efGUI.dispose();
		wsGUI.setVisible(true);
	}

}
