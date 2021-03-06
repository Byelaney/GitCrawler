package search;

import java.io.IOException;
import java.util.List;

import entity.Issue;
import entity.Project;


/**
 * An interface that defines the forge search functionality.
 * A forge search must receive a search term and a page number (1-indexed) and
 * return a list of ForgeProjects objects. These projects can be used by a ForgeCrawler
 * to download source code.
 * 
 * 
 */
public interface ForgeSearch {
  
	/**
	 * Uses search functionality of the forge to get projects.
	 * 
	 * @param term term to be searched (ex: a project name, like h2database). 
	 * If the term is a null String then the method will return all the forge projects. 
	 * @param page 1-indexed page to get results (ie: starts with 1).
	 * @param limit Maximum number of projects to be returned. Negative values 
	 * for this parameter will result in an unbounded search.
	 * 
	 * @return list of ForgeProject entities with projects info
	 * @throws SearchException when something nasty happens
	 */
	public List<Project> getProjects(String term, int page, int limit) throws SearchException;
	
	/**
	 * Uses search functionality of the forge to get projects.
	 * 
	 * @param term term to be searched (ex: a project name, like h2database)
	 * @parm username the user that should have the aforementioned project
	 * @param page 1-indexed page to get results (ie: starts with 1)
	 * 
	 * @return list of ForgeProject entities with projects info
	 * @throws SearchException when something nasty happens
	 */
	public List<Project> getProjects(String term, String username, int page) throws SearchException;
	
	/**
	 * Provides a dump of every projects in the forge.
	 * 
	 * @param since The search will start from the projects 
	 * with the specified integer ID.
	 * @param limit Maximum number of projects to be returned. Negative values 
	 * for this parameter will result in an unbounded search.
	 * 
	 * @return list of ForgeProject entities with projects info
	 * @throws Exception when something nasty happens
	 */
	public List<Project> getAllForgeProjects(int since , int limit) throws SearchException;

	public List<Issue> getAllProjectIssues(Project gr) throws IOException;
}