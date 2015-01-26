package dao;

import java.util.List;

import usefuldata.Developer;
import usefuldata.Release;

public interface ProjectDao {
	
	/**
	 * try to find project for project's basic info
	 * @param owner
	 * @param projectName
	 * @return project with basic info
	 */
	public usefuldata.Project getProject(String owner,String projectName);
	
	public usefuldata.Project getProjectWithId(int project_id,String projectName);
	
	public usefuldata.Project getProject(String projectName);
	
	/**
	 * 
	 * @param project_id
	 * @return developers with reference not initialized
	 */
	public List<Developer> getAllDevelopers(int project_id);
	
	public List<Release> getAllReleases(int project_id);
	
	
	public void deleteProject(String owner,String projectName);
	public void updateProject(usefuldata.Project project);
	public void addProject(usefuldata.Project project);
}
