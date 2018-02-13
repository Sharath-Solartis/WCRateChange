package Solartis.test.RateChanger;

import java.sql.SQLException;
import Solartis.test.Common.DatabaseOperation;
import Solartis.test.CommonException.DatabaseException;
import Solartis.test.CommonException.POIException;
import Solartis.test.exception.RateChangeException;

public class RateChanger 
{
	private static DatabaseOperation DB = null;
	private static ExcelDataExtractor excelExtract =null;
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws DatabaseException, SQLException, POIException, RateChangeException 
	{
		System.setProperty("jsse.enableSNIExtension", "false");
		DB = new DatabaseOperation();

		DB.ConnectionSetup(System.getProperty("JDBC_DRIVER"), System.getProperty("DB_URL"), System.getProperty("USER"), System.getProperty("password"));
		excelExtract.ExcelExtractor(System.getProperty("ExcelPath"),DB);
		
		//DB.ConnectionSetup("com.mysql.jdbc.Driver", "jdbc:mysql://192.168.1.228:3306/test", "root", "password");
		//excelExtract.ExcelExtractor("C:\\Users\\sabarinath_jv.SOLARTISTECH\\Desktop\\WCRate.xls",DB);
		DB.CloseConn();
	}
}
