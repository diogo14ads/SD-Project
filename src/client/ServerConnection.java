package client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;

import common.DatabaseRow;
import common.TCPMessage;
import common.TCPMessageType;

public class ServerConnection{
	
	static Properties prop = new Properties();

	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private int connectionRetrys;
	private Client client;
	

	

	public ServerConnection(Client client)
	{
		try {
			readProperties();

			socket = new Socket(prop.getProperty("host"), Integer.parseInt(prop.getProperty("TcpPort")));
			connectionRetrys = Integer.parseInt(prop.getProperty("connectionRetrys"));
			
			this.client = client;
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			System.out.println("SOCKET="+socket);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Exception: "+ e);
		}
	}
	
	private boolean reconnect() {
		TCPMessage message = new TCPMessage(TCPMessageType.RECONNECT_REQUEST);
		readProperties();
		try {
			socket = new Socket(prop.getProperty("host"), Integer.parseInt(prop.getProperty("TcpPort")));
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			message.getStrings().add(client.loggedUser);
			sendTCPMessage(message);
			
			return true;
			
		} catch (NumberFormatException e) {
			return false;
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
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
	
	
	public Socket getSocket() {
		return socket;
	}
	
	public boolean registerAccount(ArrayList<String> registerData)
	{
		TCPMessage message = new TCPMessage(TCPMessageType.REGISTER_REQUEST,registerData);
		TCPMessage response = null;
		
		while(true)
		{
			if(!sendTCPMessage(message))
			{
				System.out.println("It was not possible to send the message!");
				break;
			}
			
			try {
				response = receiveTCPMessage();
				break;
			} catch (ClassNotFoundException e) {
				System.err.println("Exception: "+e);
				break;
			} catch (IOException e){}
		}
		
		if(response != null && response.getStrings().get(0).equals("1"))
			return true;
		else
			return false;
	}
	
	public boolean login(ArrayList<String> loginData)		
	{
		TCPMessage message = new TCPMessage(TCPMessageType.LOGIN_REQUEST, loginData);
		TCPMessage response = null;
		
		while(true)
		{
			if(!sendTCPMessage(message))
			{
				System.out.println("It was not possible to send the message!");
				break;
			}
			
			try {
				response = receiveTCPMessage();
				break;
			} catch (ClassNotFoundException e) {
				System.err.println("Exception: "+e);
				break;
			} catch (IOException e){}
		}
		
		if(response != null && response.getStrings().get(0).equals("1"))
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

	public void createProject(ArrayList<String> projectData) {
		
		TCPMessage message = new TCPMessage(TCPMessageType.CREATE_PROJECT_REQUEST, projectData);
		
		sendTCPMessage(message);
		
	}
	
	public int checkBalance() {
		TCPMessage message = new TCPMessage(TCPMessageType.CHECK_BALANCE_REQUEST);
		TCPMessage response = null;
		
		while(true)
		{
			if(!sendTCPMessage(message))
			{
				System.out.println("It was not possible to send the message!");
				break;
			}
			
			try {
				response = receiveTCPMessage();
				break;
			} catch (ClassNotFoundException e) {
				System.err.println("Exception: "+e);
				break;
			} catch (IOException e){}
		}
		
		if(response != null)
			return response.getIntegers().get(0);
		else
			return -1;
	}

	public ArrayList<DatabaseRow> getMyProjectList() {
		TCPMessage message = new TCPMessage(TCPMessageType.MY_PROJECTS_REQUEST);
		TCPMessage response = null;
		ArrayList<DatabaseRow> myProjects = null;
		
		while(true)
		{
			if(!sendTCPMessage(message))
			{
				System.out.println("It was not possible to send the message!");
				break;
			}
			
			try {
				response = receiveTCPMessage();
				break;
			} catch (ClassNotFoundException e) {
				System.err.println("Exception: "+e);
				break;
			} catch (IOException e){}
		}
		
		if(response != null)
		{
			myProjects = response.getTable();
		
			return myProjects;
		}
		else
		{
			return null;
		}
		
		
	}

	public ArrayList<DatabaseRow> getProjectLevels(int projectId) {
		TCPMessage message = new TCPMessage(TCPMessageType.PROJECT_LEVELS_REQUEST);
		TCPMessage response = null;
		
		message.getIntegers().add(projectId);
		
		while(true)
		{
			if(!sendTCPMessage(message))
			{
				System.out.println("It was not possible to send the message!");
				break;
			}
			
			try {
				response = receiveTCPMessage();
				break;
			} catch (ClassNotFoundException e) {
				System.err.println("Exception: "+e);
				break;
			} catch (IOException e){}
		}
		
		if(response != null)
			return response.getTable();
		else
			return null;
	}

	public void changeGoal(int projectId, int levelId, int goal) {
		TCPMessage message = new TCPMessage(TCPMessageType.CHANGE_LEVEL_GOAL_REQUEST);
		
		message.getIntegers().add(projectId); //só é preciso se o ID do nivel for ZERO(é o objectivo minimo do projecto, tem sempre o ID zero)

		message.getIntegers().add(levelId);

		message.getIntegers().add(goal);
		
		sendTCPMessage(message);
	}

	public void addLevel(int projectId, int goal) {
		TCPMessage message = new TCPMessage(TCPMessageType.ADD_LEVEL_REQUEST);
		
		message.getIntegers().add(projectId);

		message.getIntegers().add(goal);
		

		sendTCPMessage(message);
		
	}

	public void addReward(int projectId, int levelId, String description, int value) {
		TCPMessage message = new TCPMessage(TCPMessageType.ADD_REWARD_REQUEST);
		
		message.getIntegers().add(projectId);

		message.getIntegers().add(levelId);

		message.getStrings().add(description);

		message.getIntegers().add(value);
		
		sendTCPMessage(message);
	}

	public ArrayList<DatabaseRow> getLevelRewards(int projectId, int levelId) {
		TCPMessage message = new TCPMessage(TCPMessageType.LEVEL_REWARDS_REQUEST);
		TCPMessage response = null;
		
		message.getIntegers().add(projectId);

		message.getIntegers().add(levelId);
		
		while(true)
		{
			if(!sendTCPMessage(message))
			{
				System.out.println("It was not possible to send the message!");
				break;
			}
			
			try {
				response = receiveTCPMessage();
				break;
			} catch (ClassNotFoundException e) {
				System.err.println("Exception: "+e);
				break;
			} catch (IOException e){}
		}
		
		if(response != null)
			return response.getTable();
		else
			return null;
	}

	public void removeReward(int rewardId) {
		TCPMessage message = new TCPMessage(TCPMessageType.REMOVE_REWARD_REQUEST);
		
		message.getIntegers().add(rewardId);
		
		sendTCPMessage(message);

	}

	public void removeLevel(int levelId) {
		TCPMessage message = new TCPMessage(TCPMessageType.REMOVE_LEVEL_REQUEST);
		
		message.getIntegers().add(levelId);
		
		sendTCPMessage(message);
		
	}

	public void addAdministrator(int projectId, String email) {
		TCPMessage message = new TCPMessage(TCPMessageType.ADD_ADMIN_REQUEST);
		
		message.getIntegers().add(projectId);

		message.getStrings().add(email);
		
		sendTCPMessage(message);
		
	}

	public ArrayList<DatabaseRow> getProjects(boolean isCurrent) {
		TCPMessageType type;
		TCPMessage message; 
		TCPMessage response = null;
		ArrayList<DatabaseRow> projects = null;
		
		if(isCurrent)
			type = TCPMessageType.CURRENT_PROJECTS_REQUEST;
		else
			type = TCPMessageType.PAST_PROJECTS_REQUEST;
		
		message = new TCPMessage(type);
		
		while(true)
		{
			if(!sendTCPMessage(message))
			{
				System.out.println("It was not possible to send the message!");
				break;
			}
			
			try {
				response = receiveTCPMessage();
				break;
			} catch (ClassNotFoundException e) {
				System.err.println("Exception: "+e);
				break;
			} catch (IOException e){}
		}
		
		if(response != null)
		{
			projects = response.getTable();
			return projects;
		}
		else
			return null;
	}

	public ArrayList<DatabaseRow> getActiveRewards(int projectId) {
		TCPMessage message = new TCPMessage(TCPMessageType.ACTIVE_REWARDS_REQUEST);
		TCPMessage response = null;
		
		message.getIntegers().add(projectId);
		
		while(true)
		{
			if(!sendTCPMessage(message))
			{
				System.out.println("It was not possible to send the message!");
				break;
			}
			
			try {
				response = receiveTCPMessage();
				break;
			} catch (ClassNotFoundException e) {
				System.err.println("Exception: "+e);
				break;
			} catch (IOException e){}
		}

		if(response != null)
			return response.getTable();
		else
			return  null;
	}

	public void buyReward(int rewardId) {
		TCPMessage message = new TCPMessage(TCPMessageType.BUY_REWARD_REQUEST);
		
		message.getIntegers().add(rewardId);
		
		sendTCPMessage(message);
	}

	public ArrayList<DatabaseRow> checkMyRewards() {
		TCPMessage message = new TCPMessage(TCPMessageType.MY_REWARDS_REQUEST);
		TCPMessage response = null;
		
		while(true)
		{
			if(!sendTCPMessage(message))
			{
				System.out.println("It was not possible to send the message!");
				break;
			}
			
			try {
				response = receiveTCPMessage();
				break;
			} catch (ClassNotFoundException e) {
				System.err.println("Exception: "+e);
				break;
			} catch (IOException e){}
		}
		
		if(response != null)
			return response.getTable();
		else
			return null;
	}

	public void giveawayReward(int pledgeId, String emailReceiver) {
		TCPMessage message = new TCPMessage(TCPMessageType.GIVEAWAY_REWARD_REQUEST);
		
		message.getIntegers().add(pledgeId);
		message.getStrings().add(emailReceiver);
		
		sendTCPMessage(message);
		
	}

	public void cancelProject(int projectId) {
		TCPMessage message = new TCPMessage(TCPMessageType.CANCEL_PROJECT_REQUEST);
		
		message.getIntegers().add(projectId);
		
		sendTCPMessage(message);
		
	}

	public void sendMessageProject(int projectId, String msg) {
		TCPMessage message = new TCPMessage(TCPMessageType.MESSAGE_PROJECT_REQUEST);
		
		message.getIntegers().add(projectId);
		message.getStrings().add(msg);
		
		sendTCPMessage(message);
		
	}

	public ArrayList<DatabaseRow> getMyMessages() {
		TCPMessage message = new TCPMessage(TCPMessageType.MY_MESSAGES_REQUEST);
		TCPMessage response = null;
		
		while(true)
		{
			if(!sendTCPMessage(message))
			{
				System.out.println("It was not possible to send the message!");
				break;
			}
			
			try {
				response = receiveTCPMessage();
				break;
			} catch (ClassNotFoundException e) {
				System.err.println("Exception: "+e);
				break;
			} catch (IOException e){}
		}
		
		if(response != null)
			return response.getTable();
		else 
			return null;
	}

	public void sendMessageUser(int projectId, String email, String msg) {
		TCPMessage message = new TCPMessage(TCPMessageType.MESSAGE_USER_REQUEST);
		
		message.getIntegers().add(projectId);
		message.getStrings().add(email);
		message.getStrings().add(msg);
		
		sendTCPMessage(message);
	}
	
	public boolean sendTCPMessage(TCPMessage message)
	{
		boolean isConnected=false;
		while(true)
		{
			try {
				oos.writeObject(message);
				return true;
			} catch (IOException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnect())
						break;
				}
				if(isConnected==false)
				{
					return false;
				}
			}
		}
	}
	
	public TCPMessage receiveTCPMessage() throws ClassNotFoundException, IOException
	{
		return ((TCPMessage) ois.readObject());
	}

}
