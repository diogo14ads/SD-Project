package client;

public class Client {
	ServerConnection servConn;
	
	public Client()
	{
		this.servConn = new ServerConnection();
	}
	
	private void launch()
	{
		
	}

	public static void main(String args[])
	{
		Client client = new Client();
		client.launch();
	}
}
