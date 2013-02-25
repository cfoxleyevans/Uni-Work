package Extensions;

import java.util.ArrayList;
import DBManager.DBObject;

/**
 * @author Chris Foxley-Evans
 * 
 * Wrapper class used to clean up the main method.
 *
 */
public class Wrappers
{
	/**
	 * Method to get encapsulate the calls needed to get a list of the db names from the server
	 * and create DBObjects for each one
	 * 
	 * @return an ArrayList of the DBObjects
	 */
	public static ArrayList<DBObject> getDBObjects()
	{
		ArrayList<DBObject> temp = new ArrayList<DBObject>();
		
		ArrayList<String> names = DBObject.getDBNames();
		
		for (String name : names)
		{
			temp.add(new DBObject(name));
		}
		
		return temp;
	}
	
	/**
	 * Method that encapsulates the entire program
	 */
	public static void runMethods()
	{
		DBObject.createSchemastoreDB();
		
		ArrayList<DBObject> dbs = Wrappers.getDBObjects();
		
		for(DBObject db : dbs)
		{
			System.out.println();
			db.connectionManager.connect();
			db.getMetaData();
			db.getTableNames();
			db.getColNames();
			db.connectionManager.closeConnection();
			System.out.println();
		}
	}

	/**
	 * Method that encapsulates getting all the metadata for all of the databases in the argument
	 * 
	 * @param dbs an ArrayList of the DBObjects
	 */
	public static void getMetaData(ArrayList<DBObject> dbs)
	{
		for(DBObject db : dbs)
		{
			db.connectionManager.connect();
			db.getMetaData();
			db.connectionManager.closeConnection();
		}
	}

	/**
	 * Method that encapsulates getting all of the table names from the metadata
	 * 
	 * @param dbs an ArrayList of the DBObjects
	 */
	public static void getTables(ArrayList<DBObject> dbs)
	{
		for(DBObject db : dbs)
		{
			db.connectionManager.connect();
			db.getTableNames();
			db.connectionManager.closeConnection();
			
		}
	}
	
	/**
	 * Method that encapsulates getting all of the coloumn info out of a table 
	 * and inserting it into the schemastore db
	 * 
	 * @param dbs an ArrayList of the DBObjects
	 */
	public static void getColsAndInsertIntoDB(ArrayList<DBObject> dbs)
	{
		for(DBObject db : dbs)
		{
			db.connectionManager.connect();
			db.getColNames();
			db.connectionManager.closeConnection();
		}
	}
	
}
