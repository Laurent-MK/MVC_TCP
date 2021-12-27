package utilitairesMK;

/**
 * Classe permettant de construire des messages a destination de la console syst�me
 * 
 * @author Balou
 *
 */
public class MsgToConsole {
	
	private int numConsoleDest;
	private String msg;
	
	/**
	 * Constructeur
	 */
	public MsgToConsole(int numConsole, String msg) {
		
		this.numConsoleDest = numConsole;
//		this.msg = msg;
		this.msg = String.copyValueOf(msg.toCharArray());
	}
	
	
	/**
	 * methodes d'acces aux proprietes de cette classe
	 * 
	 * @return
	 */
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}

	
	public int getNumConsoleDest() {
		return numConsoleDest;
	}
	

}
