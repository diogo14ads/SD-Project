package client;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

import common.DatabaseRow;
import javafx.scene.chart.PieChart.Data;

public class Client {
	ServerConnection servConn;
	Scanner sc;
	boolean loggedIn;
	
	public Client()
	{
		this.servConn = new ServerConnection();
		this.sc = new Scanner(System.in);
		this.loggedIn = false;
	}
	
	public void printGuestMenu()
	{
        System.out.println("");
        System.out.println("Main Menu");
        System.out.println("---------------------------");
        System.out.println("1. Login");
        System.out.println("2. Register Account");
        System.out.println("3. View Current Projects");
        System.out.println("4. View Past Projects");
        System.out.println("0. Exit the program");
        System.out.println("----------------------------");
        System.out.println("");
        System.out.print("Please select an option from 1-2");
        System.out.println("");
        System.out.println("");
	}
	
	public void printLoggedMenu()
	{
		System.out.println("");
        System.out.println("Main Menu");
        System.out.println("---------------------------");
        System.out.println("1. View Current Projects");
        System.out.println("2. View Past Projects");
        System.out.println("3. My Account");
        System.out.println("4. Create Project");
        System.out.println("5. My Message");
        System.out.println("0. Logout");
        System.out.println("----------------------------");
        System.out.println("");
        System.out.print("Please select an option from 1-2");
        System.out.println("");
        System.out.println("");
	}
	
	public void printMyAccountMenu()
	{
		System.out.println("My Account");
        System.out.println("---------------------------");
        System.out.println("1. Check Balance");
        System.out.println("2. Check Rewards");
        System.out.println("3. Manage Projects");
        System.out.println("0. <- Back");
        System.out.println("----------------------------");
        System.out.println("");
        System.out.print("Please select an option from 0-3");
        System.out.println("");
        System.out.println("");
	}
	
	public void printManageProjectsMenu()
	{
		System.out.println("Project Manager");
        System.out.println("---------------------------");
        System.out.println("1. Manage Levels");
        System.out.println("2. Cancel Project");
        System.out.println("3. Appoint New Administrator");
        System.out.println("0. <- Back");
        System.out.println("----------------------------");
        System.out.println("");
        System.out.print("Please select an option from 0-3");
        System.out.println("");
        System.out.println("");
	}
	
	public void printManageLevelsMenu()
	{
		System.out.println("Manage Levels");
        System.out.println("---------------------------");
        System.out.println("1. Add Level");
        System.out.println("2. Remove Level");
        System.out.println("3. Edit Level");
        System.out.println("0. <- Back");
        System.out.println("----------------------------");
        System.out.println("");
        System.out.print("Please select an option from 0-3");
        System.out.println("");
        System.out.println("");
	}
	
	public void printEditLevelMenu()
	{
		System.out.println("Edit Level");
        System.out.println("---------------------------");
        System.out.println("1. Add Reward");
        System.out.println("2. Remove Reward");
        System.out.println("3. Change Goal");
        System.out.println("0. <- Back");
        System.out.println("----------------------------");
        System.out.println("");
        System.out.print("Please select an option from 0-3");
        System.out.println("");
        System.out.println("");
	}
	
	public void manageLevelsMenu(int projectId)
	{

		String op = null;
		
		
		
		while(true)
		{

			printManageLevelsMenu();
			
			op = sc.nextLine();
			
			if(op.equals("1"))
			{
				addLevel(projectId);
			}
			else if(op.equals("2"))
			{
				removeLevel(projectId);
			}
			else if(op.equals("3"))
			{
				editLevelMenu(projectId);
			}
			else if(op.equals("0"))
			{
				break;
			}
			else
			{
				System.out.println("Please choose a valid option!");
			}
		}
		
	}
	
	private void removeLevel(int projectId) {
		int levelId = chooseLevel(servConn.getProjectLevels(projectId),false);
		
		if(levelId < 1) //nível zero nao pode ser eliminado (é onde está guardado o limite base a atingir pelo projecto)
		{
			System.out.println("There was an unexpected problem!");
		}
		else
		{
			
			servConn.removeLevel(levelId);
		}
		
	}

	private void addLevel(int projectId) {
		
		int goal;
		
		System.out.println("Insert goal for the level: ");
		goal = sc.nextInt();
		sc.nextLine(); //flush
		
		servConn.addLevel(projectId,goal);
		
	}

	private void editLevelMenu(int projectId) 
	{
		String op = null;
		int levelId = chooseLevel(servConn.getProjectLevels(projectId), true);
		
		if(levelId < 0)
		{
			System.out.println("There was an unexpected problem!");
		}
		else if(levelId >= 0) //se for zero volta para trás
		{
			
			while(true)
			{

				printEditLevelMenu();
				
				op = sc.nextLine();
				
				if(op.equals("1"))
				{
					addReward(projectId,levelId);
				}
				else if(op.equals("2"))
				{
					removeReward(projectId,levelId);
				}
				else if(op.equals("3"))
				{
					changeGoal(projectId,levelId);
				}
				else if(op.equals("0"))
				{
					break;
				}
				else
				{
					
				}
			}
		}
	}

	private void removeReward(int projectId, int levelId) {
		// TODO Auto-generated method stub
		int rewardId = chooseReward(servConn.getLevelRewards(projectId,levelId));
		
		if(rewardId < 0)
		{
			System.out.println("There was an unexpected problem!");
		}
		else
		{
			
			servConn.removeReward(rewardId);
		}
	}

	private int chooseReward(ArrayList<DatabaseRow> table) 
	{
		int op = -1;
		
		if(table != null & table.size()>0)
		{
			System.out.println("Rewards: ");
			
			for(int i=0;i<table.size();i++)
			{
				System.out.println((i+1)+": ( "+table.get(i).getColumns().get(2)+"$ ) "+table.get(i).getColumns().get(1));
			}
			System.out.println("0: <- Back");
			System.out.println("Please select a reward from 1-"+table.size()+": ");
			System.out.println();
			System.out.println();
			
			while(true)
			{
				try {
					op = sc.nextInt();

					sc.nextLine(); //flush
					if(op<0 || op>table.size())
						System.out.println("Invalid option!\nPlease enter the your option again: ");
					else
						break;
				} catch (InputMismatchException e) {
					System.out.println("Invalid option!\nPlease enter the your option again: ");
					System.out.println();
					System.out.println();
					
				}
			}
			
			if(op!=0)
				return Integer.parseInt(table.get(op-1).getColumns().get(0)); //para obter o id real da reward
			else
				return 0;
			
		}
		else if(table.size()==0)
		{
			System.out.println("This level does not have rewards"); //isto nunca deve acontecer
			return 0;
		}
		else
			return -1;
	}


	private void addReward(int projectId, int levelId) {
		
		String description;
		int value;
		
		System.out.println("Insert the description of the reward: ");
		description = sc.nextLine();
		
		System.out.println("Insert the value of the reward: ");
		while(true)
		{
			try{
				value = sc.nextInt();
				sc.nextLine(); //flush
				break;
			} catch(InputMismatchException e){
				System.out.println("Invalid Option!\nPlease insert a numeric value: ");
			}
		}
		
		servConn.addReward(projectId,levelId,description,value);
		
		
	}

	private void changeGoal(int projectId, int levelId) 
	{
		int goal;
		
		System.out.println("Insert new goal for this level: ");
		
		while(true)
		{
			try{
				goal = sc.nextInt();
				sc.nextLine(); //flush
				break;
			} catch(InputMismatchException e){
				System.out.println("Invalid Option!\nPlease insert a numeric value: ");
			}
		}
		
		servConn.changeGoal(projectId,levelId,goal);
		
	}

	private int chooseLevel(ArrayList<DatabaseRow> table, boolean includeZero)
	{
		int op = -1;
		
		if(table != null & table.size()>0)
		{
			System.out.println("Reward Levels: ");
			
			for(int i=0;i<table.size();i++)
			{
				if(table.get(i).getColumns().get(0).equals("0"))
				{
					if(!includeZero) //se for para apagar niveis, exclui o zero porque nao pode ser apagado
					{
						table.remove(i);
						i--;
					}
					else
						System.out.println((i+1)+": "+table.get(i).getColumns().get(1));
						
				}
				else
					System.out.println((i+1)+": "+table.get(i).getColumns().get(1));
			}
			System.out.println("0: <- Back");
			System.out.println("Please select a level from 1-"+table.size()+": ");
			System.out.println();
			System.out.println();
			
			while(true)
			{
				try {
					op = sc.nextInt();
					sc.nextLine(); //flush
					if(op<0 || op>table.size())
						System.out.println("Invalid option!\nPlease enter the your option again: ");
					else
						break;
				} catch (InputMismatchException e) {
					System.out.println("Invalid option!\nPlease enter the your option again: ");
					System.out.println();
					System.out.println();
					
				}
			}
			
			if(op!=0)
				return Integer.parseInt(table.get(op-1).getColumns().get(0)); //para obter o id real do projecto
			else
				return 0;
			
		}
		else if(table.size()==0)
		{
			System.out.println("This project does not have reward levels"); //isto nunca deve acontecer
			return 0;
		}
		else
			return -1;
		
	}
	
	public void manageProjectsMenu()
	{
		int projectId;
		String op = null;
		projectId = chooseMyProject(servConn.getMyProjectList());
		
		if(projectId > 0)
		{
			System.out.println("Escolheu: "+projectId);
			
			
			
			while(true)
			{

				printManageProjectsMenu();
				
				op = sc.nextLine();
				
				if(op.equals("1"))
		        {
		        	manageLevelsMenu(projectId);
		        }
		        else if(op.equals("2"))
		        {
		        	
		        }
		        else if(op.equals("3"))
		        {
		        	addAdministrator(projectId);
		        }
				else if (op.equals("0")) {
					break;
				}
				else
				{
					System.out.println("Please choose a valid option!");
				}
			}
			
		}
		else
		{
			//TODO
			System.out.println("There was an unexpected problem!");
		}
		
	}
	
	private void addAdministrator(int projectId) {
		String email;
		int value;
		
		System.out.println("Insert the email of the new admnistrator: ");
		email = sc.nextLine();
		
		servConn.addAdministrator(projectId,email);
		
	}

	private int chooseMyProject(ArrayList<DatabaseRow> table) {
		int op = -1;
		
		if(table!= null && table.size()>0)
		{
			System.out.println("My Projects:");
			
			for(int i=0;i<table.size();i++)
			{
				System.out.println((i+1)+": "+table.get(i).getColumns().get(1));
			}
			System.out.println("Please select a project from 1-"+table.size()+": ");
			System.out.println();
			System.out.println();
			
			while(true)
			{
				try {
					op = sc.nextInt();
					sc.nextLine(); //flush
					if(op<0 || op>table.size())
						System.out.println("Invalid option!\nPlease enter the your option again: ");
					else
						break;
				} catch (InputMismatchException e) {
					System.out.println("Invalid option!\nPlease enter the your option again: ");
					System.out.println();
					System.out.println();
					
				}
			}
			
			return Integer.parseInt(table.get(op-1).getColumns().get(0)); //para obter o id real do projecto
			
		}
		else if(table.size()==0)
		{
			System.out.println("You don't have any projects.");
			return 0;
		}
		else {
			return -1;
		}
		//TODO tratar erros, e simplificar este funçao
		
	}

	public void askLoginData()
	{
		ArrayList<String> loginData = new ArrayList<>();
		
		System.out.println("Enter e-mail:");
		loginData.add(sc.nextLine());
		
		System.out.println("Enter password:");
		loginData.add(sc.nextLine());
		
		if(servConn.login(loginData))
		{
			System.out.println("Valid");
			loggedIn = true;
		}
		else
		{
			System.out.println("Invalid");
		}
	}
	
	public void askRegisterData()
	{
		ArrayList<String> registerData = new ArrayList<>();
		
		System.out.println("Enter your first and last name: ");
		registerData.add(sc.nextLine());
		
		System.out.println("Enter your e-mail: ");
		registerData.add(sc.nextLine());
		
		System.out.println("Enter the desired password: ");
		registerData.add(sc.nextLine());
		
		if(servConn.registerAccount(registerData))
		{
			System.out.println("Register successfull.");
			loggedIn = true;
		}
		else
		{
			System.out.println("Register failed!");
		}
	}
	

	private void askProjectData() {
		
		//start date is system date
		ArrayList<String> projectData = new ArrayList<>();
		String limitDate;
		
		System.out.println("Insert project name: ");
		projectData.add(sc.nextLine());
		
		System.out.println("Describe your project: ");
		projectData.add(sc.nextLine());
		
		System.out.println("Insert date limit (dd/mm/yyyy hh:mm): ");
		limitDate = sc.nextLine();
		while(!isDate(limitDate))
		{
			limitDate = sc.nextLine();
		}
		projectData.add(limitDate);
		
		//TODO proteger o input
		System.out.println("Insert the money goal: ");
		projectData.add(Integer.toString(sc.nextInt()));
		sc.nextLine(); //flush
		
		servConn.createProject(projectData);
		
	}
	
	public void myAccount()
	{
		String op;
		op = "";
		
		
		while(true)
		{
			printMyAccountMenu();
			
			op = sc.nextLine();
			
			if(op.equals("1"))
			{
				System.out.println("Current Balance: "+servConn.checkBalance());
			}
			else if(op.equals("2"))
			{
				checkRewards();
			}
			else if(op.equals("3"))
			{
				manageProjectsMenu();
			}
			else if(op.equals("0"))
			{
				break;
			}
			else
			{
				System.out.println("Please choose a valid option!");
			}
		}
	}
	
	
	private void checkRewards() {
		printMyRewards(servConn.checkMyRewards());
		
	}

	private void printMyRewards(ArrayList<DatabaseRow> table) {
		String status;
		
		for(int i=0;i<table.size();i++)
		{
			if(table.get(i).getColumns().get(2).equals("t"))
				status = "Pending";
			else
				status = "Successfull";
			System.out.println((i+1)+": ( "+table.get(i).getColumns().get(3)+"$ ) "+table.get(i).getColumns().get(0)+" | Status: "+status);
		}
		
		System.out.println("\n<Enter> to continue");
		sc.nextLine();
	}

	public void launch()
	{
		while(true)
		{
			String op;
			
			if(!loggedIn)
				printGuestMenu();
			else
				printLoggedMenu();
			
			op = sc.nextLine();
			
			if(!loggedIn)
			{
				if(op.equals("1"))
				{
					askLoginData();
				}
				else if(op.equals("2"))
				{
					askRegisterData();
				}
				else if(op.equals("3"))
				{
					viewCurrentProjects();
				}
				else if(op.equals("4"))
				{
					viewPastProjects();
				}
				else if(op.equals("0"))
				{
					servConn.closeConnection();
					break;
				}
			}
			
			else{
				if(op.equals("1"))
				{
					viewCurrentProjects();
				}
				else if(op.equals("2"))
				{
					viewPastProjects();
				}
				else if(op.equals("3"))
				{
					myAccount();
				}
				else if(op.equals("4"))
				{
					askProjectData();
				}
				else if(op.equals("0"))
				{
					loggedIn=false;
				}
			}
			
		}
	}
	
	private void viewPastProjects() {
		ArrayList<DatabaseRow> table = servConn.getProjects(false); //false significa past projects
		int index = chooseProject(table);
		
		printProjectInfo(table.get(index).getColumns(),false);
		
	}

	private void viewCurrentProjects() {
		ArrayList<DatabaseRow> table = servConn.getProjects(true); //true significa current projects
		int index = chooseProject(table);
		
		printProjectInfo(table.get(index).getColumns(),true);
		
	}
	
	private void printProjectInfo(ArrayList<String> columns, boolean isCurrent) {
		int projectId = Integer.parseInt(columns.get(0));
		String need, expire, success;
		String pledge;
		
		if(isCurrent) //Para alterar o tempo verbal
		{
			need = "needs";
			expire = "Expires";
		}
		else
		{
			need = "needed";
			expire = "Expired";
		}
		
		if(!isCurrent && Integer.parseInt(columns.get(3))<Integer.parseInt(columns.get(4)))
		{
			success = "FAILED,";
		}
		else if(!isCurrent && Integer.parseInt(columns.get(3))>=Integer.parseInt(columns.get(4)))
			success = "SUCCEEDED,";
		else {
			success = "";
		}
		
		ArrayList<DatabaseRow> table;
		System.out.println();
		System.out.println("================================================================");
		System.out.println();
		System.out.println("Project - "+columns.get(2)+" : \t\t"+columns.get(5));
		System.out.println("Money raised: \t\t\t\t"+columns.get(3)+" ("+success+" "+need+" at least "+columns.get(4)+"$ )"); //TODO pôr em percentagem
		System.out.println(expire+" at: \t\t\t\t"+columns.get(1));
		
		table = servConn.getProjectLevels(projectId);
		
		printLevelInfo(table,projectId);
		
		System.out.println();
		System.out.println("================================================================");
		System.out.println();
		
		if(isCurrent)
		{
			System.out.println("Do you want to pledge money for this project? ( (Y)es / (N)o )");
			while(true)
			{
				pledge = sc.nextLine();
				if(pledge.toUpperCase().equals("Y"))
				{
					pledgeMoney(projectId);
					break;
				}
				else if(pledge.toUpperCase().equals("N"))
					break;
				else
					System.out.println("Please answer (Y)es or (N)o: ");
					
			}
		}
		else
		{
			System.out.println("\n<Enter> to continue");
			sc.nextLine();
		}
		
		
	}

	private void pledgeMoney(int projectId) {
		int rewardId = chooseReward(servConn.getActiveRewards(projectId));
		
		servConn.buyReward(rewardId);
		
	}

	private void printLevelInfo(ArrayList<DatabaseRow> table, int projectId) {
		int index;
		for(index=0; !(table.get(index).getColumns().get(0)).equals("0") ;index++);
		
		System.out.println("\nStandard Rewards: ");
		printRewards(Integer.parseInt(table.get(index).getColumns().get(0)),projectId);
		System.out.println();
		
		if(table.size()>1)
		{
			System.out.println("Reward Levels: ");
		
			for(int i=0;i<table.size();i++)
			{
				if(!table.get(i).getColumns().get(0).equals("0"))
				{
					System.out.println("-> "+table.get(i).getColumns().get(1)); //TODO ordenar por valor?
					printRewards(Integer.parseInt(table.get(i).getColumns().get(0)),projectId);
				}
			}
		}
		else
			System.out.println("This projects does not have reward levels");
		
	}

	private void printRewards(int levelId, int projectId) {
		ArrayList<DatabaseRow> table = servConn.getLevelRewards(projectId,levelId);
		
		if(table.size() == 0 )
			System.out.println("- No Rewards");
		
		for(int i=0;i<table.size();i++)
		{
			System.out.println((i+1)+": ( "+table.get(i).getColumns().get(2)+"$ ) "+table.get(i).getColumns().get(1));
		}
		
	}

	private int chooseProject(ArrayList<DatabaseRow> table) {
		int op = -1;
		
		if(table!= null && table.size()>0)
		{
			System.out.println("Projects:");
			
			for(int i=0;i<table.size();i++)
			{
				System.out.println((i+1)+": \t( "+table.get(i).getColumns().get(1)+"$ ) \t"+table.get(i).getColumns().get(2)+" ");
			}
			System.out.println("Please select a project from 1-"+table.size()+": ");
			System.out.println();
			System.out.println();
			
			while(true)
			{
				try {
					op = sc.nextInt();
					sc.nextLine(); //flush
					if(op<0 || op>table.size())
						System.out.println("Invalid option!\nPlease enter the your option again: ");
					else
						break;
				} catch (InputMismatchException e) {
					System.out.println("Invalid option!\nPlease enter the your option again: ");
					System.out.println();
					System.out.println();
					
				}
			}
			
			return op-1; //para obter o id real do projecto
			
		}
		else if(table.size()==0)
		{
			System.out.println("You don't have any projects.");
			return -1;
		}
		else {
			return -2;
		}
		//TODO tratar erros, e simplificar este funçao
	}

	public boolean isDate(String date)
	{
		boolean result = false;
	    DateFormat df = new SimpleDateFormat("dd/MM/yyyy kk:mm", Locale.ENGLISH);
	    try {
			df.parse(date);
			result = true;
		} catch (ParseException e) {
			System.out.println("Date not well formed! Please insert correct date (hh:mm dd/mm/yyyy): ");
		}
	    
	    return result;
	}
	
	public java.util.Date inputDate(String date){
	    DateFormat df = new SimpleDateFormat("dd/MM/yyyy kk:mm", Locale.ENGLISH);
	    java.util.Date result = null;
		try {
			result = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return result;
		
	}


	public static void main(String[] args) {
		Client client = new Client();
		client.launch();
	}
}
