package client;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

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
				if(op.equals("4"))
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
