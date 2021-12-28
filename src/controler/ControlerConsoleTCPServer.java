package controler;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import model.Constantes;
import utilitairesMK.ConsoleMK;
import utilitairesMK.MsgToConsole;
import utilitairesMK.ServeurSocket;
import view.IHM_ConsoleTCP;




/**
 * Classe de gestion de l'application de gestion d'une console distante
 * la console disante est en ecoute sur une socket et affiche les message qu'elle recoit dans 
 * une IHM locale
 * 
 * @author Balou
 *
 */
public class ControlerConsoleTCPServer implements Controler, Constantes {
	
    /**
     *  proprietes pour la gestion des affichages dans la console
     */
    private ConsoleMK console;	// l'objet pour manipuler la console
    private ArrayBlockingQueue<MsgToConsole> msgQ_Console;	// queue de message utilisee pour les envois de messages dans la console

    	
	private IHM_ConsoleTCP ihmApplication;

	public static void main(String[] args) throws InterruptedException {
		new ControlerConsoleTCPServer();	
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
	 */
	public ControlerConsoleTCPServer() {
		
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
			ServeurSocket ServeurSoc = new ServeurSocket();
		} catch (ClassNotFoundException e1) {
			// TODO Bloc catch généré automatiquement
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Bloc catch généré automatiquement
			e1.printStackTrace();
		}
    	
        /***
         * boucle principale de l'application : on attend de recevoir un message dans la socket
         * quand un msg arrive, on l'envoit vers l'IHM pour qu'elle l'affiche
         */
        while(true) {

        	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_TCP, AJOUTER_NUM_MESSAGE,  "ok, ca fonctionne"));
        	
            try {
				Thread.sleep(1000);	// on s'endort durant un certain temps
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
	}
}
