package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.TCPMessage;
import common.TCPMessageType;

public class ServerConnection {

	final String host = "localhost";
	final int port = 4000;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Socket socket;
	
	public ServerConnection()
	{
		try {

			socket = new Socket(host, port);
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			System.out.println("SOCKET="+socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void login()
	{
		TCPMessage msg = new TCPMessage(TCPMessageType.LOGIN_REQUEST);
		
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
