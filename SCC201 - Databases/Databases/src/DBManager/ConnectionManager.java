package DBManager;

import java.sql.*;

/**
* @author Chris Foxley-Evans
 * 
 * This class is designed to manage the connections to the DB server
 * To connect to the server call the constructor with no string
 * To connect to a specific DB call the constructor passing in the name of the DB
 */
public class ConnectionManager
{
	//instance vars
	/**
	 * Holds the URL to the local server
	 */
	static final String URL = "jdbc:mysql://localhost/";

	/**
	 * The connection that will be managed by the instance 
	 */
	public Connection connection;
	
	/**
	 * The final URL that will be used to generate the connection 
	 */
	public String finalURL;
	
//******************************************************************************
	//constructors
	/**
	 * Constructor to connect to the server directly 
	 */
	public ConnectionManager()
	{
		this.finalURL = URL;
		this.connect();
	}
	
	/**
	 * Constructor to connect to a specific DB on the server
	 * 
	 * @param dbName The name of the DB that is to be connected to
	 */
	public ConnectionManager(String dbName)
	{
		//set the url for the db connection
		this.finalURL = URL + dbName;
	}
	
//**********************************************************************************
	//methods
	/**
	 * Method that will establish the connection 
	 * this is called as part of the construction process
	 */
	public void connect()
	{
		//create new connection
		Connection con = null;
		try
		{
			//get a new connection to the database
			con = DriverManager.getConnection(finalURL, "root", null);
			//con.setAutoCommit(false);
		} 
		catch (SQLException e)
		{
			//recover from errors
			System.out.println("Connection to " + this.finalURL + " Unsuccessful.....");
			System.out.println(e.getMessage());
			return;
		}
		//print out success set objects connection object to new connection
		System.out.println("Connection to " + this.finalURL + " Successful.....");
		this.connection = con;
	}

	/**
	 * Method to close the connection to the DB server
	 */
	public void closeConnection()
	{
		try
		{
			//call the close methods on the connection
			this.connection.close();
			System.out.println("Connection to " + this.finalURL + " Closed.....");
		} 
		catch (SQLException e)
		{
			//print error message
			e.printStackTrace();
		}
	}

}
