package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import common.DatabaseRow;
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

	public ArrayList<DatabaseRow> getMyProjectList() {
		TCPMessage message = new TCPMessage(TCPMessageType.MY_PROJECTS_REQUEST);
		TCPMessage response = null;
		ArrayList<DatabaseRow> myProjects = null;
		
		try {
			oos.writeObject(message);
			
			response=(TCPMessage) ois.readObject();
			myProjects = response.getTable();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return myProjects;
		
		
	}

	public ArrayList<DatabaseRow> getProjectLevels(int projectId) {
		TCPMessage message = new TCPMessage(TCPMessageType.PROJECT_LEVELS_REQUEST);
		TCPMessage response = null;
		
		message.getIntegers().add(projectId);
		
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
		
		
		return response.getTable();
	}

	public void changeGoal(int projectId, int levelId, int goal) {
		TCPMessage message = new TCPMessage(TCPMessageType.CHANGE_LEVEL_GOAL_REQUEST);
		
		message.getIntegers().add(projectId); //só é preciso se o ID do nivel for ZERO(é o objectivo minimo do projecto, tem sempre o ID zero)

		message.getIntegers().add(levelId);

		message.getIntegers().add(goal);
		
		try {
			oos.writeObject(message);
			
			//TODO persistencia
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addLevel(int projectId, int goal) {
		TCPMessage message = new TCPMessage(TCPMessageType.ADD_LEVEL_REQUEST);
		
		message.getIntegers().add(projectId);

		message.getIntegers().add(goal);
		
		try {
			oos.writeObject(message);
			
			//TODO persistencia
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void addReward(int projectId, int levelId, String description, int value) {
		TCPMessage message = new TCPMessage(TCPMessageType.ADD_REWARD_REQUEST);
		
		message.getIntegers().add(projectId);

		message.getIntegers().add(levelId);

		message.getStrings().add(description);

		message.getIntegers().add(value);
		
		try {
			oos.writeObject(message);
			
			//TODO persistencia
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<DatabaseRow> getLevelRewards(int projectId, int levelId) {
		TCPMessage message = new TCPMessage(TCPMessageType.LEVEL_REWARDS_REQUEST);
		TCPMessage response = null;
		
		message.getIntegers().add(projectId);

		message.getIntegers().add(levelId);
		
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
		
		
		return response.getTable();
	}

	public void removeReward(int rewardId) {
		TCPMessage message = new TCPMessage(TCPMessageType.REMOVE_REWARD_REQUEST);
		
		message.getIntegers().add(rewardId);
		
		try {
			oos.writeObject(message);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void removeLevel(int levelId) {
		TCPMessage message = new TCPMessage(TCPMessageType.REMOVE_LEVEL_REQUEST);
		
		message.getIntegers().add(levelId);
		
		try {
			oos.writeObject(message);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void addAdministrator(int projectId, String email) {
		TCPMessage message = new TCPMessage(TCPMessageType.ADD_ADMIN_REQUEST);
		
		message.getIntegers().add(projectId);

		message.getStrings().add(email);
		
		try {
			oos.writeObject(message);
			
			//TODO persistencia
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
		try {
			oos.writeObject(message);
			
			response=(TCPMessage) ois.readObject();
			projects = response.getTable();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return projects;
	}

	public ArrayList<DatabaseRow> getActiveRewards(int projectId) {
		TCPMessage message = new TCPMessage(TCPMessageType.ACTIVE_REWARDS_REQUEST);
		TCPMessage response = null;
		
		message.getIntegers().add(projectId);
		
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
		
		
		return response.getTable();
	}

	public void buyReward(int rewardId) {
		TCPMessage message = new TCPMessage(TCPMessageType.BUY_REWARD_REQUEST);
		
		message.getIntegers().add(rewardId);
		
		try {
			oos.writeObject(message);
			
			//TODO persistencia
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<DatabaseRow> checkMyRewards() {
		TCPMessage message = new TCPMessage(TCPMessageType.MY_REWARDS_REQUEST);
		TCPMessage response = null;
		ArrayList<DatabaseRow> table = null;
		
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
		
		return response.getTable();
	}

	

}
