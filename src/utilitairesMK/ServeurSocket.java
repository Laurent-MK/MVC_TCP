package utilitairesMK;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class ServeurSocket {

    static final int port = 9999;
    
    /**
     * constructeur de la classe de serveur de socket
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ServeurSocket() throws IOException, ClassNotFoundException {
    
    	ServerSocket s = new ServerSocket(port);
        System.out.println("Socket serveur: " + s);
        System.out.println("en attente d'un objet\n");

        Socket soc = s.accept();
        ObjectOutputStream out = new ObjectOutputStream(soc.getOutputStream());
        out.flush();
        ObjectInputStream in = new ObjectInputStream(soc.getInputStream());

        System.out.println("Serveur a cree les flux");

//        while (true) {
        	
        	System.out.println("Serveur a accepte connexion: " + soc);
     
            MsgToConsole objetRecu = (MsgToConsole) in.readObject();

            System.out.println("Serveur : message recu = " + objetRecu.getMsg());
            
            out.writeObject(new MsgToConsole(2, false, "message venant du serveur"));
            out.flush();
            System.out.println("Serveur : message envoye\n");

/*            if (objetRecu.getMsg().equals("STOP")) {
            	break;
            }
       }
*/
        System.out.println("Serveur : fermeture de la socket et arret du serveur\n");
        in.close();
        out.close();
        soc.close();        	 
    }
}
