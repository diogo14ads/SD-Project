package tcpserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

import common.RMIInterface;
import common.TCPMessage;
import common.TCPMessageType;

public class ClientConnection implements Runnable {
	
	private Socket socket;
	private RMIInterface ri;
	private DataOutputStream dout;
	private DataInputStream din;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public ClientConnection(Socket socket, RMIInterface ri) {
		super();
		this.socket = socket;
		this.ri = ri;
		try {
			//Para receber requests
			this.oos = new ObjectOutputStream(socket.getOutputStream());
			this.ois = new ObjectInputStream(socket.getInputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		while(!socket.isClosed())
		{
			TCPMessage msg = null;
			try {
				msg = (TCPMessage) ois.readObject();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Verifica request e encaminha para o metodo correspondente
			if(msg != null)
			{
				if(msg.getType() == TCPMessageType.LOGIN_REQUEST)
				{
					login();
					break;
				}
			}
		}
		
	}

	private void login() {
		// TODO Auto-generated method stub
		System.out.println("For now, this only says shit");
		
		try {
			ri.login("Big", "Boss");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
