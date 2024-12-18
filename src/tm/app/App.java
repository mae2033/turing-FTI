package tm.app;

/**
 * Inicia la aplicación
 */
public class App {

	public static void main(String[] args) {
		AppConfigurator af = new AppConfigurator();
		af.configurarComponentes();
		af.iniciarEjecucion();
	}
}
