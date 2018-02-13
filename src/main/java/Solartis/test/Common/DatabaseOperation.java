package Solartis.test.Common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;

//import Solartis.test.Configuration.PropertiesHandle;
import Solartis.test.CommonException.DatabaseException;

public class DatabaseOperation
{
	private static Connection conn = null;
	private static CallableStatement cs = null;
	private static String JDBC_DRIVER = null;
	private static String DB_URL =null;
	private static String USER=null;
	private static String PASS =null;
	private static String PrepareStatement =null;
	protected String query = null;
	protected Statement stmt = null;
	protected ResultSet rs = null;
	protected int rs_row = 1;
	protected LinkedHashMap<Integer, LinkedHashMap<String, String>> table = null;
	protected ResultSetMetaData meta = null;
	//private static ListIterator<String> cs_listIterator = null;
	
	/*public static void ConnectionSetup(PropertiesHandle config) throws DatabaseException 
	{
		JDBC_DRIVER =config.getProperty("jdbc_driver");
		DB_URL = config.getProperty("db_url");
		USER=config.getProperty("db_username");
		PASS =config.getProperty("db_password");
		if(conn == null)
		{
			try 
			{
				Class.forName(JDBC_DRIVER);
			} 
			catch (ClassNotFoundException e) 
			{
				throw new DatabaseException("ERROR IN JDBC_DRIVER : " + JDBC_DRIVER, e);
			}
			try 
			{
				conn = DriverManager.getConnection(DB_URL,USER,PASS);
			} 
			catch (SQLException e) 
			{
				throw new DatabaseException("ERROR IN DB - URL / USERNAME / PASSWORD", e);	
			}	
		}		
	}*/
	
	public static void ConnectionSetup(String JDBC_DRIVER, String DB_URL, String USER, String password) throws DatabaseException 
	{
		if(conn == null)
		{
			
			try 
			{
				Class.forName(JDBC_DRIVER);
			} 
			catch (ClassNotFoundException e) 
			{
				throw new DatabaseException("ERROR IN JDBC_DRIVER : " + JDBC_DRIVER, e);
			}
			try 
			{
				conn = DriverManager.getConnection(DB_URL,USER,password);
			} 
			catch (SQLException e) 
			{
				throw new DatabaseException("ERROR IN DB - URL / USERNAME / PASSWORD", e);	
			}	
		}		
	}
	
	public static Connection ReturnConnectionSetup(String JDBC_DRIVER, String DB_URL, String USER, String password) throws DatabaseException 
	{
		if(conn == null)
		{
			
			try 
			{
				Class.forName(JDBC_DRIVER);
			} 
			catch (ClassNotFoundException e) 
			{
				throw new DatabaseException("ERROR IN JDBC_DRIVER : " + JDBC_DRIVER, e);
			}
			try 
			{
				conn = DriverManager.getConnection(DB_URL,USER,password);
			} 
			catch (SQLException e) 
			{
				throw new DatabaseException("ERROR IN DB - URL / USERNAME / PASSWORD", e);	
			}	
		}
		return conn;
	}
	
	public static void CloseConn() throws DatabaseException
	{
		try 
		{
			conn.close();
		} 
		catch (SQLException e) 
		{
			throw new DatabaseException("PROBLEM WITH CLOSING DB-CONNECTION", e);
		}
		conn = null;
	}
	
	public LinkedHashMap<Integer, LinkedHashMap<String, String>> GetDataObjects(String query) throws DatabaseException
	{
		this.query = query;
		LinkedHashMap<String, String> row = null;
		try 
		{
			stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
		    rs =    stmt.executeQuery(this.query);
		    table = new LinkedHashMap<Integer, LinkedHashMap<String, String>>();
	        meta = rs.getMetaData();        
	        while (rs.next())
	        {
	        	row = new LinkedHashMap<String, String>();
	            for (int columnIterator = 1; columnIterator <= meta.getColumnCount(); columnIterator++) 
	            {
	                String key = meta.getColumnName(columnIterator);
	                String value = rs.getString(key);
	                row.put(key, value);
	            }
	            table.put(rs_row, row);
	            rs_row = rs_row + 1;   
	        } 
	        return table;  
		} 
		catch (SQLException e) 
		{
			throw new DatabaseException("PROBLEM WITH RESULT-SET OBTAINED FROM DB",e);
		}
		
	}
	
	public void UpdateRow(Integer rowNumber, LinkedHashMap<String, String> row) throws DatabaseException
	{
		
		try 
		{
			rs.first();
		    int rowIterator = 1;
			do
			{
				if(rowNumber == rowIterator)
			    {
					for (int i = 1; i <= meta.getColumnCount(); i++) 
					{  
				       rs.updateString(meta.getColumnName(i), row.get(meta.getColumnName(i)));     
				    }
					rs.updateRow();
			    } 
			 
			    rowIterator++;
			 }while (rs.next());	
		}	
		
		catch (SQLException e) 
		{
			throw new DatabaseException("PROBLEM WITH UPDATE ROW IN DB", e);
		}
	}
	
	
	
	public void UpdateTable(LinkedHashMap<Integer, LinkedHashMap<String, String>> table) throws DatabaseException
	{
		this.table = table;
		LinkedHashMap<String, String> row = null;
		try 
		{
			rs.first();
		    int rowIterator = 1;
			do
			{
				for (int columnIterator = 1; columnIterator <= meta.getColumnCount(); columnIterator++) 
				{  
			       row = table.get(rowIterator);
			       rs.updateString(meta.getColumnName(columnIterator), row.get(meta.getColumnName(columnIterator)));
			    }
			 
			    rs.updateRow();
			    rowIterator++;
			 }while (rs.next());
		}	
		
		catch (SQLException e) 
		{
			throw new DatabaseException("PROBLEM WITH UPDATE ROW IN DB", e);
		}
	}
	
	public void callStoreProcedure(String Table, List<String> Parameter) throws DatabaseException
	{
		String tempVar = "";
		for(int ParaIndex = 1 ; ParaIndex <= Parameter.size() ; ParaIndex++)
		{
			//System.out.println(Parameter.size());
			if(ParaIndex == Parameter.size())
			{
				tempVar = tempVar + "?";
			}
			else
			{
				tempVar = tempVar + "?, ";
			}
		}
		try 
		{
			cs = conn.prepareCall("{call " + Table + " ( " + tempVar + " )}");
			//System.out.println("{call " + Table + " ( " + tempVar + " )}");
			//System.out.println(Parameter.size());
			for(int ParaIndex = 0 ; ParaIndex < Parameter.size() ; ParaIndex++)
			{
				cs.setString((ParaIndex+1), Parameter.get(ParaIndex));
				//System.out.println("cs.setString("+(ParaIndex+1)+", "+Parameter.get(ParaIndex)+");");
			}
			cs.execute();
		} 
		catch (SQLException e) 
		{
			throw new DatabaseException("PROBLEM WITH CALLING STORE PROCEDURE", e);
		}
	}
}
