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
		printManageLevelsMenu();
		
		
		
		while(true)
		{
			op = sc.nextLine();
			
			if(op.equals("1"))
			{
				
			}
			else if(op.equals("2"))
			{
				
			}
			else if(op.equals("3"))
			{
				editLevelMenu(projectId);
			}
			else if(op.equals("0"))
			{
				
			}
			else
			{
				System.out.println("Please choose a valid option!");
			}
		}
		
		
	}
	
	private void editLevelMenu(int projectId) 
	{
		String op = null;
		int levelId = chooseLevel(servConn.getProjectLevels(projectId));
		
		if(levelId < 0)
		{
			System.out.println("There was an unexpected problem!");
		}
		else
		{
			
			while(true)
			{

				printEditLevelMenu();
				
				sc.nextLine(); //flush
				op = sc.nextLine();
				
				if(op.equals("1"))
				{
					
				}
				else if(op.equals("2"))
				{
					
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

	private void changeGoal(int projectId, int levelId) 
	{
		int goal;
		
		System.out.println("Insert new goal for this level: ");
		
		while(true)
		{
			try{
				goal = sc.nextInt();
				break;
			} catch(InputMismatchException e){
				System.out.println("Invalid Option!\nPlease insert a numeric value: ");
			}
		}
		
		servConn.changeGoal(projectId,levelId,goal);
		
	}

	private int chooseLevel(ArrayList<DatabaseRow> table)
	{
		int op = -1;
		
		if(table != null & table.size()>0)
		{
			System.out.println("Reward Levels: ");
			
			for(int i=0;i<table.size();i++)
			{
				System.out.println((i+1)+": "+table.get(i).getColumns().get(1));
			}
			System.out.println("Please select a level from 1-"+table.size()+": ");
			System.out.println();
			System.out.println();
			
			while(true)
			{
				try {
					op = sc.nextInt();
					
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
				
				sc.nextLine(); //flush
				
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
				else if(op.equals("0"))
				{
					servConn.closeConnection();
					break;
				}
			}
			
			else{
				if(op.equals("3"))
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
