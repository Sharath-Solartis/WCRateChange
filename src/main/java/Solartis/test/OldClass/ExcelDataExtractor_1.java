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

public class ExcelDataExtractor_1 
{
	private static RateChangeUpdater_1 DBupdater = null;
	
	private static ExcelOperationsPOI poi_sheet1 = null;
	private static ExcelOperationsPOI poi_sheet2 = null;
	private static ExcelOperationsPOI poi_sheet3 = null;
	
	@SuppressWarnings("static-access")
	public static void ExcelExtractor(String ExcelPath, DatabaseOperation dB) throws SQLException, POIException, DatabaseException, RateChangeException
	{
		poi_sheet1 = new ExcelOperationsPOI(ExcelPath);
		poi_sheet2 = new ExcelOperationsPOI(ExcelPath);
		poi_sheet3 = new ExcelOperationsPOI(ExcelPath);
		
		LinkedHashMap<String,String> LHM_sheet1 = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> LHM_sheet2 = null;
		List<LinkedHashMap<String,String>> List_sheet2 = new LinkedList<LinkedHashMap<String,String>>();
		LinkedHashMap<String,String> LHM_sheet3 = null;
		List<LinkedHashMap<String,String>> List_sheet3 = new LinkedList<LinkedHashMap<String,String>>();
		
		String state;
		int Row;
		poi_sheet1.getsheets("Rate Factors");
		int noOfRow_RateFactors = poi_sheet1.Num_Row();
		for(Row = 1; Row<noOfRow_RateFactors; Row++)
        {		
		    state = poi_sheet1.read_data(Row, 0);
			int noOfColumns_RateFactors = poi_sheet1.Num_Column();
			for(int Sheetl_col = 0; Sheetl_col<noOfColumns_RateFactors; Sheetl_col++)
	        {
	        	//System.out.print(Jxl_sheet1.read_data(Row, Sheetl_col) + " ");
	        	LHM_sheet1.put(poi_sheet1.read_data(0, Sheetl_col), poi_sheet1.read_data(Row, Sheetl_col));
	        }
			//System.out.println();
	        
			poi_sheet2.getsheets("Hazard Group");
			int noOfColumns_HazardGroup = poi_sheet2.Num_Column();
			int noOfRow_HazardGroup = poi_sheet2.Num_Row();
			for(int Sheet2_row = 1; Sheet2_row<noOfRow_HazardGroup; Sheet2_row++)
		    {
				if(poi_sheet2.read_data(Sheet2_row, 0).equals(state))
				{
					LHM_sheet2 = new LinkedHashMap<String,String>();
					for(int Sheet2_col = 0; Sheet2_col<noOfColumns_HazardGroup; Sheet2_col++)
			        {
			        	//System.out.print(Jxl_sheet2.read_data(Row, Sheet2_col) + " ");
			        	LHM_sheet2.put(poi_sheet2.read_data(0, Sheet2_col), poi_sheet2.read_data(Sheet2_row, Sheet2_col));
			        }
					//System.out.println();
					List_sheet2.add(LHM_sheet2);
				}
		    }
			
			poi_sheet3.getsheets("ELPPF");
			int noOfRow_ELPPF = poi_sheet3.Num_Row();
			int noOfColumns_ELPPF = poi_sheet3.Num_Column();
	        for(int ELPPF_row = 1; ELPPF_row<noOfRow_ELPPF; ELPPF_row++)
	        {
	        	if(poi_sheet3.read_data(ELPPF_row, 0).equals(state))
	        	{
	        		LHM_sheet3 = new LinkedHashMap<String,String>();
		        	for(int ELPPF_col = 0; ELPPF_col<noOfColumns_ELPPF; ELPPF_col++)
		        	{
		        		//System.out.print(Jxl_sheet3.read_data(ELPPF_row, ELPPF_col) + " ");
		        		LHM_sheet3.put(poi_sheet3.read_data(0, ELPPF_col), poi_sheet3.read_data(ELPPF_row, ELPPF_col));
		        	}
		        	//System.out.println();
		        	List_sheet3.add(LHM_sheet3);
	        	}	
			}
	        DBupdater.update_wcRatingDB(dB,LHM_sheet1,List_sheet2,List_sheet3);
	        System.out.println("-------------------------------------");
        }
		
		/*ArrayList ListRateFactors = poi_sheet3.getDataFromExcel(1, 0);
		ArrayList ListHazardGroup = poi_sheet3.getDataFromExcel(1, 0);
		ArrayList ListELPPF = poi_sheet3.getDataFromExcel(1, 0);*/
		
		/*for(int i =0 ;i < excelArray.size(); i++)
		{
			System.out.println(excelArray.get(i));
		}*/
		
		poi_sheet1.save();
		poi_sheet2.save();
		poi_sheet3.save();
	}
}

