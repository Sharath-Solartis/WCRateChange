package Solartis.test.OldClass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import Solartis.test.Common.ConditionVerify;
import Solartis.test.Common.DatabaseOperation;
import Solartis.test.CommonException.DatabaseException;
import Solartis.test.exception.RateChangeException;

public class RateChangeUpdater_1 
{
	private static String createdBy = "king";
	private static List<String> StoreProcedure_param = null;
	private static LinkedHashMap<Integer, LinkedHashMap<String, String>>  ConditionalInput=null;
	private static ConditionVerify conditionverify = null;
	private static boolean flag = false;
	//private static String[]  col =  { "A", "B", "C", "D", "E", "F", "G" } ;
	
	public static void update_wcRatingDB(DatabaseOperation dB, LinkedHashMap<String, String> lHM_sheet1, List<LinkedHashMap<String, String>> list_sheet2, List<LinkedHashMap<String, String>> list_sheet3) throws SQLException, DatabaseException, RateChangeException
	{
		updateRateFactorDB(dB,lHM_sheet1);
		updateHazardGroupDB(dB,list_sheet2);
		updateElppfsDB(dB,list_sheet3);
	}
	
	public static void updateRateFactorDB(DatabaseOperation dB, LinkedHashMap<String, String> lHM_sheet1) throws SQLException, DatabaseException, RateChangeException
	{
		System.out.println("UPDATING RATE FACTOR");
		conditionverify = new ConditionVerify();
		ConditionalInput = dB.GetDataObjects("SELECT * FROM `wc_ratefactor_conditionaltable`");
		Iterator<Entry<Integer, LinkedHashMap<String, String>>> ConditionalInputIterator = ConditionalInput.entrySet().iterator();
    	while (ConditionalInputIterator.hasNext())
		{
    		StoreProcedure_param = new ArrayList<String>();
    		Entry<Integer, LinkedHashMap<String, String>> ConditionalInputRow = ConditionalInputIterator.next();
    		flag = false;
    		
	    	for( Entry<String, String> HashMapIterator : ConditionalInputRow.getValue().entrySet())
	    	{
	    		try
	    		{
		    		if(conditionverify.ConditionReading(ConditionalInputRow.getValue().get("Condition"), lHM_sheet1) || ConditionalInputRow.getValue().get("Condition").equals(""))
					{
		    			flag = true;
			    		if(!HashMapIterator.getKey().equals("Condition") && !HashMapIterator.getKey().equals("StoreProcedureName"))
			    		{
			    			StoreProcedure_param.add(HashMapIterator.getValue());
			    		}
					}
	    		}
	    		catch(Exception ConditionMismatch) 
	    		{
	    			throw new RateChangeException("Error in Condition Verification for " + (ConditionalInputRow.getValue().get("RateFactor_ColumnName")), ConditionMismatch);
	    		}
	    	}
	    	if(flag)
	    	{
		    	StoreProcedure_param.add(lHM_sheet1.get(ConditionalInputRow.getValue().get("RateFactor_ColumnName")));
		    	StoreProcedure_param.add(lHM_sheet1.get("STATE"));
		    	StoreProcedure_param.add(lHM_sheet1.get("EFFECTIVE_DATE"));
		    	StoreProcedure_param.add(createdBy);
		    	dB.callStoreProcedure(ConditionalInputRow.getValue().get("StoreProcedureName"), StoreProcedure_param);
	    	}
		}
	}
	
	public static void updateHazardGroupDB(DatabaseOperation dB, List<LinkedHashMap<String, String>> list_sheet2) throws SQLException, DatabaseException
	{		
		System.out.println("UPDATING HAZARD GROUPS");
		for (int i = 0; i < list_sheet2.size(); i++)
		{
			LinkedHashMap<String, String> hazardgroup_row = list_sheet2.get(i);
			
			ConditionalInput = dB.GetDataObjects("SELECT * FROM `wc_hazardfactor_conditionaltable`");
			Iterator<Entry<Integer, LinkedHashMap<String, String>>> ConditionalInputIterator = ConditionalInput.entrySet().iterator();
	    	while (ConditionalInputIterator.hasNext())
			{
	    		StoreProcedure_param = new ArrayList<String>();
	    		Entry<Integer, LinkedHashMap<String, String>> ConditionalInputRow = ConditionalInputIterator.next();
	    		for( Entry<String, String> HashMapIterator : ConditionalInputRow.getValue().entrySet())
	    		{
	    			StoreProcedure_param.add(HashMapIterator.getValue());
	    		}
	    		StoreProcedure_param.add(hazardgroup_row.get("Limitation"));
	    		StoreProcedure_param.add(hazardgroup_row.get(ConditionalInputRow.getValue().get("HazardGroup_ColumnName")));
	    		StoreProcedure_param.add(hazardgroup_row.get("STATE"));
	    		StoreProcedure_param.add(hazardgroup_row.get("EFFECTIVE_DATE"));
	    		StoreProcedure_param.add(createdBy);
	    		dB.callStoreProcedure("sproc_hazard_group", StoreProcedure_param);
			}
		}
	}
	
	private static void updateElppfsDB(DatabaseOperation dB, List<LinkedHashMap<String, String>> list_sheet3) throws SQLException, DatabaseException
	{
		System.out.println("UPDATING ELPPFS");
		for (int i = 0; i < list_sheet3.size(); i++)
		{
			LinkedHashMap<String, String> elppf_row = list_sheet3.get(i);
			
			ConditionalInput = dB.GetDataObjects("SELECT * FROM `wc_elppf_conditionaltable`");
			Iterator<Entry<Integer, LinkedHashMap<String, String>>> ConditionalInputIterator = ConditionalInput.entrySet().iterator();
	    	while (ConditionalInputIterator.hasNext())
			{
	    		StoreProcedure_param = new ArrayList<String>();
	    		Entry<Integer, LinkedHashMap<String, String>> ConditionalInputRow = ConditionalInputIterator.next();
	    		
	    		StoreProcedure_param.add(elppf_row.get("STATE"));
	    		StoreProcedure_param.add(elppf_row.get("EFFECTIVE_DATE"));
	    		StoreProcedure_param.add(elppf_row.get("Limitation").replaceAll(",", ""));
	    		for( Entry<String, String> HashMapIterator : ConditionalInputRow.getValue().entrySet())
	    		{
	    			StoreProcedure_param.add(HashMapIterator.getValue());
	    		}
	    		StoreProcedure_param.add(elppf_row.get(ConditionalInputRow.getValue().get("Elppf_ColumnName")));
	    		StoreProcedure_param.add(createdBy);
	    		dB.callStoreProcedure("sproc_elppf", StoreProcedure_param);
			}	
		}
	}
	
}
