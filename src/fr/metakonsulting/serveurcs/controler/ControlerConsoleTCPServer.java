package fr.metakonsulting.serveurcs.controler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import fr.metakonsulting.mvc.controler.Controler;
import fr.metakonsulting.mvc.model.Constantes;
import fr.metakonsulting.mvc.utilitaires.ConsoleMK;
import fr.metakonsulting.mvc.utilitaires.MsgToConsole;
import fr.metakonsulting.serveurcs.utilitaires.Constantes_SERVER_TCP;
import fr.metakonsulting.serveurcs.utilitaires.ServeurSocketTCP;
import fr.metakonsulting.serveurcs.view.IHM_ConsoleTCP;




/**
 * META KONSULTING
 * 
 * Classe de gestion de l'application de gestion d'une console distante
 * la console disante est en ecoute sur une socket et affiche les message qu'elle recoit dans 
 * une IHM locale
 * 
 * @author Balou
 *
 */
public class ControlerConsoleTCPServer implements Controler, Constantes_SERVER_TCP, Constantes {
	
    /**
     *  proprietes pour la gestion des affichages dans la console
     */
    private ConsoleMK console;	// l'objet pour manipuler la console
    private ArrayBlockingQueue<MsgToConsole> msgQ_Console;	// queue de message utilisee pour les envois de messages dans la console
    private ServerSocket socketServer;

	private IHM_ConsoleTCP ihmApplication;

 	private boolean VERBOSE_LOCAL = VERBOSE_ON_SERVER_TCP & true;

	
	
	/**
	 * main de la classe gerant le serveur TCP
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		try {
			new ControlerConsoleTCPServer();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Bloc catch g�n�r� automatiquement
			e.printStackTrace();
		}	
	}
	

	/**
	 * methode pour obtenir la reference pointant sur l'objet de console
	 */
	@Override
	public ConsoleMK getConsole() {
		return this.console;
	}
	
	
	
	
	/**
	 * Constructeur de la classe
	 * 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public ControlerConsoleTCPServer() throws IOException, ClassNotFoundException {
		
		Socket soc;
		
		/**
		 * creation de l'instance de l'IHM et affichage de la fenetre principale
		 */
		ihmApplication = new IHM_ConsoleTCP(this);	// creation de l'IHM
    	ihmApplication.setVisible(true);			// affichage de l'IHM


    	/* lancement du thread de gestion de la console :
         * On commence par creer la MessageQueue qui va recevoir les messages a afficher dans la console (soit celle du nord soit celle du sud)
         * puis on cree l'objet de gestion de la console et enfin, on lance le thread qui abrite l'objet console
         * 
         * La console du nord est celle dans laquelle les messages affiches sont ceux qui sont egalement affiches dans la console locale du client
         * distant
         * La console sud est la console systeme locale à l'application de console distante.
         */
    	this.msgQ_Console = new ArrayBlockingQueue<MsgToConsole>(TAILLE_BUFFER_CONSOLE);
        this.console = new ConsoleMK("Console", NUM_CONSOLE_TCP, PRIORITE_CONSOLE_SERVER_TCP, msgQ_Console, ihmApplication);        
        new Thread(console).start();
        
    	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM,""));
    	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, Thread.currentThread() + " : creation et lancement du thread de console"));
    	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, Thread.currentThread() + " : Lancement controleur"));

    	if (VERBOSE_LOCAL)
    		System.out.println("Lancement controleur");
    	
    	try {
			socketServer = new ServerSocket(NUM_PORT_SERVER);	// creation de la socket du serveur. on se met en ecoute sur le port indique
		} catch (Exception e) {
			// TODO Bloc catch g�n�r� automatiquement
			e.printStackTrace();
		}
    	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, Thread.currentThread() + " : Lancement du serveur de socket " + socketServer));
    	

    	/**
    	 * boucle de traitement des demandes de connexion arrivant sur la socket du serveur
    	 * pour chaque connexion, on lance un thread qui va gerer cette communication
    	 * 
    	 */
    	while(true) {
    		// on attend une demande de connexion venant d'un client. Des reception d'une connexion, on lance un thread
    		// pour gerer la communication client <-> serveur TCP
       		soc = socketServer.accept();
       		
       		// si une connexion se fait, on lance un thread de gestion de cette connexion client/serveur
        	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, Thread.currentThread() + " : Le serveur : " + soc + "a accepte une connexion avec un client"));
        	if (VERBOSE_LOCAL)
        		System.out.println(Thread.currentThread() + " : une connexion vient d'etre accetee => on cree un Thread pour la gerer");

        	new Thread(new ServeurSocketTCP(this, soc)).start();
    	}
	}
}

