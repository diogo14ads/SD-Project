package tcpserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Properties;

import common.DatabaseRow;
import common.RMIInterface;

public class TCPServer {

	static Properties prop = new Properties();
	static boolean startAsPrimary = false;
	static DatagramSocket aSocket = null;
	static Socket testSocket;
	private RMIInterface ri;
	static int connectionRetrys;
	static int RMIRegistry;
    static String rmiLookup;


	public static void main(String[] args) {
		readProperties();
		RMIRegistry = Integer.parseInt(prop.getProperty("RmiRegistry"));
        rmiLookup = prop.getProperty("RmiLookup");
        new TCPServer();
	}

	public TCPServer() {
		try {
			
			connectionRetrys = Integer.parseInt(prop.getProperty("connectionRetrys"));
			ri = (RMIInterface) LocateRegistry.getRegistry(RMIRegistry).lookup(rmiLookup);
			
			System.out.println("Listening in port: " + prop.getProperty("TcpPort") + "...");

			/**
			 * Verifica se j� existe um servidor a correr tentando criar uma
			 * socket. Se criar uma socket funcionar ent�o fecha a socket de
			 * teste e come�a a thread do servidor secund�rio.
			 */
			try {
				testSocket = new Socket(prop.getProperty("host"), Integer.parseInt(prop.getProperty("TcpPort")));
			} catch (IOException e) {
				startAsPrimary = true;
			}

			if (!startAsPrimary) {
				try {
					testSocket.close();
				} catch (Exception e) {
					System.out.println("There was a problem closing the testSocket.");
				}
				new TCPServerSec(Integer.parseInt(prop.getProperty("maxTries")),prop.getProperty("host"),Integer.parseInt(prop.getProperty("UDPPort")));

			} else {
				System.out.println("Server set as: Primary");

				UDPServer conectServer = new UDPServer(Integer.parseInt(prop.getProperty("UDPPort")));

				ServerSocket listenSocket = new ServerSocket(Integer.parseInt(prop.getProperty("TcpPort")));
				System.out.println("LISTEN SOCKET = " + listenSocket);
				while (true) {
					Socket clientSocket = listenSocket.accept();
					System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);

					new ClientConnection(clientSocket, this);

				}
			}

		} catch (IOException io) {
			System.out.println("Listen: " + io.getMessage());
		} catch (NotBoundException nb) {
			System.out.println("Bound: " + nb.getMessage());
		}

	}

	public static void readProperties() {

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

	public synchronized void sendMessageUser(final int projectId, final String email, final String msg) throws RemoteException
	{
		boolean isConnected = false;
		while(true)
		{
			try {
				ri.sendMessageUser(projectId, email, msg);
				return;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return;
				}
			}
		}
	}
	public synchronized void sendMessageProject(int projectId, String email, String msg) throws RemoteException
	{
		boolean isConnected = false;
		while(true)
		{
			try {
				ri.sendMessageProject(projectId, email, msg);
				return;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return;
				}
			}
		}
	}
	public synchronized void cancelProject(int projectId) throws RemoteException
	{
		
		boolean isConnected = false;
		while(true)
		{
			try {
				ri.cancelProject(projectId);
				return;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return;
				}
			}
		}
	}
	public synchronized void giveawayReward(int pledgeId, String emailReceiver) throws RemoteException
	{
		
		boolean isConnected = false;
		while(true)
		{
			try {
				ri.giveawayReward(pledgeId, emailReceiver);
				return;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return;
				}
			}
		}
	}
	public synchronized void buyReward(int rewardId, String email) throws RemoteException
	{
		boolean isConnected = false;
		while(true)
		{
			try {
				ri.buyReward(rewardId, email);
				return;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return;
				}
			}
		}
	}
	public synchronized void addAdministrator(int projectId, String email) throws RemoteException
	{
		boolean isConnected = false;
		while(true)
		{
			try {
				ri.addAdministrator(projectId, email);
				return;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return;
				}
			}
		}
	}
	public synchronized void removeLevel(int levelId) throws RemoteException
	{
		
		boolean isConnected = false;
		while(true)
		{
			try {

				ri.removeLevel(levelId);
				return;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return;
				}
			}
		}
	}
	public synchronized void removeReward(int rewardId) throws RemoteException
	{
		boolean isConnected = false;
		while(true)
		{
			try {

				ri.removeReward(rewardId);
				return;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return;
				}
			}
		}
	}
	public synchronized void addReward(int projectId, int levelId, String description, int value) throws RemoteException
	{
		boolean isConnected = false;
		while(true)
		{
			try {

				ri.addReward(projectId, levelId, description, value);
				return;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return;
				}
			}
		}
	}
	public synchronized void addLevel(int projectId, int goal) throws RemoteException
	{
		boolean isConnected = false;
		while(true)
		{
			try {

				ri.addLevel(projectId, goal);
				return;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return;
				}
			}
		}
	}
	public synchronized void changeLevelGoal(int projectId, int levelId, int goal) throws RemoteException
	{
		ri.changeLevelGoal(projectId, levelId, goal);
		boolean isConnected = false;
		while(true)
		{
			try {
				ri.changeLevelGoal(projectId, levelId, goal);
				return;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return;
				}
			}
		}
	}
	public synchronized void createProject(String name, String description, String date, String goal, String userEmail) throws RemoteException
	{
		boolean isConnected = false;
		while(true)
		{
			try {
				ri.createProject(name, description, date, goal, userEmail);
				return;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return;
				}
			}
		}
		
	}
	public synchronized boolean register(String name, String email, String password) throws RemoteException
	{
		boolean success = false;
		boolean isConnected = false;
		while(true)
		{
			try {
				success = ri.register(name, email, password);
				return success;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return false;
				}
			}
		}
	}

	public ArrayList<DatabaseRow> getMyMessages(String activeUser) throws RemoteException {
		
		boolean isConnected = false;
		while(true) 
		{
			try {
				return ri.getMyMessages(activeUser);
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return null;
				}
			}
		}
	}

	public ArrayList<DatabaseRow> getMyRewards(String activeUser) throws RemoteException {
		
		boolean isConnected = false;
		while(true) 
		{
			try {
				return ri.getMyRewards(activeUser);
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return null;
				}
			}
		}
		
	}

	public int checkRewardPrice(int rewardId) throws RemoteException {
		
		boolean isConnected = false;
		while(true) 
		{
			try {
				return ri.checkRewardPrice(rewardId);
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return -1;
				}
			}
		}
	}

	public int checkBalance(String activeUser) throws RemoteException { 

		boolean isConnected = false;
		while(true) 
		{
			try {
				return ri.checkBalance(activeUser);
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return -1;
				}
			}
		}
	}

	public ArrayList<DatabaseRow> activeRewardsList(int projectId) throws RemoteException {
		
		
		boolean isConnected = false;
		while(true) 
		{
			try {
				return ri.activeRewardsList(projectId);
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return null;
				}
			}
		}
		
		
	}

	public ArrayList<DatabaseRow> pastProjectsList() throws RemoteException {
		
		boolean isConnected = false;
		while(true) 
		{
			try {
				return ri.pastProjectsList();
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return null;
				}
			}
		}
	}

	public ArrayList<DatabaseRow> currentProjectsList() {
		boolean isConnected = false;
		while(true) 
		{
			try {
				return ri.currentProjectsList();
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return null;
				}
			}
		}
		
	}

	public ArrayList<DatabaseRow> levelRewardsList(int projectId, int levelId) throws RemoteException {
		
		boolean isConnected = false;
		while(true)
		{
			try {
				return ri.levelRewardsList(projectId, levelId);
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return null;
				}
			}
		}
		
	}

	public ArrayList<DatabaseRow> projectLevelsList(Integer projectId) throws RemoteException {
		
		
		boolean isConnected = false;
		while(true)
		{
			try {
				return ri.projectLevelsList(projectId);
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return null;
				}
			}
		}
	}

	public ArrayList<DatabaseRow> myProjectsList(String activeUser) {

		boolean isConnected = false;
		while(true)
		{
			try {
				return ri.myProjectsList(activeUser);
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return null;
				}
			}
		}
	}

	public boolean login(String email, String password) {
		boolean success = false;
		boolean isConnected = false;
		while(true)
		{
			try {
				success = ri.login(email, password);
				return success;
			} catch (RemoteException e) {
				for(int i = 0; i < connectionRetrys ; i++)
				{
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(isConnected=reconnectRMI())
						break;
				}
				if(isConnected==false)
				{
					return false;
				}
			}
		}
	}
	
	private boolean reconnectRMI(){
		System.out.println("Reconnecting to RMI...");
		try {
			ri = (RMIInterface) LocateRegistry.getRegistry(4001).lookup("rmi");
			return true;
		} catch (AccessException e) {
			System.err.println("AccessException: "+e);
			return false;
		} catch (RemoteException e) {
			System.err.println("RemoteException: "+e);
			return false;
		} catch (NotBoundException e) {
			System.err.println("NotBoundException "+e);
			return false;
		}
	}

}

class TCPServerSec extends Thread {

	String _host;
	int _maxtries;
	int _udpPort;
	DatagramSocket udpConection;
	byte[] pingMessage;
	int conectionFail = 0;
	InetAddress hostConection;
	DatagramPacket sender, reciver;
	int counter;
	String[] args = null;

	TCPServerSec(int maxtries, String host, int udpPort) {
		_maxtries = maxtries;
		_host = host;
		_udpPort = udpPort;
		counter = 0;
		this.start();

	}

	public void run() {

		try {
			udpConection = new DatagramSocket();

			while (true) {

				try {
					pingMessage = "ping".getBytes();
					hostConection = InetAddress.getByName(_host);
					sender = new DatagramPacket(pingMessage, pingMessage.length, hostConection,_udpPort);
					udpConection.send(sender);
					udpConection.setSoTimeout(2000);
					pingMessage = new byte[1000];
					reciver = new DatagramPacket(pingMessage, pingMessage.length);
					udpConection.receive(reciver);
					System.out.println("[Secondary Server] Got the "
							+ new String(reciver.getData(), 0, reciver.getLength()) + " back from the Primary Server");

					Thread.sleep(2000);

				} catch (SocketTimeoutException e) {
					if (counter < _maxtries) {
						System.out.println("[Secondary Server] Primary Server failed to respond");
						counter++;

					} else {
						System.out.println("[Secondary Server] Taking over as Primary Server");
						udpConection.close();
						new TCPServer();
					}
				}

			}

		} catch (Exception e) {
			System.out.print("[BackupServer]");
			e.printStackTrace();
		}

	}

}

class UDPServer extends Thread {

	DatagramSocket conection;
	byte[] buffer = new byte[1000];
	DatagramPacket sender, receiver;
	int serverPort;
	String pingMessage;

	UDPServer(int port) {

		this.serverPort = port;
		this.start();

	}

	public void run() {
		try {

			conection = new DatagramSocket(serverPort);

			while (true) {

				receiver = new DatagramPacket(buffer, buffer.length);
				conection.receive(receiver);
				pingMessage = new String(receiver.getData(), 0, receiver.getLength());
				System.out.println("[UDPServer]Secondary Server tried to: " + pingMessage);
				sender = new DatagramPacket(receiver.getData(), receiver.getLength(), receiver.getAddress(),
						receiver.getPort());
				conection.send(sender);

			}

		} catch (Exception e) {
			System.out.print("[UDPServer]");
			e.printStackTrace();
		}
	}

}
