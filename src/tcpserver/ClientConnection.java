package tcpserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;

import common.DatabaseRow;
import common.RMIInterface;
import common.TCPMessage;
import common.TCPMessageType;

public class ClientConnection extends Thread {
	
	private Socket socket;
	private TCPServer mainServer;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private String activeUser;

	public ClientConnection(Socket socket, TCPServer mainServer) {
		super();
		this.socket = socket;
		this.mainServer = mainServer;
		try {
			//Para receber requests
			this.oos = new ObjectOutputStream(socket.getOutputStream());
			this.ois = new ObjectInputStream(socket.getInputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.start();
	}

	@Override
	public void run() {

		while(!socket.isClosed())
		{
			TCPMessage message = null;
			try {
				message = (TCPMessage) ois.readObject();
			} catch (ClassNotFoundException e) {
				System.err.println("ClassNotFound: "+e);
				break;
			} catch (IOException e) {
				System.err.println("IOException in socket "+socket.getInetAddress()+": "+e);
				break;
			}
			
			//Verifica request e encaminha para o metodo correspondente
			if(message != null)
			{
				if(message.getType() == TCPMessageType.LOGIN_REQUEST)
				{
					//TODO dá para fazer um try/catch para todos?
					try {
						login(message);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(message.getType() == TCPMessageType.REGISTER_REQUEST)
				{	
					try {
						register(message);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(message.getType() == TCPMessageType.CREATE_PROJECT_REQUEST)
				{
					createProject(message);
				}
				else if(message.getType() == TCPMessageType.CHECK_BALANCE_REQUEST)
				{
					checkBalance(message);
				}
				else if(message.getType() == TCPMessageType.MY_PROJECTS_REQUEST)
				{
					myProjectsList(message);
				}
				else if(message.getType() == TCPMessageType.PROJECT_LEVELS_REQUEST)
				{
					projectLevelsList(message);
				}
				else if(message.getType() == TCPMessageType.CHANGE_LEVEL_GOAL_REQUEST)
				{
					changeLevelGoal(message);
				}
				else if(message.getType() == TCPMessageType.ADD_LEVEL_REQUEST)
				{
					addLevel(message);
				}
				else if(message.getType() == TCPMessageType.ADD_REWARD_REQUEST)
				{
					addReward(message);
				}
				else if(message.getType() == TCPMessageType.LEVEL_REWARDS_REQUEST)
				{
					levelRewardsList(message);
				}
				else if(message.getType() == TCPMessageType.REMOVE_REWARD_REQUEST)
				{
					removeReward(message);
				}
				else if(message.getType() == TCPMessageType.REMOVE_LEVEL_REQUEST)
				{
					removeLevel(message);
				}
				else if(message.getType() == TCPMessageType.ADD_ADMIN_REQUEST)
				{
					addAdministrator(message);
				}
				else if(message.getType() == TCPMessageType.CURRENT_PROJECTS_REQUEST)
				{
					currentProjectList(message);
				}
				else if(message.getType() == TCPMessageType.PAST_PROJECTS_REQUEST)
				{
					pastProjectList(message);
				}
				else if(message.getType() == TCPMessageType.ACTIVE_REWARDS_REQUEST)
				{
					getActiveRewards(message);
				}
				else if(message.getType() == TCPMessageType.BUY_REWARD_REQUEST)
				{
					buyReward(message);
				}
				else if(message.getType() == TCPMessageType.MY_REWARDS_REQUEST)
				{
					getMyRewards(message);
				}
				else if(message.getType() == TCPMessageType.GIVEAWAY_REWARD_REQUEST)
				{
					giveawayReward(message);
				}
				else if(message.getType() == TCPMessageType.CANCEL_PROJECT_REQUEST)
				{
					cancelProject(message);
				}
				else if(message.getType() == TCPMessageType.MESSAGE_PROJECT_REQUEST)
				{
					sendMessageProject(message);
				}
				else if(message.getType() == TCPMessageType.MY_MESSAGES_REQUEST)
				{
					getMyMessages(message);
				}
				else if(message.getType() == TCPMessageType.MESSAGE_USER_REQUEST)
				{
					sendMessageUser(message);
				}
				else if(message.getType() == TCPMessageType.RECONNECT_REQUEST)
				{
					reconnect(message);
				}
			}
		}
		
	}

	private void reconnect(TCPMessage message) {
		
		activeUser = message.getStrings().get(0);
		
	}

	private void sendMessageUser(TCPMessage message) {
		
		int projectId = message.getIntegers().get(0);
		String email = message.getStrings().get(0);
		String msg = message.getStrings().get(1);
		
		try {
			mainServer.sendMessageUser(projectId,email, msg);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getMyMessages(TCPMessage message) {
		TCPMessage response = new TCPMessage(TCPMessageType.MY_MESSAGES_REQUEST);
		
		try {
			response.setTable(mainServer.getMyMessages(activeUser));
			
			oos.writeObject(response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void sendMessageProject(TCPMessage message) {
		
		int projectId = message.getIntegers().get(0);
		String msg = message.getStrings().get(0);
		
		try {
			mainServer.sendMessageProject(projectId,activeUser, msg);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void cancelProject(TCPMessage message) {
		int projectId = message.getIntegers().get(0);
		
		try {
			mainServer.cancelProject(projectId);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void giveawayReward(TCPMessage message) {
		
		int pledgeId = message.getIntegers().get(0);
		String emailReceiver = message.getStrings().get(0);
		
		try {
			mainServer.giveawayReward(pledgeId,emailReceiver);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void getMyRewards(TCPMessage message) {
		TCPMessage response = new TCPMessage(TCPMessageType.MY_REWARDS_REQUEST);
		
		try {
			response.setTable(mainServer.getMyRewards(activeUser));
			
			oos.writeObject(response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private boolean buyReward(TCPMessage message) {

		int rewardId = message.getIntegers().get(0);
		
		try {
			if(mainServer.checkBalance(activeUser)>mainServer.checkRewardPrice(rewardId))
			{
				mainServer.buyReward(rewardId,activeUser);
				return true;
			}
			else
				return false;

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private void getActiveRewards(TCPMessage message) {
		TCPMessage response = new TCPMessage(TCPMessageType.ACTIVE_REWARDS_REQUEST);
		int projectId = message.getIntegers().get(0);
		
		try {
			response.setTable(mainServer.activeRewardsList(projectId));
			
			oos.writeObject(response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void pastProjectList(TCPMessage message) {
		
		TCPMessage response = new TCPMessage(TCPMessageType.PAST_PROJECTS_REQUEST);
		ArrayList<DatabaseRow> table = null;
		
		try {
			response.setTable(mainServer.pastProjectsList());
			oos.writeObject(response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}

	private void currentProjectList(TCPMessage message) {
		
		TCPMessage response = new TCPMessage(TCPMessageType.CURRENT_PROJECTS_REQUEST);
		ArrayList<DatabaseRow> table = null;
		
		try {
			response.setTable(mainServer.currentProjectsList());
			oos.writeObject(response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}

	private void addAdministrator(TCPMessage message) {
		int projectId = message.getIntegers().get(0);
		String email = message.getStrings().get(0);
		
		if(!message.getIntegers().isEmpty() && !message.getStrings().isEmpty() 
				&& message.getIntegers().size()==1 && message.getStrings().size()==1)
		{
			try {
				mainServer.addAdministrator(projectId,email);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void removeLevel(TCPMessage message) {
		int levelId = message.getIntegers().get(0);
		
		try {
			mainServer.removeLevel(levelId);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void removeReward(TCPMessage message) {
		int rewardId = message.getIntegers().get(0);
		
		try {
			mainServer.removeReward(rewardId);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void levelRewardsList(TCPMessage message) {
		TCPMessage response = new TCPMessage(TCPMessageType.LEVEL_REWARDS_REQUEST);
		int projectId = message.getIntegers().get(0);
		int levelId = message.getIntegers().get(1);
		
		try {
			response.setTable(mainServer.levelRewardsList(projectId,levelId));
			
			oos.writeObject(response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addReward(TCPMessage message) {
		int projectId = message.getIntegers().get(0);
		int levelId = message.getIntegers().get(1);
		String description = message.getStrings().get(0);
		int value = message.getIntegers().get(2);
		//falta a resposta para garantir persistência
		
		if(!message.getIntegers().isEmpty() && !message.getStrings().isEmpty() 
				&& message.getIntegers().size()==3 && message.getStrings().size()==1)
		{
			try {
				mainServer.addReward(projectId,levelId,description,value);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void addLevel(TCPMessage message) {
		
		int projectId = message.getIntegers().get(0);
		int goal = message.getIntegers().get(1);
		//falta a resposta para garantir persistência
		
		
		
		if(!message.getIntegers().isEmpty() && message.getIntegers().size()==2)
		{
			try {
				mainServer.addLevel(projectId,goal);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void changeLevelGoal(TCPMessage message) {
	
		int projectId = message.getIntegers().get(0);
		int levelId = message.getIntegers().get(1);
		int goal = message.getIntegers().get(2);
		
		try {
			mainServer.changeLevelGoal(projectId,levelId,goal);
			//TODO persistencia
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void projectLevelsList(TCPMessage message) {
		TCPMessage response = new TCPMessage(TCPMessageType.PROJECT_LEVELS_REQUEST);
		ArrayList<DatabaseRow> table = null;
		
		try {
			response.setTable(mainServer.projectLevelsList(message.getIntegers().get(0)));
			
			oos.writeObject(response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void myProjectsList(TCPMessage message) {
		
		TCPMessage response = new TCPMessage(TCPMessageType.CHECK_BALANCE_REQUEST);
		
		try {
			response.setTable(mainServer.myProjectsList(activeUser));
			oos.writeObject(response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private void checkBalance(TCPMessage message) {
		TCPMessage response = new TCPMessage(TCPMessageType.CHECK_BALANCE_REQUEST);
		int balance = -1;
		
		try {
			balance = mainServer.checkBalance(activeUser);
			
			response.getIntegers().add(balance);
			
			oos.writeObject(response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void createProject(TCPMessage message) {
		ArrayList<String> projectData = message.getStrings(); //0: name | 1:description | 2: limit date 
		//falta a resposta para garantir persistência
		
		if(!projectData.isEmpty() && projectData.size()==4)
		{
			try {
				mainServer.createProject(projectData.get(0),projectData.get(1),projectData.get(2),projectData.get(3),activeUser);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void login(TCPMessage msg) throws IOException {
		
		ArrayList<String> loginData = msg.getStrings(); // index 0: E-mail || index 1: Password
		TCPMessage response = new TCPMessage(TCPMessageType.LOGIN_REQUEST);
		boolean valid = false;
		
		if(!loginData.isEmpty() && loginData.size()==2)
		{
				valid=mainServer.login(loginData.get(0), loginData.get(1));
		}
		
		//TODO criar função à parte? por num objecto tipo Mensagem?
		if(valid == true)
		{
			response.getStrings().add("1");
			activeUser = loginData.get(0);
		}
		else {
			response.getStrings().add("0");
		}

		oos.writeObject(response);
	}
	
	private void register(TCPMessage msg) throws IOException
	{
		ArrayList<String> registerData = msg.getStrings();	// index 0: primeiro e ultimo nome || index 1: email || index 2: password
		TCPMessage response = new TCPMessage(TCPMessageType.REGISTER_REQUEST);
		boolean success = false;
		
		if(!registerData.isEmpty() && registerData.size()==3
				&& registerData.get(1).length()>0 && registerData.get(2).length()>0)
		{
			try {
				success = mainServer.register(registerData.get(0), registerData.get(1), registerData.get(2));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(success == true)
		{
			response.getStrings().add("1");
			activeUser = registerData.get(1);
		}
		else {
			response.getStrings().add("0");
		}
		
		oos.writeObject(response);
	}
	
	
}
