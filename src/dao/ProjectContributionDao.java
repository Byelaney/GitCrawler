package dao;

import java.util.ArrayList;
import java.util.List;

import usefuldata.ProjectContribution;

public interface ProjectContributionDao {
	
	/**
	 * try to add projectContribution to database
	 * @param projectContribution
	 * @return
	 */
	public boolean addProjectContribution(ProjectContribution projectContribution);
	
	
	/**
	 * try to find a ProjectContribution 
	 * @return
	 */
	public ProjectContribution findProjectContribution(int project_id,int developer_id);
	
	
	public List<ProjectContribution> findProjectContribution(String developerName);
	
	/**
	 * try to update a ProjectContribution
	 * @param pct
	 * @return
	 */
	public boolean updateProjectContribution(ProjectContribution pct);
	
	/**
	 * try to delete a ProjectContribution
	 * @param pct
	 * @return
	 */
	public boolean deleteProjectContribution(ProjectContribution pct);
	
	
	public int getProjectContributions(int developer_id, int project_id);
	
	public ArrayList<Integer> getAllProjectContributors(int project_id);
	
	
}
