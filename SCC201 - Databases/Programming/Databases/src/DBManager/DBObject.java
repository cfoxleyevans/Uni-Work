package DBManager;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author Chris Foxley-Evans
 * 
 * This class is designed to represent a DB thus it holds a reference to a ConnectionManager
 * this class also holds methods that are used to collect a DB meta data 
 * as well as a static method to perform the set up of the "schemastore" DB
 */
public class DBObject
{
	//instance vars
	/**
	 * The ConnectionManager for this DBObject
	 */
	public ConnectionManager connectionManager;
	
	/**
	 * The meta data for this DBObject
	 */
	public DatabaseMetaData metaData;
	
	/**
	 * ArrayList to store the names of the tables in this db
	 */
	public ArrayList<String> tableNames;

	/**
	 * The name of the DB that is being connected to
	 */
	public String dbName;
	
//**********************************************************************************
	//static vars
	/**
	 * ArrayList to hold all of the database names on the server 
	 */
	public static ArrayList<String> DBNames;
	
//*************************************************************************************
	//constructors
	/**
	 * Constructor to connect to the server directly 
	 */
	public DBObject()
	{
		//initialise the connection manager for this object 
		this.connectionManager = new ConnectionManager();
	}
	
	/**
	 * Constructor to connect to a specific DB on the server
	 * 
	 * @param dbName The name of the DB that is to be connected to
	 */
	public DBObject(String dbName)
	{
		//as we are connecting to a specific db
		//inint vars that we might need
		this.connectionManager = new ConnectionManager(dbName);
		this.dbName = dbName;
		this.metaData = null;
		this.tableNames = new ArrayList<String>();
	}
	
//******************************************************************************************	
	//methods
	/**
	 * Method to get the meta data object for this db
	 */
	public void getMetaData()
	{
		try
		{
			//get the meta data from the db
			this.metaData = this.connectionManager.connection.getMetaData();
		}
		catch (SQLException e)
		{
			//print out error message
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Method to get the table names for this db
	 */
	public void getTableNames()
	{
		try
		{
			//get the table name from the meta data
			ResultSet tables = this.metaData.getTables(null, null, "%", null);
			while (tables.next())
			{
				//add tables to the ArrayList
				this.tableNames.add(tables.getString("TABLE_NAME"));
			}
		}
		catch (SQLException e)
		{
			//print out error message
			System.out.println(e.getMessage());
		}
		//System.out.println();
	}
	
	/**
	 * Method to get the cols for each table in the db
	 */
	public void getColNames()
	{
		//local variabe init
		ArrayList<String> primKeys = new ArrayList<String>();
		ArrayList<String> forKeys = new ArrayList<String>();
		ArrayList<String> forTables = new ArrayList<String>();
		String colName = "NULL";
		String fkTableName = "NULL";
		String fkFieldName = "NULL";
		String isPKey = "NO";
		String isFKey = "NO";
		
		//for all of the tables in the db
		for (String table : this.tableNames)
		{
			//get the key info for this table
			try
			{
				//get the primary keys for this table
				ResultSet pkeys = this.metaData.getPrimaryKeys(null, null, table);
				while(pkeys.next())
				{
					primKeys.add(pkeys.getString("COLUMN_NAME"));
				}
			
				//get the forigen keys for this table
				ResultSet fkeys = this.metaData.getImportedKeys(null, null, table);
				while(fkeys.next())	
				{
					forKeys.add(fkeys.getString("PKCOLUMN_NAME"));
					forTables.add(fkeys.getString("PKTABLE_NAME"));
				}
			}
			catch(SQLException e)
			{
				System.out.println(e.getMessage());
			}
			
			//get the info from the cols for this table
			try
			{
				//get the cols info
				ResultSet cols = this.metaData.getColumns(null, null, table, "%");
				
				//while ther are more cols
				while (cols.next())
				{	
					//extract the col name
					colName = cols.getString("COLUMN_NAME");
					//is it a pkey
					if(primKeys.contains(colName))
						isPKey = "YES";
					//is it an fkey
					if(forKeys.contains(colName))
					{
						isFKey = "YES";
						fkTableName = forTables.get(forKeys.indexOf(colName));
						fkFieldName = forKeys.get(forKeys.indexOf(colName));
					}
						
					//System.out.println(this.dbName + " " + table + " " + cols.getString(4) + " " +  cols.getString("TYPE_NAME") + " " + cols.getInt("COLUMN_SIZE") +  
							//" " + isPKey + " " + isFKey + " " + fkTableName + " " + fkFieldName);
					
					//tell it which db to use
					String sql = "use schemastore;";
					this.executeUpdate(sql);
					
					//build the row
					sql = "INSERT INTO schemainfo VALUES (\"" + this.dbName + "\",\"" + table + "\",\"" + colName + "\",\"" + cols.getString("TYPE_NAME") + 
					 "\",\"" + cols.getInt("COLUMN_SIZE") + "\",\"" + isPKey + "\", \"" + isFKey + "\", \"" + fkTableName + "\", \"" + fkFieldName + "\")";
					System.out.println(sql);
					//insert the row
					this.executeUpdate(sql);
				}
			}
			catch (SQLException e)
			{
				//print error
				System.out.println(e.getMessage());
			}
			//System.out.println();
		}
	}
	
	/**
	 * Method that take an sql string and runs it on the server
	 * 
	 * @param sql the sql string that is to be run
	 */
	public void executeUpdate(String sql)
	{
		try
		{
			//create a statment 
			Statement ST = this.connectionManager.connection.createStatement();
			//execute the statment
			ST.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			//print error message
			System.out.println(e.getMessage());
		}	
	}
	
//*******************************************************************************************	
	//static methods
	/**
	 * Static method to get a list of all the db names on the server
	 * @return a ArrayList containing all of the db names
	 */
	public static ArrayList<String> getDBNames()
	{
		System.out.println();
		
		//arraylist to hold the db names
		ArrayList<String> dbNames = new ArrayList<String>();
		
		//temp meta var
		DatabaseMetaData meta;
		//new connection
		ConnectionManager connection = new ConnectionManager();
		
		try
		{
			//get the table names
			meta = connection.connection.getMetaData();
			ResultSet dbs = meta.getCatalogs();
			System.out.println("Databases on the server");
			while (dbs.next())
			{
				//print table names and add them to the arraylist
				System.out.println(dbs.getString("TABLE_CAT"));
				dbNames.add(dbs.getString("TABLE_CAT"));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		//close connection and retrun names
		connection.closeConnection();
		System.out.println();
		return dbNames;
	}

	/**
	 * Static method that can be called to create the "schemastore" DB 
	 */
	public static void createSchemastoreDB()
	{
		try
		{
			//create new connection to the server
			ConnectionManager con = new ConnectionManager();
			
			//create new statment
			Statement statment = con.connection.createStatement();

			//drop the db if it all redy exists
			String sql = "drop database if exists schemastore;";
			statment.execute(sql);

			//create the db
			sql = "create database schemastore;";
			statment.execute(sql);

			//use the new db
			sql = "use schemastore;";
			statment.execute(sql);
			
			//create the schemastore table
			sql = "create table schemainfo(dbname varchar(255), tname varchar(255), "
					+ "fname varchar(255), primary key (dbname, tname, fname), typename varchar(255), size int(5), isPK varchar(255), isFK varchar(255) ,fktable varchar(255), fkfield varchar(255)) "
					+ "engine = InnoDB";
			statment.execute(sql);

			//print confirmation
			System.out.println("The schemastore db and the schemainfo table have been created");
			
			//close the connection
			con.closeConnection();
		}
		catch (SQLException e)
		{
			//print out error message
			System.out.println(e.getMessage());
		}
	}
}
