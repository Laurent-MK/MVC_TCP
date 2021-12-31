package utilitairesMK;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import controlerServeurCS.ControlerConsoleTCPServer;
import modelMVC.Constantes;
import utilitairesMK_MVC.MsgClientServeur;
import utilitairesMK_MVC.MsgToConsole;

public class ServeurSocketTCP implements Constantes_SERVER_TCP, Constantes, Runnable {

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
 //       	out.flush();

        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE, Thread.currentThread() + " Serveur a cree les flux"));
        	System.out.println(Thread.currentThread() +" Serveur a cree les flux");
        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE, Thread.currentThread() +" Le serveur a accepte connexion venant du client : " + socketClient));
        	System.out.println(Thread.currentThread() + " Serveur a accepte connexion: " + socketClient);
   
        	while(true) {
            	
        		Object msgRecu = in.readObject();
            	
            	if (msgRecu instanceof MsgToConsole) {
            		traiteMsgToConsole((MsgToConsole)msgRecu);
            		continue;
              	}
            	else {
            		System.out.println("ERREUR SUR LE TYPE DE MESSAGE RECU !!!!!");
            	}
            	
//            	Object msgRecu2 = in.readObject();
            	if (msgRecu instanceof MsgClientServeur) {
            		int retour = traiteMsgCS((MsgClientServeur) msgRecu);
            		
            		if (retour == 666) {
            			controleur.getConsole().sendMsgToConsole(
            				new MsgToConsole(NUM_CONSOLE_TCP, !AJOUTER_NUM_MESSAGE, Thread.currentThread() + " : Message de fin de com recu => le thread s'arrete"));
            			
            			controleur.getConsole().sendMsgToConsole(
                				new MsgToConsole(NUM_CONSOLE_TCP, !AJOUTER_NUM_MESSAGE,
                						Thread.currentThread() + " : Message contenu dans le message = " 
                				+ (((MsgToConsole)((MsgClientServeur)msgRecu).getObj())).getMsg()));
            			break;            			
            		}
            	}
            	else {
            		System.out.println("ERREUR SUR LE TYPE DE MESSAGE RECU !!!!!");
                	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_TCP, !AJOUTER_NUM_MESSAGE, Thread.currentThread() + "ERREUR SUR LE TYPE DE MESSAGE RECU !!!!!"));
            	}        		
        	}
        	      	
        	out.writeObject(new MsgToConsole(2, false, Thread.currentThread() + " message venant du serveur"));
            out.flush();
            
            System.out.println(Thread.currentThread() + " Serveur : message envoye\n");
        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE, Thread.currentThread() + " Serveur : message envoye\n"));
        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE, Thread.currentThread() + " Serveur : fermeture de la socket et arret du serveur\n"));
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
			System.out.println("ERREUR DE CLASSE");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}		
	}
    
    /**
     * methode de gestion des messages destines a la console systeme
     * 
     * @param msg
     */
    private void traiteMsgToConsole(MsgToConsole msg) {
		System.out.println("Message de type MsgToConsole reçu");
    	System.out.println("Serveur : message recu = " + ((MsgToConsole)msg).getMsg());
    	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_TCP, !AJOUTER_NUM_MESSAGE, Thread.currentThread() + " Serveur : message recu = " + ((MsgToConsole)msg).getMsg()));
    }
    

    /**
     *methode utilise pour taiter les messages de controle
     * 
     * @param msg
     */
    private int traiteMsgCS(MsgClientServeur msg) {
    	
		System.out.println("Message de type MsgClientServeur reçu");
    	System.out.println("Serveur : message recu = " + ((MsgClientServeur)msg).getLibelleMsg());
    	controleur.getConsole().sendMsgToConsole(
    			new MsgToConsole(NUM_CONSOLE_TCP, !AJOUTER_NUM_MESSAGE, Thread.currentThread() 
    							+ " Serveur : message recu ayant le libelle = "
    							+ ((MsgClientServeur)msg).getLibelleMsg()));        		
    	
    	return msg.getTypeMsg();
    }
    
}
