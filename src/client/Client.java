package client;

import java.util.Scanner;

public class Client {
	ServerConnection servConn;
	Scanner sc;
	
	public Client()
	{
		this.servConn = new ServerConnection();
		this.sc = new Scanner(System.in);
	}
	
	public void printMenu()
	{
        System.out.println("");
        System.out.println("Main Menu");
        System.out.println("---------------------------");
        System.out.println("1. Login");
        System.out.println("0. Exit the program");
        System.out.println("----------------------------");
        System.out.println("");
        System.out.print("Please select an option from 1-2");
        System.out.println("");
        System.out.println("");
	}
	
	private void launch()
	{
		while(true)
		{
			String op;
			printMenu();
			op = sc.nextLine();
			if(op.equals("1"))
			{
				servConn.login();
			}
			else if(op.equals("0"))
			{
				break;
			}
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.launch();
	}
}
