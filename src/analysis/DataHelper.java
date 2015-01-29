package analysis;

import java.util.ArrayList;
import java.util.Map;

public interface DataHelper {
	
	/**
	 * try to get a developer's contribution
	 * @param developerName
	 * @param projectName
	 * @param releaseName
	 * @return
	 */
	public int getSize(String developerName, String projectName,
			String releaseName);

	
	/**
	 * get all filenames of a commit
	 * @param developerName
	 * @param projectName
	 * @param releaseName
	 * @return
	 */
	public ArrayList<String> getFiles(Map<String,String> dateMap,String developerName,String owner,String projectName,
			String releaseName);
}
