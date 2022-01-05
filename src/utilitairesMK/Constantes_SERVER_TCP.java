package utilitairesMK;

public interface Constantes_SERVER_TCP {
	/**
	 * Definition de toutes les constantes de l'application
	 */
	public static final int		VALEUR_NUM_NON_DEFINIE = 0;
	
	public static final boolean	VERBOSE_ON_SERVER_TCP = true;	// pour declencher les affichage dans la console systeme
	public static final boolean	VERBOSE_ON_CONSOLE = false;

	public static final int 	TAILLE_MSG_Q_CONSOLE = 250;		// taille de la message queue du thread de console IHM
	public static final int 	MAX_MSG_CONSOLE_CENTRE = 500;	// maximum de messages stockes dans la console
	public static final int 	MAX_MSG_CONSOLE_SUD = 200;	// maximum de messages stockes dans la console
	public static final int		TAILLE_BUFFER_CONSOLE = 500;
	
	public static final int 	NUM_CONSOLE_SYSTEM = 0;			// numero de la console d'affichage
	public static final int 	NUM_CONSOLE_TCP = 1;			// numero de la console d'affichage
	
	public static final int		NUM_PORT_SERVER = 9999;
	
	public static final int 	NUMERO_THREAD_CONSOLE = 1;		// numero du thread de console

	public static final int 	PRIORITE_CONSOLE_SERVER_TCP = 1; // priorite du thread de console
}
