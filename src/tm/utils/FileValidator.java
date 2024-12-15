package tm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FileValidator {

	private List<String> errs;
	private Set<Character> alpha; // usarlo para validar que se lee y que se escribe

	public boolean validarArchivo(File file) throws IOException {
		errs = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			int numeroLinea = 1;
			String linea;

			linea = leerLineaValida(reader, numeroLinea++);
			if (!validarNombre(linea, numeroLinea++))
				errs.add("L�nea " + numeroLinea + ": Falta el nombre de la m�quina o formato incorrecto.");

			linea = leerLineaValida(reader, numeroLinea++);
			if (!validarAlfabeto(linea, numeroLinea++))
				errs.add("L�nea " + numeroLinea + ": Falta el alfabeto o formato incorrecto.");

			linea = leerLineaValida(reader, numeroLinea++);
			if (!validarEspacio(linea, numeroLinea++))
				errs.add("L�nea " + numeroLinea + ": El s�mbolo de espacio debe ser un �nico car�cter.");

			linea = leerLineaValida(reader, numeroLinea++);
			int numeroEstados = validarNumeroEstados(linea, numeroLinea++);
			if (numeroEstados == -1) {
				errs.add("L�nea " + numeroLinea + ": Error en la cantidad de estados.");
			}

			linea = leerLineaValida(reader, numeroLinea++);
			if (!validarEstadoInicial(linea, numeroLinea++, numeroEstados)) {
				errs.add("L�nea " + numeroLinea + ": Error en el estado inicial.");
			}

			for (int estado = 0; estado < numeroEstados; estado++) {
				linea = leerLineaValida(reader, numeroLinea++);
				int numeroTransiciones = validarTransicionesInicio(linea, estado, numeroLinea++);
				if (numeroTransiciones == -1)
					errs.add("L�nea " + numeroLinea + ": Falta la cantidad de transiciones para el estado " + estado
							+ ".");

				for (int i = 0; i < numeroTransiciones; i++) {
					linea = leerLineaValida(reader, numeroLinea++);
					if (!validarTransicion(linea, numeroLinea++))
						errs.add("L�nea " + numeroLinea + ": Error de transici�n en el estado " + estado
								+ ", transacci�n " + i + ".");
				}
			}

		}

		return errs.isEmpty();
	}

	/**
	 * Lee las lineas que no sean vacias y no comiencen con '//'
	 */
	private String leerLineaValida(BufferedReader reader, int numeroLinea) throws IOException {
		String linea;
		do {
			linea = reader.readLine();
			numeroLinea++;
		} while (linea != null && (linea.trim().isEmpty() || linea.trim().startsWith("//")));
		return linea;
	}

	public List<String> getErrores() {
		return errs;
	}

	private boolean validarNombre(String linea, int numeroLinea) {
		if (linea == null || !linea.startsWith("MAQUINA ")) {
			return false;
		}
		return true;
	}

	private boolean validarAlfabeto(String linea, int numeroLinea) {
		if (linea == null || linea.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	private boolean validarEspacio(String linea, int numeroLinea) {
		if (linea == null || linea.length() != 1) {
			return false;
		}
		return true;
	}

	private int validarNumeroEstados(String linea, int numeroLinea) {
		if (linea == null || linea.trim().isEmpty()) {
			errs.add("L�nea " + numeroLinea + ": Falta el n�mero de estados.");
			return -1;
		}

		// Verificar que la l�nea contenga solo d�gitos
		String trimmedLinea = linea.trim();
		if (!trimmedLinea.matches("\\d+")) {
			errs.add("L�nea " + numeroLinea + ": El n�mero de estados debe ser un n�mero entero positivo.");
			return -1;
		}
		try {
			int numeroEstados = Integer.parseInt(trimmedLinea);
			if (numeroEstados <= 0) {
				errs.add("L�nea " + numeroLinea + ": La cantidad de estados debe ser mayor a 0.");
				return -1;
			}
			return numeroEstados;
		} catch (NumberFormatException e) {
			errs.add("L�nea " + numeroLinea + ": La cantidad de estados no es un n�mero v�lido.");
			return -1;
		}
	}

	private boolean validarEstadoInicial(String linea, int numeroLinea, int numeroEstados) {
		if (linea == null || linea.trim().isEmpty()) {
			errs.add("L�nea " + numeroLinea + ": Falta el estado inicial.");
			return false;
		}
		String trimmedLinea = linea.trim();
		if (!trimmedLinea.matches("\\d+")) {
			errs.add("L�nea " + numeroLinea + ": El estado inicial debe ser un n�mero entero positivo.");
			return false;
		}

		try {
			int estadoInicial = Integer.parseInt(trimmedLinea);
			if (estadoInicial < 0 || estadoInicial >= numeroEstados) {
				errs.add("L�nea " + numeroLinea + ": El estado inicial debe estar entre 0 y " + (numeroEstados - 1)
						+ ".");
				return false;
			}
		} catch (NumberFormatException e) {
			errs.add("L�nea " + numeroLinea + ": El estado inicial no es un n�mero v�lido.");
			return false;
		}
		return true;
	}

	private int validarTransicionesInicio(String linea, int estado, int numeroLinea) {
		if (linea == null || linea.trim().isEmpty()) {
			return -1;
		}

		String trimmedLinea = linea.trim();
		if (!trimmedLinea.matches("\\d+")) {
			errs.add("L�nea " + numeroLinea + ": La cantidad de transiciones debe ser un n�mero entero positivo.");
			return -1;
		}

		try {
			int cantidadTransiciones = Integer.parseInt(trimmedLinea);
			if (cantidadTransiciones < 0) {
				errs.add("L�nea " + numeroLinea + ": La cantidad de transiciones debe ser mayor o igual a 0.");
				return -1;
			}
			return cantidadTransiciones;
		} catch (NumberFormatException e) {
			errs.add("La cantidad de transiciones no es un n�mero v�lido.");
			return -1;
		}
	}

	private boolean validarTransicion(String linea, int numeroLinea) {
		if (linea == null) {
			errs.add("Falta la transici�n o formato incorrecto.");
			return false;
		}
		String[] partes = linea.split(" ");
		if (partes.length != 4) {
			errs.add("L�nea " + numeroLinea + ": La transici�n debe tener el formato 'X Y R/L N'.");
			return false;
		}

		if (!partes[2].equals("L") && !partes[2].equals("R") && !partes[2].equals("N")) {
			errs.add("L�nea " + numeroLinea + ": La transici�n debe aclarar el desplazamiento en cinta (L,R,N).");
			return false;
		}
		return true;
	}
}
