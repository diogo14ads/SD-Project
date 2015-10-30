package rmi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Properties;

import common.RMIInterface;

public class RMIServer extends UnicastRemoteObject implements RMIInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2844437670470761818L;
	
	private DatabaseConnection dbCon = null;
	
	static Properties prop = new Properties();

	protected RMIServer() throws RemoteException{
		this.dbCon = new DatabaseConnection();
	}
	
	public boolean login(String email, String password) throws RemoteException
	{
		return dbCon.checkLogin(email,password);
	}
	
	public boolean register(String name, String email, String password) throws RemoteException
	{
		return dbCon.registerAccount(name,email,password);
	}
	
	public static void main(String[] args) throws RemoteException{
		RMIInterface ri = new RMIServer();
		String rmiRegistry = prop.getProperty("rmiRegistry");
		System.out.println("TESTE\n"+rmiRegistry+"\nTESTE");
		String rmiLookup = prop.getProperty("rmiLookup");
		LocateRegistry.createRegistry(Integer.parseInt(rmiRegistry)).rebind(rmiLookup, ri);
		System.out.println("RMI Server ready...");
	}
	
	public static void readProperties(){

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
