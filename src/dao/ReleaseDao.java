package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import usefuldata.Developer;
import usefuldata.Release;

public interface ReleaseDao {

	
	/**
	 * try to find release for release's basic info
	 * @param projectId
	 * @param releaseName
	 * @return
	 */
	public usefuldata.Release getRelease(int projectId,String releaseName);
	
	/**
	 * remember these releases are already sorted by date
	 * @param projectName
	 * @return 
	 */
	public List<usefuldata.Release> getAllRelease(String projectName);
	
	/**
	 * get a certain release
	 * @param release_id
	 * @return
	 */
	public usefuldata.Release getRelease(int release_id);
	
	
	/**
	 * try to find all developers involved in this release of the project
	 * @param projectId
	 * @param releaseName
	 * @return
	 */
	public List<Developer> getAllDeveloper(int projectId,String releaseName);
	
	
	/**
	 * try to find all contributions in certain release
	 * @param release_id
	 * @return developer_id and submits
	 */
	public HashMap<Integer,Integer> getContributions(int release_id);
	
	
	//public ArrayList<ReleaseContribution> getSortedContributions(int release_id);
	
	
	//not finished yet
	public void deleteRelease(String projectName,String releaseName);
	
	
	
	/**
	 * try to update developer contribution
	 * @param release_id
	 * @param developer_id
	 * @param project_id
	 * @param submits
	 * @return
	 */
	public boolean updateRelease(int release_id,int developer_id,int project_id,int submits);
	
	/**
	 * try to update release basic info
	 * @param release
	 * @return
	 */
	public boolean updateReleaseInfo(Release release);
	
	
	/**
	 * try to add basic release info 
	 * @param release
	 * @param project_id
	 * @return
	 */
	public boolean addReleaseInfo(usefuldata.Release release,int project_id);

	
	/**
	 * try to add developer contribution and 
	 * remember to first use addReleaseInfo then this method
	 * @param release_id
	 * @param developer_id
	 * @param project_id
	 * @param contributions
	 * @return
	 */
	public boolean addRelease(int release_id,int developer_id,int project_id,int contributions);
	
	
	public Map<String,Integer> getReleaseCommitNum(String projectName);
	
}
