package tcpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	public static void main(String[] args) {
		try{
			int serverPort = 4000;
			System.out.println("Listening in port 4000...");
			ServerSocket listenSocket = new ServerSocket(serverPort);
			System.out.println("LISTEN SOCKET = "+ listenSocket);
			
			while(true)
			{
				Socket clientSocket = listenSocket.accept();
				System.out.println("CLIENT_SOCKET (created at accept())="+ clientSocket);
				
				(new ClientConnection(clientSocket)).run(); //Client Thread
				
			}
		} catch(IOException e) {
			System.out.println("Listen: "+ e.getMessage());
		}

	}

}
