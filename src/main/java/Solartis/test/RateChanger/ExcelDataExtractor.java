package Solartis.test.RateChanger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import Solartis.test.Common.DatabaseOperation;
import Solartis.test.Common.ExcelOperationsPOI;
import Solartis.test.CommonException.DatabaseException;
import Solartis.test.CommonException.POIException;
import Solartis.test.exception.RateChangeException;

public class ExcelDataExtractor 
{
	private static RateChangeUpdater DBupdater = null;
	private static ExcelOperationsPOI poi_sheet = null;
	private static LinkedHashMap<String , String> LHM =null;
	
	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	public static void ExcelExtractor(String ExcelPath, DatabaseOperation dB) throws SQLException, POIException, DatabaseException, RateChangeException
	{		
		poi_sheet = new ExcelOperationsPOI(ExcelPath);
	
		poi_sheet.getsheets("Rate Factors");
		ArrayList ListRateFactors = poi_sheet.getDataFromExcel(0, 0);

		poi_sheet.getsheets("Hazard Group");
		ArrayList ListHazardGroup = poi_sheet.getDataFromExcel(0, 0);
	
		poi_sheet.getsheets("ELPPF");
		ArrayList ListELPPF = poi_sheet.getDataFromExcel(0, 0);
		
		poi_sheet.getsheets("ELAEF");
		ArrayList ListELAEF = poi_sheet.getDataFromExcel(0, 0);
		
		poi_sheet.save();
		
		LHM = new LinkedHashMap<String , String>();
		
		for(int RateFactorRow = 1; RateFactorRow<ListRateFactors.size(); RateFactorRow++)
		{
			LHM =  (LinkedHashMap<String, String>) ListRateFactors.get(RateFactorRow);
			System.out.println(LHM.get("STATE"));
			DBupdater.updateRateFactorDB(dB, LHM);
		}
		
		for(int HazardGrouprow = 1; HazardGrouprow<ListHazardGroup.size(); HazardGrouprow++)
		{	
			LHM =  (LinkedHashMap<String, String>) ListHazardGroup.get(HazardGrouprow);
			System.out.println(LHM.get("STATE"));
			DBupdater.updateHazardGroupDB(dB, LHM);
		}
			
		for(int Elppfrow = 1; Elppfrow<ListELPPF.size(); Elppfrow++)
		{	
			LHM =  (LinkedHashMap<String, String>) ListELPPF.get(Elppfrow);
			System.out.println(LHM.get("STATE"));
			DBupdater.updateElppfs_ElaefDB(dB, LHM, "wc_elppf_conditionaltable");
		}
		
		for(int ELAEFrow = 1; ELAEFrow<ListELAEF.size(); ELAEFrow++)
		{	
			LHM =  (LinkedHashMap<String, String>) ListELAEF.get(ELAEFrow);
			System.out.println(LHM.get("STATE"));
			if(LHM.get("STATE").equals("MA"))
				DBupdater.updateElppfs_ElaefDB(dB, LHM, "wc_elaef_conditionaltable");
		}
	}
}

