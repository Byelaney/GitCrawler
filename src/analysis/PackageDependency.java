package analysis;

import java.util.ArrayList;

public interface PackageDependency {
	
	/**
	 * 
	 * @param release_location
	 * @param all languages
	 * @return package structure with json format
	 */
	public ArrayList<String> getPakageDependency(ArrayList<String> release_location,ArrayList<String> languages);
	
	/**
	 * get main package structure
	 * @param release_location
	 * @param all languages
	 * @return package structure with json format
	 */
	public ArrayList<String> getMainPakageDependency(ArrayList<String> release_location,ArrayList<String> languages);
}
