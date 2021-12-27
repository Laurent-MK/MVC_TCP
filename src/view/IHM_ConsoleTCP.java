package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controler.ControlerConsoleTCPServer;
import model.Constantes;
import utilitairesMK.MsgToConsole;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.SwingConstants;

public class IHM_ConsoleTCP extends JFrame implements IHM, Constantes {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7894248121929711049L;
	
	private JPanel contentPane;
	private ControlerConsoleTCPServer controleur;
	private JTextArea textAreaCentre = new JTextArea();
	private JTextArea textAreaSud = new JTextArea();
	private int tailleZoneAffichageConsole = VALEUR_NUM_NON_DEFINIE;


	
	@Override
	public void initIHM() {
		textAreaCentre.append("\n");
		textAreaSud.append("\n");
	}

	@Override
	public void affichageConsole(MsgToConsole msgConsole) {
		
		if (msgConsole.getNumConsoleDest() == NUM_CONSOLE_TCP)
			textAreaCentre.append(msgConsole.getMsg());
		else if (msgConsole.getNumConsoleDest() == NUM_CONSOLE_SYSTEM)
			textAreaSud.append(msgConsole.getMsg());			
		
	}

	@Override
	public void affichageRemplissageMQ_Console(int nbVal) {
		// TODO Stub de la méthode généré automatiquement
		
	}

	
	/**
	 * obtenir la taille reservee pour l'affichage des messages dans la console
	 */
	public int getTailleBufferConsole() {
		return tailleZoneAffichageConsole;
	}

	/**
	 * Create the frame.
	 */
	public IHM_ConsoleTCP(ControlerConsoleTCPServer controleur) {

		this.controleur = controleur;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 928, 851);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
		/**
		 * Zone NORD
		 */
		JPanel panelNORD = new JPanel();
		contentPane.add(panelNORD, BorderLayout.NORTH);
		
		JLabel lblNewLabelNord= new JLabel("CONSOLE TCP");
		panelNORD.add(lblNewLabelNord);
		
		JButton btnNewButton = new JButton("btn Nord");
		panelNORD.add(btnNewButton);

		
		/**
		 * Zone SUD
		 */
		JScrollPane scrollPaneSUD = new JScrollPane();
		scrollPaneSUD.setPreferredSize(new Dimension(5, 150));
		contentPane.add(scrollPaneSUD, BorderLayout.SOUTH);
		scrollPaneSUD.setViewportView(textAreaSud);
		textAreaSud.setText("on est au SUD");
		textAreaCentre.append("\n");

		
		/**
		 * zone CENTER
		 */
		JScrollPane scrollPaneCENTER = new JScrollPane();
		contentPane.add(scrollPaneCENTER, BorderLayout.CENTER);
		
		scrollPaneCENTER.setViewportView(textAreaCentre);
		textAreaCentre.setText("On est au centre");
		textAreaCentre.append("\n");
		
		/**
		 * zone EAST
		 */
		JPanel panelEAST = new JPanel();
		contentPane.add(panelEAST, BorderLayout.EAST);

		JLabel lblNewLabelEast = new JLabel("zone EAST");
		panelEAST.add(lblNewLabelEast);

		JButton btnNewButtonEast = new JButton("btn EST");
		panelEAST.add(btnNewButtonEast);


		/**
		 * zone WEST
		 */
		JPanel panelWEST = new JPanel();
		contentPane.add(panelWEST, BorderLayout.WEST);

		JLabel lblNewLabelWest = new JLabel("zone WEST");
		lblNewLabelWest.setVerticalTextPosition(SwingConstants.TOP);
		panelWEST.add(lblNewLabelWest);

	}

}
