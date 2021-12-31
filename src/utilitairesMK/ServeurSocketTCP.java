package utilitairesMK;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import controlerServeurCS.ControlerConsoleTCPServer;
import modelMVC.Constantes;
import utilitairesMK_MVC.MessageMK;
import utilitairesMK_MVC.MsgClientServeur;
import utilitairesMK_MVC.MsgToConsole;

public class ServeurSocketTCP implements Constantes_SERVER_TCP, Constantes, Runnable {

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
    	
    	out = new ObjectOutputStream(socketClient.getOutputStream());	// ouverture du stream de sortie
    	in = new ObjectInputStream(socketClient.getInputStream());		// ouverture du stream d'entree
    	
    	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, "Socket server: flux I/O crees => en attente d'un objet\\n"));

    	if (VERBOSE_ON_SERVER_TCP)
    		System.out.println(Thread.currentThread() + "Socket server: en attente d'un objet\n");
    	}

    
    /**
     * "main()" du thread devant traiter un message recu sur la socket du serveur
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    private void gererServerTCP() throws IOException, ClassNotFoundException, InterruptedException {
    	
    	boolean connexionOK = true;
    	
 //       	out.flush();

        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, Thread.currentThread() +" Le serveur a accepte connexion venant du client : " + socketClient));
        	if (VERBOSE_ON_SERVER_TCP)
        		System.out.println(Thread.currentThread() +"Le serveur a accepte la connexion : " + socketClient);
   
        	/**
        	 * boucle de gestion des messages venant du client qui est connecte
        	 * Le msg TYPE_MSG_FIN_CONNEXION permet de mettre fin a la communication
        	 * en le client et le serveur
        	 * 
        	 */
        	while(connexionOK) {
            	
        		// reception d'un msg. En fait il s'agit d'un objet
        		Object msgRecu = in.readObject();

        		switch (traiteMsgToConsole(msgRecu)) {
        			case TYPE_MSG_CONSOLE :
        				break;
        				
        			case TYPE_MSG_CONTROLE :
                    	controleur.getConsole().sendMsgToConsole(
            					new MsgToConsole(NUM_CONSOLE_SYSTEM, Thread.currentThread()
            							+ "\n\tMessage de type TYPE_MSG_CONTROLE"));
        				break;
        				
        			case TYPE_MSG_FIN_CONNEXION :
                    	controleur.getConsole().sendMsgToConsole(
                    					new MsgToConsole(NUM_CONSOLE_SYSTEM, Thread.currentThread()
                    							+ "\n\tMessage de fin de com recu => le thread s'arrete"));
						
                    	connexionOK = false;
        				break;
        				
        			default :
        				if (VERBOSE_ON_SERVER_TCP)
                			System.out.println("ERREUR SUR LE TYPE DE MESSAGE RECU : TYPE INCONNU!!!!!");
                    	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, Thread.currentThread() + "ERREUR SUR LE TYPE DE MESSAGE RECU !!!!!"));
                    	break;
        		}
        	}
        	      	
        	      	
        	// on envoie un msg au client pour acquitter le fait que nous avons bien mis fin a la communication
        	out.writeObject(new MsgToConsole(2, false, Thread.currentThread() + " MsgServeur : OK pour la fermeture de la connexion avec le serveur"));
            out.flush();
            
        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, 
        											Thread.currentThread()
        											+ " Serveur : message envoye\nServeur : fermeture de la socket et arret du serveur\n"
        											));
        	if (VERBOSE_ON_SERVER_TCP) {
                System.out.println(Thread.currentThread()
                					+ " Serveur : fermeture de la socket et arret du serveur\n"
                					+ " Serveur : message envoye\n"
                					);
        	}
        	
        	// fermeture des streams de communication avec le client
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
			gererServerTCP();	// gestion de la connexion et des messages recus
		} catch (ClassNotFoundException e) {
			System.out.println("ERREUR DE CLASSE");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Bloc catch g�n�r� automatiquement
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Bloc catch g�n�r� automatiquement
			e.printStackTrace();
		}		
	}
    
    
    
    /**
     * Traitement du message recu : on appelle la bonne methode en fonction du type d'objet recu
     * 
     */
    private int traiteMsgToConsole(Object msg) {
    	if (msg instanceof MsgToConsole) {
    		return (traiteMsgToConsole((MsgToConsole)msg));
    	}
       	if (msg instanceof MsgClientServeur) {
    		return (traiteMsgToConsole((MsgClientServeur)msg));       		
       	}
    	else return TYPE_MSG_INCONNU;	// type de message non traite
    }
    
    
    /**
     * methode de gestion des messages destines a la console systeme
     * 
     * @param msg
     */
    private int traiteMsgToConsole(MsgToConsole msg) {
    	if (VERBOSE_ON_SERVER_TCP) {
    		System.out.println("Message de type MsgToConsole re�u"
    							+ "Serveur : message recu = " + ((MsgToConsole)msg).getMsg()
    							);
    	}
    	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_TCP, Thread.currentThread()
    											+ " Serveur : message recu = "
    											+ ((MsgToConsole)msg).getMsg()));

    	return TYPE_MSG_CONSOLE;
    }
    
    
    /**
     *methode utilise pour taiter les messages de controle
     * 
     * @param msg
     */
    private int traiteMsgToConsole(MsgClientServeur msg) {
    	
    	if (VERBOSE_ON_SERVER_TCP) {
    		System.out.println("Message de type MsgClientServeur re�u");
        	System.out.println("Serveur : message recu = " + ((MsgClientServeur)msg).getLibelleMsg());
    	}

    	controleur.getConsole().sendMsgToConsole(
				new MsgToConsole(NUM_CONSOLE_SYSTEM,
								Thread.currentThread()
								+ " : Message syst�me re�u et contenant ce message txt = "
								+ (((MsgToConsole)((MsgClientServeur)msg).getObj())).getMsg())
								);
    	
    	return msg.getTypeMsg();
    }

}
