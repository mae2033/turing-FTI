package tm.app;

/**
 * Inicia la aplicaci�n
 */
public class App {

	public static void main(String[] args) {
		AppConfigurator af = new AppConfigurator();
		af.configurarComponentes();
		af.iniciarEjecucion();
	}
}
