package controler;

import java.util.concurrent.ArrayBlockingQueue;

import model.Constantes;
import utilitairesMK.ConsoleMK;
import utilitairesMK.MsgToConsole;
import view.IHM_ConsoleTCP;

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
		// TODO Stub de la methode generee automatiquement
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
        
    	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, "creation et lancement du thread de console"));
    	console.sendMsgToConsole((new MsgToConsole(NUM_CONSOLE_SYSTEM, "Message de debug")));
    	console.sendMsgToConsole((new MsgToConsole(NUM_CONSOLE_SYSTEM, "Lancement controleur")));

    	if (VERBOSE_ON)
    		System.out.println("Lancement controleur");

        /***
         * boucle de surveillance des threads et affichage de leur etat dans l'IHM
         */
        while(true) {

        	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_TCP, "ok, ca fonctionne"));
        	
            try {
				Thread.sleep(100);	// on s'endort durant un certain temps
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
	}
}
