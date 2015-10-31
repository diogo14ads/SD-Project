package tcpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;

import common.RMIInterface;

public class TCPServer {

	public static void main(String[] args) {
		try{
			RMIInterface ri = (RMIInterface) LocateRegistry.getRegistry(4001).lookup("rmi");
			int serverPort = 4000;
			System.out.println("Listening in port 4000...");
			ServerSocket listenSocket = new ServerSocket(serverPort);
			System.out.println("LISTEN SOCKET = "+ listenSocket);
			
			while(true)
			{
				Socket clientSocket = listenSocket.accept();
				System.out.println("CLIENT_SOCKET (created at accept())="+ clientSocket);
				
				new ClientConnection(clientSocket,ri); //Client Thread
				
			}
		} catch(IOException io) {
			System.out.println("Listen: "+ io.getMessage());
		} catch (NotBoundException nb) {
			System.out.println("Bound: "+ nb.getMessage());
		}
		
		
	}

}
