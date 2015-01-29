package factory;

import java.util.ArrayList;

public interface DataFactory {
	
	public String getRelationsDestination(ArrayList<String> developers,
			String projectName, String releaseName);

	public ArrayList<String> getPakageDependency(ArrayList<String> files,
			ArrayList<String> languages);

	public int getCodeLines(String file,ArrayList<String> languages);
	
	public String getDevelopDigramByVersion( ArrayList<String> filenames);
}
