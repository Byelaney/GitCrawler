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
	public ArrayList<ReleaseContribution> getReleaseContribution(int project_id, int developer_id);
	
	
	public int getReleaseContributions(int developer_id, int project_id,
			int release_id);
	

}
