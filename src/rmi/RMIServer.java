package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import common.RMIInterface;

public class RMIServer extends UnicastRemoteObject implements RMIInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2844437670470761818L;

	protected RMIServer() throws RemoteException{
		
	}
	
	public boolean login(String email, String password)
	{
		System.out.println("I received some form of elvish from "+email);
		return true;
	}
	
	public static void main(String[] args) throws RemoteException{
		RMIInterface ri = new RMIServer();
		RMIServer rmi = new RMIServer();
		DatabaseConnection dbCon = new DatabaseConnection();
		
		try {
			
			Statement stm = dbCon.getConnection().createStatement();
			String sqlQuery = "select * from user_account";
			
			ResultSet res = stm.executeQuery(sqlQuery);
			res.next();
			System.out.println(res.getString("email"));
			dbCon.closeConnection();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		LocateRegistry.createRegistry(4001).rebind("rmi", ri);
		System.out.println("RMI Server ready...");
	}

}
