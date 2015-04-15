package util;

public class URLHelper {

	public static String getProjectName(String URL){
		String[] a= URL.split("/");		
		return a[a.length-1];
	}
	
	public static String getProjectOwner(String URL){
		String[] a= URL.split("/");		
		return a[a.length-2];
	}
	
}
