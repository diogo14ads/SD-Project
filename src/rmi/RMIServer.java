package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

import common.DatabaseRow;
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
	
	public boolean register(String name, String email, String password) throws RemoteException
	{
		return dbCon.registerAccount(name,email,password);
	}
	
	public boolean createProject(String name, String description, String date, String goal, String userEmail) throws RemoteException
	{
		return dbCon.insertNewProject(name,description,date,goal,userEmail);
	}
	
	public int checkBalance(String activeUser) throws RemoteException
	{
		return dbCon.checkBalance(activeUser);
	}
	
	public ArrayList<DatabaseRow> myProjectsList(String email) throws RemoteException 
	{
		return dbCon.getMyProjectsList(email);
	}

	public ArrayList<DatabaseRow> projectLevelsList(Integer projectId) throws RemoteException 
	{
		return dbCon.getProjectLevelsList(projectId);
	}	

	public boolean changeLevelGoal(int projectId, int levelId, int goal) throws RemoteException {
		return dbCon.changeLevelGoal(projectId,levelId,goal);
		
	}

	public boolean addLevel(int projectId, int goal) throws RemoteException {
		return dbCon.addLevel(projectId, goal);
	}

	public boolean addReward(int projectId, int levelId, String description, int value) throws RemoteException {
		return dbCon.addReward(projectId,levelId,description,value);
	}

	public ArrayList<DatabaseRow> levelRewardsList(int projectId, int levelId) throws RemoteException {
		return dbCon.getLevelRewardList(projectId,levelId);
	}

	public boolean removeReward(int rewardId) throws RemoteException {
		return dbCon.removeReward(rewardId);
	}
	
	public boolean removeLevel(int levelId) throws RemoteException {
		return dbCon.removeLevel(levelId);
	}

	public boolean addAdministrator(int projectId, String email) throws RemoteException {
		return dbCon.addAdministrator(projectId,email);
	}

	public ArrayList<DatabaseRow> currentProjectsList() throws RemoteException {
		return dbCon.currentProjectsList();
	}

	public ArrayList<DatabaseRow> pastProjectsList() throws RemoteException {
		return dbCon.pastProjectsList();
	}
	
	public static void main(String[] args) throws RemoteException{
		RMIInterface ri = new RMIServer();
		
		LocateRegistry.createRegistry(4001).rebind("rmi", ri);
		System.out.println("RMI Server ready...");
	}


}
