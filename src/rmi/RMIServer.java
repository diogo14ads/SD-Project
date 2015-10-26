package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import common.RMIInterface;

public class RMIServer extends UnicastRemoteObject implements RMIInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2844437670470761818L;
	
	private DatabaseConnection dbCon = null;

	protected RMIServer() throws RemoteException{
		this.dbCon = new DatabaseConnection();
	}
	
	public boolean login(String email, String password) throws RemoteException
	{
		return dbCon.checkLogin(email,password);
	}
	
	public static void main(String[] args) throws RemoteException{
		RMIInterface ri = new RMIServer();
		
		LocateRegistry.createRegistry(4001).rebind("rmi", ri);
		System.out.println("RMI Server ready...");
	}

}
