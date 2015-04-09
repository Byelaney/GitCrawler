package util;

import helper.QuickSort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import entity.UnPublishedRelease;
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
	
	
	//<tagName,date>
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
	
	/**
	 * if date1 < date2 return -1
	 * if date1 == date2 return 0
	 * if date1 > date2 return 1
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compare_date(String date1,String date2){
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
		
		QuickSort.qsort(dates);
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
	
	public static List<UnPublishedRelease> unPublishedReleaseSort(List<UnPublishedRelease> unsorted_release){
		String[] dates= new String[unsorted_release.size()];
		for(int i = 0;i<unsorted_release.size();i++){
			String date = unsorted_release.get(i).getDate();
			dates[i] = date;
			
		}
		
		QuickSort.qsort(dates);
		
		List<UnPublishedRelease> results = new ArrayList<UnPublishedRelease>();
		
		
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
	
	public static String dateToString(Date date){
		   SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		   return df.format(date);
	}
	
	public static Date stringToDate(String date){
		if(date.equals("")){
			return null;
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		try{
			java.util.Date result=sdf.parse(date);
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static long dayDiffer(String date1,String date2){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		try{
			java.util.Date da1=sdf.parse(date1);
			java.util.Date da2=sdf.parse(date2);
			Calendar cal1=Calendar.getInstance();
			Calendar cal2=Calendar.getInstance();
			
			cal1.setTime(da1);
			cal2.setTime(da2);
			
			long timeNow=cal1.getTimeInMillis();
			long timeOld=cal2.getTimeInMillis();
			
			long tt =(timeNow-timeOld)/(1000*60*60*24);//化为天
			
			return Math.abs(tt);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * judges if a commit belongs to a certain release
	 * using date to judge
	 * @return
	 */
	public static boolean BelongToRelease(String commit_date,String release_date,List<String> all_dates){
		Collections.sort(all_dates, new Comparator<String>(){
			@Override
			public int compare(String str1, String str2) {
				return compare_date(str1,str2);
			}
		});
		
		for(int i = 0;i<all_dates.size();i++){
			if(release_date.equals(all_dates.get(i))){
				if(i == all_dates.size()-1){
					if(compare_date(commit_date,all_dates.get(i))>0)
						return true;
				}
				else{
					if(compare_date(commit_date,all_dates.get(i))>=0 && compare_date(commit_date,all_dates.get(i+1))<=0)
						return true;
				}
			}
		}
		
		return false;
	}
	
}
