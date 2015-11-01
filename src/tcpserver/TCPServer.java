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
			 * Verifica se já existe um servidor a correr tentando criar uma
			 * socket. Se criar uma socket funcionar então fecha a socket de
			 * teste e começa a thread do servidor secundário.
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
