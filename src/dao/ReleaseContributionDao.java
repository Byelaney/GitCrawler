package dao;

import java.util.ArrayList;

import usefuldata.ReleaseContribution;

public interface ReleaseContributionDao {
	
	public ArrayList<ReleaseContribution> getReleaseContribution(String projectName,String developer);
}
