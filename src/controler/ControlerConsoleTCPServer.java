package controler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import utilitairesMK.Constantes_SERVER_TCP;
import utilitairesMK.ConsoleMK;
import utilitairesMK.MsgToConsole;
import utilitairesMK.ServeurSocketTCP;
import view.IHM_ConsoleTCP;




/**
 * Classe de gestion de l'application de gestion d'une console distante
 * la console disante est en ecoute sur une socket et affiche les message qu'elle recoit dans 
 * une IHM locale
 * 
 * @author Balou
 *
 */
public class ControlerConsoleTCPServer implements Controler, Constantes_SERVER_TCP {
	
    /**
     *  proprietes pour la gestion des affichages dans la console
     */
    private ConsoleMK console;	// l'objet pour manipuler la console
    private ArrayBlockingQueue<MsgToConsole> msgQ_Console;	// queue de message utilisee pour les envois de messages dans la console
    private ServerSocket socketServer;

    	
	private IHM_ConsoleTCP ihmApplication;

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
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public ControlerConsoleTCPServer() throws IOException, ClassNotFoundException {
		
		Socket soc;
		
		/**
		 * creation de l'instance de l'IHM et affichage de la fenetre principale
		 */
		ihmApplication = new IHM_ConsoleTCP(this);
    	ihmApplication.setVisible(true);	// affichage de l'IHM


    	/* lancement du thread de gestion de la console :
         * On commence par creer la MessageQueue qui va recevoir les messages a afficher dans la console
         */
    	this.msgQ_Console = new ArrayBlockingQueue<MsgToConsole>(TAILLE_BUFFER_CONSOLE);
        this.console = new ConsoleMK("Console", NUM_CONSOLE_TCP, PRIORITE_CONSOLE, msgQ_Console, ihmApplication);
        
        new Thread(console).start();
        
    	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE,  ""));
    	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE, "creation et lancement du thread de console"));
    	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE, "Lancement controleur"));

    	if (VERBOSE_ON)
    		System.out.println("Lancement controleur");
    	
    	try {
			socketServer = new ServerSocket(NUM_PORT_SERVER);
		} catch (Exception e) {
			// TODO Bloc catch g�n�r� automatiquement
			e.printStackTrace();
		}
    	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE, "Lancement du serveur de socket " + socketServer));
    	

    	while(true) {
       		soc = socketServer.accept();
        	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE, "Le serveur : " + soc + "a accepte une connexion avec un client"));
  
          	new Thread(new ServeurSocketTCP(this, soc)).start();

          	/*Thread t =  new Thread(new ServeurSocketTCP(this, soc)).start();
       		t.start();*/

/*        	ServeurSocketTCP serveurSurSocket = new ServeurSocketTCP(this, socketServer, soc);
        	Thread t = new Thread(serveurSurSocket);
 */
    	}
	}
}

