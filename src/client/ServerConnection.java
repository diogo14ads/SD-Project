package client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

import common.TCPMessage;
import common.TCPMessageType;

public class ServerConnection{
	
	static Properties prop = new Properties();

	ObjectOutputStream oos;
	ObjectInputStream ois;
	Socket socket;
		
	public ServerConnection()
	{
		try {
			readProperties();

			socket = new Socket(prop.getProperty("host"), Integer.parseInt(prop.getProperty("TcpPort")));
			
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
	
	public boolean registerAccount(ArrayList<String> registerData)	//Dá para fazer uma função geral
	{
		TCPMessage msg = new TCPMessage(TCPMessageType.REGISTER_REQUEST,registerData);
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
	
	public boolean login(ArrayList<String> loginData)				//Dá para fazer uma função geral
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
	
	public static void readProperties(){

		InputStream input = null;

		try {

			input = new FileInputStream("config.properties");
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
