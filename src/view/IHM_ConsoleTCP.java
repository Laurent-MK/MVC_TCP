package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import utilitairesMK.MsgToConsole;

public class IHM_ConsoleTCP extends JFrame implements IHM {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IHM_ConsoleTCP frame = new IHM_ConsoleTCP();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
*/
	
	/**
	 * Create the frame.
	 */
	public IHM_ConsoleTCP() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

	
	
	@Override
	public void initIHM() {
		// TODO Stub de la méthode généré automatiquement
		
	}

	@Override
	public void affichageConsole(MsgToConsole msgConsole) {
		// TODO Stub de la méthode généré automatiquement
		
	}

	@Override
	public void affichageRemplissageMQ_Console(int nbVal) {
		// TODO Stub de la méthode généré automatiquement
		
	}

}
