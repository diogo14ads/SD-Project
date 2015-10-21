package client;

import java.net.Socket;

public class ServerConnection {

	final String host = "localhost";
	final int port = 4000;
	Socket socket;
	
	public ServerConnection()
	{
		socket = new Socket(host, port);
	}

}
