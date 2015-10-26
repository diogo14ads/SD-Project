package rmi;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	public boolean checkLogin(String email, String password)
	{
		String sqlQuery = null;
		ResultSet result = null;
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			System.err.println("SQL: "+e);
		}
		sqlQuery =
				"select count(*) "
				+ "from user_account "
				+ "where email = '"+email+"' and password='"+password+"'";
		
		
		
		try {
			result = statement.executeQuery(sqlQuery);
			result.next();
			
			if(result.getInt(1)==1)
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	//Rascunho
	/*
	Statement stm = dbCon.getConnection().createStatement();
	String sqlQuery = "select * from user_account";
	
	ResultSet res = stm.executeQuery(sqlQuery);
	res.next();
	System.out.println(res.getString("email"));
	*/
}
