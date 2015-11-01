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
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

import common.RMIInterface;

public class TCPServer {

	static Properties prop = new Properties();
	static boolean startAsPrimary = false;
	static DatagramSocket aSocket = null;
	static Socket testSocket;

	public static void main(String[] args) {
		readProperties();
		new TCPServer();
	}

	public TCPServer() {
		try {

			RMIInterface ri = (RMIInterface) LocateRegistry.getRegistry(4001).lookup("rmi");
			System.out.println("Listening in port: " + prop.getProperty("TcpPort") + "...");
			
			/**
			 * Verifica se já existe um servidor a correr tentando criar uma socket.
			 * Se criar uma socket funcionar então fecha a socket de teste
			 * e começa a thread do servidor secundário.
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
				new TCPServerSec();
				
			} else {
				System.out.println("Server set as: Primary");

				ServerSocket listenSocket = new ServerSocket(Integer.parseInt(prop.getProperty("TcpPort")));
				System.out.println("LISTEN SOCKET = " + listenSocket);
				while (true) {
					Socket clientSocket = listenSocket.accept();
					System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);

					new ClientConnection(clientSocket, ri);

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

}

class TCPServerSec extends Thread {

	static Properties prop = new Properties();
	DatagramSocket udpConection;
	byte[] pingMessage;
	int conectionFail = 0;
	InetAddress hostConection;
	DatagramPacket sender, reciver;
	int counter;
	String[] args = null;

	TCPServerSec() {
		counter = 0;
		readProperties();
		this.start();

	}

	public void run() {

		try {
			udpConection = new DatagramSocket();

			while (true) {

				try {
					pingMessage = "ping".getBytes();
					hostConection = InetAddress.getByName(prop.getProperty("host"));
					sender = new DatagramPacket(pingMessage, pingMessage.length, hostConection,
							Integer.parseInt(prop.getProperty("UDPPort")));
					udpConection.send(sender);
					udpConection.setSoTimeout(2000);
					pingMessage = new byte[1000];
					reciver = new DatagramPacket(pingMessage, pingMessage.length);
					udpConection.receive(reciver);
					System.out.println("[Backup Server] Recebi esta mensagem do Serviodr Principal: "
							+ new String(reciver.getData(), 0, reciver.getLength()));

					Thread.sleep(2000);

				} catch (SocketTimeoutException e) {
					if (counter < Integer.parseInt(prop.getProperty("maxTries"))) {
						System.out.println("[Backup Server] Não recebi ping do Servidor Principal.");
						counter++;

					} else {
						System.out
								.println("[Backup Server] O Servidor principal está em baixo, vouassumir o controlo.");
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
