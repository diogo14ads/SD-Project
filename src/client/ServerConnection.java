package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
	
	public void closeConnection()
	{
		try {
			oos.close();
			ois.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Connection ended");
	}
	
	public boolean login(ArrayList<String> loginData)
	{
		TCPMessage msg = new TCPMessage(TCPMessageType.LOGIN_REQUEST, loginData);
		TCPMessage response = null;
		
		try {
			oos.writeObject(msg);
			
			response = (TCPMessage) ois.readObject();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(response.getStrings().get(0).equals("1"))
			return true;
		else
			return false;
		
	}

}
