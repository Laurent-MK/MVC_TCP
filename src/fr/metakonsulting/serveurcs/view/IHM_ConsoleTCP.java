package fr.metakonsulting.serveurcs.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fr.metakonsulting.mvc.controler.Controler;
import fr.metakonsulting.mvc.utilitaires.MsgToConsole;
import fr.metakonsulting.mvc.view.IHM;
import fr.metakonsulting.serveurcs.controler.ControlerConsoleTCPServer;
import fr.metakonsulting.serveurcs.utilitaires.Constantes_SERVER_TCP;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;

public class IHM_ConsoleTCP extends JFrame implements IHM, Constantes_SERVER_TCP {

	/**
	 * Classe de gestion de l'IHM du server de socket TCP
	 * 
	 */
	private static final long serialVersionUID = 7894248121929711049L;
	
	private JPanel contentPane;
	private ControlerConsoleTCPServer controleur;
	private JTextArea textAreaCentre = new JTextArea();
	private JTextArea textAreaSud = new JTextArea();
	private JProgressBar progressBarConsoleSud = new JProgressBar(0, MAX_MSG_CONSOLE_SUD);
	private JProgressBar progressBarConsoleCentre = new JProgressBar(0, MAX_MSG_CONSOLE_CENTRE);
	
	private int tailleZoneAffichageConsole = VALEUR_NUM_NON_DEFINIE;

  	private boolean VERBOSE_LOCAL = VERBOSE_ON_SERVER_TCP & false;
  	
	/**
	 * methode d'initialisation de l'IHM
	 */
	@Override
	public void initIHM() {
		textAreaCentre.append("\n");
		textAreaSud.append("\n");
		
		progressBarConsoleSud.setForeground(Color.GREEN);	
		progressBarConsoleSud.setForeground(Color.GREEN);	// la progressBar est en gris au début

		progressBarConsoleCentre.setBackground(Color.WHITE);
		progressBarConsoleCentre.setForeground(Color.GREEN);	
	}


	/**
	 * methode utlisee pour afficher un message soit dans la console du centre
	 * soit dans la console du sud
	 */
	@Override
	public void affichageConsole(MsgToConsole msgConsole) {

		if (textAreaCentre.getLineCount() > MAX_MSG_CONSOLE_CENTRE)
			textAreaCentre.setText("");
		progressBarConsoleCentre.setValue(textAreaCentre.getLineCount());

		if (textAreaSud.getLineCount() > MAX_MSG_CONSOLE_SUD)
			textAreaSud.setText("");
		progressBarConsoleSud.setValue(textAreaSud.getLineCount());

		if (msgConsole.getNumConsoleDest() == NUM_CONSOLE_TCP)
			textAreaCentre.append(msgConsole.getMsg());
		else if (msgConsole.getNumConsoleDest() == NUM_CONSOLE_SYSTEM)
			textAreaSud.append(msgConsole.getMsg());			
		
	}

	@Override
	public void affichageRemplissageMQ_Console(int nbVal) {
		// TODO Stub de la m�thode g�n�r� automatiquement
		
	}

	
	/**
	 * obtenir la taille reservee pour l'affichage des messages dans la console
	 */
	public int getTailleBufferConsole() {
		return tailleZoneAffichageConsole;
	}

	/**
	 * Creation de la fenetre.
	 */
	public IHM_ConsoleTCP(ControlerConsoleTCPServer controleur) {

		this.controleur = controleur;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1120, 1064);
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
		scrollPaneSUD.setPreferredSize(new Dimension(5, 350));
		contentPane.add(scrollPaneSUD, BorderLayout.SOUTH);
		scrollPaneSUD.setViewportView(textAreaSud);
		textAreaSud.setText("on est au SUD");
		
		scrollPaneSUD.setColumnHeaderView(progressBarConsoleSud);
		textAreaCentre.append("\n");

		
		/**
		 * zone CENTER
		 */
		JScrollPane scrollPaneCENTER = new JScrollPane();
		contentPane.add(scrollPaneCENTER, BorderLayout.CENTER);
		
		scrollPaneCENTER.setViewportView(textAreaCentre);
		textAreaCentre.setText("On est au centre");
		
		progressBarConsoleCentre.setOrientation(SwingConstants.VERTICAL);
		scrollPaneCENTER.setRowHeaderView(progressBarConsoleCentre);
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
		
		/**
		 * initialisation de l'IHM
		 */
		initIHM();
	}

	public ControlerConsoleTCPServer getControleur() {
		return controleur;
	}

	@Override
	public Controler getControler() {
		// TODO Stub de la méthode généré automatiquement
		return null;
	}

}
