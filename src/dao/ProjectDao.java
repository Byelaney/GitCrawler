package dao;

import java.util.ArrayList;
import java.util.List;

import usefuldata.Developer;
import usefuldata.Project;
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
	
	public usefuldata.Project getProjectById(int project_id);
	
	public ArrayList<Project> getAllProjects();
	
	/**
	 * 
	 * @param project_id
	 * @return developers with reference not initialized
	 */
	public List<Developer> getAllDevelopers(int project_id);
	
	/**
	 * get all sorted releases 
	 * @param projectName
	 * @return
	 */
	public List<Release> getAllReleases(String projectName);
	
	
	public void deleteProject(String owner,String projectName);
	public boolean updateProject(usefuldata.Project project);
	public boolean addProject(usefuldata.Project project);
}
