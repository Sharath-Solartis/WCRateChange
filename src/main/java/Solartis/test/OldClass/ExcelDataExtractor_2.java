package Solartis.test.OldClass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import Solartis.test.Common.DatabaseOperation;
import Solartis.test.Common.ExcelOperationsPOI;
import Solartis.test.CommonException.DatabaseException;
import Solartis.test.CommonException.POIException;
import Solartis.test.exception.RateChangeException;

public class ExcelDataExtractor_2 
{
	private static RateChangeUpdater_1 DBupdater = null;
	private static ExcelOperationsPOI poi_sheet = null;
	private static boolean flag ;
	
	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	public static void ExcelExtractor(String ExcelPath, DatabaseOperation dB) throws SQLException, POIException, DatabaseException, RateChangeException
	{
		LinkedHashMap<String , String> LHM_RateFactor = new LinkedHashMap<String , String>();
		LinkedHashMap<String , String> LHM_HazardGroup = new LinkedHashMap<String , String>();
		List<LinkedHashMap<String,String>> List_HazardGroup = null;
		LinkedHashMap<String , String> LHM_Elppf = new LinkedHashMap<String , String>();
		List<LinkedHashMap<String,String>> List_Elppf = null;
		
		poi_sheet = new ExcelOperationsPOI("C:\\Users\\vigneshkumar_p.SOLARTISTECH\\Desktop\\WCRate.xls");
	
		poi_sheet.getsheets("Rate Factors");
		ArrayList ListRateFactors = poi_sheet.getDataFromExcel(0, 0);

		poi_sheet.getsheets("Hazard Group");
		ArrayList ListHazardGroup = poi_sheet.getDataFromExcel(0, 0);
	
		poi_sheet.getsheets("ELPPF");
		ArrayList ListELPPF = poi_sheet.getDataFromExcel(0, 0);
		
		poi_sheet.save();
		
		for(int RateFactorRow = 1; RateFactorRow<ListRateFactors.size(); RateFactorRow++)
		{
			flag =false;
			LHM_RateFactor =  (LinkedHashMap<String, String>) ListRateFactors.get(RateFactorRow);
			String state = LHM_RateFactor.get("STATE");
			System.out.println(LHM_RateFactor.get("STATE"));
			//System.out.println(LHM_RateFactor);
			
			List_HazardGroup = new LinkedList<LinkedHashMap<String,String>>();
			for(int HazardGrouprow = 1; HazardGrouprow<ListHazardGroup.size(); HazardGrouprow++)
			{				
				LHM_HazardGroup =  (LinkedHashMap<String, String>) ListHazardGroup.get(HazardGrouprow);
				if(LHM_HazardGroup.get("STATE").equals(state))
				{
					flag =true;
					List_HazardGroup.add(LHM_HazardGroup);
					//System.out.println(LHM_HazardGroup);
				}
			}
			
			List_Elppf = new LinkedList<LinkedHashMap<String,String>>();
			for(int Elppfrow = 1; Elppfrow<ListELPPF.size(); Elppfrow++)
			{				
				LHM_Elppf =  (LinkedHashMap<String, String>) ListELPPF.get(Elppfrow);
				if(LHM_Elppf.get("STATE").equals(state))
				{
					flag =true;
					List_Elppf.add(LHM_Elppf);
					//System.out.println(LHM_Elppf);
				}
			}	
			if(flag)
				DBupdater.update_wcRatingDB(dB,LHM_RateFactor,List_HazardGroup,List_Elppf);
		}
	}
}

