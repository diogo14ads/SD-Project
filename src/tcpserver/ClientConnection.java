package tcpserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;

import common.RMIInterface;
import common.TCPMessage;
import common.TCPMessageType;

public class ClientConnection extends Thread {
	
	private Socket socket;
	private RMIInterface ri;
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
		this.start();
	}

	@Override
	public void run() {

		while(!socket.isClosed())
		{
			TCPMessage msg = null;
			try {
				msg = (TCPMessage) ois.readObject();
			} catch (ClassNotFoundException e) {
				System.err.println("ClassNotFound: "+e);
				break;
			} catch (IOException e) {
				System.err.println("IOException in socket "+socket.getInetAddress()+": "+e);
				break;
			}
			
			//Verifica request e encaminha para o metodo correspondente
			if(msg != null)
			{
				if(msg.getType() == TCPMessageType.LOGIN_REQUEST)
				{
					//TODO dá para fazer um try/catch para todos?
					try {
						login(msg);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(msg.getType() == TCPMessageType.REGISTER_REQUEST)
				{	
					try {
						register(msg);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}

	private void login(TCPMessage msg) throws IOException {
		
		ArrayList<String> loginData = msg.getStrings(); // index 0: E-mail || index 1: Password
		TCPMessage response = new TCPMessage(TCPMessageType.LOGIN_REQUEST);
		boolean valid = false;
		
		if(!loginData.isEmpty() && loginData.size()==2)
		{
			try {
				valid=ri.login(loginData.get(0), loginData.get(1));
			} catch (RemoteException e) {
				System.err.println("Remore: "+e);
			}
		}
		
		//TODO criar função à parte? por num objecto tipo Mensagem?
		if(valid == true)
		{
			response.getStrings().add("1");
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
				success = ri.register(registerData.get(0), registerData.get(1), registerData.get(2));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(success == true)
		{
			response.getStrings().add("1");
		}
		else {
			response.getStrings().add("0");
		}
		
		oos.writeObject(response);
	}
	
	
}
