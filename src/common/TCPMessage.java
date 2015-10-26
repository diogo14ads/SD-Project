package common;

import java.io.Serializable;

public class TCPMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1828279559078599563L;
	private TCPMessageType type;
	

	public TCPMessage(TCPMessageType type) {
		super();
		this.type = type;
	}

	public TCPMessageType getType() {
		return type;
	}

	public void setType(TCPMessageType type) {
		this.type = type;
	}
	
	//para a persistencia dos dados
	//private int id;
	
}
