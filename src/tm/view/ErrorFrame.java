package tm.view;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;

@SuppressWarnings("serial")
public class ErrorFrame extends JFrame {

	private JTextArea errorTextArea;

	public ErrorFrame() {
		setTitle("Error");
		setSize(400, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initComponents();
	}

	private void initComponents() {
		errorTextArea = new JTextArea();
		errorTextArea.setFont(new Font("Tahoma", Font.PLAIN, 18));
		errorTextArea.setEditable(false);
		errorTextArea.setLineWrap(true);
		errorTextArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(errorTextArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JButton closeButton = new JButton("Cerrar");
		closeButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		closeButton.addActionListener(e -> dispose());

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(closeButton);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * Muestra el mensaje de error.
	 * 
	 * @param error El mensaje de error (puede ser String, List, Map, etc.)
	 */
	public void showError(Object error) {
		String errorMessage = parseError(error);
		errorTextArea.setText(errorMessage);
		setVisible(true);
	}

	/**
	 * Convierte cualquier tipo de dato en un mensaje de error legible.
	 * 
	 * @param error El objeto de error.
	 * @return Un String representando el error.
	 */
	private String parseError(Object error) {
		if (error instanceof String) {
			return (String) error;
		} else if (error instanceof Throwable) {
			return getStackTrace((Throwable) error);
		} else if (error instanceof Map) {
			StringBuilder sb = new StringBuilder("Detalles del error:\n");
			((Map<?, ?>) error).forEach((key, value) -> sb.append(key).append(": ").append(value).append("\n"));
			return sb.toString();
		} else if (error instanceof Iterable) {
			StringBuilder sb = new StringBuilder("Lista de errores:\n");
			for (Object item : (Iterable<?>) error) {
				sb.append("- ").append(item).append("\n");
			}
			return sb.toString();
		} else {
			return "Error desconocido:\n" + error.toString();
		}
	}

	/**
	 * Obtiene el stack trace de una excepción como String.
	 * 
	 * @param throwable La excepción.
	 * @return El stack trace.
	 */
	private String getStackTrace(Throwable throwable) {
		StringBuilder sb = new StringBuilder(throwable.toString()).append("\n");
		for (StackTraceElement element : throwable.getStackTrace()) {
			sb.append("\tat ").append(element).append("\n");
		}
		return sb.toString();
	}

//	public static void main(String[] args) {
//		// Ejemplo de uso
//		ErrorFrame errorFrame = new ErrorFrame();
//
//		// Muestra un error simple
//		errorFrame.showError("Error inesperado!");
//
//		// Muestra una excepción
//		errorFrame.showError(new Exception("Archivo no encontrado"));
//
//		// Muestra un mapa
//		Map<String, String> errorMap = Map.of("Código", "404", "Mensaje", "No encontrado");
//		errorFrame.showError(errorMap);
//
//		// Muestra una lista de errores
//		List<String> errorList = List.of("Error 1", "Error 2", "Error 3");
//		errorFrame.showError(errorList);
//	}
}
