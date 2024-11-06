package tm.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import tm.model.Maquina;

/* completar:
 * resultado inicial: vacio 
 * toma el resultado de la ejecucion
 * lo manda a la maquina, repite cada presion ejecutar
*/
@SuppressWarnings("serial")
public class ExecutionFrame extends JFrame {
	String resultado = "";
	JButton btnExecute;
	JButton btnBack;
	Maquina aMaquina;
	JTextPane jtpCalculo;
	boolean taken = false;

	public ExecutionFrame(Maquina aMaquina) {
		super();
		this.aMaquina = aMaquina;

		setTitle(aMaquina.getName());

		// Frame config
		setSize(450, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		// Frame Panel
		getContentPane().setLayout(null);

		btnExecute = new JButton("ejecutar");
		btnExecute.setBounds(10, 11, 125, 27);
		getContentPane().add(btnExecute);
		btnExecute.setFont(new Font("Tahoma", Font.PLAIN, 15));

		btnBack = new JButton("volver a inicio");
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnBack.setBounds(146, 11, 125, 27);
		getContentPane().add(btnBack);

		jtpCalculo = new JTextPane();
		jtpCalculo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		jtpCalculo.setEditable(false);
		jtpCalculo.setBounds(24, 48, 42, 275);
		getContentPane().add(jtpCalculo);
		btnExecute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				run(resultado);
			}
		});

		JScrollPane scrollPane = new JScrollPane(jtpCalculo);
		scrollPane.setBounds(10, 66, 414, 273);
		getContentPane().add(scrollPane);

		setVisible(true);

	}

	private void run(String input) {
		if (taken) // completar
			aMaquina.interrupt();
		taken = true;
		aMaquina.setTape(resultado);
		try {
			aMaquina.runTuring(6);
			setResultado(aMaquina.getResult());
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
		}
		taken = false;
	}

	public String setResultado(String s) {
		return resultado = s;
	}
}