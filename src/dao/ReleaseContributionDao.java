package dao;

import java.util.ArrayList;

import usefuldata.ReleaseContribution;

public interface ReleaseContributionDao {
	
	/**
	 * get sorted ReleaseContributions
	 * @param projectName
	 * @param developer
	 * @return
	 */
	public ArrayList<ReleaseContribution> getReleaseContribution(String projectName,String developer);
	
	
	public int getReleaseContributions(int developer_id, int project_id,
			int release_id);
	

}
