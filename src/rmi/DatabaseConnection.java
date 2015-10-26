package rmi;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private Connection connection;
	
	public DatabaseConnection()
	{
		try {
			this.connection = getDatabaseConnection();
			System.out.println("Connected to Heroku Database");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return connection;
	}
	
	public void closeConnection()
	{
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Database connection closed!");
	}

	private Connection getDatabaseConnection() throws URISyntaxException, SQLException {
		
		URI dbUri = new URI("postgres://fzekibiamscilk:6zSggqjUA1eNrp9zNKT1hrlpdl@ec2-75-101-162-243.compute-1.amazonaws.com:5432/dujiun5jm8qrj");
		
	    String username = dbUri.getUserInfo().split(":")[0];
	    String password = dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()+"?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
	    
	    return DriverManager.getConnection(dbUrl, username, password);
	}
	
}
