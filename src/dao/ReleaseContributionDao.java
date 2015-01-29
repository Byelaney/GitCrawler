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
	
	public int getSize(String developerName, String projectName,
			String releaseName);
}
