package tm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Analisa si un archivo cumple con restricciones establecidas para el armado de
 * una Maquina
 */
public class FileValidator {

	private List<String> errs; // Lista de errores encontrados en el archivo

	public boolean validarArchivo(File file) throws IOException {
		errs = new ArrayList<>();
		int[] numeroLinea = { 0 }; // Contador global de líneas
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String linea;

			// Validar nombre de la máquina
			linea = leerLineaValida(reader, numeroLinea);
			if (!validarNombre(linea))
				errs.add("Línea " + numeroLinea[0] + ": Falta el nombre de la máquina o formato incorrecto.");

			// Validar alfabeto
			linea = leerLineaValida(reader, numeroLinea);
			if (!validarAlfabeto(linea))
				errs.add("Línea " + numeroLinea[0] + ": Falta el alfabeto o formato incorrecto.");

			// Validar símbolo de espacio
			linea = leerLineaValida(reader, numeroLinea);
			if (!validarEspacio(linea))
				errs.add("Línea " + numeroLinea[0] + ": El símbolo de espacio debe ser un único carácter.");

			// Validar cantidad de estados
			linea = leerLineaValida(reader, numeroLinea);
			int numeroEstados = validarNumeroEstados(linea, numeroLinea[0]);
			if (numeroEstados == -1)
				errs.add("Línea " + numeroLinea[0] + ": Error en la cantidad de estados.");

			// Validar estado inicial
			linea = leerLineaValida(reader, numeroLinea);
			if (!validarEstadoInicial(linea, numeroLinea[0], numeroEstados))
				errs.add("Línea " + numeroLinea[0] + ": Error en el estado inicial.");

			// Validar transiciones por estado
			for (int estado = 0; estado < numeroEstados; estado++) {
				linea = leerLineaValida(reader, numeroLinea);
				int numeroTransiciones = validarTransicionesInicio(linea, numeroLinea[0], estado);
				if (numeroTransiciones == -1) {
					errs.add(
							"Línea " + numeroLinea[0] + ": Falta la cantidad de transiciones para el estado " + estado);
					continue;
				}

				for (int i = 0; i < numeroTransiciones; i++) {
					linea = leerLineaValida(reader, numeroLinea);
					if (!validarTransicion(linea))
						errs.add("Línea " + numeroLinea[0] + ": Error en la transición " + i + " del estado " + estado
								+ ".");
				}
			}
		}
		return errs.isEmpty();
	}

	private String leerLineaValida(BufferedReader reader, int[] numeroLinea) throws IOException {
		String linea;
		while ((linea = reader.readLine()) != null) {
			numeroLinea[0]++; // Incrementar el contador global
			if (!linea.trim().isEmpty() && !linea.trim().startsWith("//"))
				return linea; // Retorna la primera línea válida encontrada
		}
		return null; // Fin del archivo
	}

	private boolean validarNombre(String linea) {
		return linea != null && linea.startsWith("MAQUINA ");
	}

	private boolean validarAlfabeto(String linea) {
		return linea != null && !linea.trim().isEmpty();
	}

	private boolean validarEspacio(String linea) {
		return linea != null && linea.trim().length() == 1;
	}

	private int validarNumeroEstados(String linea, int numeroLinea) {
		if (linea == null || !linea.trim().matches("\\d+")) {
			errs.add("Línea " + numeroLinea + ": La cantidad de estados debe ser un número entero positivo.");
			return -1;
		}
		int numeroEstados = Integer.parseInt(linea.trim());
		if (numeroEstados <= 0) {
			errs.add("Línea " + numeroLinea + ": La cantidad de estados debe ser mayor a 0.");
			return -1;
		}
		return numeroEstados;
	}

	private boolean validarEstadoInicial(String linea, int numeroLinea, int numeroEstados) {
		if (linea == null || !linea.trim().matches("\\d+"))
			return false;

		int estadoInicial = Integer.parseInt(linea.trim());
		return estadoInicial >= 0 && estadoInicial < numeroEstados;
	}

	private int validarTransicionesInicio(String linea, int numeroLinea, int estado) {
		if (linea == null || !linea.trim().matches("\\d+")) {
			errs.add("Línea " + numeroLinea + ": La cantidad de transiciones para el estado " + estado
					+ " no es válida.");
			return -1;
		}
		return Integer.parseInt(linea.trim());
	}

	private boolean validarTransicion(String linea) {
		if (linea == null)
			return false;

		String[] partes = linea.trim().split("\\s+");
		if (partes.length != 4)
			return false;

		String simboloLee = partes[0];
		String simboloEscribe = partes[1];
		String desplazamiento = partes[2];
		String estadoDestino = partes[3];

		// Validar desplazamiento
		if (!desplazamiento.matches("[LRN]"))
			return false;

		// Validar estado destino
		return estadoDestino.matches("\\d+");
	}

	public List<String> getErrores() {
		return errs;
	}
}
