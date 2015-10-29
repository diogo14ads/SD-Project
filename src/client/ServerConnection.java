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
	
	public boolean registerAccount(ArrayList<String> registerData)	//Dá para fazer uma função geral
	{
		TCPMessage message = new TCPMessage(TCPMessageType.REGISTER_REQUEST,registerData);
		TCPMessage response = null;
		
		try {
			oos.writeObject(message);
			
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
		TCPMessage message = new TCPMessage(TCPMessageType.LOGIN_REQUEST, loginData);
		TCPMessage response = null;
		
		try {
			oos.writeObject(message);
			
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

	public void createProject(ArrayList<String> projectData) {
		
		TCPMessage message = new TCPMessage(TCPMessageType.CREATE_PROJECT_REQUEST, projectData);
		//Nao precisa de response, se for preciso reenviar faz em background (é preciso implementar)
		
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int checkBalance() {
		TCPMessage message = new TCPMessage(TCPMessageType.CHECK_BALANCE_REQUEST);
		TCPMessage response = null;
		
		try {
			oos.writeObject(message);
			
			response = (TCPMessage) ois.readObject(); //saldo vem no index 0 do arraylist de integers
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.getIntegers().get(0);
	}
	
	public void sendMessage()
	{
		//TODO implementar funçao genérica de envio de mensagens (se houver tempo)
	}

	

}
