package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {
	

	public boolean login(String email, String password) throws RemoteException;
	

	public boolean register(String name,String email, String password) throws RemoteException;


	public boolean createProject(String name, String description, String date, String goal,String userEmail) throws RemoteException;

}
