package common;

public class TCPMessage {
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
