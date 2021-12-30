package utilitairesMK;

import java.io.Serializable;

/**
 * Classe permettant de construire des messages a destination de la console systï¿½me
 * 
 * @author Balou
 *
 */
public class MsgToConsole extends MessageMK implements Serializable, Constantes_SERVER_TCP {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6392917557600619944L;
	/**
	 * numero de verion pour garantir la serialization compatible avec l'objet qui va deserialiser 
	 * cet objet.
	 * Si les versions de compilateur et runtime JAVA ne sont pas compatibles en matière de serialization,
	 * une exception InvalidClassException sera levée
	 */
//	private static final long serialVersionUID = 5097307363623042972L;
	
	private int numConsoleDest;
	private String msg;
	private boolean ajoutNumMsg = true;

	
	/**
	 * Constructeur
	 */
	public MsgToConsole(int numConsole, boolean isAjoutNumMsg, String msg) {
		
		this.numConsoleDest = numConsole;
//		this.msg = msg;
		this.msg = String.copyValueOf(msg.toCharArray());
		this.ajoutNumMsg = isAjoutNumMsg;
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
	
	public boolean isAjoutNumMsg() {
		return ajoutNumMsg;
	}

}
