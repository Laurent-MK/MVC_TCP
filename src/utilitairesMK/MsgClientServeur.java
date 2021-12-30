package utilitairesMK;


import java.io.Serializable;

public class MsgClientServeur extends MessageMK implements Serializable, Constantes_SERVER_TCP {

/**
	 * 
	 */
	private static final long serialVersionUID = 8860951286914925272L;
	
	private Object obj = null;
	private String libelleMsg = "LIBELLE NON DEFINI";

	
	public MsgClientServeur(int typeMsg, long numMsg, String libelleMsg, Object obj) {

		setNumMsg(numMsg);
		setTypeMsg(typeMsg);
		this.obj = obj;			// objet eventuel a transporter
		this.libelleMsg = String.copyValueOf(libelleMsg.toCharArray());

//		this.libelleMsg = libelleMsg;
	}


	/**
	 * @return le obj
	 */
	public Object getObj() {
		return obj;
	}


	/**
	 * @param obj le obj à définir
	 */
	public void setObj(Object obj) {
		this.obj = obj;
	}


	/**
	 * @return le libelleMsg
	 */
	public String getLibelleMsg() {
		return libelleMsg;
	}

}
