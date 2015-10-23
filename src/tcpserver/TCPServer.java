package tcpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer {

	public static void main(String[] args) {
		
		int nrOfSockets = 0;
		int serverPort = 4000;
		
		ArrayList<Socket> clientSockets = new ArrayList<>();
		
		try{
			System.out.println("Listening in port 4000...");
			ServerSocket listenSocket = new ServerSocket(serverPort);
			System.out.println("LISTEN SOCKET = "+ listenSocket);
			
			while(true)
			{
				Socket clientSocket = listenSocket.accept();
				clientSockets.add(clientSocket);
				nrOfSockets++;
				System.out.println("CLIENT_SOCKET (created at accept())= "+ clientSocket);
				(new ClientConnection(clientSocket,nrOfSockets, clientSockets)).run(); //Client Thread
			}
		} catch(IOException e) {
			System.out.println("Listen: "+ e.getMessage());
		}

	}

}
