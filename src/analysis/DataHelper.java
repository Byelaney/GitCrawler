package analysis;

import java.util.ArrayList;
import java.util.List;

import usefuldata.Comment;
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
	public int getReleaseSize(String developerName, String projectName,
			String releaseName,String owner);// use in force chart
	
	/**
	 * try to get a developer's contribution from some release(contained)  to Now
	 * @param developerName
	 * @param projectName
	 * @return int
	 */
	public int getSize(String developerName, String projectName,
			String releaseName,String owner);//use in evolve

	
	
	/**
	 * try to get a developer's contribution to this project
	 * @param developerName
	 * @param projectName
	 * @return
	 */
	public int getSize(String developerName, String projectName,String owner);
	
	
	/**
	 * get all filenames of a project which modified by one in a release
	 * @param developerName
	 * @param projectName
	 * @param releaseName
	 * @return
	 */
	
	public ArrayList<String> getFiles(String projectName,String release,String developer,String owner); //use in force chart
	
	/**
	 * get all developers of a project
	 * @param projectName 
	 * @return ArrayList<String>
	 */

	public ArrayList<String> getAllDeveloperNames(String projectName,String owner);//use in evolve
	
	
	/**
	 * get all files modified by some developer during a period of time(start to end )
	 * @prama projectName
	 * @prama developerName(one)
	 * @param start date (yy-mm-dd)
	 * @param end date (yy-mm-dd)
	 * @return ArrayList<String>
	 */
	
	public ArrayList<String> getFiles(String projectName,String developer,String start,String end,String owner);//use in evolve
	
	
	/**
	 *get VersionDate enties of whole project
	 *@prama projectName
	 * @return ArrayList<VersionDate>
	 *
	 */
	
	public ArrayList<VersionDate> getVersions(String projectName,String owner);//use in evolve
	
	
	/**
	 *get CommitDate enties of whole project
	 *@prama projectName
	 *@prama release
	 *@return ArrayList<CommitDate>
	 *
	 */
	
	public ArrayList<CommitDate> getCommits(String projectName,String owner);//use in evolve
	
	
	/**
	 *get issues of one project
	 *@prama projectName
	 *
	 *@return ArrayList<Issue>
	 *
	 */
	
	public ArrayList<usefuldata.Issue> getIssues(String projectName,String owner);//use in evolve
	

	/**
	 *get comments of one project
	 *@prama projectName
	 *
	 *@return ArrayList<Issue>
	 *
	 */
	public ArrayList<Comment> getComments(String projectName,String owner);//use in evolve
	
	
	/**
	 *get codes of one release
	 *@prama projectName
	 *@prama release
	 *@return int
	 *
	 */
	
	public int getCodes(String projectName,String release,String owner);//use in evolve
	
	/**
	 * get commit nums of a certain release
	 * @param projectName
	 * @param release
	 * @return
	 */
	public int getReleaseCommits(String projectName,String release,String owner);
	
	/**
	 * get issue nums of a certain release
	 * @param projectName
	 * @param release
	 * @return
	 */
	public int getIssueNum(String projectName,String release,String owner);
	
	public int getComprehensive();
	
	public int getTest();
	
	public int getDocument();
	
	public List<usefuldata.Vitality> getVitality(String projectName,String developer,String owner);
	
	/**
	 *check if file downloaded is zip, or pack it a zipfile 
	 *@param path
	 * @return zip path( and delete the prior file)
	 */
	public String packFile(String filepath);
}
