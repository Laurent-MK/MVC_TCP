package utilitairesMK;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import controler.ControlerConsoleTCPServer;
import model.Constantes;


public class ServeurSocketTCP implements Constantes, Runnable {

//    static final int port = NUM_PORT_SERVER;
    private ControlerConsoleTCPServer controleur;
    private Socket socketClient;
   	private ObjectOutputStream out;
  	private ObjectInputStream in;
    
    
    /**
     * constructeur de la classe de serveur de socket
     * 
  	 * @param controleur
  	 * @param socketClient
  	 * @throws IOException
  	 * @throws ClassNotFoundException
  	 */
    public ServeurSocketTCP(ControlerConsoleTCPServer controleur, Socket socketClient) throws IOException, ClassNotFoundException {
    	this.controleur = controleur;
    	this.socketClient = socketClient;
    	
    	out = new ObjectOutputStream(socketClient.getOutputStream());
    	in = new ObjectInputStream(socketClient.getInputStream());
    	
    	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE,  "Socket serveur: "));
    	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE,  "en attente d'un objet\n"));

    	if (VERBOSE_ON) {
    		System.out.println(Thread.currentThread() + "Socket serveur: ");
            System.out.println("en attente d'un objet\n");
            }
    	}

    
    /**
     * "main()" du thread devant traiter un message recu sur la socket du serveur
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    private void gererServerTCP() throws IOException, ClassNotFoundException, InterruptedException {
        	out.flush();

        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE,  Thread.currentThread() + " Serveur a cree les flux"));
        	System.out.println(Thread.currentThread() +" Serveur a cree les flux");

        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE,  Thread.currentThread() +" Le serveur a accepte connexion venant du client : " + socketClient));
        	System.out.println(Thread.currentThread() + " Serveur a accepte connexion: " + socketClient);
     
            MsgToConsole objetRecu = (MsgToConsole) in.readObject();

            System.out.println("Serveur : message recu = " + objetRecu.getMsg());
        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_TCP, !AJOUTER_NUM_MESSAGE,  Thread.currentThread() + " Serveur : message recu = " + objetRecu.getMsg()));

        	out.writeObject(new MsgToConsole(2, false, Thread.currentThread() + " message venant du serveur"));
            out.flush();
            System.out.println(Thread.currentThread() + " Serveur : message envoye\n");
        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE,  Thread.currentThread() + " Serveur : message envoye\n"));

//        	Thread.sleep(2000);
        	
        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE,  Thread.currentThread() + " Serveur : fermeture de la socket et arret du serveur\n"));
            System.out.println(Thread.currentThread() + " Serveur : fermeture de la socket et arret du serveur\n");
            in.close();
            out.close();
    }



    /**
     * Methode "main()" du thread
     * 
     */
    @Override
	public void run() {
		try {
			gererServerTCP();
		} catch (ClassNotFoundException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}		
	}
}
