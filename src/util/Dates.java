package util;

import helper.QuickSort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import usefuldata.Release;

/**
 * Helper class to deal with dates
 * 
 * 
 */
public class Dates {

	private SimpleDateFormat simpleDateFormat;

	public Dates() {
		simpleDateFormat = new SimpleDateFormat();
	}

	public Dates(String pattern) {
		simpleDateFormat = new SimpleDateFormat(pattern);
	}

	public String format(Date date) {
		return simpleDateFormat.format(date);
	}

	public Date format(String date) {
		try {
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * trim date like 2012-06-30T20:18:25Z to 2012-06-30
	 * @param unformatedDates
	 * @return
	 */
	public static Map<String,String> dateFormat(Map<String,String> unformatedDates){
		Set<String> keys = unformatedDates.keySet();
		Map<String,String> results = new HashMap<String,String>();
		
		for(String name:keys){
			String date = unformatedDates.get(name);
			String formatedDate = date.substring(0, 10);
			results.put(name, formatedDate);
		}
			
		return results;
	}
	
	public static String dateFormat(String unformatedDate){
		return unformatedDate.substring(0, 10);
	}
	
	/**
	 * 
	 * @param unSortedDates
	 * @return ArrayList<String> with sorted tagNames
	 */
	public static ArrayList<String> dateSort(Map<String,String> unSortedDates){
		Map<String,String> tmp = dateFormat(unSortedDates);
		
		List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(  
		        tmp.entrySet()); 
		
		Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {  
		    public int compare(Map.Entry<String, String> o1,  
		            Map.Entry<String, String> o2) {  
		        String s1 = o1.getValue();
		        String s2 = o2.getValue();
		    	if(s1.equals(s2))
		    		return 0;
		    	else{
		    		int year1 = Integer.parseInt(s1.substring(0, 4));
		    		int year2 = Integer.parseInt(s2.substring(0, 4));
		    		int month1 = Integer.parseInt(s1.substring(5, 7));
		    		int month2 = Integer.parseInt(s2.substring(5, 7));
		    		int day1 = Integer.parseInt(s1.substring(8, 10));
		    		int day2 = Integer.parseInt(s2.substring(8, 10));
		    		
		    		if(year1 < year2)
		    			return -1;
		    		else if(year1 > year2)
		    			return 1;
		    		else{
		    			//year is the same
		    			if(month1 < month2)
		    				return -1;
		    			else if(month1 > month2)
		    				return 1;
		    			else{
		    				//year and month is the same
		    				if(day1 < day2)
		    					return -1;
		    				else if(day1 > day2)
		    					return 1;
		    				else
		    					return 0;
		    			}
		    		}
		    	}
		        
		    }  
		});  
		
		ArrayList<String> results = new ArrayList<String>();
		for (int i = 0; i < infoIds.size(); i++) {  
		    Entry<String,String> ent=infoIds.get(i);  		    
		    results.add(ent.getKey());
		}  
		
		return results;
	}
	
	
	public static int getDateIndex(String unformated_date,Map<String,String> release_date){
		String formated_date = unformated_date.substring(0, 10);
		ArrayList<String> sorted_dates = dateSort(release_date);
		
		String tmp_date = null;
		for(int i = 0;i<sorted_dates.size();i++){
			tmp_date = sorted_dates.get(i);
			if (compare_date(formated_date,release_date.get(tmp_date)) <=0){
				return i;
			}
			
		}
		
		return sorted_dates.size();
	}
	
	private static int compare_date(String date1,String date2){
	    	if(date1.equals(date2))
	    		return 0;
	    	else{
	    		int year1 = Integer.parseInt(date1.substring(0, 4));
	    		int year2 = Integer.parseInt(date2.substring(0, 4));
	    		int month1 = Integer.parseInt(date1.substring(5, 7));
	    		int month2 = Integer.parseInt(date2.substring(5, 7));
	    		int day1 = Integer.parseInt(date1.substring(8, 10));
	    		int day2 = Integer.parseInt(date2.substring(8, 10));
	    		
	    		if(year1 < year2)
	    			return -1;
	    		else if(year1 > year2)
	    			return 1;
	    		else{
	    			//year is the same
	    			if(month1 < month2)
	    				return -1;
	    			else if(month1 > month2)
	    				return 1;
	    			else{
	    				//year and month is the same
	    				if(day1 < day2)
	    					return -1;
	    				else if(day1 > day2)
	    					return 1;
	    				else
	    					return 0;
	    			}
	    		}
	    	}
	        
	    }  
	
	
	public static List<Release> releaseSort(List<Release> unsorted_release){
		String[] dates= new String[unsorted_release.size()];
		for(int i = 0;i<unsorted_release.size();i++){
			String date = unsorted_release.get(i).getDate();
			dates[i] = date;
		}
		
		QuickSort.quick(dates);
		List<Release> results = new ArrayList<Release>();
		
		for(int j = 0;j<dates.length;j++){
			for(int i =0;i<unsorted_release.size();i++){
				if(dates[j].equals(unsorted_release.get(i).getDate())){
					results.add(unsorted_release.get(i));
					break;
				}
			}
				
		}
		
		return results;
	}
	
	public static String metaDateFormat(String date){
		//"Fri Sep 21 21:43:26 CST 2012";
		String []months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		String result;
		try{
		String year = date.substring(24);// 2012
		String month = date.substring(4, 7);
		
		for(int i =0;i<months.length;i++){
			
			if(months[i].equals(month)){
				
				int idx = i + 1;
				if(idx<10){
					month = "0" + idx;
				}
				else
					month = "" + idx;
			}
				
		}
		
		String day = date.substring(8, 10);
		result = year + "-" + month + "-" + day;
		return result;
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
}
