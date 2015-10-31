package tcpserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

import common.RMIInterface;

public class TCPServer {

	static Properties prop = new Properties();
	static boolean isPrimary = false;
	static DatagramSocket aSocket = null;

	public static void main(String[] args) {
		try {
			readProperties();

			RMIInterface ri = (RMIInterface) LocateRegistry.getRegistry(prop.getProperty("rmiRegistry"))
					.lookup(prop.getProperty("rmiLookpup"));
			System.out.println("Listening in port: " + prop.getProperty("tcpPort") + "...");

			isPrimary = setToPrimary();

			if (isPrimary) {
				System.out.println("Server set as: Primary");

				aSocket = new DatagramSocket(6789);
				System.out.println("Socket Datagram à escuta no porto 6789");

				ServerSocket listenSocket = new ServerSocket(Integer.parseInt(prop.getProperty("tcpPort")));
				System.out.println("LISTEN SOCKET = " + listenSocket);

				while (true) {
					byte[] buffer = new byte[1000];
					DatagramPacket request = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(request);
					System.out.println("Server Recebeu: " + (new String(request.getData(), 0, request.getLength())));

					DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(),
							request.getAddress(), request.getPort());
					aSocket.send(reply);

					Socket clientSocket = listenSocket.accept();
					System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);

					(new ClientConnection(clientSocket, ri)).run(); // Client
																	// Thread

				}
			} else {

			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException io) {
			System.out.println("Listen: " + io.getMessage());
		} catch (NotBoundException nb) {
			System.out.println("Bound: " + nb.getMessage());
		}

	}

	public static boolean setToPrimary() {
		String ipUdp = "127.0.0.1";
		System.out.println("teste!");
		try {
			aSocket = new DatagramSocket();
			while (true) {
				try {
					int estado = 0;
					String texto = "teste";
					byte[] m = texto.getBytes();

					InetAddress aHost = InetAddress.getByName(ipUdp);
					int serverPort = 6789;
					DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
					System.out.println("ping!");
					for (int i = 0; i < 5; i++) {
						aSocket.send(request);
						byte[] buffer = new byte[1000];
						DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
						aSocket.setSoTimeout(1000);
						try {
							aSocket.receive(reply);
							estado = 1;
						} catch (IOException E) {
							System.out.println("reply: " + new String(reply.getData(), 0, reply.getLength()));
						}
					}
					if (estado == 0) {
						return true;
					}

				} catch (SocketException e) {
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SocketException e) {
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
		return false;

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

}
