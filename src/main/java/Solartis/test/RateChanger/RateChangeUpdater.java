package Solartis.test.RateChanger;

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

public class RateChangeUpdater 
{
	private static String createdBy = "coder";
	private static List<String> StoreProcedure_param = null;
	private static LinkedHashMap<Integer, LinkedHashMap<String, String>>  ConditionalInput=null;
	private static ConditionVerify conditionverify = null;
	private static boolean flag = false;
	//private static String[]  col =  { "A", "B", "C", "D", "E", "F", "G" } ;
	
	/*public static void update_wcRatingDB(DatabaseOperation dB, LinkedHashMap<String, String> lHM_sheet1, LinkedHashMap<String, String> lHM_sheet2, LinkedHashMap<String, String> lHM_sheet3) throws SQLException, DatabaseException, RateChangeException
	{
		updateRateFactorDB(dB,lHM_sheet1);
		updateHazardGroupDB(dB,lHM_sheet2);
		updateElppfsDB(dB,lHM_sheet3);
	}*/
	
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
			    		if(!HashMapIterator.getKey().equals("Condition") && !HashMapIterator.getKey().equals("StoreProcedureName") && !HashMapIterator.getKey().equals(""))
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
	
	public static void updateHazardGroupDB(DatabaseOperation dB, LinkedHashMap<String, String> lHM_sheet2) throws SQLException, DatabaseException
	{		
		System.out.println("UPDATING HAZARD GROUPS");
		conditionverify = new ConditionVerify();
		ConditionalInput = dB.GetDataObjects("SELECT * FROM `wc_hazardfactor_conditionaltable`");
		Iterator<Entry<Integer, LinkedHashMap<String, String>>> ConditionalInputIterator = ConditionalInput.entrySet().iterator();
	    while (ConditionalInputIterator.hasNext())
		{
	    	flag = false;
	    	StoreProcedure_param = new ArrayList<String>();
	    	Entry<Integer, LinkedHashMap<String, String>> ConditionalInputRow = ConditionalInputIterator.next();
	    	for( Entry<String, String> HashMapIterator : ConditionalInputRow.getValue().entrySet())
	    	{
	    		if(!HashMapIterator.getKey().equals("StoreProcedureName") && !HashMapIterator.getKey().equals(""))
				{
	    			flag = true;
	    			StoreProcedure_param.add(HashMapIterator.getValue());
				}
	    	}
	    	if(flag)
	    	{
	    	StoreProcedure_param.add(lHM_sheet2.get("Limitation"));
	    	StoreProcedure_param.add(lHM_sheet2.get(ConditionalInputRow.getValue().get("HazardGroup_ColumnName")));
	    	StoreProcedure_param.add(lHM_sheet2.get("STATE"));
	    	StoreProcedure_param.add(lHM_sheet2.get("EFFECTIVE_DATE"));
	    	StoreProcedure_param.add(createdBy);
	    	dB.callStoreProcedure(ConditionalInputRow.getValue().get("StoreProcedureName"), StoreProcedure_param);
	    	}
		}
	}
	
	public static void updateElppfs_ElaefDB(DatabaseOperation dB, LinkedHashMap<String, String> lHM_sheet3, String ConditionalTable) throws SQLException, DatabaseException
	{
		System.out.println("UPDATING ELPPFS/ELAEF");
		
		conditionverify = new ConditionVerify();
		ConditionalInput = dB.GetDataObjects("SELECT * FROM `"+ ConditionalTable +"`");
		Iterator<Entry<Integer, LinkedHashMap<String, String>>> ConditionalInputIterator = ConditionalInput.entrySet().iterator();
	    while (ConditionalInputIterator.hasNext())
		{
	    	flag = false;
	    	StoreProcedure_param = new ArrayList<String>();
	    	Entry<Integer, LinkedHashMap<String, String>> ConditionalInputRow = ConditionalInputIterator.next();
	    		
	    	StoreProcedure_param.add(lHM_sheet3.get("STATE"));
	    	StoreProcedure_param.add(lHM_sheet3.get("EFFECTIVE_DATE"));
	    	StoreProcedure_param.add(lHM_sheet3.get("Limitation").replaceAll(",", ""));
	    	for( Entry<String, String> HashMapIterator : ConditionalInputRow.getValue().entrySet())
	    	{
	    		if(!HashMapIterator.getKey().equals("StoreProcedureName") && !HashMapIterator.getKey().equals(""))
				{
	    			flag = true;
	    			StoreProcedure_param.add(HashMapIterator.getValue());
				}
	    	}
	    	if(flag)
	    	{
	    	StoreProcedure_param.add(lHM_sheet3.get(ConditionalInputRow.getValue().get("Elppf_ColumnName")));
	    	StoreProcedure_param.add(createdBy);
	    	dB.callStoreProcedure(ConditionalInputRow.getValue().get("StoreProcedureName"), StoreProcedure_param);
	    	}
		}	
	}
}
