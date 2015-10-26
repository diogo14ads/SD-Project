package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {
	

	public boolean login(String email, String password) throws RemoteException;

}
