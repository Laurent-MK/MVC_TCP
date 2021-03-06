package fr.metakonsulting.serveurcs.utilitaires;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import fr.metakonsulting.mvc.model.Constantes;
import fr.metakonsulting.mvc.utilitaires.MsgDeControle;
import fr.metakonsulting.mvc.utilitaires.MsgToConsole;
import fr.metakonsulting.mvc.utilitaires.TypeMsgCS;
import fr.metakonsulting.mvc.view.IHM_SERIALISABLE;
import fr.metakonsulting.serveurcs.controler.ControlerConsoleTCPServer;

public class ServeurSocketTCP implements Constantes_SERVER_TCP, Constantes, Runnable {

    private ControlerConsoleTCPServer controleur;
    private Socket socketServeur;
   	private ObjectOutputStream out;
  	private ObjectInputStream in;
  	
  	private boolean VERBOSE_LOCAL = VERBOSE_ON_SERVER_TCP & true;

    
    
    /**
     * constructeur de la classe de serveur de socket
     * 
  	 * @param controleur
  	 * @param socketClient
  	 * @throws IOException
  	 * @throws ClassNotFoundException
  	 */
    public ServeurSocketTCP(ControlerConsoleTCPServer controleur, Socket socket) throws IOException {
    	this.controleur = controleur;	// le controleur de l'application serveur TCP
    	this.socketServeur = socket;	// la socket pour communiquer avec le client TCP
    	  
    	out = new ObjectOutputStream(this.socketServeur.getOutputStream());		// ouverture du stream de sortie
    	in = new ObjectInputStream(this.socketServeur.getInputStream());		// ouverture du stream d'entree
    	
    	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, Thread.currentThread() + "  ==> Serveur : flux I/O crees => en attente d'un objet\\n"));
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
       	out.flush();

        controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, Thread.currentThread() + "  ==> Serveur : a accepte connexion venant du client : " + socketServeur));
        if (VERBOSE_LOCAL)
        	System.out.println(Thread.currentThread() + " ==> Serveur : a accepte la connexion : " + socketServeur);
   
    	/**
    	 * boucle de gestion des messages venant du client qui est connecte
    	 * Le msg TYPE_MSG_FIN_CONNEXION permet de mettre fin a la communication
    	 * en le client et le serveur
    	 * 
    	 */
    	while(connexionOK) {
        	
    		// reception d'un msg via la socket TCP
    		Object msgRecu = in.readObject();

    		/**
    		 * reception et traitement des messages recus venant du client
    		 */
    		switch (traiteMsgRecu(msgRecu)) {

    			/**
    			 * traitement des messages destines a la console deportee (celle qui est une copie de la console
    			 * locale du client).
    			 */
    			case MSG_CONSOLE :
    		    	if (VERBOSE_LOCAL)
    		    		System.out.println(Thread.currentThread() + " ==> Serveur : Message de type MSG_CONSOLE recu et transmis a la console");
    				break;

    				
    			/**
    			 * messages lies au protocole de communication entre le client et le sevreur
    			 */
    			case MSG_CONTROLE :
                	controleur.getConsole().sendMsgToConsole(
        					new MsgToConsole(NUM_CONSOLE_SYSTEM, Thread.currentThread()
        							+ "\n\tMessage de type MSG_CONTROLE"));
    				break;


    			/**
    			 * message de test de la liaison entre le client et le serveur
    			 * On recoit le msg puis on retourne un msg de test vers le client
    			 */
    			case MSG_TEST_LINK :       				
                	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM,
                																Thread.currentThread()
					                    										+ "\n\tMessage de type MSG_TEST_LINK recu par le serveur : "
					                    										+ ((MsgDeControle)msgRecu).getLibelleMsg()
					                    										)
                											);
                	
                	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_TCP,
                																Thread.currentThread()
																				+ " Serveur : message recu = "
																				+ ((MsgDeControle)msgRecu).getLibelleMsg()
																				)
                											);

                	// envoi d'un message d'acquittement du message de test de liaison
                	out.writeObject(new MsgDeControle(TypeMsgCS.MSG_TEST_LINK, NUM_MSG_NOT_USED, MSG_ACQ_TEST, null));
                    out.flush();
    				break;


    			/**
    			 * message indiquant au serveur que le client va rompre la communication
    			 */
    			case MSG_FIN_CONNEXION :
                	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM,
                											Thread.currentThread()
                											+ "\n\tMessage de type MSG_FIN_CONNEXION recu => le thread va s'arreter"));

                	connexionOK = false;	// pour sortir de la boucle de reception des messages venant du client
    				break;

    				
    			/**
    			 * cas de la reception d'une IHM : on la recoit puis on l'affiche sur l'???cran
    			 * 
    			 */
    			case MSG_TRF_IHM :
    		    	if (VERBOSE_LOCAL)
            			System.out.println(Thread.currentThread() + " ==> Serveur : Objet IHM recu");

    				IHM_SERIALISABLE ihm = (IHM_SERIALISABLE)msgRecu;
    				ihm.setVisible(true);
    				ihm.setLocation(0, 0);
    				break;
    				      
    				
    			/**
    			* reception d'un objet passe directement dans la socket
    			*/
    			case MSG_TRF_OBJET :
    		    	if (VERBOSE_LOCAL)
            			System.out.println(Thread.currentThread() + " ==> Serveur : Objet de type inconnu recu");
  				break;

    				
    			case MSG_INCONNU :
    				/**
    				 * on a recu un message qui n'est pas connu par le protocole
    				 * Il s'agit donc d'un objet inconnu contenu dans "msgRecu".
    				 */
    		    	if (VERBOSE_LOCAL)
            			System.out.println(Thread.currentThread() + " ==> Serveur : Objet de type inconnu recu");
                	break;

    			
    			default :
    		    	if (VERBOSE_LOCAL)
            			System.out.println(Thread.currentThread() + " ==> Serveur : ERREUR SUR LE TYPE DE MESSAGE RECU : TYPE INCONNU!!!!!");
                	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, Thread.currentThread() + "ERREUR SUR LE TYPE DE MESSAGE RECU !!!!!"));
    				break;
    		}
    	}
    	
    	// fermeture des streams de communication avec le client
    	out.flush();
    	in.close();
        out.close();
        socketServeur.close();

    	if (VERBOSE_LOCAL) {
            System.out.println(Thread.currentThread() + " ==> Serveur : socket fermee et arret du serveur\n");
    	}           
    }



    /**
     * Methode "main()" du thread
     * 
     */
    @Override
	public void run() {
		try {
	    	if (VERBOSE_LOCAL)
	    		System.out.println(Thread.currentThread() + " ==> Serveur : en attente d'un objet\n");

			gererServerTCP();	// gestion de la connexion et des messages recus
			
		} catch (ClassNotFoundException e) {
			System.out.println(Thread.currentThread() + "==> ERREUR DE CLASSE");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(Thread.currentThread() + "==> ERREUR : IOException e = " + e.toString());
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Bloc catch g???n???r??? automatiquement
			e.printStackTrace();
		}		
	}
    
    
    
    /**
     * Traitement du message recu : on appelle la bonne methode en fonction du type d'objet recu
     * 
     */
    private TypeMsgCS traiteMsgRecu(Object msg) {
    	
    	if (msg instanceof MsgToConsole) {
    		return (afficheMsgDansConsole((MsgToConsole)msg)); // msg de type msg pour la console
    	}
    	else {
           	if (msg instanceof MsgDeControle) {
        		return (afficheMsgDansConsole((MsgDeControle)msg)); // msg de type controle entre client et serveur
           	}
           	else if (msg instanceof IHM_SERIALISABLE) {
           		return TypeMsgCS.MSG_TRF_IHM; // msg de type IHM serialisable
           	}
           	else if (msg instanceof Object) {
           		return TypeMsgCS.MSG_TRF_OBJET; // msg de type Objet (non connu a ce niveau)
           	}
    	}
    	return TypeMsgCS.MSG_INCONNU;	// type de message a traite au niveau du dessus
    }
    
    
    /**
     * methode de gestion des messages destines a la console systeme
     * 
     * @param msg
     */
    private TypeMsgCS afficheMsgDansConsole(MsgToConsole msg) {
    	if (VERBOSE_LOCAL) {
    		System.out.println(Thread.currentThread()
    							+ " ==> Serveur : Message de type \"MsgToConsole contenant ce msg \" : \""
    							+ ((MsgToConsole)msg).getMsg()
    							+ "\""
    							);
    	}
    	/**
    	 * on envoie le message dans la console d'affichage des messages distants.
    	 * La console du centre est utilisee pour ces affichages
    	 */
    	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_TCP, Thread.currentThread()
    											+ " Serveur : message recu = "
    											+ ((MsgToConsole)msg).getMsg()));

    	return TypeMsgCS.MSG_CONSOLE;
    }
    
    
    /**
     *methode utilise pour traiter les messages de controle
     * 
     * @param msg
     */
    private TypeMsgCS afficheMsgDansConsole(MsgDeControle msg) {
    	
    	if (VERBOSE_LOCAL) {
    		System.out.println(Thread.currentThread()
    							+ " ==> Serveur : Message de type \"MsgDeControle\" contenant ce msg \" : \""
    							+ ((MsgDeControle)msg).getLibelleMsg()
    							+ "\""
    							);
    	}

    	/*
    	 * on envoi le message a afficher dans la console systeme : la zone de texte en bas de la page (zone sud)
    	 */
    	Object obj = (((MsgDeControle)msg).getObj());
    	if (obj != null) {
        	controleur.getConsole().sendMsgToConsole(
    				new MsgToConsole(NUM_CONSOLE_SYSTEM,
    								Thread.currentThread()
    								+ " : Message systeme recu et contenant un Object contenant ce message txt = "
    								+ (((MsgToConsole)((MsgDeControle)msg).getObj())).getMsg())
    								);    		
    	}
    	else {
        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, "Message systeme recu par le thread : " + Thread.currentThread() + " mais sans Object dedans" ));    		
    	}
    	
    	return msg.getTypeMsg();
    }

}
