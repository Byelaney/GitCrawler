package analysis;

import java.util.ArrayList;

public interface DevelopDigram {
	/**
	 * focus on a release of a project developed by a developer 
	 * @param filenames 
	 * @return package structure with json format
	 */
	public String getDevelopDigramByVersion( ArrayList<String> filenames);

	
}
