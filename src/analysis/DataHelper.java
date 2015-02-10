package analysis;

import java.util.ArrayList;
import java.util.Map;

import usefuldata.CommitDate;
import usefuldata.VersionDate;

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
	 * try to get a developer's contribution
	 * @param developerName
	 * @param projectName
	 * @return int
	 */
	public int getSize(String developerName, String projectName);



	//----relation----	
	/**
	 * get all filenames of a commit
	 * @param developerName
	 * @param projectName
	 * @param releaseName
	 * @return
	 */
	

	public ArrayList<String> getFiles(Map<String,String> dateMap,String developerName,String owner,String projectName,
			String releaseName);
	
	//----new-----------------------------	
	/**
	 * get all developers of a project
	 * @param projectName 
	 * @return ArrayList<String>
	 */

	public ArrayList<String> getAllDeveloperNames(String projectName);
	
	
	/**
	 * get all files modified by some developer
	 * @prama projectName
	 * @prama developerName(one)
	 * @return ArrayList<String>
	 */
	
	public ArrayList<String> getFiles(String projectName,String developer);//----evolve----
	
	
	/**
	 *get VersionDate enties of whole project
	 *@prama projectName
	 * @return ArrayList<VersionDate>
	 *
	 */
	
	public ArrayList<VersionDate> getVersions(String projectName);
	
	
	/**
	 *get CommitDate enties of whole project
	 *@prama projectName
	 *@prama release
	 *@return ArrayList<CommitDate>
	 *
	 */
	
	public ArrayList<CommitDate> getCommits(String projectName);
	
	
	/**
	 *get issue count of one release
	 *@prama projectName
	 *@prama release
	 *@return int
	 *
	 */
	
	public int getIssueCount(String projectName,String release);
	
	
	/**
	 *get comments count of one release
	 *@prama projectName
	 *@prama release
	 *@return int
	 *
	 */
	public int getCommentCount(String projectName,String release);
	
	
	/**
	 *get codes of one release
	 *@prama projectName
	 *@prama release
	 *@return int
	 *
	 */
	
	public int getCodes(String projectName,String release);
	
	
}
