package metadao;

import entity.Project;

public interface ProjectDao {
	/**
	 * after crawling call this method
	 * inside the method use Dates to format the date
	 * @param project
	 * @return
	 */
	public boolean addProject(Project project);

	public Project getProject(String owner,String projectName);
}
