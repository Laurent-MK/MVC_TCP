package utilitairesMK;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import controler.ControlerConsoleTCPServer;
import model.Constantes;


public class ServeurSocket implements Constantes {

    static final int port = 9999;
    private ControlerConsoleTCPServer controleur;
    
    /**
     * constructeur de la classe de serveur de socket
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ServeurSocket(ControlerConsoleTCPServer controleur) throws IOException, ClassNotFoundException {
    
    	this.controleur = controleur;
    	
    	ServerSocket s = new ServerSocket(port);
    	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE,  "Socket serveur: " + s));
    	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE,  "en attente d'un objet\n"));

    	if (VERBOSE_ON) {
            System.out.println("Socket serveur: " + s);
            System.out.println("en attente d'un objet\n");    		
    	}

        while (true) {

        	Socket soc = s.accept();
        	ObjectOutputStream out = new ObjectOutputStream(soc.getOutputStream());
        	out.flush();
        	ObjectInputStream in = new ObjectInputStream(soc.getInputStream());

        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE,  "Serveur a cree les flux"));
        	System.out.println("Serveur a cree les flux");

        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE,  "Serveur a accepte connexion: " + soc));
        	System.out.println("Serveur a accepte connexion: " + soc);
     
            MsgToConsole objetRecu = (MsgToConsole) in.readObject();

            System.out.println("Serveur : message recu = " + objetRecu.getMsg());
        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_TCP, !AJOUTER_NUM_MESSAGE,  "Serveur : message recu = " + objetRecu.getMsg()));

        	out.writeObject(new MsgToConsole(2, false, "message venant du serveur"));
            out.flush();
            System.out.println("Serveur : message envoye\n");
        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE,  "Serveur : message envoye\n"));

            if (objetRecu.getMsg().equals("STOP")) {
            	break;
            }

        	controleur.getConsole().sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_SYSTEM, !AJOUTER_NUM_MESSAGE,  "Serveur : fermeture de la socket et arret du serveur\n"));
            System.out.println("Serveur : fermeture de la socket et arret du serveur\n");
            in.close();
            out.close();
            soc.close();        	 
        }
    }
}
