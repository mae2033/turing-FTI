package tm.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import tm.app.AppController;

/* completar: tomar indice y cambios 
*/
@SuppressWarnings("serial")
public class ExecutionFrame extends JFrame {

	private AppController controller;
	private JButton btnExecute;
	private JButton btnBack;

	private JTextField texto;
	private JLabel[] cinta;
	private int TAPE_SIZE = 50;
	private int headPosition = 4;

	public ExecutionFrame() {
		super();

		// Frame config
		setSize(530, 198);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		// Frame Panel
		getContentPane().setLayout(null);

		btnExecute = new JButton("ejecutar");
		btnExecute.setToolTipText("run machine");
		btnExecute.setBounds(244, 121, 125, 27);
		getContentPane().add(btnExecute);
		btnExecute.setFont(new Font("Tahoma", Font.PLAIN, 15));

		btnBack = new JButton("volver");
		btnBack.setToolTipText("return to main");
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnBack.setBounds(379, 121, 125, 27);
		getContentPane().add(btnBack);
		btnExecute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				run(texto.getText());
			}
		});
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.volverInicio();
			}
		});

		texto = new JTextField();
		texto.setToolTipText("input");
		texto.setFont(new Font("Tahoma", Font.PLAIN, 15));
		texto.setLocation(10, 121);
		texto.setSize(224, 27);
		getContentPane().add(texto);

		JPanel cintaPanel = new JPanel();
		cintaPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		cintaPanel.setLayout(new GridLayout(1, TAPE_SIZE));

		JScrollPane scrollPane = new JScrollPane(cintaPanel);
		scrollPane.setToolTipText("\u25B2 blank symbol");
		scrollPane.setBounds(10, 11, 494, 76); // Establecer límites en el JScrollPane

		// Añadir el JScrollPane al contentPane en lugar de al panel
		getContentPane().add(scrollPane);

		cinta = new JLabel[TAPE_SIZE];

		for (int i = 0; i < TAPE_SIZE; i++) {
			cinta[i] = new JLabel("\u25B2", SwingConstants.CENTER);
			cinta[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			cinta[i].setFont(new Font("Arial", Font.PLAIN, 24));
			cintaPanel.add(cinta[i]);
		}
		updateTapeDisplay();

	}

	private void run(String input) {
		try {
			controller.ejecutar(input);
			setText(controller.getResultado());
			actualizarCinta();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
		}
	}

	public void actualizarCinta() {
//	    redimensionarCinta();
	    String s = controller.getResultado();

	    // Llenar los JLabel de la cinta con el resultado
	    for (int i = 0; i < s.length(); i++) {
	        cinta[i + 2].setText(String.valueOf(s.charAt(i)));
	    }
	    
	    // Rellenar los JLabel restantes con el símbolo en blanco
	    for (int i = s.length() + 2; i < cinta.length; i++) {
	        cinta[i].setText("\u25B2");  // Usar setText en lugar de crear un nuevo JLabel
	    }
	}

//	public void redimensionarCinta() {
//	    int resultadoLength = controller.getResultado().length();
//
//	    if (resultadoLength > cinta.length / 2) {
//	        // Duplicar el tamaño de la cinta
//	        cinta = Arrays.copyOf(cinta, cinta.length + 5);
//
//	        // Inicializar los nuevos JLabel en el panel
//	        JPanel cintaPanel = (JPanel) ((JScrollPane) getContentPane().getComponent(3)).getViewport().getView();
//	        cintaPanel.removeAll(); // Limpiar y reconstruir el panel
//
//	        for (int i = 0; i < cinta.length; i++) {
//	            if (cinta[i] == null) { // Solo inicializar los nuevos JLabel
//	                cinta[i] = new JLabel("\u25B2", SwingConstants.CENTER);
//	                cinta[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
//	                cinta[i].setFont(new Font("Arial", Font.PLAIN, 24));
//	            }
//	            cintaPanel.add(cinta[i]);
//	        }
//	        
//	        cintaPanel.revalidate();
//	        cintaPanel.repaint();
//	    }
//	}


	public void cleanText() {

		texto.setText("");
	}

	private void setText(String resultado) {
		texto.setText(resultado);
	}

	public void setTape(String s) {

	}

	public void title(String s) {
		setTitle(s);
	}

	public void setController(AppController controller) {
		this.controller = controller;

	}

	private void updateTapeDisplay() {
		for (int i = 0; i < TAPE_SIZE; i++) {
			if (i == headPosition) {
				cinta[i].setText("[" + cinta[i].getText() + "]");
				cinta[i].setForeground(Color.BLACK);
			} else {
				cinta[i].setText(cinta[i].getText().replace("[", "").replace("]", ""));
				cinta[i].setForeground(Color.BLACK);
			}
		}

	}

}