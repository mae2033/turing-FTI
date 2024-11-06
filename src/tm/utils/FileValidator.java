package tm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import java.util.Set;

/* refactorizar arreglar lista de errores */
public class FileValidator {

	private List<String> errs = new ArrayList<>();
//	private int countState; // cantidad de estados usarlo para la validacion de saltos
//	private Set<Character> alpha; // usarlo para validar que se lee y que se escribe
	
	public boolean validarArchivo(File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			int numeroLinea = 0;
			String linea;

			linea = leerLineaValida(reader, numeroLinea++);
			if (!validarNombre(linea, numeroLinea++))
				return false;

			linea = leerLineaValida(reader, numeroLinea++);
			if (!validarAlfabeto(linea, numeroLinea++))
				return false;

			linea = leerLineaValida(reader, numeroLinea++);
			if (!validarEspacio(linea, numeroLinea++))
				return false;

			linea = leerLineaValida(reader, numeroLinea++);
			int numeroEstados = validarNumeroEstados(linea, numeroLinea++);
			if (numeroEstados == -1)
				return false;

			linea = leerLineaValida(reader, numeroLinea++);
			if (!validarEstadoInicial(linea, numeroLinea++, numeroEstados))
				return false;

			for (int estado = 0; estado < numeroEstados; estado++) {
				linea = leerLineaValida(reader, numeroLinea++);
				int numeroTransiciones = validarTransicionesInicio(linea, estado, numeroLinea++);
				if (numeroTransiciones == -1)
					return false;

				for (int i = 0; i < numeroTransiciones; i++) {
					linea = leerLineaValida(reader, numeroLinea++);
					if (!validarTransicion(linea, numeroLinea++))
						return false;
				}
			}

		} catch (IOException e) {
			errs.add("Error al leer el archivo: " + e.getMessage());
			return false;
		}

		return errs.isEmpty();
	}

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
			errs.add("Línea " + numeroLinea + ": Falta el nombre de la máquina o formato incorrecto.");
			return false;
		}
		return true;
	}

	private boolean validarAlfabeto(String linea, int numeroLinea) {
		if (linea == null || linea.trim().isEmpty()) {
			errs.add("Línea " + numeroLinea + ": Falta el alfabeto o formato incorrecto.");
			return false;
		}
		return true;
	}

	private boolean validarEspacio(String linea, int numeroLinea) {
		if (linea == null || linea.length() != 1) {
			errs.add("Línea " + numeroLinea + ": El símbolo de espacio debe ser un único carácter.");
			return false;
		}
		return true;
	}

	private int validarNumeroEstados(String linea, int numeroLinea) {
		if (linea == null || linea.trim().isEmpty()) {
			errs.add("Línea " + numeroLinea + ": Falta el número de estados.");
			return -1;
		}

		// Verificar que la línea contenga solo dígitos
		String trimmedLinea = linea.trim();
		if (!trimmedLinea.matches("\\d+")) {
			errs.add("Línea " + numeroLinea + ": El número de estados debe ser un número entero positivo.");
			return -1;
		}
		try {
			int numeroEstados = Integer.parseInt(trimmedLinea);
			if (numeroEstados <= 0) {
				errs.add("Línea " + numeroLinea + ": La cantidad de estados debe ser mayor a 0.");
				return -1;
			}
			return numeroEstados;
		} catch (NumberFormatException e) {
			errs.add("Línea " + numeroLinea + ": La cantidad de estados no es un número válido.");
			return -1;
		}
	}

	private boolean validarEstadoInicial(String linea, int numeroLinea, int numeroEstados) {
		if (linea == null || linea.trim().isEmpty()) {
			errs.add("Línea " + numeroLinea + ": Falta el estado inicial.");
			return false;
		}
		String trimmedLinea = linea.trim();
		if (!trimmedLinea.matches("\\d+")) {
			errs.add("Línea " + numeroLinea + ": El estado inicial debe ser un número entero positivo.");
			return false;
		}

		try {
			int estadoInicial = Integer.parseInt(trimmedLinea);
			if (estadoInicial < 0 || estadoInicial >= numeroEstados) {
				errs.add("Línea " + numeroLinea + ": El estado inicial debe estar entre 0 y " + (numeroEstados - 1)
						+ ".");
				return false;
			}
		} catch (NumberFormatException e) {
			errs.add("Línea " + numeroLinea + ": El estado inicial no es un número válido.");
			return false;
		}
		return true;
	}

	private int validarTransicionesInicio(String linea, int estado, int numeroLinea) {
		if (linea == null || linea.trim().isEmpty()) {
			errs.add("Línea " + numeroLinea + ": Falta la cantidad de transiciones para el estado " + estado + ".");
			return -1;
		}

		// Verificar que la línea contenga solo un número entero positivo
		String trimmedLinea = linea.trim();
		if (!trimmedLinea.matches("\\d+")) {
			errs.add("Línea " + numeroLinea + ": La cantidad de transiciones debe ser un número entero positivo.");
			return -1;
		}

		try {
			int cantidadTransiciones = Integer.parseInt(trimmedLinea);
			if (cantidadTransiciones < 0) {
				errs.add("Línea " + numeroLinea + ": La cantidad de transiciones debe ser mayor o igual a 0.");
				return -1;
			}
			return cantidadTransiciones;
		} catch (NumberFormatException e) {
			errs.add("Línea " + numeroLinea + ": La cantidad de transiciones no es un número válido.");
			return -1;
		}
	}

	private boolean validarTransicion(String linea, int numeroLinea) {
		if (linea == null) {
			errs.add("Línea " + numeroLinea + ": Falta la transición o formato incorrecto.");
			return false;
		}
		String[] partes = linea.split(" ");
		if (partes.length != 4) {
			errs.add("Línea " + numeroLinea + ": La transición debe tener el formato 'X Y R/L N'.");
			return false;
		}

		if (!partes[2].equals("L") && !partes[2].equals("R")) {
			errs.add("Línea " + numeroLinea + ": La transición debe aclarar el desplazamiento en cinta (L,R).");
			return false;
		}
		return true;
	}
}
