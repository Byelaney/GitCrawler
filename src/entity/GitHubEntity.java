package entity;

/**
 * Base interface for all GitHub API entities representations 
 * 
 *
 */
public abstract class GitHubEntity {
	
	/**
	 * Returns the Entity's API URL
	 * @return a String 
	 */
	public abstract String getURL();
}
