package tcpserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

import common.RMIInterface;

public class TCPServer {
	
	static Properties prop = new Properties();
	
	public static void main(String[] args) {
		try{
			readProperties();
			RMIInterface ri = (RMIInterface) LocateRegistry.getRegistry(prop.getProperty("rmiRegistry")).lookup(prop.getProperty("rmiLookpup"));
			System.out.println("Listening in port: " + prop.getProperty("tcpPort")+"...");
			ServerSocket listenSocket = new ServerSocket(Integer.parseInt(prop.getProperty("tcpPort")));
			System.out.println("LISTEN SOCKET = "+ listenSocket);
			
			while(true)
			{
				Socket clientSocket = listenSocket.accept();
				System.out.println("CLIENT_SOCKET (created at accept())="+ clientSocket);
				
				(new ClientConnection(clientSocket,ri)).run(); //Client Thread
				
			}
		} catch(IOException io) {
			System.out.println("Listen: "+ io.getMessage());
		} catch (NotBoundException nb) {
			System.out.println("Bound: "+ nb.getMessage());
		}
		
		
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
