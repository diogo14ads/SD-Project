package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RMIInterface extends Remote {
	

	public boolean login(String email, String password) throws RemoteException;
	

	public boolean register(String name,String email, String password) throws RemoteException;


	public boolean createProject(String name, String description, String date, String goal,String userEmail) throws RemoteException;


	public int checkBalance(String activeUser) throws RemoteException;


	public ArrayList<DatabaseRow> myProjectsList(String email) throws RemoteException;


	public ArrayList<DatabaseRow> projectLevelsList(Integer projectId) throws RemoteException;


	public boolean changeLevelGoal(int projectId, int levelId, int goal) throws RemoteException;


	public boolean addLevel(int projectId, int goal) throws RemoteException;


	public boolean addReward(int projectId, int levelId, String description, int value) throws RemoteException;


	public ArrayList<DatabaseRow> levelRewardsList(int projectId, int levelId) throws RemoteException;

	public boolean removeReward(int rewardId) throws RemoteException;

	public boolean removeLevel(int levelId) throws RemoteException;


	public boolean addAdministrator(int projectId, String email) throws RemoteException;


	public ArrayList<DatabaseRow> currentProjectsList() throws RemoteException;


	public ArrayList<DatabaseRow> pastProjectsList() throws RemoteException;


	public ArrayList<DatabaseRow> activeRewardsList(int projectId) throws RemoteException;


	public boolean buyReward(int rewardId, String email) throws RemoteException;


	public ArrayList<DatabaseRow> getMyRewards(String activeUser) throws RemoteException;


	public boolean giveawayReward(int pledgeId, String emailReceiver) throws RemoteException;


	public boolean cancelProject(int projectId) throws RemoteException;


	public boolean sendMessageProject(int projectId, String activeUser, String msg) throws RemoteException;

}
